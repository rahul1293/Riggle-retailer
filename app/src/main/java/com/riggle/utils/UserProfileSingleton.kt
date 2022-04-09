package com.riggle.utils

import com.riggle.data.models.response.UserData
import com.riggle.data.models.response.UserDetails
import com.riggle.data.pref.SharedPreferencesUtil

class UserProfileSingleton(var sharedPreferencesUtil: SharedPreferencesUtil) {
   
   // var sharedPreferencesUtil: SharedPreferencesUtil? = null
    
     var userData: UserData?
    fun updateUserData(userData: UserData?) {
        this.userData = userData
        sharedPreferencesUtil!!.saveUserData(userData)
    }

    fun  saveRetailerDetails(retailer : UserDetails){
        sharedPreferencesUtil!!.saveRetailerDetails(retailer)
    }

    val isLogin: Boolean
        get() = userData != null && userData!!.session_key != null && !userData!!.session_key.isEmpty()
    val isAddressAvailable: Boolean
        get() = userData != null && userData!!.address != null && !userData!!.address.isEmpty()
    val bearerToken: String
        get() {
            var token = ""
            if (userData != null && userData!!.session_key != null && !userData!!.session_key.isEmpty()) {
                token = userData!!.session_key.trim { it <= ' ' }
            }
            return token
        }

    fun getProfileData(profile_properties: PROFILE_PROPERTIES?): String {
        var value = ""
        if (userData != null) when (profile_properties) {
            /*PROFILE_PROPERTIES.STORE_NAME -> if (userData!!.store_name != null && !userData!!.store_name.isEmpty()) value =
                userData!!.store_name*/
            PROFILE_PROPERTIES.STORE_NAME -> if (userData!!.retailer != null && !userData!!.retailer?.store_type.toString()?.isEmpty()) value =
                userData!!.retailer?.store_type.toString()
            PROFILE_PROPERTIES.USER_NAME -> if (userData!!.retailer?.name != null && !userData!!.retailer?.name?.isEmpty()!!) value =
                userData!!.retailer?.name.toString()
            PROFILE_PROPERTIES.MOBILE -> if (userData!!.mobile != null && !userData!!.mobile.isEmpty()) value =
                userData!!.mobile
            PROFILE_PROPERTIES.PINCODE -> if (userData!!.retailer?.pincode != null && !userData!!.retailer?.pincode?.isEmpty()!!) value =
                userData!!.retailer?.pincode.toString()
            PROFILE_PROPERTIES.ADDRESS -> if (userData!!.retailer?.address != null && !userData!!.retailer?.address?.isEmpty()!!) value =
                userData!!.retailer?.address.toString()
            PROFILE_PROPERTIES.CITY -> if (userData!!.city != null && !userData!!.city.isEmpty()) value =
                userData!!.city
            PROFILE_PROPERTIES.STATE -> if (userData!!.state != null && !userData!!.state.isEmpty()) value =
                userData!!.state
            else -> value = ""
        }
        return value
    }

    val userId: Int
        get() = if (userData != null) userData!!.id else 0

    enum class PROFILE_PROPERTIES {
        STORE_NAME, USER_NAME, MOBILE, PINCODE, ADDRESS, CITY, STATE
    }

    /*companion object {
        private var instance: UserProfileSingleton? = null
        @JvmStatic
        fun getInstance(context: Context): UserProfileSingleton? {
            if (instance == null) instance = UserProfileSingleton(context)
            return instance
        }
    }*/

    init {
        //RiggleApplication.getInstance().getComponent().inject(this);
        userData = sharedPreferencesUtil.userData
    }
}