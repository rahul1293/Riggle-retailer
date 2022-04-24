package com.riggle.ui.other.registration.addressdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.riggle.R
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.CoreConstantsResponse
import com.riggle.data.models.response.PinCodeLookupResponse
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.other.registration.WelcomeScreen
import kotlinx.android.synthetic.main.fragment_address_details.*

class AddressDetailsFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {

    companion object {
        fun newInstance(): AddressDetailsFragment {
            val fragment = AddressDetailsFragment()
            return fragment
        }

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
        return return R.layout.fragment_address_details
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        onTextChanged()
    }

    private fun onTextChanged() {

        rgLocation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbYes -> {
                    WelcomeScreen.isAtStore = true
                }
                R.id.rbNo -> {
                    WelcomeScreen.isAtStore = false
                }
            }
        }

        etPinCode.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.pincode = text.toString()
            if (text.toString().length >= 6) {
                getPinCodeLookup(text.toString())
            }
        }
        etStoreName.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.address = text.toString()
        }
        etAddress.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.locality = text.toString()
        }
        etCity.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.city = text.toString()
        }
        etState.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.state = text.toString()
        }
        etLandMark.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.landmark = text.toString()
        }

    }

    private fun getPinCodeLookup(pinCode: String) {
        showHideLoader(true)
        dataManager.pinCodeLookup(object : ApiResponseListener<PinCodeLookupResponse> {
            override fun onSuccess(response: PinCodeLookupResponse) {
                showHideLoader(false)
                if (response.Status.equals("Success", true)) {
                    if (response.PostOffice.isNotEmpty()) {
                        etAddress.setText(response.PostOffice[0].Name)
                        etCity.setText(response.PostOffice[0].Block)
                        etState.setText(response.PostOffice[0].State)

                        WelcomeScreen.retailerRequest.locality = response.PostOffice[0].Name
                        WelcomeScreen.retailerRequest.city = response.PostOffice[0].Block
                        WelcomeScreen.retailerRequest.state = response.PostOffice[0].State
                    }
                } else {
                    etPinCode.error = "Pincode is not valid"
                }
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                etPinCode.error = "Pincode is not valid"
                Toast.makeText(requireContext(), apiError.toString(), Toast.LENGTH_SHORT).show()
            }

        }, pinCode)
    }

    private var loadingDialog: LoadingDialog? = null
    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.dismiss()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

}