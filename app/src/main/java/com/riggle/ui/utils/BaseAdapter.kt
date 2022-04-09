package com.riggle.ui.utils

import androidx.recyclerview.widget.RecyclerView

abstract class  BaseAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    var listener: OnRecyclerViewItemClick? = null

    fun setItemClickListener(listener: OnRecyclerViewItemClick) {
        this.listener = listener
    }
}