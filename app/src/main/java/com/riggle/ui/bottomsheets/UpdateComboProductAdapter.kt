package com.riggle.ui.bottomsheets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import java.util.ArrayList

class UpdateComboProductAdapter internal constructor(
    private val mContext: Context,
    private val productsData: ArrayList<ProductsData>
) : RecyclerView.Adapter<UpdateComboProductAdapter.ViewHolder>() {

    private var listener: HomeProductsListener? = null

    fun setListener(listener: HomeProductsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.list_update_combo_products, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var combo = ""
        holder.tvQuantity.text =
            productsData[position].product?.name
        holder.tvAdd.setOnClickListener {
            addUpdateItem(holder)
        }

        productsData[position].quantity?.let {
            if (it > 0) {
                holder.cvQuant.visibility = View.VISIBLE
                holder.tvAdd.visibility = View.GONE
                holder.tvQuantSet.text = it.toString()
            }
        }

        holder.ivPlus.setOnClickListener {
            var quantity =
                holder.tvQuantSet.text.toString().toInt() + 1
            holder.tvQuantSet.text = quantity.toString()
            productsData[position].quantity = quantity
            listener?.variantAdded(productsData[position].id, quantity)
        }

        holder.ivMinus.setOnClickListener {
            if (holder.tvQuantSet.text.toString().toInt() > 0) {
                var quantity =
                    holder.tvQuantSet.text.toString()
                        .toInt() - 1
                holder.tvQuantSet.text = (quantity).toString()
                productsData[position].quantity = quantity
                listener?.variantAdded(productsData[position].id, quantity)
                if (quantity == 0)
                    addUpdateItemAdd(holder)
            }
        }
    }

    private fun addUpdateItem(holder: UpdateComboProductAdapter.ViewHolder) {
        holder.cvQuant.visibility = View.VISIBLE
        holder.tvAdd.visibility = View.GONE
        holder.tvQuantSet.text = "0"
        holder.ivPlus.performClick()
    }

    private fun addUpdateItemAdd(holder: UpdateComboProductAdapter.ViewHolder) {
        holder.cvQuant.visibility = View.GONE
        holder.tvAdd.visibility = View.VISIBLE
        holder.tvQuantSet.text = "0"
    }

    open fun getList(): ArrayList<ProductsData> {
        return productsData
    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvMOQ: TextView = itemView.findViewById(R.id.tvMOQ)
        var tvAdd: TextView = itemView.findViewById(R.id.tvAdd)

        var cvQuant: CardView = itemView.findViewById(R.id.cvQuant)
        var tvQuantSet: TextView = itemView.findViewById(R.id.tvQuantSet)
        var ivMinus: AppCompatImageView = itemView.findViewById(R.id.ivMinus)
        var ivPlus: AppCompatImageView = itemView.findViewById(R.id.ivPlus)
    }

    interface HomeProductsListener {
        fun variantAdded(product_id: Int, quantity: Int)
    }
}
