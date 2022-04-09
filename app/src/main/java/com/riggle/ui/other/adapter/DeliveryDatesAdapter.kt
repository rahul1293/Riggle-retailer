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
import com.riggle.R
import com.riggle.data.models.response.DeliveryDateSlots
import java.util.*

class DeliveryDatesAdapter  // data is passed into the constructor
(private val mContext: Context, private val datesData: ArrayList<DeliveryDateSlots>) : RecyclerView.Adapter<DeliveryDatesAdapter.ViewHolder>() {

    private var listener: DeliveryDateListener? = null

    fun setListener(listener: DeliveryDateListener) {
        this.listener = listener
    }

    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_variant_delivery_dates, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: DeliveryDateSlots = datesData[position]

        holder.tvDay.text = data.title
        holder.tvDate.text = data.sub_title

        if (selectedPosition == position) {
            holder.ivUpArrow.visibility = View.VISIBLE
            holder.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            holder.rlDateSlots.background = ContextCompat.getDrawable(mContext, R.drawable.delivery_date_border_selected)
        } else {
            holder.ivUpArrow.visibility = View.INVISIBLE
            holder.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.black_85))
            holder.rlDateSlots.background = ContextCompat.getDrawable(mContext, R.drawable.delivery_date_border)

        }

        holder.rlDateSlots.setOnClickListener {
            listener?.dateSelected(position)
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return datesData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rlDateSlots: RelativeLayout = itemView.findViewById(R.id.rlDateSlots)
        var ivUpArrow: AppCompatImageView = itemView.findViewById(R.id.ivUpArrow);
        var tvDay: TextView = itemView.findViewById(R.id.tvDay)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    interface DeliveryDateListener {
        fun dateSelected(pos: Int)
    }

    fun selectedPosition(pos: Int) {
        this.selectedPosition = pos
    }
}