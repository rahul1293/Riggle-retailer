package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.riggle.R
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.other.fragments.BrandCategoryFragment
import com.riggle.ui.other.fragments.BrandCategoryFragment.Companion.newInstance
import com.riggle.utils.Constants.PageTypes
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_brand_category_detail.*
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.util.*

class BrandCategoryDetailActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    private val userPreference: UserProfileSingleton by inject()

    private var data: ArrayList<BrandsCategoryData> = ArrayList<BrandsCategoryData>()
    private var adapter: PagerAdapter? = null
    private var currentPos = -1
    private var page_type = PageTypes.UNDEFINED
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_brand_category_detail
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        //ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        userPreference.userId
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        if (bundle != null) {
            page_type = if (bundle.getString(KEY_PAGE_TITLE)
                    ?.contains(getString(R.string.shop_by_brands)) == true
            ) PageTypes.BRAND_PAGE else PageTypes.CATEGORY_PAGE
            supportActionBar?.title = bundle.getString(KEY_PAGE_TITLE)
            data = bundle.getParcelableArrayList("data") ?: ArrayList<BrandsCategoryData>()
            currentPos = bundle.getInt(KEY_CURRENT_POS, 0)
            setData()
        }

        setUpListeners()

        setData()
    }

    private fun setUpListeners() {
        llShort.setOnClickListener {

        }
        llFilter.setOnClickListener {
            AddFilterActivity.start(this)
        }
        ivCartView.setOnClickListener {
            HomeActivity.start(this,true)
        }
    }

    private fun setData() {
        adapter = PagerAdapter(
            supportFragmentManager, applicationContext
        )
        viewPager?.adapter = adapter
        viewPager?.offscreenPageLimit = 1
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                /*if (positionOffset == 0f && positionOffsetPixels == 0) {
                    adapter?.let { adapter->
                        if (adapter.fragments[position] != null) {
                            currentPos = position
                            adapter.fragments[position]?.loadData(data[currentPos].id, page_type)
                        }
                    }

                }*/
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager?.currentItem = currentPos
    }

    internal inner class PagerAdapter(var fm: FragmentManager, private val mContext: Context) :
        FragmentPagerAdapter(
            fm
        ) {
        var fragments: Array<BrandCategoryFragment?>
        override fun getCount(): Int {
            return 4/*data?.size?:0*/
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return newInstance()/*if (fragments[position] != null) fragments[position] as BrandCategoryFragment else {
                    val fragment = newInstance()
                    fragments[position] = fragment
                    fragments[position] as BrandCategoryFragment
                }*/

        }

        override fun getPageTitle(position: Int): CharSequence? {
            data?.let {
                return "Sub Category" + position/*it[position].name*/
            }
            return ""
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, object);
        }

        init {
            fragments = arrayOfNulls(data?.size)
        }
    }

    companion object {
        const val KEY_PAGE_TITLE = "title"
        const val KEY_DATA = "data"
        const val KEY_CURRENT_POS = "current_pos"
        fun start(
            context: Context,
            brandsData: ArrayList<BrandsCategoryData>,
            page_title: String?,
            position: Int
        ) {
            val bundle = Bundle()
            bundle.putString(KEY_PAGE_TITLE, page_title)
            bundle.putParcelableArrayList(KEY_DATA, brandsData)
            bundle.putInt(KEY_CURRENT_POS, position)
            val intent = Intent(context, BrandCategoryDetailActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}