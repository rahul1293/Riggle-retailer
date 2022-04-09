package com.riggle.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riggle.R
import com.riggle.data.models.response.BrandsCategoryData
import com.riggle.ui.other.BrandCategoryDetailActivity
import com.riggle.ui.other.ShopByBrandCategory
import com.riggle.ui.subcategory.SubCategoryActivity
import java.util.*

class HomeBrandAdapter  // data is passed into the constructor
internal constructor(
    private val mContext: Context,
    private val brandsData: ArrayList<BrandsCategoryData>
) : RecyclerView.Adapter<HomeBrandAdapter.ViewHolder>() {

    var isMoreAvailable: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_home_brand, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (brandsData != null && brandsData.size > 0) {
            if (position == 5 && isMoreAvailable) {
                holder.ivBrand.visibility = View.GONE
                holder.tvMore.text = "+" + (brandsData.size - 5) + "\nMore"
                holder.cvBrand.setOnClickListener {
                    //ShopByBrandCategory.start(mContext, ShopByBrandCategory.KEY_BRAND_PAGE, brandsData, 0)
                    BrandCategoryDetailActivity.start(
                        mContext,
                        brandsData,
                        (it.context).resources.getString(R.string.shop_by_brands),
                        position
                    )
                }
            } else {
                Glide.with(mContext)
                    .load(brandsData[position].image)
                    .placeholder(R.mipmap.ic_launcher)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.ivBrand)
                holder.tvMore.visibility = View.GONE
                holder.cvBrand.setOnClickListener {
                    // ShopByBrandCategory.start(mContext, ShopByBrandCategory.KEY_BRAND_PAGE, brandsData, position)
                    BrandCategoryDetailActivity.start(
                        mContext,
                        brandsData,
                        (it.context).resources.getString(R.string.shop_by_brands),
                        position
                    )

                }
            }
            holder.tvCategoryName.text = brandsData[position].name
        }

        holder.cvBrand.setOnClickListener {
            // ShopByBrandCategory.start(mContext, ShopByBrandCategory.KEY_BRAND_PAGE, brandsData, position)
            mContext?.let {
                SubCategoryActivity.start(
                    mContext,
                    brandsData.get(position)
                )
            }

            /*mContext?.let {
                ShopByBrandCategory.start(
                    it,
                    ShopByBrandCategory.KEY_CATEGORY_PAGE,
                    brandsData,
                    0
                )
            }*/

        }

    }

    // total number of cells
    override fun getItemCount(): Int {
        return if (brandsData == null || brandsData.size == 0) 5 else brandsData.size
        /*return if (brandsData.size > 6) {
                isMoreAvailable = true
                6
            } else {
                isMoreAvailable = false
                brandsData.size
            }*/
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val cvBrand: CardView = itemView.findViewById(R.id.cvBrand)
        var ivBrand: AppCompatImageView = itemView.findViewById(R.id.ivBrandImg)
        var tvMore: TextView = itemView.findViewById(R.id.tvMore)
        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)

    }
}