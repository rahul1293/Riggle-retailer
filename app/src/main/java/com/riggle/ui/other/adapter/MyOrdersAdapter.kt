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
import com.google.gson.Gson
import com.riggle.R
import com.riggle.data.models.response.MyOrderDataOuter
import com.riggle.data.models.response.Products
import com.riggle.ui.other.OrderDetailActivity
import com.riggle.utils.Utility
import java.util.*

class MyOrdersAdapter  // data is passed into the constructor
    (private val mContext: Context, private val ordersData: ArrayList<MyOrderDataOuter>) :
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_my_orders, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order: MyOrderDataOuter = ordersData[position]

        holder.tvOrderId.text = order.code.toString()
        holder.tvOrderStatus.text = order.status
        if (order.status.equals("delivered", false)) {
            holder.tvDeliverDate.text = "Delivery date:  ${Utility.convertDate(order.deliveredAt)}"
        } else
            holder.tvDeliverDate.text = "Delivery date:  ${Utility.convertDate(order.deliveryDate)}"
        //holder.tvOrderDate.text = "Order date:  ${order.orderDate}"


        var x: Int = 0
        loop@ while (x < order.products.size) {
            val product: Products = order.products[x]
            when (x) {
                0 -> {
                    Glide.with(mContext)
                        .load(order.products[x].product?.banner_image?.image)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.ivImg1)
                    holder.tvProductName1.text = product.product?.name
                    holder.tvProductQuant1.text = product.quantity.toString()
                    if (order.products[x].freeProductQuantity == null || order.products[x].freeProductQuantity == 0) {
                        holder.tvFreeProductName.visibility = View.GONE
                    } else {
                        holder.tvFreeProductName.visibility = View.VISIBLE
                        holder.tvFreeProductName.text =
                            order.products[x].freeProductQuantity.toString() + " Free, " + order.products[x].freeProduct?.name
                    }
                    //holder.tvVariant1.text = product.productSize
                }
                1 -> {
                    holder.rlProd2.visibility = View.VISIBLE
                    Glide.with(mContext)
                        .load(order.products[x].product?.banner_image?.image)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.ivImg2)
                    holder.tvProductName2.text = product.product?.name
                    holder.tvProductQuant2.text = product.quantity.toString()
                    //holder.tvVariant2.text = product.productSize
                    if (order.products[x].freeProductQuantity == null || order.products[x].freeProductQuantity == 0) {
                        holder.tvFreeProductName1.visibility = View.GONE
                    } else {
                        holder.tvFreeProductName1.visibility = View.VISIBLE
                        holder.tvFreeProductName1.text =
                            order.products[x].freeProductQuantity.toString() + " Free, " + order.products[x].freeProduct?.name
                    }
                }
                2 -> {
                    holder.rlProd3.visibility = View.VISIBLE
                    Glide.with(mContext)
                        .load(order.products[x].product?.banner_image?.image)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.ivImg3)
                    holder.tvProductName3.text = product.product?.name
                    holder.tvProductQuant3.text = product.quantity.toString()

                    if (order.products[x].freeProductQuantity == null || order.products[x].freeProductQuantity == 0) {
                        holder.tvFreeProductName2.visibility = View.GONE
                    } else {
                        holder.tvFreeProductName2.visibility = View.VISIBLE
                        holder.tvFreeProductName2.text =
                            order.products[x].freeProductQuantity.toString() + " Free, " + order.products[x].freeProduct?.name
                    }
                    //holder.tvVariant3.text = product.productSize

                    /*if(order.count.isNotEmpty()){
                        holder.rlMore.visibility = View.VISIBLE
                        // holder.tvMoreProds.text = String.format(mContext.getString(R.string.value_more_items_in_this_order), order.products.size - 3)
                        holder.tvMoreProds.text = order.count
                    }*/
                }
                3 -> {
                    holder.rlMore.visibility = View.VISIBLE
                    // holder.tvMoreProds.text = String.format(mContext.getString(R.string.value_more_items_in_this_order), order.products.size - 3)
                    //holder.tvMoreProds.text = order.count
                    break@loop
                }
            }
            x++
        }

        holder.rlOrder.setOnClickListener {
            OrderDetailActivity.start(
                mContext,
                order.id,
                Gson().toJson(order)
            )
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return ordersData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        var tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        var tvProductName1: TextView = itemView.findViewById(R.id.tvProductName1)
        var ivImg1: AppCompatImageView = itemView.findViewById(R.id.ivImg1)
        var tvProductQuant1: TextView = itemView.findViewById(R.id.tvProductQuant1)
        var tvVariant1: TextView = itemView.findViewById(R.id.tvVariant1)
        var tvVariant2: TextView = itemView.findViewById(R.id.tvVariant2)
        var tvVariant3: TextView = itemView.findViewById(R.id.tvVariant3)
        var rlProd2: RelativeLayout = itemView.findViewById(R.id.rlProd2)
        var tvProductName2: TextView = itemView.findViewById(R.id.tvProductName2)
        var ivImg2: AppCompatImageView = itemView.findViewById(R.id.ivImg2)
        var tvProductQuant2: TextView = itemView.findViewById(R.id.tvProductQuant2)
        var rlProd3: RelativeLayout = itemView.findViewById(R.id.rlProd3)
        var tvProductName3: TextView = itemView.findViewById(R.id.tvProductName3)
        var ivImg3: AppCompatImageView = itemView.findViewById(R.id.ivImg3)
        var tvProductQuant3: TextView = itemView.findViewById(R.id.tvProductQuant3)

        var tvFreeProductName: TextView = itemView.findViewById(R.id.tvFreeProductName)
        var tvFreeProductName1: TextView = itemView.findViewById(R.id.tvFreeProductName1)
        var tvFreeProductName2: TextView = itemView.findViewById(R.id.tvFreeProductName2)



        val rlMore: RelativeLayout = itemView.findViewById(R.id.rlMore)
        var tvMoreProds: TextView = itemView.findViewById(R.id.tvMoreProds)
        var tvDeliverDate: TextView = itemView.findViewById(R.id.tvDeliverDate)
        var tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val rlOrder: RelativeLayout = itemView.findViewById(R.id.rlOrder)
    }
}