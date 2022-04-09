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
import com.riggle.ui.subcategory.ProductListActivity
import java.util.*

class HomeCategoryAdapter  // data is passed into the constructor
internal constructor(
    private val mContext: Context,
    private val categoriesData: ArrayList<BrandsCategoryData>
) : RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.list_home_categories, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext)
            .load(categoriesData[position].image)
            .placeholder(R.mipmap.ic_launcher)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(holder.ivCategory)

        holder.tvCategoryName.text = categoriesData[position].name
        holder.tvCategoryItems.text = categoriesData[position].name
        holder.cvCategory.setOnClickListener {
            //ShopByBrandCategory.start(mContext, ShopByBrandCategory.KEY_CATEGORY_PAGE, categoriesData, position)
            /*BrandCategoryDetailActivity.start(
                mContext,
                categoriesData,
                (it.context).resources.getString(R.string.shop_by_category),
                position
            )*/
            ProductListActivity.start(
                mContext,
                categoriesData.get(position),
                categoriesData.get(position).name,
                position
            )
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return categoriesData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val cvCategory: CardView = itemView.findViewById(R.id.cvCategory)
        var ivCategory: AppCompatImageView = itemView.findViewById(R.id.ivCategoryImg)
        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        var tvCategoryItems: TextView = itemView.findViewById(R.id.tvCategoryItems)

    }
}