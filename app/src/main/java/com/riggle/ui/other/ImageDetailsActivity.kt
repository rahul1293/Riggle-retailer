package com.riggle.ui.other

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.databinding.HolderImageItemBinding
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import kotlinx.android.synthetic.main.activity_image_details.*
import kotlinx.android.synthetic.main.activity_image_details.appBar
import kotlinx.android.synthetic.main.layout_appbar.*
import java.lang.reflect.Type


class ImageDetailsActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private var pos = 0

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, ImageDetailsActivity::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
        appBar?.findViewById<View>(R.id.toolbar)?.setBackgroundColor(Color.parseColor("#ffffff"))
    }

    override fun setView(): Int {
        return R.layout.activity_image_details
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.product_image)
        intent?.getIntExtra("position", 0)?.let {
            pos = it
        }
        val type: Type = object : TypeToken<ArrayList<SlideModel>?>() {}.type
        val yourList: List<SlideModel> =
            Gson().fromJson(intent?.getStringExtra("image_list"), type)
        setUpRecycler(yourList)
    }

    private fun setUpRecycler(yourList: List<SlideModel>) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val imageAdapter = SimpleRecyclerViewAdapter<SlideModel,HolderImageItemBinding>(R.layout.holder_image_item,BR.bean
        ) { v, m, pos -> }
        rvImageList.layoutManager = layoutManager
        rvImageList.adapter = imageAdapter
        imageAdapter.list = yourList
        layoutManager.scrollToPosition(pos)
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(rvImageList)
    }

}