package com.riggle.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatImageView
import com.riggle.R
import com.riggle.data.firebase.FirebaseConfig
import com.riggle.data.firebase.FirebaseRemoteConfigUtil
import com.riggle.data.models.response.MemberList
import com.riggle.ui.dialogs.listener.AddRemoveMemberDialogListener

class RemoveMemberDialog(private val mContext: Context, private val member: MemberList) :
    AppCompatDialog(
        mContext
    ) {
    /*@Inject
    DataManager dataManager;*/
    private var firebaseConfig: FirebaseConfig? = null
    private var tvCancel: TextView? = null
    private var tvRemove: TextView? = null
    private var tvMobile: TextView? = null
    private var ivCross: AppCompatImageView? = null
    private var listener: AddRemoveMemberDialogListener? = null
    fun setListener(listener: AddRemoveMemberDialogListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_remove_members)
        // RiggleApplication.getInstance().getComponent().inject(this);
        initViews()
    }

    private fun initViews() {
        if (firebaseConfig == null) {
            firebaseConfig = FirebaseRemoteConfigUtil.instance?.fireBaseConfigValues
        }
        tvCancel = findViewById(R.id.tvCancel)
        tvMobile = findViewById(R.id.tvMobile)
        tvRemove = findViewById(R.id.tvRemove)
        ivCross = findViewById(R.id.ivCross)
        tvMobile!!.text = "+91-" + member.mobile
        tvCancel!!.setOnClickListener { dismiss() }
        tvRemove!!.setOnClickListener { removeMember() }
        ivCross!!.setOnClickListener { dismiss() }
    }

    private fun removeMember() {
        /*  dataManager.removeMember(new ApiResponseListener<APICommonResponse<String>>() {
            @Override
            public void onSuccess(APICommonResponse<String> response) {
                Toast.makeText(mContext, "User removed", Toast.LENGTH_SHORT).show();
                if (listener != null)
                    listener.userAddRemoved();
                dismiss();
            }

            @Override
            public void onError(ApiError apiError) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        }, member.getId());*/
    }
}