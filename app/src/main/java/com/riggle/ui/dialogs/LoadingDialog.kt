package com.riggle.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.riggle.R

class LoadingDialog(context: Context?) : Dialog(context!!) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}