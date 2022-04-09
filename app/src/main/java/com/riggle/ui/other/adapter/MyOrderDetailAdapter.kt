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
import com.riggle.data.models.response.MyOrderData
import com.riggle.data.models.response.OrderProduct
import com.riggle.data.models.response.Products
import com.riggle.data.models.response.ProductsData
import com.riggle.ui.other.OrderDetailActivity
import java.util.*

class MyOrderDetailAdapter  // data is passed into the constructor
    (private val mContext: Context, private val productsData: ArrayList<Products>) :
    RecyclerView.Adapter<MyOrderDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.list_my_order_detail, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product: Products = productsData[position]

        if (position == 0)
            holder.viewOrder.visibility = View.GONE
        else
            holder.viewOrder.visibility = View.VISIBLE

        Glide.with(mContext)
            .load(product.product?.banner_image?.image)
            .placeholder(R.mipmap.ic_launcher)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(holder.ivImg)
        holder.tvProductName.text = product.product?.name
        holder.tvProductQuant.text = product.quantity.toString()
        //holder.tvVariant.text = product.productSize
        //holder.tvVariant.text = product.productSize
        holder.tvGrandPrice.text =
            String.format(mContext.getString(R.string.rupees_value_double), product.amount)

        if (product.freeProductQuantity == null || product.freeProductQuantity == 0) {
            holder.tvFreeProductName.visibility = View.GONE
        } else {
            holder.tvFreeProductName.visibility = View.VISIBLE
            holder.tvFreeProductName.text =
                product.freeProductQuantity.toString() + " Free, " + product.freeProduct?.name
        }

    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg)
        var tvProductQuant: TextView = itemView.findViewById(R.id.tvProductQuant)
        var tvVariant: TextView = itemView.findViewById(R.id.tvVariant)
        val viewOrder: View = itemView.findViewById(R.id.viewOrder)
        val tvFreeProductName: TextView = itemView.findViewById(R.id.tvFreeProductName)
        val tvGrandPrice: TextView = itemView.findViewById(R.id.tvGrandPrice)
    }
}