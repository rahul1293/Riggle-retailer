package com.riggle.ui.other.registration.addressdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.firebase.StoreType
import com.riggle.data.models.MenuBean
import com.riggle.databinding.ItemImageListBinding
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.other.registration.StoreTypeAdapter
import com.riggle.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_personal_details.*

class PersonalDetailsFragment :  CustomAppFragmentViewImpl(), CustomAppViewConnector {

    companion object {
        fun newInstance(): PersonalDetailsFragment {
            val fragment = PersonalDetailsFragment()
            return fragment
        }
    }

    private var storeList = arrayListOf<StoreType>()
    private var adapter: StoreTypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_personal_details
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSpinner()
        setUpRecyclerView()
    }

    private fun setSpinner() {
        storeList?.apply {
            add(0, StoreType(getString(R.string.store_type), 0))
            add(StoreType("General Store", 1))
            add(StoreType("Dairy", 1))
            add(StoreType("Medical Store", 1))
            add(StoreType("Supermarket", 1))
            add(StoreType("QSR / Food Joint", 1))
            add(StoreType("24 hr convenience store", 1))
            add(StoreType("Other", 1))
        }

        val hint = StoreType(getString(R.string.store_type), 0)
        storeList.add(0, hint)

        adapter = StoreTypeAdapter(requireContext(), storeList)
        storeTypeSpinner?.adapter = adapter
        storeTypeSpinner?.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) hideKeyboard(requireActivity())
            false
        }
        storeTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val (_, key) = adapterView.getItemAtPosition(i) as StoreType
                /*selectedStoreKey = key
                selectedStoreType = storeList.get(i).store_type
                checkValidation()*/
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

    }

    var adapterImages: SimpleRecyclerViewAdapter<MenuBean, ItemImageListBinding>? = null
    private fun setUpRecyclerView() {
        adapterImages = SimpleRecyclerViewAdapter<MenuBean, ItemImageListBinding>(
            R.layout.item_image_list, BR.bean
        ) { v, m, pos ->
            when (pos) {
                0 -> {
                    /*launcher.launch(
                        ImagePicker.with(this)
                            .cropSquare()
                            .cameraOnly() // or galleryOnly()
                            .createIntent()
                    )*/
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvImageList.layoutManager = layoutManager
        rvImageList.adapter = adapterImages
        adapterImages?.list = getList()
    }

    private fun getList(): MutableList<MenuBean> {
        val dataList = ArrayList<MenuBean>()
        dataList.apply {
            add(MenuBean(1, "", 0, false))
        }
        return dataList
    }

}