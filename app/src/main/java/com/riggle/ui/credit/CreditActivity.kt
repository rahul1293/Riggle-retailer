package com.riggle.ui.credit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.models.response.*
import com.riggle.databinding.ActivityCreditBinding
import com.riggle.databinding.ListPaylaterItemsBinding
import com.riggle.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


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

        val schemeList = Gson().fromJson<List<Lenders>>(
            intent.getStringExtra("lenders").toString(),
            object : TypeToken<ArrayList<Lenders>>() {}.type
        )
        setListioners()
        if (schemeList != null) {
            initAdapter(schemeList)
        }
    }

    private fun initAdapter(schemeList: List<Lenders>) {
        val adapter = SimpleRecyclerViewAdapter<Lenders, ListPaylaterItemsBinding>(
            R.layout.list_paylater_items, BR.bean
        ) { v, m, pos ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(m.onboarding_link)
            startActivity(i)
        }
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        //val adapter = LenderAdapter(schemeList/*listOf<Lenders>()*/)
        binding.rvOne.adapter = adapter
        adapter.list = schemeList
    }

    private fun setListioners() {
        binding.toolbar.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
        binding.toolbar.findViewById<TextView>(R.id.tvToolbarTitle).text = "Riggle Credit"
    }


    private fun getDetails() {
        /*userPreference.userData?.retailer?.id?.let {
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
        }*/
    }
}
