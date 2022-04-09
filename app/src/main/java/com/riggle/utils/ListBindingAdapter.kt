package com.riggle.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.data.models.response.CoinsEarning
import com.riggle.ui.profile.earnings.EarningsAdapter
import com.riggle.ui.profile.editprofile.DocumentImagesAdapter
import com.riggle.ui.profile.editprofile.DocumentsItemListViewModel
import com.riggle.ui.utils.OnRecyclerViewItemClick

@BindingAdapter("uriList")
fun tempList(view: RecyclerView, list: ObservableList<DocumentsItemListViewModel>) {
    view.adapter = DocumentImagesAdapter(list)

    (view.adapter as DocumentImagesAdapter).setItemClickListener(object : OnRecyclerViewItemClick {
        override fun onItemClicked(view: View, position: Int) {
            when(view.id){
                R.id.deleteImageView->{

                }
            }
        }
    })
}

@BindingAdapter("earningsList")
fun earningsList(view: RecyclerView, list: ObservableList<CoinsEarning>) {
    if(view.adapter==null){
        view.setHasFixedSize(true)
        view.adapter = EarningsAdapter(list)
    }else{
        view.adapter?.notifyDataSetChanged()
    }



/*
    (view.adapter as EarningsAdapter).setItemClickListener(object : OnRecyclerViewItemClick {
        override fun onItemClicked(view: View, position: Int) {
            when(view.id){
                R.id.deleteImageView->{

                }
            }
        }
    })*/
}