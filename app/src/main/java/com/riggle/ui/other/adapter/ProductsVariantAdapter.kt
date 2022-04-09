package com.riggle.ui.other.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import java.util.*

class ProductsVariantAdapter  // data is passed into the constructor
internal constructor(private val mContext: Context, private val productsData: ArrayList<ProductsData>) : RecyclerView.Adapter<ProductsVariantAdapter.ViewHolder>() {

    private var listener: HomeProductsListener? = null

    fun setListener(listener: HomeProductsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_variant_products, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (productsData!=null && productsData.size>0){

            val data: ProductsData = productsData[position]

            holder.tvQuantity.text = data.productSize
            val profit: Int? = data.profit
            if (profit != null) {
                holder.tvProfit.text = String.format(mContext.getString(R.string.rupees_value_profit), data.profit)
                holder.tvProfit.visibility = View.VISIBLE
            } else {
                holder.tvProfit.visibility = View.GONE
            }

            holder.tvPrice.text = String.format(mContext.getString(R.string.rupees_value), data.retailer_price)
            holder.tvMRP.text = String.format(mContext.getString(R.string.rupees_value), data.packMrp)
            holder.tvMOQ.text = data.moq.toString()

            if (data.item_cart != 0) {
                holder.cvQuant.visibility = View.VISIBLE
                holder.tvQuantSet.text = data.item_cart.toString()
                holder.tvAdd.visibility = View.GONE
            } else {
                holder.tvAdd.visibility = View.VISIBLE
                holder.cvQuant.visibility = View.GONE
            }

            holder.tvAdd.setOnClickListener {
                holder.cvQuant.visibility = View.VISIBLE
                holder.tvQuantSet.text = data.moq.toString()
                holder.tvAdd.visibility = View.GONE
            }

            holder.ivPlus.setOnClickListener {
                holder.tvQuantSet.text = (holder.tvQuantSet.text.toString().toInt() + (1 * data.moq)).toString()
            }

            holder.ivMinus.setOnClickListener {
                if (holder.tvQuantSet.text.toString().toInt() > 0)
                    holder.tvQuantSet.text = (holder.tvQuantSet.text.toString().toInt() - (1 * data.moq)).toString()
            }

            holder.tvQuantSet.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (s.isNotEmpty())
                        listener?.variantAdded(data.id, s.toString().toInt())
                    if (s.toString() == "0") {
                        holder.tvAdd.visibility = View.VISIBLE
                        holder.cvQuant.visibility = View.GONE
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                }
            })
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return 3/*productsData.size*/
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvProfit: TextView = itemView.findViewById(R.id.tvProfit)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvMRP: TextView = itemView.findViewById(R.id.tvMRP)
        var tvMOQ: TextView = itemView.findViewById(R.id.tvMOQ)
        var tvAdd: TextView = itemView.findViewById(R.id.tvAdd)
        var cvQuant: CardView = itemView.findViewById(R.id.cvQuant)
        var ivMinus: AppCompatImageView = itemView.findViewById(R.id.ivMinus)
        var tvQuantSet: TextView = itemView.findViewById(R.id.tvQuantSet)
        var ivPlus: AppCompatImageView = itemView.findViewById(R.id.ivPlus)
    }

    interface HomeProductsListener {
        fun variantAdded(product_id: Int, quantity: Int)
    }
}