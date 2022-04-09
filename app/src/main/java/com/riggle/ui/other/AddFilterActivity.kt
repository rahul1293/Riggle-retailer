package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.riggle.R
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import kotlinx.android.synthetic.main.activity_add_filter.*
import kotlinx.android.synthetic.main.layout_appbar.*

class AddFilterActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_add_filter
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Filter"
        ivCartView.visibility = View.GONE
        listener()
    }

    private fun listener() {
        tvPrice.setOnClickListener {
            updateBg(1)
        }
        tvMoq.setOnClickListener {
            updateBg(2)
        }
        tvMargin.setOnClickListener {
            updateBg(3)
        }
        tvType.setOnClickListener {
            updateBg(4)
        }
        tvFlavour.setOnClickListener {
            updateBg(5)
        }
        tvProductType.setOnClickListener {
            updateBg(6)
        }
        tvBrand.setOnClickListener {
            updateBg(7)
        }
    }

    private fun updateBg(value: Int) {
        when (value) {
            1 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            2 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            3 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            4 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            5 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            6 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
            }
            7 -> {
                tvPrice.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMoq.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvMargin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvFlavour.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvProductType.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyTrackingStatusBackground))
                tvBrand.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }

    companion object {
        fun start(
            context: Context,
        ) {
            val intent = Intent(context, AddFilterActivity::class.java)
            context.startActivity(intent)
        }
    }

}