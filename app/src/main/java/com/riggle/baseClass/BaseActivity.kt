package com.riggle.baseClass

import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.riggle.data.DataManager
import com.riggle.data.pref.SharedPreferencesUtil
import com.riggle.ui.dialogs.LoadingDialog
import org.koin.android.ext.android.inject

open class BaseActivity : AppCompatActivity() {

     var loaderDialog: LoadingDialog? = null
    val dataManager : DataManager by inject()
    val sharedPreferencesUtil : SharedPreferencesUtil by inject()

    fun onBackImageClicked(view: View){
        onBackPressed()
    }

     fun showHideLoader(state: Boolean) {
        if (loaderDialog != null) {
            if (state) loaderDialog?.show() else loaderDialog?.dismiss()
        } else {
            this?.let {
                loaderDialog = LoadingDialog(this)
                showHideLoader(state)
            }

        }
    }



}