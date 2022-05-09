package com.riggle.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.R
import com.riggle.data.DataManager
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.ComboProducts
import com.riggle.data.models.response.ProductsData
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.pendingDetails.PendingOrderDetails
import com.riggle.ui.listener.ProductChooseListener
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.events.Resource
import kotlinx.android.synthetic.main.bottom_sheet_combo_update.*
import kotlinx.android.synthetic.main.bottom_sheet_combo_update.view.*
import org.koin.android.ext.android.inject

class UpdateComboSheet : BottomSheetDialogFragment(),
    UpdateComboProductAdapter.HomeProductsListener {
    val dataManager: DataManager by inject()
    private val userPreference: UserProfileSingleton by inject()
    var adapter: UpdateComboProductAdapter? = null
    private var final_combo_count: Int = 0
    private var moqStep: Int = 0
    private var moqSteper: Int = 0
    private var count = 1
    private var check = false

    override fun variantAdded(combo_id: Int, quantity: Int) {
        final_combo_count = 0
        adapter?.getList()?.let {
            for (index in it.indices) {
                final_combo_count += it[index].quantity
            }

            if (!check) {
                val c = final_combo_count / moqStep
                if (c > 0 && final_combo_count % moqStep == 0) {
                    moqStep = c * moqSteper
                    /*moqSteper = it[0].step*/
                    count = c
                }
            }

            val dividend: Float = final_combo_count.toFloat() / moqSteper.toFloat()
            val reminder: Float = final_combo_count.toFloat() % moqSteper.toFloat()
            var step = 0
            if (reminder == 0f) {
                tvMoqCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.profit_color
                    )
                )
                step = if (final_combo_count == 0) 1 else (dividend).toInt()
            } else {
                tvMoqCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorError
                    )
                )
                step = (dividend + 1).toInt()
            }
            tvMoqCount.text =
                "MOQ : " + final_combo_count + "/" + (step * moqSteper)

        }
    }

    private var product_id = 0
    var schemeList: ArrayList<ComboProducts>? = null
    private var listener: ProductChooseListener? = null
    fun setListener(listener: ProductChooseListener?) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_combo_update, container, false)
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
                    behavior.isDraggable = false
                    behavior.peekHeight = 0
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val bundle = arguments
        if (bundle != null) {
            product_id = bundle.getInt("product_id", 0)
            schemeList = Gson().fromJson(
                bundle.getString("scheme"),
                object : TypeToken<ArrayList<ComboProducts>>() {}.type
            )
            schemeList?.let {
                if (it.isNotEmpty()) {
                    moqStep = it[0].step
                    moqSteper = it[0].step
                }
            }
        }
        schemeList?.let {
            if (it.size > 0) {
                product_id = it[0].id
                tvName.text = it[0].name
                tvMOQ.text = it[0].step.toString()
                tvMoqCount.text = "MOQ : 0/" + it[0].step.toString()
            }
            populateRecyclerView(it)
        }
        view.ivCross?.setOnClickListener { dismiss() }
        view.ivDone?.setOnClickListener {
            schemeList?.let {
                if ((final_combo_count == it[0].step || final_combo_count % it[0].step == 0) && final_combo_count != 0) {
                    apiCallCombo()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "mix items are equal to multiple of MOQ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun apiCallCombo() {
        val products = ArrayList<VariantUpdate>()
        adapter?.getList()?.let {
            for (index in it.indices) {
                if (it[index].quantity > 0) {
                    val update = VariantUpdate(
                        it[index].id,
                        it[index].quantity,
                        product_id
                    )
                    products.add(update)
                }
            }
            PendingOrderDetails.obrComboSelect.value = Resource.success(products, "")
        }
    }

    private fun populateRecyclerView(schemeList: ArrayList<ComboProducts>) {
        schemeList?.let { data ->
            if (data.size > 0) {
                view?.rootView?.let { rootView ->
                    activity?.let { activity ->
                        adapter = UpdateComboProductAdapter(
                            requireActivity(),
                            data[0].products as ArrayList<ProductsData>
                        )
                        rootView.rvVariants?.layoutManager = LinearLayoutManager(activity)
                        rootView.rvVariants?.adapter = adapter
                        adapter?.setListener(this)
                        variantAdded(0, 0)
                        check = true
                    }
                }
            }
        }
    }

}