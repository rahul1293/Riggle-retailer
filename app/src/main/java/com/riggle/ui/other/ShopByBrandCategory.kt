package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.riggle.R
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.other.adapter.BrandCategoryAdapter
import com.riggle.utils.Constants
import kotlinx.android.synthetic.main.activity_shop_by_brand_category.*
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlin.collections.ArrayList

class ShopByBrandCategory : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private var data = ArrayList<BrandsCategoryData>()
    private var defaultPos = 0
    private var adapter: BrandCategoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_shop_by_brand_category
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        //ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle[KEY_PAGE_TYPE] == KEY_BRAND_PAGE) supportActionBar?.title =
                resources.getString(R.string.shop_by_brands) else supportActionBar?.title =
                resources.getString(R.string.shop_by_category)

            bundle.getParcelableArrayList<BrandsCategoryData>("data")?.let {
                data = it
            }

            defaultPos = bundle.getInt(Constants.DataKeys.KEY_POSITION)
            setData()
        }
    }

    private fun setData() {
        /*if (data.size > 0) {
            rvBrandCategory?.visibility = View.VISIBLE
            adapter = BrandCategoryAdapter(activity as? Context, data, supportActionBar?.title?.toString()?:"")
            rvBrandCategory?.layoutManager = LinearLayoutManager(activity)
            rvBrandCategory?.adapter = adapter
            rvBrandCategory?.scrollToPosition(defaultPos)
        }*/

        rvBrandCategory?.visibility = View.VISIBLE
        adapter = BrandCategoryAdapter(
            activity as? Context,
            data,
            supportActionBar?.title?.toString() ?: ""
        )
        rvBrandCategory?.layoutManager = LinearLayoutManager(activity)
        rvBrandCategory?.adapter = adapter
        rvBrandCategory?.scrollToPosition(defaultPos)

    }

    companion object {
        const val KEY_PAGE_TYPE = "page_type"
        const val KEY_BRAND_PAGE = "brand"
        const val KEY_CATEGORY_PAGE = "category"
        fun start(
            context: Context,
            page_type: String?,
            brandsData: ArrayList<BrandsCategoryData>,
            defaultPos: Int
        ) {
            val bundle = Bundle()
            bundle.putString(KEY_PAGE_TYPE, page_type)
            bundle.putInt(Constants.DataKeys.KEY_POSITION, defaultPos)
            bundle.putParcelableArrayList("data", brandsData)
            val intent = Intent(context, ShopByBrandCategory::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}