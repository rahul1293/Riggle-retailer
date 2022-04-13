package com.riggle.ui.other.registration.addressdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riggle.R
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl

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

    }
}