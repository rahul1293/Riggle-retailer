package com.riggle.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.MemberList
import com.riggle.data.models.response.MemberListOuter
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.AddMemberDialog
import com.riggle.ui.dialogs.RemoveMemberDialog
import com.riggle.ui.dialogs.listener.AddRemoveMemberDialogListener
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_add_member.*
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.util.*

class AddMemberActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector,
    MemberListListener, AddRemoveMemberDialogListener {
    private val userPreference: UserProfileSingleton by inject()
    private var membersAdapter: MembersAdapter? = null
    private var memberLists = ArrayList<MemberList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_add_member
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.add_member)

        addOnClickListeners()
    }

    private fun addOnClickListeners() {
        fabAdd.setOnClickListener {

            this?.let {
                val dialog = AddMemberDialog(this@AddMemberActivity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setListener(this)
                dialog.show()
            }

        }
    }

    private val membersData: Unit
        private get() {
            dataManager.getMemberList(object :
                ApiResponseListener<APICommonResponse<ArrayList<MemberList>>> {
                override fun onSuccess(response: APICommonResponse<ArrayList<MemberList>>) {
                    if (response != null) {
                        tvTotalMembers?.text = "" + response.results?.size
                        response.results?.let {
                            if (it.size > 0) {
                                memberLists = it
                                populateRecyclerView()
                            }
                        }

                    }
                }

                override fun onError(apiError: ApiError?) {}
            }, userPreference.userData?.retailer?.id.toString())
        }

    private fun populateRecyclerView() {
        rvMembers?.layoutManager = LinearLayoutManager(this)
        membersAdapter = MembersAdapter(memberLists, this)
        rvMembers?.adapter = membersAdapter
        membersAdapter?.listener = this
    }

    override fun itemRemove(pos: Int) {
        removeUser(pos)
    }

    private fun removeUser(pos: Int) {
        val member = memberLists[pos]
        val dialog = RemoveMemberDialog(this, member)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setListener(this)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        membersData
    }

    override fun userAddRemoved() {
        membersData
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddMemberActivity::class.java)
            context.startActivity(intent)
        }
    }
}