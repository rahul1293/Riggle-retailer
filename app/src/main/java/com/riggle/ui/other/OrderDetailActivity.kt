package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.google.gson.Gson
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.MyOrderDataOuter
import com.riggle.data.models.response.OrderDetail
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.HomeActivity.Companion.start
import com.riggle.ui.other.adapter.MyOrderDetailAdapter
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.Utility
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject

class OrderDetailActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private val userPreference: UserProfileSingleton by inject()

    private var loadingDialog: LoadingDialog? = null
    private var cart_id = -1

    //private var orderDetail: OrderDetail? = null
    private var orderDetail: MyOrderDataOuter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)

        addOnClickListeners()
    }

    private fun addOnClickListeners() {

        ivCartView.setOnClickListener {
            HomeActivity.start(this, true)
        }

        btnGoToOrders.setOnClickListener {
            MyOrdersActivity.Companion.start(this)
        }

        btnContinue.setOnClickListener {
            start(this, false)
            finish()
        }

        tvSeeInvoice.setOnClickListener {
            orderDetail?.challanFile?.let {
                val url =
                    "https://assets.riggleapp.in/orders/8b120e0f-def9-407b-a68e-56c2e1435d38.pdf"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(it)
                startActivity(i)
            }
        }

    }

    override fun setView(): Int {
        return R.layout.activity_order_detail
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.order_details)
        val bundle = intent.extras
        if (bundle != null) {
            cart_id = bundle.getInt(KEY_CART_ID, -1)
            orderDetail =
                Gson().fromJson(bundle.getString(KEY_ORDER_DETAILS), MyOrderDataOuter::class.java)
        }
        //if (cart_id != -1) fetchData()

        orderDetail?.let {
            setData()
        }

    }

    private fun fetchData() {
        showHideLoader(true)
        dataManager.getOrderDetail(object : ApiResponseListener<APICommonResponse<OrderDetail>> {
            override fun onSuccess(response: APICommonResponse<OrderDetail>) {
                if (response.isSuccess && response.data != null) {
                    //orderDetail = response.data
                    setData()
                }
                showHideLoader(false)
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
            }
        }, cart_id)
    }

    private fun setData() {
        orderDetail?.let { orderDetail ->

            setTrackingStatus(orderDetail.status)
            //setTrackingStatus(orderDetail.tracking_status)

            val adapter = MyOrderDetailAdapter(this, orderDetail.products)
            rvProducts?.layoutManager = LinearLayoutManager(this)
            rvProducts?.adapter = adapter
            tvPriceItems?.text = String.format(
                getString(R.string.price_value_items),
                orderDetail.products.size
            )
            tvPrice?.text = orderDetail.amount.toString()
            tvDiscountValue?.text = orderDetail.riggleCoins.toString()
            tvRiggleCoins?.text = orderDetail.redeemedRiggleCoins.toString()
            tvTotalAmountValue?.text = orderDetail.finalAmount.toString()

            tvPaidAmount?.text = orderDetail.paidAmount.toString()
            tvPendingAmount?.text = orderDetail.pendingAmount.toString()

            tvMode?.text = "Cash"//orderDetail.price_detail.payment_mode
            tvStoreName?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.USER_NAME)
            tvStoreAddress?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.ADDRESS)
            tvStoreMobile?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.MOBILE)

            orderIdTextView.text = "Order ID: ${orderDetail.code.toString()}"

            /*if (!orderDetail.tracking_status.isNullOrEmpty()) {
                when (orderDetail.tracking_status.toInt()) {
                    (-1) -> {
                        textView7.text = "Cancelled on:\n${orderDetail.delivery_date}"
                    }
                    else -> {
                        textView7.text = "Delivery on:\n${orderDetail.delivery_date}"
                    }
                }
            }*/

            if (orderDetail.status.equals("delivered", false)) {
                textView7.text = "Delivery on:\n${Utility.convertDate(orderDetail.deliveredAt)}"
            } else
                textView7.text = "Delivery on:\n${Utility.convertDate(orderDetail.deliveryDate)}"

            //textView7.text = "Delivery on:\n${Utility.convertDate(orderDetail.deliveryDate)}"
            tvPlaceDate.text = "Placed on:\n${Utility.convertDate(orderDetail.placedAt)}"
            if (orderDetail.challanFile == null || orderDetail.challanFile.equals("")) {
                tvSeeInvoice.visibility = View.GONE
            } else {
                tvSeeInvoice.visibility = View.VISIBLE
            }
        }

    }

    private fun setTrackingStatus(status: String?) {

        if (!status.isNullOrEmpty()) {
            when (status) {

                "pending"/*1*/ -> {
                    trackingLineOne.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorGreyTrackingStatus
                        )
                    )

                    inTransitFrameLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorGreyTrackingStatusBackground
                        )
                    )
                    inTransitImageView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorGreyTrackingStatus
                        )
                    )
                    inTransitImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    inTransitFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)

                    deliveredFrameLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorGreyTrackingStatusBackground
                        )
                    )
                    deliveredImageView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorGreyTrackingStatus
                        )
                    )
                    deliveredImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    deliveredFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)
                }

                "confirmed"/*2*/ -> {
                    trackingLineOne.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorPrimary
                        )
                    )

                    inTransitImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    inTransitFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)

                    ViewCompat.setBackgroundTintList(
                        inTransitFrameLayout,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorYellow_34
                        )
                    )
                    ViewCompat.setBackgroundTintList(
                        inTransitImageView,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorYellow
                        )
                    )

                    deliveredImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    deliveredFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)

                }

                "delivered"/*3*/ -> {
                    trackingLineOne.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorPrimary
                        )
                    )
                    trackingLineTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorYellow
                        )
                    )

                    inTransitFrameLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorYellow_34
                        )
                    )
                    inTransitImageView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorYellow
                        )
                    )
                    inTransitImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    inTransitFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)

                    deliveredImageView.setBackgroundResource(R.drawable.shape_circle_white)
                    deliveredFrameLayout.setBackgroundResource(R.drawable.shape_circle_white)



                    ViewCompat.setBackgroundTintList(
                        inTransitFrameLayout,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorYellow_34
                        )
                    )
                    ViewCompat.setBackgroundTintList(
                        inTransitImageView,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorYellow
                        )
                    )

                    ViewCompat.setBackgroundTintList(
                        deliveredFrameLayout,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorSuccess_30
                        )
                    )
                    ViewCompat.setBackgroundTintList(
                        deliveredImageView,
                        ContextCompat.getColorStateList(
                            this@OrderDetailActivity,
                            R.color.colorSuccess
                        )
                    )

                }
                ("cancelled"/*-1*/) -> {
                    textView6.text = resources.getString(R.string.cancelled)
                    inTransitFrameLayout.visibility = View.GONE
                    trackingLineTwo.visibility = View.GONE
                    tvTransit.visibility = View.GONE
                }

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
        const val KEY_CART_ID = "cart_id"
        const val KEY_ORDER_DETAILS = "order_details"
        fun start(context: Context, cart_id: Int?, order: String) {
            val bundle = Bundle()
            cart_id?.let {
                bundle.putInt(KEY_CART_ID, it)
                bundle.putString(KEY_ORDER_DETAILS, order)
            }
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (loadingDialog != null) {
            loadingDialog!!.hide()
        }
    }
}