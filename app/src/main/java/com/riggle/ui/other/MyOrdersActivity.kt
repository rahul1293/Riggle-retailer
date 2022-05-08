package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.riggle.R
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.MyOrderDataOuter
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.other.adapter.MyOrdersAdapter
import com.riggle.ui.utils.WrapContentLinearLayoutManager
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.layout_appbar.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.HashMap

class MyOrdersActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private var page = 1
    private var totalPageCount = -1
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private val visibleThreshold = 2
    private var orderData: ArrayList<MyOrderDataOuter> = ArrayList<MyOrderDataOuter>()
    private var isApiCallRunning = false
    private var layoutManager: WrapContentLinearLayoutManager? = null
    private var adapter: MyOrdersAdapter? = null

    private val userPreference: UserProfileSingleton by inject()

    private var loaderDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_my_orders
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.my_orders)
        loaderDialog = LoadingDialog(activity)
        //populateLoaderRecyclerView(rvOrders);
        rlCartIcon.visibility = View.GONE
        fetchData()
        addOnClickListeners()
    }

    private fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) if (state) loaderDialog?.show() else loaderDialog?.dismiss()
    }

    private fun addOnClickListeners() {

        ivCartView.setOnClickListener {
            HomeActivity.start(this, true)
        }
        letsGoButton?.setOnClickListener { finish() }
    }

    private fun fetchData() {
        showHideLoader(true)
        isApiCallRunning = true
        userPreference.userData?.retailer?.id?.let {

            val params = HashMap<String, String>()
            params["retailer"] = it.toString()
            params["expand"] = "products.free_product,products.product.banner_image,service_hub"

            dataManager.getMyOrdersOne(object :
                ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {

                    showHideLoader(false)
                    /*val myType = object : TypeToken<List<String>>() {}.type
                    val value = Gson().fromJson<ArrayList<String>>(list, myType)*/

                    if (response != null) {
                        isApiCallRunning = false

                        var obj = JSONObject(response.toString())
                        var result = obj.get("results")
                        if (result != null) {
                            val myType = object : TypeToken<ArrayList<MyOrderDataOuter>>() {}.type
                            val values = Gson().fromJson<ArrayList<MyOrderDataOuter>>(
                                result.toString(),
                                myType
                            )
                            values?.let {
                                if (it.size > 0) {
                                    setData(it as ArrayList<MyOrderDataOuter>)
                                    emptyOrdersLinearLayout?.visibility = View.GONE
                                } else {
                                    emptyOrdersLinearLayout?.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {
                        isApiCallRunning = false
                    }
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    isApiCallRunning = false
                    emptyOrdersLinearLayout?.visibility = View.GONE
                }
            }, params/*it, "products.free_product,products.product.banner_image"*/)


            /*dataManager.getMyOrders(object :
                ApiResponseListener<APICommonResponse<List<MyOrderDataOuter>>> {
                override fun onSuccess(response: APICommonResponse<List<MyOrderDataOuter>>) {

                    showHideLoader(false)

                    *//*val myType = object : TypeToken<List<String>>() {}.type
                    val value = Gson().fromJson<ArrayList<String>>(list, myType)*//*

                    if (response!=null && response.results != null) {
                        isApiCallRunning = false
                        response.results?.let {
                            if (it.size>0){
                                setData(it as ArrayList<MyOrderDataOuter>)
                                emptyOrdersLinearLayout?.visibility = View.GONE
                            } else {
                                emptyOrdersLinearLayout?.visibility = View.VISIBLE
                            }
                        }
                        //totalPageCount = response.data?.total_count?:0
                        *//*response.data?.orders?.let {
                                if ( it.size > 0) {
                                    setData(it)
                                    emptyOrdersLinearLayout?.visibility = View.GONE
                                } else{
                                    emptyOrdersLinearLayout?.visibility = View.VISIBLE
                                }
                            }*//*

                        if(response.results == null  ){
                            emptyOrdersLinearLayout?.visibility = View.VISIBLE
                        }else{
                            emptyOrdersLinearLayout?.visibility = View.GONE
                        }

                    } else {
                        isApiCallRunning = false
                    }
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    isApiCallRunning = false
                    emptyOrdersLinearLayout?.visibility = View.GONE
                }
            }, it,"products.free_product,products.product")*/
        }
    }

    private fun setData(orders: ArrayList<MyOrderDataOuter>) {
        if (page == 1) {
            layoutManager = WrapContentLinearLayoutManager(this)
            orderData = orders
            adapter = MyOrdersAdapter(this, orderData)
            rvOrders?.layoutManager = layoutManager
            rvOrders?.adapter = adapter
        } else {
            orderData?.addAll(orders)
            adapter?.notifyDataSetChanged()
        }
        page++
        List_scrollListener_attach()
    }

    private fun List_scrollListener_attach() {
        rvOrders?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager?.let {
                    totalItemCount = it.itemCount
                    lastVisibleItem = it.findLastVisibleItemPosition()
                    if (!isApiCallRunning && totalItemCount <= lastVisibleItem + visibleThreshold && page <= totalPageCount) {
                        // start loading
                        fetchData()
                    }
                }

            }
        })
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MyOrdersActivity::class.java)
            context.startActivity(intent)
        }
    }


    /*{
        "id": 79,
        "update_url": "https://api.riggleapp.in/api/v1/core/orders/79/",
        "created_at": "2022-01-27T21:57:57.627062",
        "updated_at": "2022-01-29T11:28:48.832463",
        "code": "RO79",
        "doc_id": "8b120e0f-def9-407b-a68e-56c2e1435d38",
        "delivery_date": "2022-01-31T21:57:57.613419",
        "final_amount": 240.0,
        "amount": 240.0,
        "riggle_coins": 0,
        "redeemed_riggle_coins": 0,
        "paid_amount": 0.0,
        "pending_amount": 240.0,
        "payment_mode": null,
        "challan_file": "https://assets.riggleapp.in/orders/8b120e0f-def9-407b-a68e-56c2e1435d38.pdf",
        "cancellation_reason": null,
        "status": "confirmed",
        "placed_at": "2022-01-27T21:57:57.626985",
        "delivered_at": null,
        "cancelled_at": null,
        "confirmed_at": "2022-01-29T11:28:47.112597",
        "retailer": 29,
        "service_hub": 11,
        "products": [{
        "id": 167,
        "update_url": "https://api.riggleapp.in/api/v1/core/orders/79/products/167/",
        "created_at": "2022-01-27T21:57:57.634869",
        "updated_at": "2022-01-29T11:28:48.831460",
        "ordered_quantity": 30,
        "quantity": 30,
        "rate": 8.0,
        "original_rate": 10.0,
        "riggle_coins": 0,
        "amount": 240.0,
        "free_product_quantity": 0,
        "order": 79,
        "product": {
        "id": 20,
        "update_url": "https://api.riggleapp.in/api/v1/core/products/20/",
        "normalized_weight": 0.16,
        "created_at": "2021-12-21T13:56:54.264155",
        "updated_at": "2022-01-24T13:39:44.248117",
        "code": "PRD20",
        "name": "Davat Jeera 160 ml",
        "is_active": true,
        "description": "With Zabardast refreshing taste Jeera is the delicious refreshing drink for all seasons. Imbibed with the goodness of Jeera, a drink fit for anytime, anywhere. Drink to refresh yourself!",
        "base_unit": "ml",
        "base_rate": 10.0,
        "base_quantity": 160,
        "company_step": 30,
        "company_rate": 7.38,
        "retailer_step": 30,
        "expiry_in_days": 180,
        "riggle_coins": 0,
        "brand": 5,
        "category": 9,
        "inactive_regions": [],
        "banner_image": {
        "id": 8,
        "update_url": "https://api.riggleapp.in/api/v1/core/products/20/images/8/",
        "created_at": "2021-12-21T13:58:16.680042",
        "updated_at": "2021-12-21T13:58:16.680049",
        "doc_id": "716dc5dd-3378-4efb-aa5d-4f50cf98e20c",
        "image": "https://assets.riggleapp.in/products/716dc5dd-3378-4efb-aa5d-4f50cf98e20c.jpg",
        "is_banner": false,
        "product": 20
    }
    },
        "product_combo": null,
        "free_product": null
    }]
    }*/

}