package com.riggle.ui.promo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.databinding.ActivityPromoBinding
import com.riggle.databinding.ListCouponItemsBinding
import com.riggle.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PromoActivity : BaseActivity() {

    private val userPreference: UserProfileSingleton by inject()
    private val viewModel: PromoViewModel by viewModel()
    private lateinit var binding: ActivityPromoBinding
    private lateinit var adapter: SimpleRecyclerViewAdapter<CouponBean, ListCouponItemsBinding>
    private var coupon_id = 0
    private var avail_riggle_coin = 0

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
        setListeners()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        getCoupons()
        getDetails()
    }

    private fun initAdapter() {
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        adapter = SimpleRecyclerViewAdapter(
            R.layout.list_coupon_items, BR.bean
        ) { v, m, pos ->
            //binding.etPromo.setText(m.code)
            m.id?.let {
                coupon_id = it
                val intent = Intent()
                intent.putExtra("code", coupon_id.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        binding.rvOne.adapter = adapter
    }

    private fun setListeners() {
        binding.toolbar.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
        binding.tvApply.findViewById<TextView>(R.id.tvApply).setOnClickListener {
            if (!TextUtils.isEmpty(binding.etPromo.text.toString().trim())) {
                val intent = Intent()
                intent.putExtra("code", binding.etPromo.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        binding.toolbar.findViewById<TextView>(R.id.tvToolbarTitle).text = "Apply Coupons"

        binding.ivMinus.setOnClickListener {
            if (binding.tvQuantSet.text.toString().toInt() - 500 > 0) {
                binding.tvQuantSet.text =
                    (binding.tvQuantSet.text.toString().toInt() - 500).toString()
            } else {
                binding.tvQuantSet.text = "0"
            }
        }
        binding.ivPlus.setOnClickListener {
            if (binding.tvQuantSet.text.toString().toInt() + 500 < avail_riggle_coin) {
                binding.tvQuantSet.text =
                    (binding.tvQuantSet.text.toString().toInt() + 500).toString()
            }
        }

        binding.tvFree.setOnClickListener {
            if (binding.tvQuantSet.text.toString().toInt() > 0) {
                val intent = Intent()
                intent.putExtra("coin_value", binding.tvQuantSet.text.toString().trim())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

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

    private fun getDetails() {
        userPreference.userData?.retailer?.id?.let {
            dataManager.getPingDetails(
                object :
                    ApiResponseListener<JsonElement> {
                    override fun onSuccess(response: JsonElement) {
                        response?.let {
                            var usrData =
                                Gson().fromJson(it.toString(), UserDetails::class.java)
                            if (usrData.riggle_coins_balance != 0) {
                                binding.tvGrandPrice.text = "(Available Coins:" + String.format(
                                    getString(R.string.available_coins_value) ?: "",
                                    usrData.riggle_coins_balance
                                ) + ")"
                                avail_riggle_coin = usrData.riggle_coins_balance
                            } else {
                                binding.tvGrandPrice.text = "(Available Coins:" + 0 + ")"
                            }
                            updateCoinView()
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

    private fun updateCoinView() {
        binding.rlUpdateCount.isEnabled = avail_riggle_coin >= 500
        if (avail_riggle_coin >= 500) {
            binding.rlUpdateCount.alpha = 1.0f
        } else {
            binding.rlUpdateCount.alpha = 0.4f
        }
        binding.tvQuantSet.text = avail_riggle_coin.toString()
    }

}
