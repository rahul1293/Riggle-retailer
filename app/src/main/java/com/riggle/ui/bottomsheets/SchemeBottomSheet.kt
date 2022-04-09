package com.riggle.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.R
import com.riggle.data.DataManager
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.SchemesBean
import com.riggle.ui.listener.ProductChooseListener
import com.riggle.ui.listener.ProductVariantListener
import kotlinx.android.synthetic.main.bottom_sheet_scheme.view.*
import org.koin.android.ext.android.inject

class SchemeBottomSheet : BottomSheetDialogFragment(),
    ProductsSchemeAdapter.HomeProductsListener {
    val dataManager: DataManager by inject()
    override fun variantAdded(position: Int, quantity: Int) {
        schemeList?.get(position)?.let { listener?.itemUpdated(it, product_id) }
        dismiss()
    }

    private var product_id = 0
    var schemeList: ArrayList<SchemesBean>? = null
    private val products: ArrayList<VariantUpdate> = ArrayList()
    private var listener: ProductChooseListener? = null
    private var product_name: String = ""
    fun setListener(listener: ProductChooseListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //RiggleApplication.getInstance().getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_scheme, container, false)
    }

    private val mBottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            view.viewTreeObserver.addOnGlobalLayoutListener {
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet =
                    dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

                bottomSheet?.let {
                    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.addBottomSheetCallback(mBottomSheetBehaviorCallback)
                    behavior.peekHeight = 0
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val bundle = arguments
        if (bundle != null) {
            product_id = bundle.getInt("product_id", 0)
            product_name = bundle.getString("product_name").toString()
            schemeList = Gson().fromJson(
                bundle.getString("scheme"),
                object : TypeToken<ArrayList<SchemesBean>>() {}.type
            )
        }
        schemeList?.let { populateRecyclerView(it) }
        view.ivCross?.setOnClickListener { dismiss() }

    }

    private fun populateRecyclerView(schemeList: ArrayList<SchemesBean>) {
        schemeList?.let { data ->
            if (data.size > 0) {
                view?.rootView?.let { rootView ->
                    activity?.let { activity ->
                        val adapter = ProductsSchemeAdapter(requireActivity(), data, product_name)
                        rootView.rvVariants?.layoutManager = LinearLayoutManager(activity)
                        rootView.rvVariants?.adapter = adapter
                        adapter.setListener(this)
                    }
                }
            }
        }
    }

}