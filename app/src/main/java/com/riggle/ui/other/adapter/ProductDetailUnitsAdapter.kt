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

class ProductDetailUnitsAdapter  // data is passed into the constructor
(private val mContext: Context, private val unitData: ArrayList<String>) : RecyclerView.Adapter<ProductDetailUnitsAdapter.ViewHolder>() {

    var selectedPosition = -1

    private var listener: ProductUnitListener? = null

    fun setListener(listener: ProductUnitListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_product_detail_unit, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: String = unitData[position]

        if (selectedPosition == position) {
            holder.rlUnit.background = ContextCompat.getDrawable(mContext, R.drawable.rounded_4dp_border_primary_clr)
            holder.tvUnitSize.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        } else {
            holder.rlUnit.background = ContextCompat.getDrawable(mContext, R.drawable.rounded_4dp_border)
            holder.tvUnitSize.setTextColor(ContextCompat.getColor(mContext, R.color.black_54))
        }

        holder.tvUnitSize.text = data

        holder.rlUnit.setOnClickListener {
            listener?.itemClicked(position)
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return unitData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rlUnit: RelativeLayout = itemView.findViewById(R.id.rlUnit)
        var tvUnitSize: TextView = itemView.findViewById(R.id.tvUnitSize);
    }

    interface ProductUnitListener {
        fun itemClicked(pos: Int)
    }

    fun selectedItem(pos: Int) {
        selectedPosition = pos
    }
}