package com.riggle.ui.home.pendingDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.RequestComboUpdate
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.databinding.ListPendingOrderDetailBinding
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.bottomsheets.CancelBottomSheet
import com.riggle.ui.bottomsheets.ComboBottomSheet
import com.riggle.ui.bottomsheets.UpdateComboSheet
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.utils.Constants
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.events.SingleRequestEvent
import com.riggle.utils.events.Status
import kotlinx.android.synthetic.main.activity_pending_details.*
import kotlinx.android.synthetic.main.activity_pending_details.tvCartPrice
import kotlinx.android.synthetic.main.activity_pending_details.tvDiscountValue
import kotlinx.android.synthetic.main.activity_pending_details.tvTotalAmountValue
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PendingOrderDetails : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private var loaderDialog: LoadingDialog? = null
    private val userPreference: UserProfileSingleton by inject()
    private var orderId = 0

    companion object {
        fun newIntent(activity: Activity): Intent? {
            val intent = Intent(activity, PendingOrderDetails::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
        var obrComboSelect = SingleRequestEvent<ArrayList<VariantUpdate>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_pending_details
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.order_details)
        rlCartIcon.visibility = View.GONE
        intent?.getIntExtra("order_id", 0)?.let {
            orderId = it
        }

        loaderDialog = LoadingDialog(activity)

        obrComboSelect.observe(this, androidx.lifecycle.Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    it.data?.let { data -> apiUpdateCalls(data) }
                }
            }
        })

        setListener()
        setUpOrderProductList()
        fetchData()
    }

    private fun apiUpdateCalls(data: ArrayList<VariantUpdate>) {
        dataManager.editComboProductItem(object :
            ApiResponseListener<List<ComboUpdateResponse>> {
            override fun onSuccess(response: List<ComboUpdateResponse>) {
                if (response != null) {
                    fetchData()
                }
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(
                    activity,
                    apiError?.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }, orderId,RequestComboUpdate(data))
    }

    private fun setListener() {
        ivNotification.setOnClickListener {
            openMenu(it)
        }
    }

    private fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) if (state) loaderDialog?.show() else loaderDialog?.dismiss()
    }

    private fun editProductData(id: Int, quantity: Int) {
        val query = HashMap<String, String>()
        query["quantity"] = quantity.toString()

        dataManager.editProductItem(object :
            ApiResponseListener<ProductResponse> {
            override fun onSuccess(response: ProductResponse) {
                if (response != null) {
                    fetchData()
                }
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(
                    activity,
                    apiError?.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }, getAuthorization(), orderId, id, query)
    }

    private fun fetchData() {
        showHideLoader(true)
        val query = HashMap<String, String>()
        //query["expand"] = "service_hub,products.product,products.free_product"
        query["expand"] =
            "service_hub,products.product.banner_image,products.free_product,products.product_combo.products"
        query["edit_view"] = "true"

        dataManager.getOrderDetails(object :
            ApiResponseListener<OrderDetailsResponse> {
            override fun onSuccess(response: OrderDetailsResponse) {

                if (response != null) {
                    Constants.DataKeys.isDeliver = response.status.equals("delivered", true)
                    updateView(response)
                    if (response.products.isNotEmpty()) {
                        productAdapter?.list = response.products
                    }
                }
                showHideLoader(false)
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Toast.makeText(
                    activity,
                    apiError?.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }, getAuthorization(), orderId, query)
    }

    private fun updateView(response: OrderDetailsResponse) {
        setDateVale(tvDate, response.delivery_date)
        tvCartPrice?.text = "₹" + response.final_amount
        tvDiscountValue?.text = "₹" + (response?.amount - response?.final_amount)
        tvTotalAmountValue?.text = "₹" + response.final_amount
    }

    private fun setDateVale(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "Date : $nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    var productAdapter: SimpleRecyclerViewAdapter<ProductsData, ListPendingOrderDetailBinding>? =
        null

    private fun setUpOrderProductList() {
        productAdapter = SimpleRecyclerViewAdapter<ProductsData, ListPendingOrderDetailBinding>(
            R.layout.list_pending_order_detail,
            BR.bean
        ) { v, m, pos ->
            when (v?.id) {
                R.id.ivMinus,R.id.tvUpdateMix -> {
                    if (m.units != null) {
                        /*if (m.quantity > 0) {
                            if (m.quantity - m.product_combo.step < 0)
                                return@SimpleRecyclerViewAdapter
                            m.quantity = m.quantity - m.product_combo.step
                        }
                        productAdapter?.notifyDataSetChanged()
                        viewModel.editProductQty(getAuthorization(), orderId, m.id, m.quantity)*/
                        showComboSheet(m)
                    } else {
                        if (m.quantity > 0) {
                            m.quantity = m.quantity - m.product?.moq!!
                        }
                        productAdapter?.notifyDataSetChanged()
                        editProductData(m.id, m.quantity)
                        //viewModel.editProductQty(getAuthorization(), orderId, m.id, m.quantity)
                    }
                }
                R.id.ivPlus, R.id.tvAdd -> {
                    if (m.units != null) {
                        /*m.quantity = m.quantity + m.product_combo.step
                        productAdapter?.notifyDataSetChanged()
                        viewModel.editProductQty(getAuthorization(), orderId, m.id, m.quantity)*/
                        showComboSheet(m)
                    } else {
                        m.quantity = m.quantity + m.product?.moq!!
                        productAdapter?.notifyDataSetChanged()
                        editProductData(m.id, m.quantity)
                        //viewModel.editProductQty(getAuthorization(), orderId, m.id, m.quantity)
                    }
                }
            }
        }
        val layoutManager = LinearLayoutManager(this)
        rvProducts.layoutManager = layoutManager
        rvProducts.adapter = productAdapter
        //productAdapter.list = getListOne()
    }

    private fun showComboSheet(m: ProductsData?) {
        m?.let { m ->
            val sheet = UpdateComboSheet()
            sheet.show(supportFragmentManager, sheet.tag)
            val bundle = Bundle()
            bundle.putBoolean("is_update", true)
            m.banner_image?.image?.let {
                bundle.putString("banner_img", it)
            }
            m.units?.let { products ->
                bundle.putInt("product_id", products[0].id!!)
                bundle.putString(
                    "scheme",
                    Gson().toJson(ArrayList<ComboProducts>().apply {
                        add(
                            ComboProducts(
                                m.code,
                                m.created_at,
                                products[0].id!!,
                                m.is_active,
                                m.name,
                                products,
                                products[0].product?.moq!!,
                                m.update_url,
                                m.updated_at
                            )
                        )
                    })
                )
            }
            sheet.arguments = bundle
            sheet.isCancelable = false
        }
    }

    private fun getAuthorization(): String {
        var authorization = ""
        userPreference.userData?.let {
            authorization = it.session_key
        }
        return authorization
    }

    private fun openMenu(view: View) {
        val menu = PopupMenu(this, view)
        //menu.menu.add("Call Sp")
        menu.menu.add("Cancel order")
        menu.setOnMenuItemClickListener {
            when (it.title.toString()) {
                /*"Call Sp" -> {
                }*/
                "Cancel order" -> {
                    val sheet = CancelBottomSheet()
                    val bundle = Bundle()
                    intent?.getIntExtra("order_id", 0)?.let {
                        bundle.putInt("order_id", it)
                    }
                    sheet.arguments = bundle
                    sheet.show(supportFragmentManager, sheet.tag)
                    sheet.isCancelable = false
                }
            }
            true
        }
        menu.show()
    }

}