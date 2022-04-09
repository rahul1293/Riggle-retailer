package com.riggle.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.riggle.R
import com.riggle.baseClass.RiggleApplication
import com.riggle.data.models.response.MemberList
import com.riggle.utils.RiggleLogger
import com.riggle.utils.UserProfileSingleton
import java.lang.reflect.Member

class MembersAdapter(
    private val memberList: java.util.ArrayList<MemberList>,
    private val mContext: Context
) : RecyclerView.Adapter<MembersAdapter.ViewHolder>() {


    public var listener: MemberListListener? = null

    @JvmName("setListener1")
    fun setListener(listener: MemberListListener?) {
        this.listener = listener
    }


    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var mobile: TextView = view.findViewById(R.id.tvMobile) as TextView
        var role: TextView = view.findViewById(R.id.tvRole) as TextView
        var ivEllipsis: AppCompatImageView = view.findViewById(R.id.ivEllipsis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //RiggleApplication.instance?.component?.inject(this)
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_rv_members, parent, false)
        )
    }

    public override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        holder.mobile.text = memberList[position].mobile
        holder.role.text = memberList[position].user_type
        /*if (UserProfileSingleton.getInstance(mContext)?.userId != 0 && UserProfileSingleton.getInstance(mContext)?.userId != memberList[position].id)
            holder.ivEllipsis.visibility = View.VISIBLE
        else
            holder.ivEllipsis.visibility = View.GONE;*/

        holder.ivEllipsis.setOnClickListener {
            /*val popup = PopupMenu(mContext, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_member_list, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_remove -> {
                        //listener!!.itemRemove(holder.adapterPosition)
                    }
                    else -> RiggleLogger.d("ERR", "Invalid Option")
                }
                return@OnMenuItemClickListener true
            })
            popup.show()*/
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

}