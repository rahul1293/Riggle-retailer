package com.riggle.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.data.models.response.RetailerDetails
import com.riggle.data.models.response.UserData
import com.riggle.data.models.response.UserDetails

class SharedPreferencesUtilImpl(private val context: Context) : SharedPreferencesUtil {
    private val contextPref: SharedPreferences?
        private get() {
            if (pref == null) {
                pref = context.getSharedPreferences("CONTEXT", Context.MODE_PRIVATE)
            }
            return pref
        }
    private val contextPrefEditor: SharedPreferences.Editor?
        private get() {
            if (prefEditor == null) {
                prefEditor = contextPref?.edit()
            }
            return prefEditor
        }

    init {
        if (pref == null) {
            pref = context.getSharedPreferences("CONTEXT", Context.MODE_PRIVATE)
        }
    }

    override fun deleteAll() {
        contextPrefEditor?.clear()
        contextPrefEditor?.remove(SharedPreferencesBean.KEY_USER_DATA)
        contextPrefEditor?.apply()

        /*getPersistentPref().edit()
                .clear()
                .apply();

        getUrlPrefEditor().clear();
        getUrlPrefEditor().apply();*/try {
            for (index in SharedPreferencesBean.Array_KEY_SHARED_PREFERENCES.indices) {
                val obj_sp = SharedPreferencesCustom(
                    context,
                    SharedPreferencesBean.Array_KEY_SHARED_PREFERENCES[index]
                )
                obj_sp.clearSharedPreferences()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun saveUserData(data: UserData): Boolean {
        val objLogin = Gson().toJson(data)
        val objSPLogin = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_USER_DATA)
        objSPLogin.putString(SharedPreferencesBean.KEY_USER_DATA, objLogin)
        return true
    }

    override fun getUserData(): UserData? {
        val obj_sp_login = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_USER_DATA)
        val loginJson = obj_sp_login.getString(SharedPreferencesBean.KEY_USER_DATA)
        try {
            val objUser = Gson().fromJson(loginJson, UserData::class.java)
            if (objUser != null) {
                return objUser
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    override fun saveRetailerDetails(data: UserDetails?): Boolean {
        val objLogin = Gson().toJson(data)
        //val objSPLogin = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_USER_DATA)
        contextPrefEditor?.putString("retailer_details", objLogin)
        contextPrefEditor?.apply()
        return true
    }

    override fun getRetailerDetails(): UserDetails? {
        pref?.getString("retailer_details","")
        try {
            val objUser = Gson().fromJson(pref?.getString("retailer_details",""), UserDetails::class.java)
            if (objUser != null) {
                return objUser
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun isOrderPlaced(): Boolean {
        return contextPref?.getBoolean("order_placed", false)?:false
    }

    override fun saveRecentSearchesList(list: ArrayList<String>) {
        val myType = object : TypeToken<List<String>>() {}.type
        val value = Gson().toJson(list, myType)

        contextPrefEditor?.putString("recent_searches", value)
        contextPrefEditor?.apply()
    }

    override fun getRecentSearchesList(): ArrayList<String> {
        var list = contextPref?.getString("recent_searches", "")
        if (!list.isNullOrEmpty()) {
            val myType = object : TypeToken<List<String>>() {}.type
            val value = Gson().fromJson<ArrayList<String>>(list, myType)

            return value
        } else {
            return ArrayList<String>()
        }


    }


    override fun setOrderPlaced(value: Boolean) {
        contextPrefEditor?.putBoolean("order_placed", value)
        contextPrefEditor?.apply()
    }

    override fun saveSupportNumber(value: String?) {
        contextPrefEditor?.putString("support_number", value)
        contextPrefEditor?.apply()
    }  
    
    override fun getSupportNumber(): String? {
       return  pref?.getString("support_number","")
    }

    companion object {
        private var pref: SharedPreferences? = null
        private var prefEditor: SharedPreferences.Editor? = null
    }
}