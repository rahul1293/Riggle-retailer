package com.riggle.ui.credit

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.data.firebase.FirebaseConfig
import com.riggle.data.firebase.StoreType
import com.riggle.data.location.LocationHandler
import com.riggle.data.location.LocationResultListener
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.data.network.ApiState
import com.riggle.data.permission.PermissionHandler
import com.riggle.data.permission.Permissions
import com.riggle.databinding.ActivityCreditBinding
import com.riggle.databinding.ActivityEditProfileBinding
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.ContactUsDialog
import com.riggle.ui.other.adapter.SubAreaAdapter
import com.riggle.ui.profile.editprofile.EditProfileViewModel
import com.riggle.ui.utils.BaseAdapter
import com.riggle.utils.*
import kotlinx.android.synthetic.main.activity_add_filter.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*

class CreditActivity : BaseActivity() {

    private val userPreference: UserProfileSingleton by inject()
    private val viewModel: CreditViewModel by viewModel()
    private lateinit var binding: ActivityCreditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCreditBinding>(
            this@CreditActivity,
            R.layout.activity_credit
        )
        setListioners()
        initAdapter()
    }

    private fun initAdapter() {
        binding.rvOne.layoutManager=LinearLayoutManager(this)
        val adapter=LenderAdapter(listOf<String>())
        binding.rvOne.adapter=adapter
    }

    private fun setListioners() {
        binding.toolbar.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
        binding.toolbar.findViewById<TextView>(R.id.tvToolbarTitle).text = "Riggle Credit"
    }


    private fun getDetails() {
        userPreference.userData?.retailer?.id?.let {
            dataManager.getPingDetails(
                object :
                    ApiResponseListener<JsonElement> {
                    override fun onSuccess(response: JsonElement) {
                        response?.let {
                            var usrData =
                                Gson().fromJson(it.toString(), UserDetails::class.java)
                            tvRiggleCoins.text = "" + usrData.riggle_coins_balance
                            if (usrData.is_serviceable) {
                                userPreference.userData?.let {
                                    if (!it.retailer.is_serviceable) {
                                        rlBrands?.visibility = View.VISIBLE
                                        tvEmpty?.visibility = View.GONE
                                        val userData = it
                                        userData.retailer.is_serviceable = usrData.is_serviceable
                                        userPreference
                                            .updateUserData(userData)
                                        userPreference
                                            .saveRetailerDetails(usrData)
                                    }
                                }
                            }
                        }
                    }

                    override fun onError(apiError: ApiError?) {
                        Log.i("TAG", "::::" + apiError?.message)
                    }
                },
                it, ""
            )
            //expand = sub_area
        }
    }
}
