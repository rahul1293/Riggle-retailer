package com.riggle.data.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.BuildConfig
import com.riggle.R
import com.riggle.utils.RiggleLogger.e
import java.util.*

class FirebaseRemoteConfigUtil private constructor() {
    val fireBaseConfigValues = FirebaseConfig()
    fun fetchConfig() {
        val mFirebaseRemoteConfig: FirebaseRemoteConfig
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(cacheExpiration)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.firebase_remote_config_defaults)
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                e("FIREBASE_REMOTE_CONFIG", "CONFIG_FETCHED")
                setFirebaseConfigData(mFirebaseRemoteConfig, false)
            } else {
                e("FIREBASE_REMOTE_CONFIG", "ERROR - KILLLLLL MMMMEEEE ")
            }
        }
        setFirebaseConfigData(mFirebaseRemoteConfig, true)
    }

    private fun setFirebaseConfigData(
        mFirebaseRemoteConfig: FirebaseRemoteConfig,
        isDefault: Boolean
    ) {
        parseStoreType(mFirebaseRemoteConfig.getString(KEY_STORE_TYPE))
        parseRoleType(mFirebaseRemoteConfig.getString(KEY_ROLE_TYPE))
    }

    private fun parseStoreType(string: String) {
        try {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<StoreType?>?>() {}.type
            fireBaseConfigValues.store_type = gson.fromJson(string, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseRoleType(string: String) {
        try {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<RoleType?>?>() {}.type
            fireBaseConfigValues.role_type = gson.fromJson(string, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // If is developer mode, cache expiration set to 0, in order to test
    private val cacheExpiration: Long
        private get() {
            var cacheExpiration: Long = 10000
            // If is developer mode, cache expiration set to 0, in order to test
            if (BuildConfig.DEBUG) {
                cacheExpiration = 0
            }
            return cacheExpiration
        }

    companion object {
        private const val KEY_STORE_TYPE = "store_type"
        private const val KEY_ROLE_TYPE = "role_type"
        var instance: FirebaseRemoteConfigUtil? = null
            get() {
                if (field == null) {
                    field = FirebaseRemoteConfigUtil()
                }
                return field
            }
            private set
    }
}