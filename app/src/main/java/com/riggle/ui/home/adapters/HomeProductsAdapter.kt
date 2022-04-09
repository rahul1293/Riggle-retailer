package com.riggle.ui.home.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import com.riggle.ui.other.ProductDetailPage
import java.util.*

class HomeProductsAdapter  // data is passed into the constructor
internal constructor(private val mContext: Context, private val productsData: ArrayList<ProductsData>) : RecyclerView.Adapter<HomeProductsAdapter.ViewHolder>() {

    private var listener: HomeProductsListener? = null

    fun setListener(listener: HomeProductsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_home_products, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: ProductsData = productsData[position]
        if (position == productsData.size - 1)
            holder.view.visibility = View.GONE
        else
            holder.view.visibility = View.VISIBLE

        data.badgeName?.let {

            if(data.badgeName.isNotEmpty()){
                holder.tvTrending.visibility = VISIBLE
                holder.tvTrending.background.setTint(Color.parseColor(data.badgeColor))
                holder.tvTrending.setTextColor(Color.parseColor(data.badgeTextColor))
            }else{
                holder.tvTrending.visibility = GONE
            }
        }


        Glide.with(mContext)
                .load(data.image)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)
        //holder.tvCategory.text = data.description
        holder.tvName.text = data.name
        holder.tvPrice.text = String.format(mContext.getString(R.string.rupees_value), data.retailer_price)
        val strikePrice: Int? = data.strikePrice
        if (strikePrice != null) {
            holder.tvStrikePrice.text = String.format(mContext.getString(R.string.rupees_value), data.strikePrice)
            holder.tvStrikePrice.paintFlags = holder.tvStrikePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvStrikePrice.visibility = View.VISIBLE
        } else
            holder.tvStrikePrice.visibility = View.GONE

        val profit: Int? = data.profit
        if (profit != null) {
            holder.tvProfit.text = String.format(mContext.getString(R.string.rupees_value_profit), data.profit)
            holder.tvProfit.visibility = View.VISIBLE
        } else {
            holder.tvProfit.visibility = View.GONE
        }
        holder.tvMRP.text = String.format(mContext.getString(R.string.rupees_value), data.packMrp)
        holder.siblingInfoTextView.text = data.siblingInfo
        val discount: Int? = data.discount
        if (discount != null) {
            holder.tvOff.text = String.format(mContext.getString(R.string.value_off), data.discount)
            holder.tvOff.visibility = View.VISIBLE
        } else
            holder.tvOff.visibility = View.GONE

        if (data.item_cart!! > 0) {
            holder.llMOQ.visibility = View.GONE
            holder.tvCartCount.text = String.format(mContext.getString(R.string.value_items_added_to_cart), data.item_cart)
            holder.tvCartCount.visibility = View.VISIBLE
            holder.tvAdd.visibility = View.GONE
            if (data.is_variant_key!!) {
                holder.tvUpdate.visibility = View.VISIBLE
                holder.cvQuant.visibility = View.GONE
            } else {
                holder.tvUpdate.visibility = View.GONE
                holder.cvQuant.visibility = View.VISIBLE
                holder.tvQuantSet.text = data.item_cart.toString()
            }

        } else {
            holder.llMOQ.visibility = View.VISIBLE
            holder.tvMOQ.text = data.moq.toString()
            holder.tvCartCount.visibility = View.GONE
            holder.tvAdd.visibility = View.VISIBLE
            holder.tvUpdate.visibility = View.GONE
            holder.cvQuant.visibility = View.GONE
        }

        holder.tvRiggleCoins.text = String.format(mContext.getString(R.string.earn_value), data.riggle_coins)

        holder.tvAdd.setOnClickListener(View.OnClickListener {
            addUpdateItem(data, holder)
        })

        holder.tvUpdate.setOnClickListener(View.OnClickListener {
            addUpdateItem(data, holder)
        })

        holder.ivPlus.setOnClickListener {
            holder.tvQuantSet.text = (holder.tvQuantSet.text.toString().toInt() + (1 * data.moq)).toString()
            listener?.singleVariantUpdate(data.id, holder.tvQuantSet.text.toString().toInt())
        }

        holder.ivMinus.setOnClickListener {
            if (holder.tvQuantSet.text.toString().toInt() > 0) {
                holder.tvQuantSet.text = (holder.tvQuantSet.text.toString().toInt() - (1 * data.moq)).toString()
                listener?.singleVariantUpdate(data.id, holder.tvQuantSet.text.toString().toInt())
            }
        }

        holder.rlProduct.setOnClickListener {
            ProductDetailPage.start(mContext, data.id)
        }
    }

    private fun addUpdateItem(data: ProductsData, holder: ViewHolder) {
        if (data.is_variant_key!!)
            listener?.itemClicked(data.siblingID)
        else {
            holder.cvQuant.visibility = View.VISIBLE
            holder.llMOQ.visibility = View.GONE
            holder.tvAdd.visibility = View.GONE
            holder.tvUpdate.visibility = View.GONE
            holder.tvQuantSet.text = "0"
            holder.ivPlus.performClick()
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View = itemView.findViewById(R.id.view)
        var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg)
        //var tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvStrikePrice: TextView = itemView.findViewById(R.id.tvStrikePrice)
        var tvProfit: TextView = itemView.findViewById(R.id.tvProfit)
        var tvMRP: TextView = itemView.findViewById(R.id.tvMRP)
        var tvOff: TextView = itemView.findViewById(R.id.tvOff)
        var tvMOQ: TextView = itemView.findViewById(R.id.tvMOQ)
        var tvAdd: TextView = itemView.findViewById(R.id.tvAdd)
        var tvCartCount: TextView = itemView.findViewById(R.id.tvCartCount)
        var tvRiggleCoins: TextView = itemView.findViewById(R.id.tvRiggleCoins)
        var siblingInfoTextView: TextView = itemView.findViewById(R.id.siblingInfoTextView)
        var llMOQ: LinearLayout = itemView.findViewById(R.id.llMOQ)
        var tvUpdate: TextView = itemView.findViewById(R.id.tvUpdate)
        var tvTrending: TextView = itemView.findViewById(R.id.tvTrending)
        var cvQuant: CardView = itemView.findViewById(R.id.cvQuant)
        var tvQuantSet: TextView = itemView.findViewById(R.id.tvQuantSet)
        var ivMinus: AppCompatImageView = itemView.findViewById(R.id.ivMinus)
        var ivPlus: AppCompatImageView = itemView.findViewById(R.id.ivPlus)
        var rlProduct: RelativeLayout = itemView.findViewById(R.id.rlProduct)
    }

    interface HomeProductsListener {
        fun itemClicked(product_id: Int)
        fun singleVariantUpdate(id: Int, toInt: Int)
    }

    fun updateCart(pos: Int, product: ProductsData) {
        productsData[pos] = product
        notifyItemChanged(pos)
    }
}