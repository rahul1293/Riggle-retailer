package com.riggle.ui.other.registration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.riggle.R
import com.riggle.data.firebase.RoleType
import com.riggle.data.firebase.StoreType

class StoreTypeAdapter(context: Context?, typeList: List<StoreType>) : ArrayAdapter<StoreType?>(
    context!!, 0, typeList!!
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) convertView =
            LayoutInflater.from(context).inflate(R.layout.spinner_store_type, parent, false)
        val tvName = convertView?.findViewById<TextView>(R.id.tvStoreType)
        val currentItem = getItem(position)
        if (currentItem != null) {
            tvName?.text = currentItem.store_type
            if (position == 0) tvName?.setTextColor(context.resources.getColor(R.color.black_18)) else tvName?.setTextColor(
                context.resources.getColor(R.color.black_85)
            )
        }
        return convertView!!
    }
}

