package com.riggle.ui.base.activity

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.other.adapter.LoaderItemsRecyclerAdapter
import com.riggle.ui.utils.WrapContentLinearLayoutManager

abstract class CustomAppCompatActivityViewImpl : CustomAppActivityImpl() {
    val TAG = this.javaClass.name
    private var viewConnector: CustomAppViewConnector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewConnector!!.setView())
        init(savedInstanceState)
    }

    public override fun onStart() {
        super.onStart()
    }

    fun connectViewToParent(viewConnector: CustomAppViewConnector?) {
        this.viewConnector = viewConnector
    }

    private fun init(savedInstanceState: Bundle?) {
        CheckConnectorAndProcessView(savedInstanceState)
    }

    private fun CheckConnectorAndProcessView(savedInstanceState: Bundle?) {
        if (viewConnector != null) {
            //   ButterKnife.bind(this);
            viewConnector!!.initializeViews(savedInstanceState)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    val view: View
        get() = findViewById(R.id.content)

    protected fun populateLoaderRecyclerView(recyclerView: RecyclerView) {
        val adapter = LoaderItemsRecyclerAdapter()
        val mLayoutManager = WrapContentLinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}