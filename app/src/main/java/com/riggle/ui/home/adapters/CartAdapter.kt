package com.riggle.ui.home.adapters

import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import kotlinx.android.synthetic.main.list_cart_items.view.*
import java.util.*

class CartAdapter  // data is passed into the constructor
internal constructor(private val mContext: Context, var productsData: ArrayList<ProductsData>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var listener: HomeProductsListener? = null

    fun setListener(listener: HomeProductsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_cart_items, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*try {*/
        if (position == productsData.size - 1)
            holder.view.visibility = View.INVISIBLE
        else
            holder.view.visibility = View.VISIBLE

        val data: ProductsData? = productsData[position].product

        if (data != null) {
            /*Glide.with(mContext)
                .load(data.banner_image?.image)
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)*/

            holder.tvName.text = data.name
            //holder.itemView.tvCategory.text = data.description
            holder.tvMOQ.text = data.moq.toString()

            val strikePrice: Int? = data.strikePrice
            if (strikePrice != null) {
                holder.tvStrikePrice.text =
                    String.format(mContext.getString(R.string.rupees_value), data.strikePrice)
                holder.tvStrikePrice.paintFlags =
                    holder.tvStrikePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvStrikePrice.visibility = View.VISIBLE
            } else
                holder.tvStrikePrice.visibility = View.GONE
        } else {
            /*Glide.with(mContext)
                .load(productsData[position].banner_image?.image)
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)*/

            holder.tvName.text = productsData[position].name
            //holder.itemView.siblingInfoTextView.text = productsData[position].productSize
            //holder.itemView.tvCategory.text = data.description
            holder.tvMOQ.text = productsData[position].step.toString()

            var mixProduct = ""
            productsData[position].units?.let {
                for (index in it) {
                    mixProduct =
                        mixProduct + index.product?.name + " " + index.quantity.toString() + ","
                }
            }
            holder.itemView.siblingInfoTextView.text = mixProduct

            val strikePrice: Int? = productsData[position].strikePrice
            if (strikePrice != null) {
                holder.tvStrikePrice.text =
                    String.format(
                        mContext.getString(R.string.rupees_value),
                        productsData[position].strikePrice
                    )
                holder.tvStrikePrice.paintFlags =
                    holder.tvStrikePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvStrikePrice.visibility = View.VISIBLE
            } else
                holder.tvStrikePrice.visibility = View.GONE

        }

        //holder.tvServiceHub.text = "Service hub : "+productsData[position].free_product?.service_hub?.name

        if (productsData[position].free_product == null) {
            holder.tvFreeProductName.visibility = View.GONE
        } else {
            holder.tvFreeProductName.visibility = View.VISIBLE
            holder.tvFreeProductName.text =
                productsData[position].free_product_quantity.toString() + " Free, " + productsData[position].free_product?.name
        }

        holder.tvPrice.text =
            String.format(
                mContext.getString(R.string.rupees_value_double),
                productsData[position].rate
            )

        holder.tvGrandPrice.text =
            String.format(
                mContext.getString(R.string.rupees_value_double),
                Math.round(productsData[position].amount).toFloat()
            )

        //val profit: Int? = data.profit
        val profit: Float? =
            productsData[position].margin//(data.base_rate?.toInt() - productsData[position]?.rate) * productsData[position].quantity
        if (profit != null && profit > 0f) {
            holder.tvProfit.text =
                String.format(mContext.getString(R.string.rupees_value_profits), profit)
            //holder.llMargin.visibility = View.VISIBLE
        } else {
            //holder.llMargin.visibility = View.INVISIBLE
        }

        val discount: Int? = if (data != null) data.discount else {
            productsData[position].discount
        }
        /*if (discount != null) {
            holder.tvOff.text =
                String.format(mContext.getString(R.string.value_off), discount)
            holder.tvOff.visibility = View.VISIBLE
        } else
            holder.tvOff.visibility = View.GONE*/

        //if due to some calculation quantity value was coming in negative, reset it to 0
        productsData[position].quantity?.let { item_cart ->
            if (item_cart < 0) {
                holder.tvQuantSet.text = "0"
                //productsData.removeAt(holder.adapterPosition)
            } else {
                //holder.tvQuantSet.text = (data.item_cart.toString())
                holder.tvQuantSet.text = (item_cart.toString())
            }
        }

        // holder.ivMinus.isEnabled = data.item_cart!! > 0
        //holder.ivMinus.isClickable = data.item_cart!! > 0

        holder.ivPlus.setOnClickListener {
            //val quantity = holder.tvQuantSet.text.toString().toInt() + (1 * data.moq)
            val quantity = if (data != null) {
                (holder.tvQuantSet.text.toString().toInt() + (1 * data.moq))
            } else {
                (holder.tvQuantSet.text.toString()
                    .toInt() + (1 * productsData[holder.adapterPosition].step))
            }

            productsData[holder.adapterPosition].item_cart = quantity
            holder.tvQuantSet.text = quantity.toString()

            //val displayPrice = (holder.tvGrandPrice.text.toString().replace("₹", "")
            //.toFloat() + data.retailer_price)
            //productsData[holder.adapterPosition].displayPrice = displayPrice.roundToInt()
            //holder.tvGrandPrice.text = String.format(mContext.getString(R.string.rupees_value), data.displayPrice)

            if (data != null) {
                listener?.itemClicked(
                    data.id,
                    quantity.toString(),
                    productsData[holder.adapterPosition],
                    1
                )
            } else {
                listener?.itemClicked(
                    productsData[holder.adapterPosition].id,
                    quantity.toString(),
                    productsData[holder.adapterPosition],
                    2
                )
            }
        }

        holder.ivMinus.setOnClickListener {

            //added the greater than 0 check so that the value doesn't go  negative
            if (holder.tvQuantSet.text.toString().toInt() > 0) {
                val quantity = if (data != null) {
                    (holder.tvQuantSet.text.toString().toInt() - (1 * data.moq))
                } else {
                    (holder.tvQuantSet.text.toString()
                        .toInt() - (1 * productsData[holder.adapterPosition].step))
                }
                productsData[holder.adapterPosition].item_cart = quantity
                holder.tvQuantSet.text = quantity.toString()
                //if after subtraction quantity becomes zero, remove the item from the list

                //val displayPrice = (holder.tvGrandPrice.text.toString().replace("₹", "")
                //  .toFloat() - data.retailer_price)
                //productsData[holder.adapterPosition].displayPrice = displayPrice.roundToInt()
                //holder.tvGrandPrice.text = String.format(mContext.getString(R.string.rupees_value), data.displayPrice)

                if (data != null) {
                    listener?.itemClicked(
                        data.id,
                        quantity.toString(),
                        productsData[holder.adapterPosition],
                        -1
                    )
                } else {
                    listener?.itemClicked(
                        productsData[holder.adapterPosition].id,
                        quantity.toString(),
                        productsData[holder.adapterPosition],
                        2
                    )
                }

                if (quantity <= 0) {
                    productsData.removeAt(holder.adapterPosition)
                    //notifyItemRemoved(holder.adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }

        holder.ivRemove.setOnClickListener {

            var numberOfSiblingItemsInCart = 0
            for (item in productsData) {
                /*if (item.siblingID == productsData[holder.adapterPosition].siblingID) {
                    ++numberOfSiblingItemsInCart
                }*/
            }

            if (productsData[position].product == null) {
                listener?.itemClicked(
                    productsData[holder.adapterPosition].id,
                    "0",
                    productsData[holder.adapterPosition],
                    2
                )
            } else {
                data?.id?.let { it1 ->
                    listener?.itemClicked(
                        it1,
                        "0",
                        productsData[holder.adapterPosition],
                        0
                    )
                }
            }
            productsData.removeAt(holder.adapterPosition)
            notifyDataSetChanged()
        }

        holder.tvQuantSet.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //using the itemClicked method with the product details
                //if (s.isNotEmpty())
                //listener?.itemClicked(data.id, s.toString(), productsData[holder.adapterPosition])
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
        /*} catch (e: Exception) {
            e.printStackTrace()
        }*/

    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var view: View = itemView.findViewById(R.id.view)
        //var ivImg: AppCompatImageView = itemView.findViewById(R.id.ivImg)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvProfit: TextView = itemView.findViewById(R.id.tvProfit)
        var llMargin: LinearLayout = itemView.findViewById(R.id.llMargin)
        var tvMOQ: TextView = itemView.findViewById(R.id.tvMOQ)
        var ivMinus: AppCompatImageView = itemView.findViewById(R.id.ivMinus)
        var tvQuantSet: TextView = itemView.findViewById(R.id.tvQuantSet)
        var ivPlus: AppCompatImageView = itemView.findViewById(R.id.ivPlus)
        var ivRemove: AppCompatImageView = itemView.findViewById(R.id.ivRemove);
        var tvStrikePrice: TextView = itemView.findViewById(R.id.tvStrikePrice);
        //var tvOff: TextView = itemView.findViewById(R.id.tvOff);
        var tvGrandPrice: TextView = itemView.findViewById(R.id.tvGrandPrice)
        var tvServiceHub: TextView = itemView.findViewById(R.id.tvServiceHub)
        var tvFreeProductName: TextView = itemView.findViewById(R.id.tvFreeProductName)
    }

    fun removeItemFromCart(pos: Int) {
        productsData.removeAt(pos);
        notifyItemRemoved(pos);
    }

    interface HomeProductsListener {
        //fun itemClicked(product_id: Int, toString: String)
        // overloaded itemClicked method to take product as parameter as well
        //fun itemClicked(product_id: Int, toString: String, product: ProductsData)

        /**
         * if -1 = means item reduced  or - symbol used
         * if 0 - means product removed
         * if 1 = means item quantity increased
         */
        fun itemClicked(product_id: Int, toString: String, product: ProductsData, type: Int)
    }
}