package com.riggle.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.riggle.R
import com.riggle.data.pref.SharedPreferencesUtil
import com.riggle.data.pref.SharedPreferencesUtilImpl

class ContactUsDialog(private val mContext: Context?) : AppCompatDialog(
    mContext
) {
    private var tvPhone: TextView? = null
    private var tvCancel: TextView? = null
    private var btnCall: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_contact_us)
        //RiggleApplication.getInstance().getComponent().inject(this);
        initViews()
    }

    private fun initViews() {
        tvPhone = findViewById(R.id.tvPhone)
        tvCancel = findViewById(R.id.tvCancel)
        btnCall = findViewById(R.id.btnCall)
        //tvPhone?.text = SharedPreferencesUtilImpl(context).getSupportNumber()
        tvCancel?.setOnClickListener { dismiss() }
        btnCall?.setOnClickListener {
            val intentDial =
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone?.text.toString()))
            mContext?.startActivity(intentDial)
        }
    }
}