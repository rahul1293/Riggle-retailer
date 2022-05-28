package com.riggle.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.UserData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.home.HomeActivity.Companion.start
import com.riggle.ui.introscreen.IntroActivity
import com.riggle.ui.other.registration.WelcomeScreen
import com.riggle.utils.UserProfileSingleton
import org.json.JSONObject
import org.koin.android.ext.android.inject

class SplashActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {
    private val userPreference: UserProfileSingleton by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
        /*setContentView(R.layout.activity_splash)
        proceedForLoginCheck()*/
    }

    private fun proceedForLoginCheck() {
        if (userPreference.isLogin) {
            /*if (userPreference.userData?.retailer != null && (userPreference.userData?.retailer?.account_status.equals(
                    "completed"
                ) || (userPreference.userData?.retailer?.store_type != null && !userPreference.userData?.retailer?.store_type.equals(
                    ""
                )))
            ) {
                start(this@SplashActivity, false)
            } else {
                WelcomeScreen.start(applicationContext)
            }*/
            getDetails()
        } else {
            startActivity(
                Intent(
                    applicationContext, IntroActivity::class.java
                )
            )
            finish()
        }
    }

    private fun getDetails() {
        userPreference.bearerToken.let {
            dataManager.getAuthPing(
                object :
                    ApiResponseListener<JsonElement> {
                    override fun onSuccess(response: JsonElement) {
                        response?.let {
                            val jsonObject = JSONObject(response.toString())
                            var usrData =
                                Gson().fromJson(
                                    jsonObject.get("user").toString(),
                                    UserData::class.java
                                )
                            usrData.session_key = jsonObject.get("session_id").toString()
                            if (usrData?.retailer?.account_status.equals("completed")) {
                                userPreference
                                    .updateUserData(usrData/*response.user*/)
                                start(this@SplashActivity, false)
                            } else {
                                WelcomeScreen.start(applicationContext)
                            }
                            finish()
                        }
                    }

                    override fun onError(apiError: ApiError?) {
                        Log.i("TAG", "::::" + apiError?.message)
                    }
                }
            )
        }
    }

    override fun setView(): Int {
        return R.layout.activity_splash
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        proceedForLoginCheck()
    }
}