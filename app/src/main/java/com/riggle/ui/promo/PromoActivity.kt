package com.riggle.ui.promo

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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.baseClass.SimpleRecyclerViewAdapter
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
import com.riggle.databinding.ActivityPromoBinding
import com.riggle.databinding.ListCouponItemsBinding
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.ContactUsDialog
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.other.adapter.SubAreaAdapter
import com.riggle.ui.other.registration.WelcomeScreen
import com.riggle.ui.profile.editprofile.EditProfileViewModel
import com.riggle.ui.utils.BaseAdapter
import com.riggle.utils.*
import kotlinx.android.synthetic.main.activity_add_filter.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.etAddress
import kotlinx.android.synthetic.main.fragment_address_details.*
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

class PromoActivity : BaseActivity() {

    private val userPreference: UserProfileSingleton by inject()
    private val viewModel: PromoViewModel by viewModel()
    private lateinit var binding: ActivityPromoBinding
    private lateinit var adapter: SimpleRecyclerViewAdapter<CouponBean, ListCouponItemsBinding>

    companion object {
        fun newIntent(active: Activity): Intent {
            val intent = Intent(active, PromoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityPromoBinding>(
            this@PromoActivity,
            R.layout.activity_promo
        )
        setListioners()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        getCoupons()
    }

    private fun initAdapter() {
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        adapter = SimpleRecyclerViewAdapter(
            R.layout.list_coupon_items, BR.bean
        ) { v, m, pos ->
            binding.etPromo.setText(m.code)
        }
        binding.rvOne.adapter = adapter
    }

    private fun setListioners() {
        binding.toolbar.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
        binding.toolbar.findViewById<TextView>(R.id.tvToolbarTitle).text = "Apply Coupons"
    }

    private fun getCoupons() {
        showHideLoader(true)
        dataManager.getCoupons(object : ApiResponseListener<List<CouponBean>> {
            override fun onSuccess(response: List<CouponBean>) {
                showHideLoader(false)
                adapter.list = response
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Toast.makeText(this@PromoActivity, apiError?.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


}
