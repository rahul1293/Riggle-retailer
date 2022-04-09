package com.riggle.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.ProductsData
import com.riggle.data.models.response.UserData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.home.HomeActivity
import com.riggle.ui.home.HomeActivity.Companion.start
import com.riggle.ui.introscreen.IntroActivity
import com.riggle.ui.other.registration.WelcomeScreen
import com.riggle.utils.UserProfileSingleton
import org.koin.android.ext.android.inject
import java.util.ArrayList

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
            if (userPreference.userData?.retailer != null && userPreference.userData?.retailer?.account_status.equals("completed")) {
                start(this@SplashActivity, false)
            } else {
                WelcomeScreen.Companion.start(applicationContext)
            }
            //HomeActivity.start(applicationContext, false)
            //getDetails()
        } else {
            startActivity(
                Intent(
                    applicationContext, IntroActivity::class.java
                )
            )
        }
        finish()
    }

    private fun getDetails() {
//        userPreference.bearerToken?.let {
//            dataManager.getPingDetails(
//                object :
//                    ApiResponseListener<APICommonResponse<UserData>> {
//                    override fun onSuccess(response: APICommonResponse<UserData>) {
//                        if (response.user != null) {
//                            response.user?.let {
//                                var usrData = it
//                                response.session_id?.let {
//                                    usrData.session_key = it
//                                }
//                                userPreference
//                                    .updateUserData(usrData)
//                                if (usrData.retailer != null && usrData.retailer.account_status.equals("completed")) {
//                                    start(this@SplashActivity, false)
//                                } else {
//                                    WelcomeScreen.Companion.start(applicationContext)
//                                }
//                                finish()
//                            }
//                        }
//                    }
//
//                    override fun onError(apiError: ApiError?) {
//                        Log.i("TAG", "::::" + apiError?.message)
//                    }
//                },
//                0
//            )
//        }
    }

    override fun setView(): Int {
        return R.layout.activity_splash
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        proceedForLoginCheck()
    }
}