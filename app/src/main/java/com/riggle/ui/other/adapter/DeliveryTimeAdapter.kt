package com.riggle.ui.other.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import java.util.*

class DeliveryTimeAdapter  // data is passed into the constructor
(private val mContext: Context, private val timeData: ArrayList<String>) : RecyclerView.Adapter<DeliveryTimeAdapter.ViewHolder>() {

    private var listener: DeliveryTimeListener? = null

    fun setListener(listener: DeliveryTimeListener) {
        this.listener = listener
    }

    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_variant_delivery_time, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: String = timeData[position]

        holder.rbTime.text = data

        holder.rbTime.isChecked = selectedPosition == position

        holder.cvTimeSlots.setOnClickListener {
            holder.rbTime.isChecked = true
            listener?.timeSelected(position)
        }

        holder.rbTime.setOnClickListener {
            holder.rbTime.isChecked = true
            listener?.timeSelected(position)
        }

    }

    // total number of cells
    override fun getItemCount(): Int {
        return timeData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvTimeSlots: CardView = itemView.findViewById(R.id.cvTimeSlot)
        var rbTime: RadioButton = itemView.findViewById(R.id.rbTime)
    }

    interface DeliveryTimeListener {
        fun timeSelected(pos: Int)
    }

    fun selectedPosition(pos: Int) {
        this.selectedPosition = pos
    }
}