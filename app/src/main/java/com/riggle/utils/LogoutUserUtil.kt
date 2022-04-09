package com.riggle.utils

import android.content.Context
import com.riggle.data.pref.SharedPreferencesUtil

object LogoutUserUtil {
    fun logoutUser(activity: Context?, sharedPreferencesUtil: SharedPreferencesUtil) {
        sharedPreferencesUtil.deleteAll()
    }
}