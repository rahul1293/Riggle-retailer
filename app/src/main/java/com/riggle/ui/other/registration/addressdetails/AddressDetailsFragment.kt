package com.riggle.ui.other.registration.addressdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.riggle.R
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
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

}