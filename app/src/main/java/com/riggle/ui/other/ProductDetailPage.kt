package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.ProductCartRequest
import com.riggle.data.models.request.RequestToAddCart
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.bottomsheets.ComboBottomSheet
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.fragment.CartFragment
import com.riggle.ui.listener.ProductChooseListener
import com.riggle.ui.other.adapter.ProductDetailUnitsAdapter
import com.riggle.ui.other.adapter.ProductDetailUnitsAdapter.ProductUnitListener
import com.riggle.ui.other.adapter.ProductDetailVariantsAdapter
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.appBar
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlinx.android.synthetic.main.layout_plus_minus_item_value.*
import kotlinx.android.synthetic.main.layout_plus_minus_item_value.view.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ProductDetailPage : CustomAppCompatActivityViewImpl(), CustomAppViewConnector,
    ProductUnitListener, ProductChooseListener {

    private val userPreference: UserProfileSingleton by inject()

    private var product_id = 0
    private var mainData: ProductsData? = null
    var unitAdapter: ProductDetailUnitsAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
        appBar?.findViewById<View>(R.id.toolbar)?.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    override fun setView(): Int {
        return R.layout.activity_product_detail
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.product)

        ivCartView.setOnClickListener {
            //HomeActivity.start(this, true)
            val intent = CartFragment.newIntent(this)
            startActivity(intent)
        }

        val bundle = intent.extras
        product_id = bundle?.getInt(KEY_PRODUCT_ID, -1) ?: -1
        if (product_id != -1) fetchData()
    }

    private fun fetchData() {
        dataManager.getProductDetail(
            object :
                ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {
                    if (response != null) {
                        mainData =
                            Gson().fromJson(response.toString(), ProductsData::class.java)
                        //mainData = response
                        setItems()
                        setVariantData()
                        itemClicked(0)
                    }
                }

                override fun onError(apiError: ApiError?) {
                    Toast.makeText(
                        this@ProductDetailPage,
                        apiError?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            product_id,
            "schemes.free_product,images,combo_products.products",
            userPreference.userData?.retailer?.id.toString()
        )
    }

    private fun setItems() {
        val itemList = ArrayList<String>()
        /*mainData?.units?.let { units ->
            for (i in units.indices) {
                itemList.add(units[i].productSize)
            }
        }*/
        mainData?.schemes?.let { units ->
            for (i in units.indices) {
                itemList.add(units[i].max_quantity.toString())
            }
        }

        if (itemList.size > 0) {
            unitAdapter = ProductDetailUnitsAdapter(this, itemList)
            rvUnits?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvUnits?.adapter = unitAdapter
            unitAdapter?.setListener(this)
        }
    }

    private fun setVariantData() {
        mainData?.variants?.let { mainDataVariants ->
            val adapter = ProductDetailVariantsAdapter(this, mainDataVariants, product_id)
            rvVariants?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvVariants?.adapter = adapter
        }

    }

    private fun setData(productsData: ProductsData?) {
        productsData?.let {
            createImageSlider(productsData.images)
            //createImageSlider(productsData.image_arr)
            tvProductCategory?.text = productsData.description
            if (productsData.discount != null && productsData.discount != 0) {
                tvDiscountOff?.visibility = View.VISIBLE
                //tvDiscountOff.setText("" + (100 * (productsData.getDiscount()) / productsData.getRetailer_price()) + "% OFF");
                tvDiscountOff?.text =
                    String.format(getString(R.string.value_off), productsData.discount)
            } else {
                tvDiscountOff?.visibility = View.INVISIBLE
            }
            tvProductName?.text = productsData.name
            tvRiggleCoins?.text =
                String.format(getString(R.string.earn_value), productsData.riggle_coins)
            val strikePrice = productsData.strikePrice
            if (strikePrice != null && strikePrice != 0) {
                tvStrikePrice?.text =
                    String.format(getString(R.string.rupees_value), productsData.strikePrice)
                tvStrikePrice?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvStrikePrice?.visibility = View.VISIBLE
            } else tvStrikePrice?.visibility = View.GONE

            productsData.schemes?.let {
                if (it.size > 0) {

                    tvPrice?.text =
                        String.format(getString(R.string.rupees_value_double), it[0].rate) + "/Unit"

                    val profit: Float? = productsData.base_rate?.toFloat() - it[0].rate
                    if (profit != null) {
                        tvProfit?.text =
                            String.format(getString(R.string.rupees_value_profits), profit)
                        tvProfit?.visibility = View.VISIBLE
                    } else {
                        tvProfit?.visibility = View.GONE
                    }
                }
            }

            tvMRP?.text =
                String.format(
                    getString(R.string.rupees_value),
                    productsData.base_rate?.toFloat().roundToInt()
                )
            /*val profit = productsData.profit
            if (profit != null) {
                tvProfit?.text =
                    String.format(getString(R.string.rupees_value_profit), productsData.profit)
                tvProfit?.visibility = View.VISIBLE
            } else {
                tvProfit?.visibility = View.GONE
            }*/

            //wv.loadData(productsData.getProductDetail(), "text/html", "UTF-8");
            val text =
                ("<html><style type='text/css'>@font-face { font-family: montserrat_regular; src: url('font/montserrat_regular.tff'); } body p {font-family: montserrat_regular;}</style>"
                        + "<body style=\"margin: 0; padding:8\" >" + "<p align=\"justify\" style=\"text-align: left; font-size: 16px; line-height: 28px; color: #292829; font-family: montserrat_regular;\">" + productsData.productDetail + "</p> " + "</body></html>")
            wv?.loadDataWithBaseURL("file:///android_res/", text, "text/html", "utf-8", null)
            tvMOQ?.text = "" + productsData.moq

            plusMinusLayout.tvQuantSet?.text = "" + productsData.moq
            tvDeliveryIn?.text =
                "Delivery in " + productsData.delivery_tat_days.toString() + " days"

            if (productsData.quantity != 0) {
                tvAdd?.visibility = View.GONE
                cvQuant?.visibility = View.VISIBLE
                plusMinusLayout?.visibility = View.VISIBLE
                plusMinusLayout.tvQuantSet?.text = "" + productsData.quantity
            } else {
                tvAdd?.visibility = View.VISIBLE
                cvQuant?.visibility = View.GONE
                plusMinusLayout?.visibility = View.GONE
            }

            ivPlus?.setOnClickListener {
                if (productsData.combo_products != null && productsData.combo_products.size > 0) {
                    val sheet = ComboBottomSheet()
                    val bundle = Bundle()
                    productsData.id?.let {
                        bundle.putInt("product_id", it)
                    }
                    bundle.putString("scheme", Gson().toJson(productsData.combo_products))
                    sheet.arguments = bundle
                    sheet.show(supportFragmentManager, sheet.tag)
                    sheet.isCancelable = false
                    sheet.setListener(this)
                } else {
                    plusMinusLayout.tvQuantSet?.text =
                        "" + (plusMinusLayout.tvQuantSet?.text.toString()
                            .toInt() + 1 * productsData.moq)
                    addItem(productsData.id, plusMinusLayout.tvQuantSet?.text.toString().toInt())
                }
            }
            ivMinus?.setOnClickListener {
                if (productsData.combo_products != null && productsData.combo_products.size > 0) {
                    val sheet = ComboBottomSheet()
                    val bundle = Bundle()
                    productsData.id?.let {
                        bundle.putInt("product_id", it)
                    }
                    bundle.putString("scheme", Gson().toJson(productsData.combo_products))
                    sheet.arguments = bundle
                    sheet.show(supportFragmentManager, sheet.tag)
                    sheet.isCancelable = false
                    sheet.setListener(this)
                } else {
                    if (plusMinusLayout.tvQuantSet?.text.toString().toInt() != 0) {
                        plusMinusLayout.tvQuantSet?.text =
                            "" + (plusMinusLayout.tvQuantSet?.text.toString()
                                .toInt() - 1 * productsData.moq)
                        addItem(
                            productsData.id,
                            plusMinusLayout.tvQuantSet?.text.toString().toInt()
                        )
                    }
                }
            }
            tvAdd?.setOnClickListener {
                if (productsData.combo_products != null && productsData.combo_products.size > 0) {
                    val sheet = ComboBottomSheet()
                    val bundle = Bundle()
                    productsData.id?.let {
                        bundle.putInt("product_id", it)
                    }
                    bundle.putString("scheme", Gson().toJson(productsData.combo_products))
                    sheet.arguments = bundle
                    sheet.show(supportFragmentManager, sheet.tag)
                    sheet.isCancelable = false
                    sheet.setListener(this)
                } else {
                    disableProductUpdateButtons()
                    tvAdd?.visibility = View.GONE
                    cvQuant?.visibility = View.VISIBLE
                    plusMinusLayout?.visibility = View.VISIBLE
                    addItem(productsData.id, productsData.moq)
                }
            }
        }

    }

    private fun disableProductUpdateButtons() {
        tvAdd?.isEnabled = false
        ivMinus?.isEnabled = false
        ivPlus?.isEnabled = false
    }

    private fun enableProductUpdateButtons() {
        tvAdd?.isEnabled = true
        ivMinus?.isEnabled = true
        ivPlus?.isEnabled = true
    }

    private fun addItem(id: Int, quant: Int) {
        showHideLoader(true)
        val products = ArrayList<VariantUpdate>()
        val update = VariantUpdate(id, quant, null)
        products.add(update)
        val cartRequest = ProductCartRequest(products)
        userPreference.userData?.retailer?.id?.let {
            showHideLoader(true)
            val cartData = RequestToAddCart(it, products)
            dataManager.addCartItems(object :
                ApiResponseListener<List<APICommonResponse<ProductsData>>> {
                override fun onSuccess(response: List<APICommonResponse<ProductsData>>) {
                    enableProductUpdateButtons()
                    if (response != null) {
                        /*response.data?.current_object?.let {
                            itemUpdated(it)
                        }*/
                    }
                    if (quant == 0) {
                        tvAdd?.visibility = View.VISIBLE
                        cvQuant?.visibility = View.GONE
                        plusMinusLayout?.visibility = View.GONE
                    }
                    showHideLoader(false)
                    if (userPreference.sharedPreferencesUtil.cartCount > 0) {
                        tvCartCount.visibility = View.VISIBLE
                    } else {
                        getCartData()
                    }
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

        /* val products = ArrayList<VariantUpdate>()
         val update = VariantUpdate(id, quant)
         products.add(update)
         val cartData = RequestCartData(products)
         dataManager.editCart(object : ApiResponseListener<APICommonResponse<EditCartResponse>> {
             override fun onSuccess(response: APICommonResponse<EditCartResponse>) {
                 showHideLoader(false)
                 enableProductUpdateButtons()
                 if (response.isSuccess && response.data != null) {

                     if (response.data?.parent_object != null) {
                         val updateProdOnHome = UpdateProdOnHome(response.data?.parent_object!!)
                         GlobalBus.bus?.post(updateProdOnHome)
                     } else {
                         response.data?.current_object?.let {  currentObject ->
                             val updateProdOnHome = UpdateProdOnHome(currentObject)
                             GlobalBus.bus?.post(updateProdOnHome)
                         }

                     }
                     setData(response.data?.current_object)
                     mainData?.units?.let {
                             for (i in it.indices) {
                                 if (it[i].id == id) {
                                     it[i].item_cart = quant
                                 }
                             }
                     }

                 } else {
                     Toast.makeText(
                         activity,
                         getString(R.string.error) + ": " + response.message,
                         Toast.LENGTH_SHORT
                     ).show()
                 }
             }

             override fun onError(apiError: ApiError?) {
                 enableProductUpdateButtons()
                 showHideLoader(false)
                 Toast.makeText(activity, getString(R.string.error), Toast.LENGTH_SHORT)
                     .show()
             }
         }, cartData)*/
    }

    private fun getCartData() {
        dataManager.fetchCart(object : ApiResponseListener<ResponseCartData> {
            override fun onSuccess(response: ResponseCartData) {
                response.let {
                    if (it.products_in_cart.isNotEmpty()) {
                        tvCartCount.visibility = View.VISIBLE
                        userPreference.sharedPreferencesUtil.cartCount = it.products_in_cart.size
                    } else {
                        userPreference.sharedPreferencesUtil.cartCount = 0
                        tvCartCount.visibility = View.GONE
                    }
                }
            }

            override fun onError(apiError: ApiError?) {

            }
        }, userPreference.userData?.retailer?.id ?: 0, "banner_image,service_hub,brand")
    }

    val imageData = ArrayList<SlideModel>()
    private fun createImageSlider(image_url: ArrayList<BannerImage>) {
        if (imageData != null) {
            imageData.clear()
        }
        image_url?.let {
            /* val imageData = ArrayList<SlideModel>()*/
            for (i in it.indices) {
                imageData.add(SlideModel(image_url[i]?.image, "", ScaleTypes.CENTER_INSIDE))
            }
            isProduct?.setImageList(imageData)
        }

        isProduct.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here
                val intent = ImageDetailsActivity.newIntent(this@ProductDetailPage)
                intent.putExtra("image_list",Gson().toJson(imageData))
                intent.putExtra("position",position)
                startActivity(intent)
            }
        })

    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.hide()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

    override fun itemClicked(pos: Int) {
        /*mainData?.units?.let {
            setData(it[pos])
            unitAdapter?.selectedPosition = pos
            unitAdapter?.notifyDataSetChanged()
        }*/
        setData(mainData)

    }

    companion object {
        const val KEY_PRODUCT_ID = "product_id"
        fun start(context: Context, product_id: Int) {
            val bundle = Bundle()
            bundle.putInt(KEY_PRODUCT_ID, product_id)
            val intent = Intent(context, ProductDetailPage::class.java)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        if (userPreference.sharedPreferencesUtil.cartCount > 0) {
            tvCartCount.visibility = View.VISIBLE
        } else {
            tvCartCount.visibility = View.GONE
        }
    }

    override fun itemUpdated(scheme: SchemesBean, pos: Int) {

    }
}