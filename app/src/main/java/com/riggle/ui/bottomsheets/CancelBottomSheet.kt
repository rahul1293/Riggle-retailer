package com.riggle.ui.bottomsheets

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.DataManager
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.CancelOrderResponse
import com.riggle.data.models.response.ConstantsResponse
import com.riggle.data.models.response.OrderCancellationReason
import com.riggle.data.network.ApiResponseListener
import com.riggle.databinding.ItemCancelListBinding
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.bottom_sheet_cancel.*
import kotlinx.android.synthetic.main.bottom_sheet_cancel.view.*
import org.koin.android.ext.android.inject

class CancelBottomSheet : BottomSheetDialogFragment() {
    val dataManager: DataManager by inject()
    private val userPreference: UserProfileSingleton by inject()
    private var loadingDialog: LoadingDialog? = null
    var order_id = 0
    private var reason = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //RiggleApplication.getInstance().getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_cancel, container, false)
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
            order_id = bundle.getInt("order_id", 0)
        }
        view.ivCross?.setOnClickListener { dismiss() }
        view.btnContinue?.setOnClickListener {
            userPreference.userData?.let { user ->
                if (reason.isNotEmpty()) {
                    if (reason == "Others") {
                        if (!TextUtils.isEmpty(etReason.text.toString().trim())) {
                            cancelOrder(
                                user.session_key,
                                order_id,
                                etReason.text.toString().trim()
                            )
                        } else {
                            etReason.error = "Add cancellation reason"
                        }
                    } else {
                        cancelOrder(user.session_key, order_id, reason)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Select cancellation reason",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setRecyclerView()
        getReasonList()
    }

    private fun cancelOrder(sessionKey: String, orderId: Int, reason: String) {
        showHideLoader(true)
        val data = HashMap<String, String>()
        data["cancellation_reason"] = reason
        dataManager.cancelOrder(object : ApiResponseListener<CancelOrderResponse> {
            override fun onSuccess(response: CancelOrderResponse) {
                showHideLoader(false)
                if (response != null) {
                    Toast.makeText(
                        requireContext(),
                        "Cancelled Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                }
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Toast.makeText(
                    requireContext(),
                    apiError?.msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, sessionKey, orderId, data)
    }

    var reasonAdapter: SimpleRecyclerViewAdapter<OrderCancellationReason, ItemCancelListBinding>? =
        null

    private fun setRecyclerView() {
        reasonAdapter = SimpleRecyclerViewAdapter<OrderCancellationReason, ItemCancelListBinding>(
            R.layout.item_cancel_list,
            BR.bean
        ) { v, m, pos ->
            for (index in reasonAdapter?.list!!) {
                if (index.name.equals(m.name, true)) {
                    index.check = true
                    if (m.name.equals("others", ignoreCase = true)) {
                        etReason.visibility = View.VISIBLE
                    } else {
                        etReason.visibility = View.GONE
                    }
                    reason = m.name
                } else {
                    index.check = false
                }
            }
            reasonAdapter?.notifyDataSetChanged()
        }
        val layoutManager = LinearLayoutManager(requireContext())
        rvVariants.layoutManager = layoutManager
        rvVariants.adapter = reasonAdapter
        //reasonAdapter?.list = cancelReason
    }

    private fun getReasonList() {
        userPreference.userData?.retailer?.id?.let {
            showHideLoader(true)
            dataManager.getConstants(object :
                ApiResponseListener<ConstantsResponse> {
                override fun onSuccess(response: ConstantsResponse) {
                    if (response != null) {
                        if (response.order_cancellation_reasons.isNotEmpty()) {
                            reasonAdapter?.list = response.order_cancellation_reasons
                        }
                    }
                    showHideLoader(false)
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    Toast.makeText(
                        requireContext(),
                        apiError?.msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.dismiss()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

}