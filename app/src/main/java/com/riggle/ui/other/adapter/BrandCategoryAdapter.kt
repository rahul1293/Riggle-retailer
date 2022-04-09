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
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.ui.other.BrandCategoryDetailActivity
import java.util.*

class BrandCategoryAdapter  // data is passed into the constructor
    (
    private val mContext: Context?,
    private val data: ArrayList<BrandsCategoryData>,
    private val title: String
) : RecyclerView.Adapter<BrandCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.list_brand_category, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* mContext?.let {
             Glide.with(mContext)
                 .load(data[position].image)
                 .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                 .into(holder.ivImg)

             holder.tvProduct.text = String.format(mContext.getString(R.string.value_products), data[position].product_count);
         }

         holder.tvName.text = data[position].name


         if (position == data.size - 1)
             holder.view.visibility = View.GONE
         else
             holder.view.visibility = View.VISIBLE*/

        holder.rlMain.setOnClickListener {
            mContext?.let {
                BrandCategoryDetailActivity.start(mContext, data, title, position)
            }
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return 10/*data.size*/
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        var view: View = itemView.findViewById(R.id.view)
        val rlMain: RelativeLayout = itemView.findViewById(R.id.rlMain);
    }
}