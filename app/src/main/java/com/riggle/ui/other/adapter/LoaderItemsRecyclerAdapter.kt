package com.riggle.ui.other.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R

class LoaderItemsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var loaderItemsSize = 3

    constructor() {}
    constructor(loaderItemsSize: Int) {
        this.loaderItemsSize = loaderItemsSize
    }

    private fun getLayoutView(parent: ViewGroup, view: Int): View {
        return LayoutInflater.from(parent.context).inflate(view, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        view = getLayoutView(parent, R.layout.list_item_loader)
        return LoaderViewHolders(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return loaderItemsSize
    }

    inner class LoaderViewHolders(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    )
}