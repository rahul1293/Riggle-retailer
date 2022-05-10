package com.riggle.ui.subcategory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.ProductCartRequest
import com.riggle.data.models.request.RequestToAddCart
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.data.models.response.ProductsData
import com.riggle.data.models.response.SchemesBean
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.bottomsheets.ComboBottomSheet
import com.riggle.ui.bottomsheets.SchemeBottomSheet
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.adapters.ShopByBrandsProductsAdapter
import com.riggle.ui.home.fragment.CartFragment
import com.riggle.ui.listener.ProductChooseListener
import com.riggle.ui.other.AddFilterActivity
import com.riggle.ui.other.BrandCategoryDetailActivity
import com.riggle.utils.Constants
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_product_list.llFilter
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.util.ArrayList

class ProductListActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector,
    ShopByBrandsProductsAdapter.ShopByBrandsProductsListener, ProductChooseListener {

    private var prod_id = 1
    private var productsData: ArrayList<ProductsData> = arrayListOf<ProductsData>()
    private var adapter: ShopByBrandsProductsAdapter? = null
    private var loadingDialog: LoadingDialog? = null

    private val userPreference: UserProfileSingleton by inject()

    lateinit var dataBean: BrandsCategoryData
    private var currentPos = -1
    private var page_type = Constants.PageTypes.UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_product_list
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        userPreference.userId
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        if (bundle != null) {
            page_type = if (bundle.getString(BrandCategoryDetailActivity.KEY_PAGE_TITLE)
                    ?.contains(getString(R.string.shop_by_brands)) == true
            ) Constants.PageTypes.BRAND_PAGE else Constants.PageTypes.CATEGORY_PAGE
            supportActionBar?.title = bundle.getString(BrandCategoryDetailActivity.KEY_PAGE_TITLE)
            bundle.getParcelable<BrandsCategoryData>("data")?.let {
                dataBean = it
            }
            currentPos = bundle.getInt(BrandCategoryDetailActivity.KEY_CURRENT_POS, 0)
            /*setData()*/
        }
        setUpListener()
    }

    override fun onResume() {
        super.onResume()
        if (userPreference.sharedPreferencesUtil.cartCount > 0) {
            tvCartCount.visibility = View.VISIBLE
        } else {
            tvCartCount.visibility = View.GONE
        }
        fetchData()
    }

    private fun fetchData() {
        val data = HashMap<String, String>()
        data.put("page", "1")
        data.put("page_size", "40")
        data.put("is_active", "True")
        dataBean?.let {
            //data.put("brand__in", it.id.toString())
            data.put("category__in", it.id.toString())
        }
        data.put("expand", "schemes.free_product,banner_image,combo_products.products,service_hub")
        userPreference.userData?.retailer?.id?.let {
            data.put("retailer", it.toString())
        }
        /*schemes.free_product*/
        if (adapter != null && adapter?.itemCount!! > 0) {
            // do nothing
        } else {
            showHideLoader(true)
        }
        if (prod_id != -1 && page_type != Constants.PageTypes.UNDEFINED)
            dataManager.getProductsList(
                object :
                    ApiResponseListener<APICommonResponse<List<ProductsData>>> {
                    override fun onSuccess(response: APICommonResponse<List<ProductsData>>) {
                        showHideLoader(false)
                        if (response.results != null) {
                            response.results?.let {
                                productsData = it as ArrayList<ProductsData>
                                if (adapter?.itemCount != null && adapter?.itemCount!! > 0) {
                                    adapter?.setList(productsData)
                                } else {
                                    setData()
                                }
                            }
                        }
                    }

                    override fun onError(apiError: ApiError?) {
                        //populateLoaderRecyclerView(null)
                        showHideLoader(false)
                        Toast.makeText(
                            this@ProductListActivity,
                            apiError?.msg.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                data
            )
    }

    private fun setData() {
        activity?.let {
            adapter = ShopByBrandsProductsAdapter(it, productsData)
            rvProducts?.layoutManager = LinearLayoutManager(activity)
            rvProducts?.adapter = adapter
            adapter?.setListener(this)
        }

    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.hide()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

    private fun setUpListener() {
        ivCartView.setOnClickListener {
            //HomeActivity.start(this, true)
            val intent = CartFragment.newIntent(this)
            startActivity(intent)
        }
        llFilter.setOnClickListener {
            AddFilterActivity.start(this)
        }
        swdRefresh.setOnRefreshListener {
            fetchData()
            swdRefresh.isRefreshing = false
        }
    }

    companion object {
        const val KEY_PAGE_TITLE = "title"
        const val KEY_DATA = "data"
        const val KEY_CURRENT_POS = "current_pos"
        var selected_pos = 0
        fun start(
            context: Context,
            brandsData: BrandsCategoryData,
            page_title: String?,
            position: Int
        ) {
            val bundle = Bundle()
            bundle.putString(KEY_PAGE_TITLE, page_title)
            bundle.putParcelable(KEY_DATA, brandsData)
            bundle.putInt(KEY_CURRENT_POS, position)
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun itemClicked(product_id: Int) {
        openProductVariantSheet(product_id)
    }

    private fun openProductVariantSheet(pos: Int) {
        val sheet = SchemeBottomSheet()
        val bundle = Bundle()
        bundle.putInt("product_id", pos)
        bundle.putString("scheme", Gson().toJson(adapter?.getList()?.get(pos)?.schemes))
        //bundle.putString("product_name", adapter?.getList()?.get(pos)?.free_product?.name)
        bundle.putString("product_name", adapter?.getList()?.get(pos)?.name)
        sheet.arguments = bundle
        sheet.show(supportFragmentManager, sheet.tag)
        sheet.setListener(this)
    }

    override fun singleVariantUpdate(id: Int, productQuant: Int) {
        val products = ArrayList<VariantUpdate>()
        val update = VariantUpdate(id, productQuant, null)
        products.add(update)
        val cartRequest = ProductCartRequest(products)
        userPreference.userData?.retailer?.id?.let {
            showHideLoader(true)
            val cartData = RequestToAddCart(it, products)
            dataManager.addCartItems(object :
                ApiResponseListener<List<APICommonResponse<ProductsData>>> {
                override fun onSuccess(response: List<APICommonResponse<ProductsData>>) {

                    if (response != null) {
                        if (response.isNotEmpty()) {
                            response[0].product?.let { it ->
                                /*it.item_cart?.let { cart ->
                                }*/
                                itemUpdatedItem(id, productQuant)
                            }
                        } else {
                            itemUpdatedItem(id, 0)
                        }
                    }

                    /*if (response.product != null) {
                        response.product?.let {
                            itemUpdatedItem(id, response.quantity)
                        }
                    } else {
                        itemUpdatedItem(id, 0)
                    }*/
                    showHideLoader(false)
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    if (!apiError?.message.equals("Parsing error", false))
                        Toast.makeText(
                            activity,
                            apiError?.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                }
            }, it, cartRequest)
        }
    }

    fun itemUpdatedItem(product_id: Int/*ProductsData*/, quantity: Int) {
        //val product_id = product?.id
        for (i in productsData.indices) {
            if (productsData[i].id == product_id) {
                //productsData[i] = product
                productsData[i].quantity = quantity
                adapter?.updateCart(i, productsData[i])
                break
            }
        }
    }

    override fun itemUpdated(scheme: SchemesBean, pos: Int) {
        /*val product_id = product?.id
        for (i in productsData.indices) {
            if (productsData[i].id == product_id) {
                productsData[i] = product
                adapter?.updateCart(i, product)
                break
            }
        }*/
        /*productsData[pos].rate = scheme.rate
        productsData[pos].moq = scheme.max_quantity
        adapter?.notifyItemChanged(pos)*/
        singleVariantUpdate(scheme.product, scheme.min_quantity)

        /*if (scheme.min_quantity > 0)
            singleVariantUpdate(productsData[pos].id, scheme.min_quantity)
        else
            singleVariantUpdate(productsData[pos].id, scheme.max_quantity)*/

    }

    override fun onRefresh() {
        super.onRefresh()
        fetchData()
    }

    override fun comboClick(pos: Int) {
        val sheet = ComboBottomSheet()
        val bundle = Bundle()
        adapter?.getList()?.get(pos)?.id?.let {
            bundle.putInt("product_id", it)
        }
        bundle.putString("scheme", Gson().toJson(adapter?.getList()?.get(pos)?.combo_products))
        sheet.arguments = bundle
        sheet.show(supportFragmentManager, sheet.tag)
        sheet.isCancelable = false
        sheet.setListener(this)
    }

}