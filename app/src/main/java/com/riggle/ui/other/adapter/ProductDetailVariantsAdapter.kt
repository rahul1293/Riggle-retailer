package com.riggle.ui.other.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.DeliveryDateSlots
import com.riggle.data.models.response.Variants
import com.riggle.ui.other.ProductDetailPage
import java.util.*

class ProductDetailVariantsAdapter  // data is passed into the constructor
(private val mContext: Context, private val variantData: ArrayList<Variants>,val product_id: Int) : RecyclerView.Adapter<ProductDetailVariantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_product_detail_variant, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: Variants = variantData[position]

        if(product_id == data.id)
            holder.rlProduct.background = ContextCompat.getDrawable(mContext, R.drawable.rounded_4dp_border_primary_clr)

        Glide.with(mContext)
                .load(data.image)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)

        holder.rlProduct.setOnClickListener {
            ProductDetailPage.start(mContext, data.id);
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return variantData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rlProduct: RelativeLayout = itemView.findViewById(R.id.rlProduct)
        var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg);
    }
}