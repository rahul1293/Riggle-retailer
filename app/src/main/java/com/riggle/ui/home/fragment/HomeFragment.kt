package com.riggle.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.RequestCartData
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.bottomsheets.ProductVariantSheet
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.adapters.HomeBrandAdapter
import com.riggle.ui.home.adapters.HomeCategoryAdapter
import com.riggle.ui.home.adapters.HomeProductsAdapter
import com.riggle.ui.listener.ProductVariantListener
import com.riggle.ui.other.SearchActivity
import com.riggle.ui.other.ShopByBrandCategory
import com.riggle.ui.other.registration.WelcomeScreen
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.ivCartView
import kotlinx.android.synthetic.main.fragment_home.tvRiggleCoins
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector,
    HomeProductsAdapter.HomeProductsListener, ProductVariantListener {

    private var preX: Float = 0f
    private var preY: Float = 0f
    private val Y_BUFFER = 10

    private val userPreference: UserProfileSingleton by inject()

    private var topPickData = arrayListOf<ProductsData>()
    private var brandsData = arrayListOf<BrandsCategoryData>()
    private var categoriesData = arrayListOf<BrandsCategoryData>()
    private var productsData = arrayListOf<ProductsData>()
    private var productsAdapter: HomeProductsAdapter? = null
    private var topPickAdapter: HomeProductsAdapter? = null
    private var loaderDialog: LoadingDialog? = null
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
        return R.layout.fragment_home
    }

    override fun initializeViews(savedInstanceState: Bundle?) {

        rlBrands?.visibility = View.VISIBLE

        /*tvStoreName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STORE_NAME)*/
        tvStoreName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.USER_NAME)
        loaderDialog = LoadingDialog(activity)
        //getProductsAPI("top")
        //brands
        //category
        //getProductsAPI("all")

        addOnClickListeners()

        /*For brand list*/
        //populateBrandView()

    }

    private fun addOnClickListeners() {
        tvBrandsViewAll.setOnClickListener {
            activity?.let {
                ShopByBrandCategory.start(it, ShopByBrandCategory.KEY_BRAND_PAGE, brandsData, 0)
            }
        }

        tvCategoryViewAll.setOnClickListener {
            activity?.let {
                ShopByBrandCategory.start(
                    it,
                    ShopByBrandCategory.KEY_CATEGORY_PAGE,
                    categoriesData,
                    0
                )
            }
        }

        tvTopPicksViewAll.setOnClickListener {
            rlProducts?.top?.let { top ->
                homeNestedScrollView?.smoothScrollTo(0, top)
            }
        }

        ivSearch.setOnClickListener {
            activity?.let {
                SearchActivity.start(it)
            }
        }

        ivCartView.setOnClickListener {
            //HomeActivity.start(this,true)
            val intent = CartFragment.newIntent(requireActivity())
            startActivity(intent)
        }

    }


    override fun onResume() {
        super.onResume()
        tvStoreName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.USER_NAME)
        getDetails()
        userPreference.userData?.let {
            if (it.retailer.is_serviceable) {
                rlBrands?.visibility = View.VISIBLE
                tvEmpty?.visibility = View.GONE
                brands
            } else {
                rlBrands?.visibility = View.GONE
                tvEmpty?.visibility = View.VISIBLE
            }
        }

        if (userPreference.sharedPreferencesUtil.cartCount > 0) {
            tvCartBadge.visibility = View.VISIBLE
        } else {
            getCartData()
        }

    }

    private fun getCartData() {
        dataManager.fetchCart(object : ApiResponseListener<ResponseCartData> {
            override fun onSuccess(response: ResponseCartData) {
                showHideLoader(false)
                response.let {
                    if (it.products_in_cart.isNotEmpty()) {
                        tvCartBadge.visibility = View.VISIBLE
                        userPreference.sharedPreferencesUtil.cartCount = it.products_in_cart.size
                    } else {
                        tvCartBadge.visibility = View.GONE
                    }
                }
            }

            override fun onError(apiError: ApiError?) {

            }
        }, userPreference.userData?.retailer?.id ?: 0, "banner_image,service_hub,brand")
    }

    private fun getProductsAPI(type: String) {
        dataManager.getProducts(object :
            ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>> {
            override fun onSuccess(response: APICommonResponse<ArrayList<ProductsData>>) {
                response?.data?.let {
                    if (response.isSuccess && response.data != null && it.size > 0) {
                        response.data?.let { dataList ->
                            when (type) {
                                "top" -> {
                                    topPickData = dataList
                                    populateTopPicksView()
                                }
                                "all" -> {
                                    productsData = dataList
                                    populateProductsView()
                                }
                            }
                        }

                    }
                }

            }

            override fun onError(apiError: ApiError?) {

            }
        }, type)
    }

    private val brands: Unit
        private get() {

            val data = HashMap<String, String>()
            data.put("page", "1")
            data.put("type", "main")
            data.put("is_active", "True")
            dataManager.getCategoryList(object :
                ApiResponseListener<APICommonResponse<List<BrandsCategoryData>>> {
                override fun onSuccess(response: APICommonResponse<List<BrandsCategoryData>>) {
                    response.results?.let {
                        if (it.size > 0) {
                            brandsData = it as ArrayList<BrandsCategoryData>
                            populateBrandView()
                        }
                    }
                }

                override fun onError(apiError: ApiError?) {
                    Log.i("Tag", ":::::::")
                }
            }, data)
        }
    val totalRiggleCoins: Unit
        get() {
            dataManager.getTotalRegalCoins(object :
                ApiResponseListener<APICommonResponse<RiggleCoinsResponse>> {
                override fun onSuccess(response: APICommonResponse<RiggleCoinsResponse>) {
                    response.data?.let {
                        if (response.isSuccess) {
                            tvRiggleCoins?.text = "" + it.riggleCointBalance
                        }
                    }

                }

                override fun onError(apiError: ApiError?) {}
            })
        }
    private val category: Unit
        private get() {
            dataManager.getCategories(object :
                ApiResponseListener<APICommonResponse<ArrayList<BrandsCategoryData>>> {
                override fun onSuccess(response: APICommonResponse<ArrayList<BrandsCategoryData>>) {
                    response.data?.let { responseData ->
                        if (response.isSuccess && responseData.size > 0) {
                            categoriesData = responseData
                            populateCategoryView()
                        }
                    }

                }

                override fun onError(apiError: ApiError?) {}
            })
        }

    private fun populateTopPicksView() {

        activity?.let {
            rlTopPicks?.visibility = View.VISIBLE
            topPickAdapter = HomeProductsAdapter(it, topPickData)
            (Objects.requireNonNull(rvTopPicks?.itemAnimator) as SimpleItemAnimator).supportsChangeAnimations =
                false
            rvTopPicks?.layoutManager = LinearLayoutManager(activity)
            rvTopPicks?.adapter = topPickAdapter
            topPickAdapter?.setListener(this)
        }

    }

    private fun populateBrandView() {
        activity?.let {
            rlBrands?.visibility = View.VISIBLE
            val brandAdapter = HomeBrandAdapter(it, brandsData)
            rvBrands?.layoutManager = GridLayoutManager(activity, 2)
            rvBrands?.adapter = brandAdapter
        }
    }

    private fun populateCategoryView() {
        activity?.let {
            rlCategories?.visibility = View.VISIBLE
            val categoriesAdapter = HomeCategoryAdapter(it, categoriesData)
            rvCategories?.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvCategories?.adapter = categoriesAdapter

            rvCategories.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

                override fun onTouchEvent(view: RecyclerView, event: MotionEvent) {}

                override fun onInterceptTouchEvent(
                    view: RecyclerView,
                    event: MotionEvent
                ): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            rvCategories.parent?.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (Math.abs(event.x - preX) > Math.abs(event.y - preY)) {
                                rvCategories.parent.requestDisallowInterceptTouchEvent(true)
                            } else if (Math.abs(event.y - preY) > Y_BUFFER) {
                                rvCategories.parent.requestDisallowInterceptTouchEvent(false)
                            }
                        }
                    }
                    preX = event.x
                    preY = event.y
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }

    }

    private fun populateProductsView() {
        activity?.let {
            rlProducts?.visibility = View.VISIBLE
            productsAdapter = HomeProductsAdapter(it, productsData)
            (Objects.requireNonNull(rvProducts?.itemAnimator) as SimpleItemAnimator).supportsChangeAnimations =
                false
            rvProducts?.layoutManager = LinearLayoutManager(activity)
            rvProducts?.adapter = productsAdapter
            productsAdapter?.setListener(this)
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

    override fun itemUpdated(product: ProductsData) {
        val product_id = product.id
        for (i in productsData.indices) {
            if (productsData[i].id == product_id) {
                productsData[i] = product
                productsAdapter?.updateCart(i, product)
                break
            }
        }


        for (i in topPickData.indices) {
            if (topPickData[i].id == product_id || topPickData[i].id == product.siblingID) {
                topPickData[i] = product
                topPickAdapter?.updateCart(i, product)
                break
            }
        }
    }

    override fun singleVariantUpdate(id: Int, productQuant: Int) {
        showHideLoader(true)
        val products = ArrayList<VariantUpdate>()
        val update = VariantUpdate(id, productQuant, null)
        products.add(update)
        val cartData = RequestCartData(products)
        dataManager.addCart(object : ApiResponseListener<APICommonResponse<ProductsData>> {
            override fun onSuccess(response: APICommonResponse<ProductsData>) {
                if (response.isSuccess) {
                    response.data?.let {
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

    private fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) if (state) loaderDialog?.show() else loaderDialog?.hide()
    }


    fun refreshData() {
        getProductsAPI("top")
        getProductsAPI("all")
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        //getDetails()
    }

    private fun getDetails() {
        userPreference.userData?.retailer?.id?.let {
            dataManager.getPingDetails(
                object :
                    ApiResponseListener<JsonElement> {
                    override fun onSuccess(response: JsonElement) {
                        response?.let {
                            var usrData =
                                Gson().fromJson(it.toString(), UserDetails::class.java)
                            tvRiggleCoins.text = "" + usrData.riggle_coins_balance
                            if (usrData.is_serviceable) {
                                userPreference.userData?.let {
                                    if (!it.retailer.is_serviceable) {
                                        rlBrands?.visibility = View.VISIBLE
                                        tvEmpty?.visibility = View.GONE
                                        val userData = it
                                        userData.retailer.is_serviceable = usrData.is_serviceable
                                        userPreference
                                            .updateUserData(userData)
                                        userPreference
                                            .saveRetailerDetails(usrData)
                                    }
                                }
                            }
                        }
                    }

                    override fun onError(apiError: ApiError?) {
                        Log.i("TAG", "::::" + apiError?.message)
                    }
                },
                it, ""
            )
            //expand = sub_area
        }
    }

}