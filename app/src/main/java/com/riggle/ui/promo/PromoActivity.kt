package com.riggle.ui.promo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
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
            //binding.etPromo.setText(m.code)
            val intent = Intent()
            intent.putExtra("code",m.code)
            setResult(Activity.RESULT_OK,intent)
            finish()
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
