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
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject

class ProfileFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {

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
        return R.layout.fragment_profile
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        addOnClickListeners()

        activity?.let {
            setData()
        }
    }

    override fun onResume() {
        super.onResume()
        tvStoreName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.USER_NAME)
        tvUserName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STORE_NAME)
        tvPincode?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.PINCODE)
    }

    private fun addOnClickListeners() {
        ivAddMember.setOnClickListener {
            activity?.let {
                AddMemberActivity.start(it)
            }
        }

        tvMyOrders.setOnClickListener {
            activity?.let {
                MyOrdersActivity.start(it)
            }
        }

        tvMyEarnings.setOnClickListener {
            activity?.let {
                startActivity(Intent(activity, MyEarningsActivity::class.java))
            }

        }

        tvContactUs.setOnClickListener {

            val dialog = ContactUsDialog(activity as? Context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.show()


        }

        tvLogout.setOnClickListener {

            showActionDialog(
                activity,
                "Are you sure you want to logout?",
                "",
                "Yes",
                "",
                object : ActionDialogCallback {
                    override fun onConfirmed() {
                        LogoutUserUtil.logoutUser(activity, sharedPreferencesUtil)
                        startActivity(Intent(activity, IntroActivity::class.java))
                        activity?.finishAffinity()
                    }
                }
            )
        }

        tvEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
    }

    private fun setData() {
        tvStoreName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.USER_NAME)
        tvUserName?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STORE_NAME)
        tvPincode?.text = userPreference
            .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.PINCODE)
        activity?.let {
            tvVersion?.text = activity?.getString(R.string.version) + " " + GetAppVersion(it)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}