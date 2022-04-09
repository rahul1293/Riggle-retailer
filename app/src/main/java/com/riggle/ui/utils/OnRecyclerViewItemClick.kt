package com.riggle.ui.utils

import android.view.View

interface OnRecyclerViewItemClick {
    fun onItemClicked(view: View, position: Int)
}