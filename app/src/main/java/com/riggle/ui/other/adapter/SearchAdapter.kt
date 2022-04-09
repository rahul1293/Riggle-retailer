package com.riggle.ui.other.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import com.riggle.ui.other.ProductDetailPage
import java.util.*

class SearchAdapter  // data is passed into the constructor
(private val mContext: Context, private val productsData: ArrayList<ProductsData>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_search_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: ProductsData = productsData[position]

        if (position == productsData.size - 1)
            holder.view.visibility = View.GONE
        else
            holder.view.visibility = View.VISIBLE

        Glide.with(mContext)
                .load(data.banner_image?.image)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)

        holder.tvName.text = data.name //+ ", " + data.productSize

        holder.rlMain.setOnClickListener {
            ProductDetailPage.start(mContext, data.id)
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    fun clear() {
        productsData.clear()
        notifyDataSetChanged()
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rlMain: RelativeLayout = itemView.findViewById(R.id.rlMain)
        var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var view: View = itemView.findViewById(R.id.view)
    }
}