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
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.ProductCartRequest
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.ComboProducts
import com.riggle.data.models.response.ProductsData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.listener.ProductChooseListener
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.bottom_sheet_combo.*
import kotlinx.android.synthetic.main.bottom_sheet_combo.view.*
import kotlinx.android.synthetic.main.bottom_sheet_scheme.view.ivCross
import kotlinx.android.synthetic.main.bottom_sheet_scheme.view.rvVariants
import org.koin.android.ext.android.inject

class ComboBottomSheet : BottomSheetDialogFragment(),
    ProductsComboAdapter.HomeProductsListener {
    val dataManager: DataManager by inject()
    private val userPreference: UserProfileSingleton by inject()
    var adapter: ProductsComboAdapter? = null
    private var final_combo_count: Int = 0
    private var moqStep: Int = 0
    private var moqSteper: Int = 0
    private var count = 1
    private var check = false

    private var loadingDialog: LoadingDialog? = null
    override fun variantAdded(combo_id: Int, quantity: Int) {
        final_combo_count = 0
        adapter?.getList()?.let {
            for (index in it.indices) {
                final_combo_count += it[index].quantity
            }

            if (!check) {
                val c = final_combo_count / moqStep
                if (c > 0 && final_combo_count % moqStep == 0) {
                    moqStep = c * it[0].moq
                    moqSteper = it[0].moq
                    count = c
                }
            }


            /*if (!check) {
                val c = final_combo_count / moqStep
                if (c > 0 && final_combo_count % moqStep == 0) {
                    arguments?.getBoolean("is_update")?.let { isUpdate ->
                        if (isUpdate) {
                            moqStep = c * it[0].product?.retailer_step!!
                            moqSteper = it[0].product?.retailer_step!!
                        } else {
                            moqStep = c * it[0].retailer_step
                            moqSteper = it[0].retailer_step
                        }
                    }
                    count = c
                }
            }*/

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


            /*if (final_combo_count == moqStep) {
                tvMoqCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.profit_color
                    )
                )
                count++
            } else {
                tvMoqCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorError
                    )
                )

                schemeList?.get(0)?.step?.let { s ->
                    if (final_combo_count % s == 0 && final_combo_count < moqStep && final_combo_count > 0) {
                        if (count > 0)
                            count--
                        tvMoqCount.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.profit_color
                            )
                        )
                    } else {
                        tvMoqCount.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorError
                            )
                        )
                    }
                    moqStep = count * s
                }

            }
            tvMoqCount.text =
                "MOQ : " + final_combo_count + "/" + moqStep.toString()*/
        }
    }

    fun itemUpdatedItem(product: ProductsData, quantity: Int) {
        /*val product_id = product?.id
        for (i in productsData.indices) {
            if (productsData[i].id == product_id) {
                //productsData[i] = product
                productsData[i].quantity = quantity
                adapter?.updateCart(i, productsData[i])
                break
            }
        }*/
    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.dismiss()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

    private var product_id = 0
    var schemeList: ArrayList<ComboProducts>? = null
    private var listener: ProductChooseListener? = null
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
        return inflater.inflate(R.layout.bottom_sheet_combo, container, false)
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
            val cartRequest = ProductCartRequest(products)
            userPreference.userData?.retailer?.id?.let {
                showHideLoader(true)
                dataManager.addCartItems(object :
                    ApiResponseListener<List<APICommonResponse<ProductsData>>> {
                    override fun onSuccess(response: List<APICommonResponse<ProductsData>>) {
                        if (response != null) {
                            if (response.isNotEmpty()) {
                                response[0].product?.let { it ->
                                    itemUpdatedItem(it, it.quantity)
                                }
                            }
                        }
                        showHideLoader(false)
                        listener?.onRefresh()
                        dismiss()
                    }

                    override fun onError(apiError: ApiError?) {
                        showHideLoader(false)
                        if (!apiError?.message.equals("Parsing error", false))
                            Toast.makeText(
                                activity,
                                apiError?.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        else {
                            listener?.onRefresh()
                            dismiss()
                        }
                    }
                }, it, cartRequest)
            }
        }
    }

    private fun populateRecyclerView(schemeList: ArrayList<ComboProducts>) {
        schemeList?.let { data ->
            if (data.size > 0) {
                view?.rootView?.let { rootView ->
                    activity?.let { activity ->
                        adapter = ProductsComboAdapter(
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