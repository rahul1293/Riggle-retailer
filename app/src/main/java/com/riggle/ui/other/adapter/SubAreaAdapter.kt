package com.riggle.ui.other.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.data.models.response.RegionsBean

class SubAreaAdapter// data is passed into the constructor
    (private val mContext: Context, private val datesData: List<RegionsBean>) :
    RecyclerView.Adapter<SubAreaAdapter.ViewHolder>() {

    private var listener: DeliveryDateListener? = null

    fun setListener(listener: DeliveryDateListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_sub_area, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: RegionsBean = datesData[position]
        holder.tvDay.text = data.name
        holder.rlDateSlots.setOnClickListener {
            listener?.dateSelected(data)
        }
    }

    // total number of cells
    override fun getItemCount(): Int {
        return datesData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rlDateSlots: RelativeLayout = itemView.findViewById(R.id.rlMain)
        var tvDay: TextView = itemView.findViewById(R.id.tvName)
    }

    interface DeliveryDateListener {
        fun dateSelected(pos: RegionsBean)
    }

}