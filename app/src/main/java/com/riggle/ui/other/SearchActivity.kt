package com.riggle.ui.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.MainThread
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.ProductsData
import com.riggle.data.models.response.SearchData
import com.riggle.data.models.response.TopSearches
import com.riggle.data.network.ApiResponseListener
import com.riggle.data.network.ApiState
import com.riggle.databinding.ActivitySearchBinding
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.other.adapter.SearchAdapter
import com.riggle.ui.other.search.SearchActivityViewModel
import com.riggle.utils.Constants
import com.riggle.utils.Utility
import com.riggle.utils.speechRecognition.OnSpeechRecognitionListener
import com.riggle.utils.speechRecognition.SpeechRecognitionDialog
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


class SearchActivity : BaseActivity(), CustomAppViewConnector,
    OnSpeechRecognitionListener {

    private val searchHandler: Handler? = Handler(Looper.getMainLooper())
    private var queryString = ""
    private var queryStringOld = ""
    private var adapter: SearchAdapter? = null
    private var speechRecognitionDialog: SpeechRecognitionDialog? = null

    var recentSearchesList = ArrayList<String>()

    val viewModel: SearchActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        //connectViewToParent(this)
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        binding.viewModel = viewModel


        setSearchView()
        speechRecognitionDialog = SpeechRecognitionDialog(this, this)
        etSearch?.requestFocus()

        addOnClickListeners()

        sharedPreferencesUtil.recentSearchesList?.let {
            recentSearchesList.addAll(it)

            if (recentSearchesList.isNotEmpty())
                updateRecentSearchesUi(true)
        }

        addApiStateListener()

        getTopSearches()

        addListeners()

    }

    private fun addListeners() {
        tvCancel.setOnClickListener {
            noResultFoundLayout.visibility = GONE

            if (recentSearchesList.isNotEmpty()) {
                recentSearchesLayout.visibility = VISIBLE
            }

            topSearchesLayout.visibility = VISIBLE
        }

        tvRecentOne.setOnClickListener {
            updateSearchView(tvRecentOne.text.toString())
        }
        tvRecentTwo.setOnClickListener {
            updateSearchView(tvRecentTwo.text.toString())
        }
        tvRecentThree.setOnClickListener {
            updateSearchView(tvRecentThree.text.toString())
        }
    }

    fun updateSearchView(query: String) {
        etSearch.setQuery(query, false)
        etSearch.clearFocus()
    }

    private fun addApiStateListener() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStateFlow.collect {
                when (it) {
                    ApiState.Loading -> {
                        showHideLoader(true)
                    }
                    is ApiState.Success<*> -> {
                        showHideLoader(false)
                        withContext(Dispatchers.Main) {
                            handleApiResponse(it.data)
                        }

                    }
                    is ApiState.Failure -> {
                        showHideLoader(false)
                        it.ex.printStackTrace()
                    }
                }
            }
        }
    }

    private fun handleApiResponse(data: Any?) {
        when (data) {
            is TopSearches -> {
                addTopSearchesChips(data as? TopSearches)
            }
        }
    }

    private fun addTopSearchesChips(topSearches: TopSearches?) {

        topSearches?.let {
            for (item in topSearches) {
                addChip(item.key)
            }
        }

    }


    private fun addChip(key: String) {
        val chip = Chip(this)
        chip.text = key
        chip.setChipBackgroundColorResource(R.color.colorChipBackground)
        chip.isCloseIconVisible = false
        chip.isCheckable = true
        chip.chipStrokeColor = getColorStateList(R.color.colorChipStroke)
        chip.chipStrokeWidth = 4.toFloat()
        chip.checkedIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_done_24)

        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, (4).toFloat())
            .build()


        chip.shapeAppearanceModel = shapeAppearanceModel
        chip.setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.colorPrimary))
        chip.setTextAppearance(R.style.ChipTextAppearance)

        chip.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                updateSearchView(key)
            }
        }


        topSearchesChipGroup.addView(chip)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setView(): Int {
        return R.layout.activity_search
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        /* setSearchView()
         speechRecognitionDialog = SpeechRecognitionDialog(this, this)
         etSearch?.requestFocus()

         addOnClickListeners()

         sharedPreferencesUtil.recentSearchesList?.let {
             recentSearchesList.addAll(it)

             if (recentSearchesList.isNotEmpty())
                 updateRecentSearchesUi(true)
         }

         getTopSearches()*/
    }

    private fun getTopSearches() {
        viewModel.getTopSearches()
    }

    private fun addOnClickListeners() {
        ivBack.setOnClickListener {
            etSearch?.clearFocus()
            finish()
        }

        ivMic.setOnClickListener {
            if (Utility.checkPermissionForMic(this)) {
                etSearch?.clearFocus()
                speechRecognitionDialog?.showDialog()
            }
        }

        tryAgainTextView.setOnClickListener {

            onSearch(queryString)
        }


    }


    private fun setSearchView() {
        etSearch?.setOnQueryTextFocusChangeListener { v, hasFocus ->
            /* if (!hasFocus) {
                 onSearch(queryString)
             }*/
        }
        etSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                etSearch?.clearFocus()
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                queryString = s
                handleEmptyQuery(s)
                if (queryString.length > 0) {
                    ivMic?.visibility = View.GONE
                    topSearchesLayout.visibility = GONE
                    recentSearchesLayout.visibility = GONE
                } else {
                    adapter?.clear()
                    showRecentAndTopSearches()
                    noResultFoundLayout.visibility = GONE
                }
                searchHandler?.removeCallbacksAndMessages(null)
                searchHandler?.postDelayed({ onSearch(queryString) }, 500)
                return false
            }
        })
    }

    private fun showRecentAndTopSearches() {
        recentSearchesLayout.visibility = VISIBLE
        topSearchesLayout.visibility = VISIBLE
    }

    private fun handleEmptyQuery(s: String) {
        if (s.isEmpty()) {
            ivMic?.visibility = View.VISIBLE
        }
    }

    private fun onSearch(queryString: String) {
        if (queryString.length < 3) {
            return
        }
        noResultFoundLayout.visibility = GONE
        recentSearchesLayout.visibility = GONE
        topSearchesLayout.visibility = GONE
        adapter?.clear()

        recentSearchesList.add(0, queryString)
        addRecentSearchToStorage(queryString)

        updateRecentSearchesUi(false)

        if (recentSearchesList.size == 3) {
            val arrayList = ArrayList<String>()
            arrayList.addAll(recentSearchesList.subList(0, 2))
            recentSearchesList.clear()
            recentSearchesList.addAll(arrayList)
        }

        loadingLayout.visibility = VISIBLE


        val data = HashMap<String, String>()
        data.put("search", queryString)
        data.put("expand", "banner_image")
        //showHideLoader(true)
        dataManager.getProductsList(
            object :
                ApiResponseListener<APICommonResponse<List<ProductsData>>> {
                override fun onSuccess(response: APICommonResponse<List<ProductsData>>) {
                    loadingLayout.visibility = GONE
                    //showHideLoader(false)
                    if (response.results != null) {
                        response.results?.let {
                            setSearchData(it as ArrayList<ProductsData>)
                            updateNoResultUi(it)
                        }
                    }
                }

                override fun onError(apiError: ApiError?) {
                    loadingLayout.visibility = GONE
                    //showHideLoader(false)
                }
            },
            data
        )

        /*dataManager.search(object : ApiResponseListener<APICommonResponse<SearchData>> {
            override fun onSuccess(response: APICommonResponse<SearchData>) {
                loadingLayout.visibility = GONE
                response.data?.products?.let {
                    setSearchData(it)

                    updateNoResultUi(it)

                }

            }

            override fun onError(apiError: ApiError?) {
                loadingLayout.visibility = GONE
            }
        }, queryString)*/

        queryStringOld = queryString

    }

    private fun updateRecentSearchesUi(visibilityRequired: Boolean) {

        if (!recentSearchesLayout.isShown && visibilityRequired) {
            recentSearchesLayout.visibility = VISIBLE
        }


        for ((index, value) in recentSearchesList.withIndex()) {
            when (index) {
                0 -> {
                    tvRecentOne.visibility = VISIBLE
                    tvRecentOne.text = value
                }

                1 -> {
                    tvRecentTwo.visibility = VISIBLE
                    tvRecentTwo.text = value
                }

                2 -> {
                    tvRecentThree.visibility = VISIBLE
                    tvRecentThree.text = value
                }
            }

            if (index > 2) {
                break
            }
        }
    }

    private fun addRecentSearchToStorage(queryString: String) {
        sharedPreferencesUtil.saveRecentSearchesList(recentSearchesList)
    }

    private fun updateNoResultUi(list: ArrayList<ProductsData>) {
        if (list.size > 0) {
            noResultFoundLayout.visibility = GONE
            recentSearchesLayout.visibility = GONE
        } else {
            noResultFoundLayout.visibility = VISIBLE
            recentSearchesLayout.visibility = GONE
            topSearchesLayout.visibility = GONE
        }
    }

    private fun setSearchData(products: ArrayList<ProductsData>) {
        adapter = SearchAdapter(this@SearchActivity, products)
        rvSearch?.layoutManager = LinearLayoutManager(this@SearchActivity)
        rvSearch?.adapter = adapter
    }


    override fun onSpeechReceived(text: String?) {
        if (text != null && !text.isEmpty()) {
            etSearch?.setQuery(text, true)
        }
        speechRecognitionDialog?.dismissDialog()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }


}