package com.riggle.ui.profile.editprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.databinding.ItemDocumentImagesBinding
import com.riggle.ui.utils.BaseAdapter
import kotlinx.android.synthetic.main.item_document_images.view.*

class DocumentImagesAdapter(var list: ObservableList<DocumentsItemListViewModel>) :
    BaseAdapter<DocumentImagesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ItemDocumentImagesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_document_images,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.viewModel  = list[position]
        holder.binding.executePendingBindings()

        holder.itemView.deleteImageView.setOnClickListener {
            listener?.onItemClicked(it, holder.absoluteAdapterPosition)
        }
    }

    class ViewHolder(var binding: ItemDocumentImagesBinding) : RecyclerView.ViewHolder(binding.root)
}