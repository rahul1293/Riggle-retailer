package com.riggle.ui.other.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.RequestCartData
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.EditCartResponse
import com.riggle.data.models.response.ProductsData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.bottomsheets.ProductVariantSheet
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.adapters.ShopByBrandsProductsAdapter
import com.riggle.ui.home.adapters.ShopByBrandsProductsAdapter.ShopByBrandsProductsListener
import com.riggle.ui.listener.ProductVariantListener
import com.riggle.utils.Constants.PageTypes
import java.util.*

class BrandCategoryFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector,
    ShopByBrandsProductsListener, ProductVariantListener {
    @JvmField
    @BindView(R.id.rvProducts)
    var rvProducts: RecyclerView? = null
    private var prod_id = -1
    private var page_type = PageTypes.UNDEFINED
    private var productsData: ArrayList<ProductsData> = arrayListOf<ProductsData>()
    private var adapter: ShopByBrandsProductsAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_brand_category_detail
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        activity?.let {
            ButterKnife.bind(it)
        }
        /*Set Data On View*/
        setData()
    }

    fun loadData(id: Int, page_type: PageTypes) {


        if(productsData.isEmpty()){
            populateLoaderRecyclerView(rvProducts)
            prod_id = id
            this.page_type = page_type
            fetchData()
        }

    }

    private fun fetchData() {
        if (prod_id != -1 && page_type != PageTypes.UNDEFINED) dataManager.getProducts(object :
            ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>> {
            override fun onSuccess(response: APICommonResponse<ArrayList<ProductsData>>) {
                if (response.isSuccess) {
                    response.data?.let {
                        productsData = it
                        setData()
                    }
                }
            }

            override fun onError(apiError: ApiError?) {
                populateLoaderRecyclerView(null)
            }
        }, page_type.toString(), prod_id)
    }

    private fun setData() {
        activity?.let {
            adapter = ShopByBrandsProductsAdapter(it, productsData)
            rvProducts?.layoutManager = LinearLayoutManager(activity)
            rvProducts?.adapter = adapter
            adapter?.setListener(this)
        }

    }

    override fun itemClicked(product_id: Int) {
        openProductVariantSheet(product_id)
    }

    private fun openProductVariantSheet(product_id: Int) {
        val sheet = ProductVariantSheet()
        val bundle = Bundle()
        bundle.putInt("product_id", product_id)
        sheet.arguments = bundle
        sheet.show(childFragmentManager, sheet.tag)
        sheet.setListener(this)
    }

    override fun singleVariantUpdate(id: Int, productQuant: Int) {
        showHideLoader(true)
        val products = ArrayList<VariantUpdate>()
        val update = VariantUpdate(id, productQuant,null)
        products.add(update)
        val cartData = RequestCartData(products)
        dataManager.editCart(object : ApiResponseListener<APICommonResponse<EditCartResponse>> {
            override fun onSuccess(response: APICommonResponse<EditCartResponse>) {
                if (response.isSuccess && response.data != null) {
                    response.data?.current_object?.let {
                        itemUpdated(it)
                    }

                }
                showHideLoader(false)
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Toast.makeText(activity, activity?.getString(R.string.error), Toast.LENGTH_SHORT)
                    .show()
            }
        }, cartData)
    }

    override fun itemUpdated(product: ProductsData) {
        val product_id = product?.id
        for (i in productsData.indices) {
            if (productsData[i].id == product_id) {
                productsData[i] = product
                adapter?.updateCart(i, product)
                break
            }
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

    companion object {
        @JvmStatic
        fun newInstance(): BrandCategoryFragment {
            val fragment = BrandCategoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}