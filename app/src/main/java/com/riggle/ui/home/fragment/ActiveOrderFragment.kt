package com.riggle.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.models.MenuBean
import com.riggle.databinding.ItemActiveOrderBinding
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import kotlinx.android.synthetic.main.fragment_active_order.*

class ActiveOrderFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {

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
        setUpRecyclerView()
    }

    private var adapterActiveOrder: SimpleRecyclerViewAdapter<MenuBean, ItemActiveOrderBinding>? =
        null

    private fun setUpRecyclerView() {
        adapterActiveOrder = SimpleRecyclerViewAdapter<MenuBean, ItemActiveOrderBinding>(
            R.layout.item_active_order, BR.bean
        ) { v, m, pos ->

        }
        val layoutManager =
            LinearLayoutManager(requireContext())
        rvActiveOrders.layoutManager = layoutManager
        rvActiveOrders.adapter = adapterActiveOrder
        adapterActiveOrder?.list = getList()
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