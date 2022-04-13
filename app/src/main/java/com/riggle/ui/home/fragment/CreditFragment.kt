package com.riggle.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.riggle.R
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.credit.CreditActivity
import com.riggle.ui.dialogs.ContactUsDialog
import com.riggle.ui.introscreen.IntroActivity
import com.riggle.ui.listener.ActionDialogCallback
import com.riggle.ui.other.MyOrdersActivity
import com.riggle.ui.profile.AddMemberActivity
import com.riggle.ui.profile.earnings.MyEarningsActivity
import com.riggle.ui.profile.editprofile.EditProfileActivity
import com.riggle.utils.GetAppVersion
import com.riggle.utils.LogoutUserUtil
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.showActionDialog
import kotlinx.android.synthetic.main.fragment_credit.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject

class CreditFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {

    private val userPreference: UserProfileSingleton by inject()

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

    private fun addOnClickListeners() {
        letsGoButton.setOnClickListener {
            val intent = Intent(context, CreditActivity::class.java)
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
}