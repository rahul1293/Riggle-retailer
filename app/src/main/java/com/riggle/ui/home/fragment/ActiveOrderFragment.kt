package com.riggle.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.models.ApiError
import com.riggle.data.models.MenuBean
import com.riggle.data.models.response.MyOrderDataOuter
import com.riggle.data.network.ApiResponseListener
import com.riggle.databinding.ItemActiveOrderBinding
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.pendingDetails.PendingOrderDetails
import com.riggle.ui.other.OrderDetailActivity
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.fragment_active_order.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class ActiveOrderFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {
    private val userPreference: UserProfileSingleton by inject()

    private var loaderDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_active_order
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        loaderDialog = LoadingDialog(requireContext())
        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getActiveOrders()
    }

    private fun getActiveOrders() {
        if (adapterActiveOrder?.list?.isEmpty() == true)
            showHideLoader(true)
        userPreference.userData?.retailer?.id?.let {

            val params = HashMap<String, String>()
            params["retailer"] = it.toString()
            params["type"] = "active"
            params["expand"] = "products.free_product,products.product.banner_image,service_hub"

            dataManager.getMyOrdersOne(object :
                ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {

                    showHideLoader(false)
                    /*val myType = object : TypeToken<List<String>>() {}.type
                    val value = Gson().fromJson<ArrayList<String>>(list, myType)*/

                    if (response != null) {

                        var obj = JSONObject(response.toString())
                        var result = obj.get("results")
                        if (result != null) {
                            val myType =
                                object : TypeToken<java.util.ArrayList<MyOrderDataOuter>>() {}.type
                            val values = Gson().fromJson<java.util.ArrayList<MyOrderDataOuter>>(
                                result.toString(),
                                myType
                            )
                            values?.let {
                                if (it.size > 0) {
                                    setData(it)
                                    emptyOrdersLinearLayout?.visibility = View.GONE
                                } else {
                                    emptyOrdersLinearLayout?.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {

                    }
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    emptyOrdersLinearLayout?.visibility = View.GONE
                }
            }, params/*it, "products.free_product,products.product.banner_image"*/)

        }
    }

    private fun setData(it: ArrayList<MyOrderDataOuter>) {
        adapterActiveOrder?.list = it
    }

    private var adapterActiveOrder: SimpleRecyclerViewAdapter<MyOrderDataOuter, ItemActiveOrderBinding>? =
        null

    private fun setUpRecyclerView() {
        adapterActiveOrder = SimpleRecyclerViewAdapter<MyOrderDataOuter, ItemActiveOrderBinding>(
            R.layout.item_active_order, BR.bean
        ) { v, m, pos ->
            if (m.status.equals("pending", true)) {
                val intent = PendingOrderDetails.newIntent(requireActivity())
                intent?.putExtra("order_id", m.id)
                //intent?.putExtra("store_name", m.retailer.name)
                startActivity(intent)
            } else {
                OrderDetailActivity.start(
                    requireContext(),
                    m.id,
                    Gson().toJson(m)
                )
            }
        }
        val layoutManager =
            LinearLayoutManager(requireContext())
        rvActiveOrders.layoutManager = layoutManager
        rvActiveOrders.adapter = adapterActiveOrder
        //adapterActiveOrder?.list = getList()
    }

    private fun getList(): MutableList<MenuBean> {
        val dataList = ArrayList<MenuBean>()
        dataList.apply {
            add(MenuBean(1, "", 0, false))
            add(MenuBean(1, "", 0, false))
        }
        return dataList
    }

    fun loadTab() {

    }

    fun clearCart() {

    }

    private fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) if (state) loaderDialog?.show() else loaderDialog?.hide()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ActiveOrderFragment {
            val fragment = ActiveOrderFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}