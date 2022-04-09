package com.riggle.ui.subcategory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.adapters.HomeCategoryAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlin.collections.ArrayList

class SubCategoryActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_sub_category
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        bundle?.getParcelable<BrandsCategoryData>("data")?.let {
            categoryBean = it
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        categoryBean?.let {
            supportActionBar?.title = it.name
        }
        categoryBean?.let {
            category
        }
        onClickListener()
    }

    lateinit var categoryBean: BrandsCategoryData
    var categoriesData = arrayListOf<BrandsCategoryData>()
    private fun populateSubCategory() {
        activity?.let {
            rlCategories?.visibility = View.VISIBLE
            val categoriesAdapter = HomeCategoryAdapter(it, categoriesData)
            rvCategories?.layoutManager = GridLayoutManager(activity, 2)
            /*LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)*/
            rvCategories?.adapter = categoriesAdapter
        }
    }

    private val category: Unit
        private get() {
            showHideLoader(true)
            val data = HashMap<String, String>()
            data.put("page", "1")
            data.put("type", "sub")
            data.put("is_active", "True")
            data.put("belongs__id", categoryBean.id.toString())
            dataManager.getCategoryList(object :
                ApiResponseListener<APICommonResponse<List<BrandsCategoryData>>> {
                override fun onSuccess(response: APICommonResponse<List<BrandsCategoryData>>) {
                    showHideLoader(false)
                    response.results?.let { responseData ->
                        if (responseData.size > 0) {
                            categoriesData = responseData as ArrayList<BrandsCategoryData>
                            populateSubCategory()
                        }
                    }

                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                }
            }, data)
        }

    private var loadingDialog: LoadingDialog? = null
    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.hide()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

    private fun onClickListener() {

        /*tvCategoryViewAll.setOnClickListener {
            activity?.let {
                ShopByBrandCategory.start(
                    it,
                    ShopByBrandCategory.KEY_CATEGORY_PAGE,
                    categoriesData,
                    0
                )
            }
        }*/

        ivCartView.setOnClickListener {
            HomeActivity.start(this,true)
        }

    }

    companion object {
        const val KEY_PAGE_TYPE = "page_type"
        const val KEY_BRAND_PAGE = "brand"
        const val KEY_CATEGORY_PAGE = "category"
        fun start(
            context: Context,
            brandsData: BrandsCategoryData
        ) {
            val bundle = Bundle()
            bundle.putParcelable("data", brandsData)
            val intent = Intent(context, SubCategoryActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

}