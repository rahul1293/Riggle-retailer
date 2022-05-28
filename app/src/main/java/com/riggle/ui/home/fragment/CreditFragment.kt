package com.riggle.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.riggle.R
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.CreditResponse
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.credit.CreditActivity
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.utils.*
import kotlinx.android.synthetic.main.fragment_credit.*
import org.koin.android.ext.android.inject

class CreditFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {
    private var loadingDialog: LoadingDialog? = null
    private val userPreference: UserProfileSingleton by inject()
    private var resp: CreditResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_credit
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        addOnClickListeners()

    }


    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun addOnClickListeners() {
        letsGoButton.setOnClickListener {
            val intent = Intent(context, CreditActivity::class.java)
            resp?.lenders?.let { lenders ->
                intent.putExtra("lenders", Gson().toJson(lenders))
            }
            startActivity(intent)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): CreditFragment {
            val fragment = CreditFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    private fun getData() {
        if (resp == null)
            showHideLoader(true)
        val id = userPreference.userData?.retailer?.id ?: 0
        dataManager.creditStatus(object : ApiResponseListener<CreditResponse> {
            override fun onSuccess(response: CreditResponse) {
                showHideLoader(false)
                lottie.playAnimation()
                resp = response
                tvtopLine.text = "Place order worth Rs 50000 to activate Riggle Credit"
                tvmessage.text = "Rs ${response.differenceValue} worth for orders more to go"
                response.percentage?.let {
                    cv_one.progress = it
                    tvpercent.text = "${it.toInt()}%"
                }
                if (response.status == Constants.DataKeys.CREDIT_INITIATED) {
                    letsGoButton.visibility = View.VISIBLE
                } else {
                    letsGoButton.visibility = View.GONE
                }
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Toast.makeText(context, apiError?.message, Toast.LENGTH_SHORT).show()
                Log.i("TAG", ":::::" + apiError?.message)
            }

        }, id)
    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.hide()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }
}