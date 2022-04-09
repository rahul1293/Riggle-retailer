package com.riggle.ui.home.adapters


import android.content.Context
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.ProductsData
import com.riggle.ui.other.ProductDetailPage
import com.riggle.ui.subcategory.ProductListActivity
import kotlin.math.roundToInt

class ShopByBrandsProductsAdapter  // data is passed into the constructor
internal constructor(
    private val mContext: Context,
    private var productsData: ArrayList<ProductsData>
) : RecyclerView.Adapter<ShopByBrandsProductsAdapter.ViewHolder>() {

    private var listener: ShopByBrandsProductsListener? = null

    fun setListener(listener: ShopByBrandsProductsListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_home_products, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in eachdiscount cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (productsData != null && productsData.size > 0) {
            val data: ProductsData = productsData[position]
            if (position == productsData.size - 1)
                holder.view.visibility = GONE
            else
                holder.view.visibility = VISIBLE

            Glide.with(mContext)
                .load(data?.banner_image?.image)
                .placeholder(R.mipmap.ic_launcher)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(holder.ivImg)
            //holder.tvCategory.text = data.description
            holder.tvName.text = data.name
            //holder.itemView.siblingInfoTextView.text = data.productSize
            productsData[position].service_hub?.name?.let {
                holder.tvServiceHub.text = "Serviced By : " + it
            }
            holder.tvPrice.text =
                String.format(
                    mContext.getString(R.string.rupees_value),
                    data.company_rate.roundToInt()/*data.retailer_price*/
                )

            holder.tvMRP.text =
                String.format(
                    mContext.getString(R.string.rupees_value),
                    data.base_rate?.toFloat().roundToInt()/*data.retailer_price*/
                )

            val strikePrice: Int? = data.base_rate?.toFloat().roundToInt()
            if (strikePrice != null) {
                holder.tvStrikePrice.text =
                    String.format(
                        mContext.getString(R.string.rupees_value),
                        strikePrice/*data.strikePrice*/
                    )
                holder.tvStrikePrice.paintFlags =
                    holder.tvStrikePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvStrikePrice.visibility = GONE //VISIBLE
            } else
                holder.tvStrikePrice.visibility = GONE

            /*val profit: Int? = data.profit
            if (profit != null) {
                holder.tvProfit.text =
                    String.format(mContext.getString(R.string.rupees_value_profit), data.profit)
                holder.tvProfit.visibility = VISIBLE
            } else {
                holder.tvProfit.visibility = GONE
            }*/

            val discount: Int? = data.discount
            if (discount != null) {
                holder.tvOff.text =
                    String.format(mContext.getString(R.string.value_off), data.discount)
                holder.tvOff.visibility = VISIBLE
            } else
                holder.tvOff.visibility = GONE

            data.quantity?.let {
                if (it > 0) {
                    //holder.llMOQ.visibility = GONE
//                    holder.tvCartCount.text = String.format(
//                        mContext.getString(R.string.value_items_added_to_cart),
//                        data.item_cart
//                    )
//                    holder.tvCartCount.visibility = VISIBLE
                    holder.tvAdd.visibility = GONE

//                if (data.is_variant_key!!) {
//                    holder.tvUpdate.visibility = View.VISIBLE
//                    holder.cvQuant.visibility = View.GONE
//                } else {
//                    holder.tvUpdate.visibility = View.GONE
//                    holder.cvQuant.visibility = View.VISIBLE
//                    holder.tvQuantSet.text = data.item_cart.toString()
//                }

                    holder.tvUpdate.visibility = GONE
                    holder.cvQuant.visibility = VISIBLE
                    holder.tvQuantSet.text = it.toString()

                } else {
                    holder.llMOQ.visibility = View.VISIBLE
                    holder.tvMOQ.text = data.moq.toString()
                    //holder.tvCartCount.visibility = View.GONE
                    holder.tvAdd.visibility = View.VISIBLE
                    holder.tvUpdate.visibility = View.GONE
                    holder.cvQuant.visibility = View.GONE
                }
            }
            holder.tvMOQ.text = data.moq.toString()
            holder.tvDeliveryIn.text = "Delivery in " + data.delivery_tat_days.toString() + " days"

            try {
                if (data.riggle_coins != null) {
                    holder.llEarnPoints.visibility = VISIBLE
                    holder.tvRiggleCoins.text = "Earn " + data.riggle_coins
                    //String.format(mContext.getString(R.string.earn_value), data.riggle_coins)
                } else {
                    holder.llEarnPoints.visibility = GONE
                }

                holder.tvAdd.setOnClickListener(View.OnClickListener {
                    if (data.combo_products != null && data.combo_products.size > 0) {
                        listener?.comboClick(position)
                    } else {
                        addUpdateItem(data, holder)
                    }
                })

                holder.tvUpdate.setOnClickListener(View.OnClickListener {
                    //addUpdateItem(data, holder)
                })

                holder.ivPlus.setOnClickListener {
                    if (data.combo_products != null && data.combo_products.size > 0) {
                        listener?.comboClick(position)
                    } else {
                        if (holder.tvQuantSet.text.toString().toInt() % data.moq == 0) {
                            var quantity =
                                holder.tvQuantSet.text.toString().toInt() + (1 * data.moq)
                            //     productsData[holder.adapterPosition].item_cart = holder.tvQuantSet.text.toString().toInt() + (1 * data.moq)
                            holder.tvQuantSet.text = quantity.toString()
                            //notifyItemChanged(holder.adapterPosition)
                            listener?.singleVariantUpdate(data.id, quantity)
                        } else {
                            listener?.comboClick(position)
                        }
                    }
                }

                holder.ivMinus.setOnClickListener {
                    if (data.combo_products != null && data.combo_products.size > 0) {
                        listener?.comboClick(position)
                    } else {
                        if (holder.tvQuantSet.text.toString().toInt() % data.moq == 0) {
                            if (holder.tvQuantSet.text.toString().toInt() > 0) {
                                var quantity =
                                    holder.tvQuantSet.text.toString().toInt() - (1 * data.moq)
                                if (quantity < 0) {
                                    holder.tvQuantSet.text = (0).toString()
                                    listener?.singleVariantUpdate(data.id, 0)
                                } else {
                                    holder.tvQuantSet.text = (quantity).toString()
                                    listener?.singleVariantUpdate(data.id, quantity)
                                }
                            }
                        } else {
                            listener?.comboClick(position)
                        }
                    }
                }

                holder.rlProduct.setOnClickListener {
                    ProductListActivity.selected_pos = position
                    ProductDetailPage.start(mContext, data.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            holder.tvCartCount.visibility = View.GONE
            data.schemes?.let {
                if (it.size > 0) {
                    holder.tvCartCount.visibility = View.VISIBLE
                    holder.tvCartCount.text =
                        "Bulk Offer upto ₹" + String.format(
                            "%.2f",
                            it[it.size - 1].rate
                        ) + "/pc"

//                    holder.tvMRP.text =
//                        "₹" + String.format(
//                            "%.2f",
//                            it[0].rate
//                        ) + "/Unit"
                    holder.tvPrice.text =
                        "₹" + String.format(
                            "%.2f",
                            it[0].rate
                        ) + ""

                    val profit: Float? = data.base_rate?.toFloat() - it[0].rate
                    if (profit != null) {
                        holder.tvProfit.text =
                            String.format(mContext.getString(R.string.rupees_value_profits), profit)
                        holder.tvProfit.visibility = VISIBLE
                    } else {
                        holder.tvProfit.visibility = GONE
                    }

                }
            }

            if (data.combo_products != null && data.combo_products.size > 0) {
                holder.tvCombo.visibility = VISIBLE
            } else {
                holder.tvCombo.visibility = GONE
            }

        }
        holder.tvCartCount.setOnClickListener {
            listener?.itemClicked(position)
        }

        holder.tvCombo.setOnClickListener {
            //listener?.comboClick(position)
        }
    }

    private fun addUpdateItem(data: ProductsData, holder: ViewHolder) {
        /*  if (data.is_variant_key!!)
              listener?.itemClicked(data.siblingID)
          else {*/
        holder.cvQuant.visibility = View.VISIBLE
        //holder.llMOQ.visibility = View.INVISIBLE
        holder.tvAdd.visibility = View.GONE
        holder.tvUpdate.visibility = View.GONE
        holder.tvQuantSet.text = "0"
        holder.ivPlus.performClick()
        //}
    }

    // total number of cells
    override fun getItemCount(): Int {
        return productsData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
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
        var llMOQ: LinearLayout = itemView.findViewById(R.id.llMOQ)
        var llEarnPoints: LinearLayout = itemView.findViewById(R.id.llEarnPoints)
        var tvUpdate: TextView = itemView.findViewById(R.id.tvUpdate)
        var cvQuant: CardView = itemView.findViewById(R.id.cvQuant)
        var tvQuantSet: TextView = itemView.findViewById(R.id.tvQuantSet)
        var ivMinus: AppCompatImageView = itemView.findViewById(R.id.ivMinus)
        var ivPlus: AppCompatImageView = itemView.findViewById(R.id.ivPlus)
        var rlProduct: RelativeLayout = itemView.findViewById(R.id.rlProduct)
        var tvCombo: TextView = itemView.findViewById(R.id.tvCombo)
        var tvServiceHub: TextView = itemView.findViewById(R.id.tvServiceHub)
        var tvDeliveryIn: TextView = itemView.findViewById(R.id.tvDeliveryIn)
    }

    interface ShopByBrandsProductsListener {
        fun itemClicked(product_id: Int)
        fun singleVariantUpdate(id: Int, toInt: Int)
        fun comboClick(product_id: Int) {

        }
    }

    fun updateCart(pos: Int, product: ProductsData) {
        productsData[pos] = product
        notifyItemChanged(pos)
    }

    fun getList(): ArrayList<ProductsData> {
        return productsData
    }

    fun setList(data: ArrayList<ProductsData>) {
        productsData = data
        notifyDataSetChanged()
    }

}