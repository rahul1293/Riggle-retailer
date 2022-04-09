package com.riggle.ui.profile.earnings

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.data.network.ApiState
import com.riggle.databinding.ActivityMyEarningsBinding
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.showInfoDialog
import kotlinx.android.synthetic.main.activity_my_earnings.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyEarningsActivity : BaseActivity() {


    private val userPreference: UserProfileSingleton by inject()
    val viewModel: MyEarningsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMyEarningsBinding>(
                this@MyEarningsActivity,
                R.layout.activity_my_earnings
        )
        binding.viewModel = viewModel

        tvToolbarTitle.text = getString(R.string.my_earnings)

        getMyEarnings()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        tvHowToEarnPoints.setOnClickListener {
            showInfoDialog(
                    this@MyEarningsActivity,
                    "How to earn points",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. "
            )
        }
    }

    private fun getMyEarnings() {
        lifecycleScope.launchWhenCreated {

            viewModel.apiStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        showHideLoader(true)
                    }
                    is ApiState.Success<*> -> {
                        showHideLoader(false)
                    }
                    is ApiState.Failure -> {
                        showHideLoader(false)
                    }
                }

            }
        }

        userPreference.userData?.retailer?.id?.let {
            viewModel.getMyEarnings(it)
        }

    }
}