package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.OrderDetailsUpload
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.other.PaymentActivity
import com.riggle.utils.showLongToast
import kotlinx.android.synthetic.main.activity_payment_page.*
import kotlinx.android.synthetic.main.layout_appbar.*

class PaymentActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private var orderDetails: OrderDetailsUpload? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_payment_page
    }

    override fun initializeViews(savedInstanceState: Bundle?) {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.proceed_to_pay)
        val bundle = intent.extras
        orderDetails = bundle?.getParcelable(KEY_ORDER_DETAILS)

        addOnClickListeners()

        fetchData()
    }

    private fun addOnClickListeners() {
        orderDetails?.let { orderDetails ->
            btn_pay.setOnClickListener {
                dataManager.addOrder(object : ApiResponseListener<APICommonResponse<String>> {
                    override fun onSuccess(response: APICommonResponse<String>) {
                        Toast.makeText(this@PaymentActivity, "Order placed", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onError(apiError: ApiError?) {}
                }, orderDetails)
            }
        } ?: run {
            showLongToast(this@PaymentActivity, getString(R.string.error_order_details))
        }

    }

    private fun fetchData() {}

    companion object {
        const val KEY_ORDER_DETAILS = "order_details"
        fun start(context: Context, details: OrderDetailsUpload?) {
            val bundle = Bundle()
            bundle.putParcelable(KEY_ORDER_DETAILS, details)
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}