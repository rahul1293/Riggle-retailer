package com.riggle.ui.base.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riggle.data.DataManager
import com.riggle.data.pref.SharedPreferencesUtil
import com.riggle.ui.base.interfaces.CustomAppActivity
import org.koin.android.ext.android.inject
import javax.inject.Inject

abstract class CustomAppActivityImpl : AppCompatActivity() {
    /*@JvmField
    @Inject
    var sharedPreferencesUtil: SharedPreferencesUtil? = null*/
    /*
    @JvmField
    @Inject
    var dataManager: DataManager? = null*/
    var activity: Activity? = null
    var context: Context? = null

    val sharedPreferencesUtil : SharedPreferencesUtil by inject()
    val dataManager : DataManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        context = this
        //RiggleApplication.getInstance().getComponent().inject(this);
        initCustomAppActivityImpl()
    }

    override fun onResume() {
        super.onResume()
    }

    fun initCustomAppActivityImpl() {
        activity = this
        context = this
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}