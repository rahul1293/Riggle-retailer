package com.riggle.ui.credit

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.data.models.response.CoinsEarning
import com.riggle.data.models.response.Lenders
import com.riggle.databinding.ItemPointHistoryBinding
import com.riggle.databinding.ListPaylaterItemsBinding
import com.riggle.ui.utils.BaseAdapter
import kotlinx.android.synthetic.main.item_point_history.view.*


class LenderAdapter(var list: List<Lenders>) :
    BaseAdapter<LenderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ListPaylaterItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_paylater_items,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

/*
        holder.binding.viewModel = PointsItemListViewModel(list[position])
        holder.binding.executePendingBindings()

        *//*     holder.itemView.deleteImageView.setOnClickListener {
                 listener?.onItemClicked(it, holder.absoluteAdapterPosition)
             }*//*

        if (position == list.size - 1) {
            holder.itemView.tvNoMoreHistory.visibility = VISIBLE
        } else {
            holder.itemView.tvNoMoreHistory.visibility = GONE
        }*/
    }

    class ViewHolder(var binding: ListPaylaterItemsBinding) : RecyclerView.ViewHolder(binding.root)
}