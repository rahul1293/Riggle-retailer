package com.riggle.ui.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.riggle.data.DataManager
import com.riggle.data.pref.SharedPreferencesUtil
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.other.adapter.LoaderItemsRecyclerAdapter
import com.riggle.ui.utils.WrapContentLinearLayoutManager
import com.riggle.utils.RiggleLogger
import org.koin.android.ext.android.inject

open class CustomAppFragmentViewImpl : Fragment() {
    val TAG = this.javaClass.name

    /*   @Inject
    public SharedPreferencesUtil sharedPreferencesUtil;

    @Inject
    public DataManager dataManager;*/

    protected val sharedPreferencesUtil : SharedPreferencesUtil by inject()
    protected val dataManager : DataManager by inject()

    //var context : Context? = null

    private var viewConnector: CustomAppViewConnector? = null
    private var rootView: View? = null
    private var unbinder: Unbinder? = null
    override fun onResume() {
        super.onResume()
        RiggleLogger.d("fatal::CustomAppFragmentViewImpl", "onResume")
        getFragmentVisibility(true)
    }

    override fun onPause() {
        RiggleLogger.d("fatal::CustomAppFragmentViewImpl", "onPause")
        getFragmentVisibility(false)
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //RiggleApplication.getInstance().getComponent().inject(this);
        init(inflater, container, savedInstanceState)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //this.context = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun connectViewToParent(viewConnector: CustomAppViewConnector?) {
        this.viewConnector = viewConnector
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
        CheckConnectorAndProcessView(inflater, container, savedInstanceState)
    }

    private fun CheckConnectorAndProcessView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        if (viewConnector != null) {
            initializeView(inflater, container, savedInstanceState)
            unbinder = ButterKnife.bind(this, requireView())
            viewConnector!!.initializeViews(savedInstanceState)
        } else {
        }
    }

    fun bindView() {
        unbinder = ButterKnife.bind(this, requireView())
    }

    fun initializeView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        rootView = inflater.inflate(viewConnector!!.setView(), container, false)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun getView(): View? {
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        RiggleLogger.e("fatal:fragment", "onDestroyView")
    }

    fun getFragmentVisibility(isVisible: Boolean) {}
    protected fun populateLoaderRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.let {
            val adapter = LoaderItemsRecyclerAdapter()
            val mLayoutManager = WrapContentLinearLayoutManager(getContext())
            recyclerView.layoutManager = mLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

    }
}