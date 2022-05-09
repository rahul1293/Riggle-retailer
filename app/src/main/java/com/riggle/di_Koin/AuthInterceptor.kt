package com.riggle.di_Koin

import android.util.Log
import com.riggle.utils.UserProfileSingleton
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(var userData: UserProfileSingleton) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder: Request.Builder = original.newBuilder()
        if (original.url.host.contains("api.riggleapp.in")) {
            builder.header("Content-Type", "application/json")
                .method(original.method, original.body);
            if (userData.isLogin) {
                builder.removeHeader("Authorization")
                //val loginToken = userData.bearerToken
                val loginToken = userData.userData?.session_key
                Log.i("AuthInterceptor", "TOKEN :: >> $loginToken")
                //builder.addHeader("Authorization", "Bearer " + loginToken)
                builder.addHeader("Authorization", loginToken.toString())
            } else {
                builder.addHeader("Authorization", "Basic OTU5OTk3NjY5MjphaW5hYTAwNw==")
            }
            builder.addHeader("Connection", "close")
        }
        //var req = builder.build();
        return chain.proceed(builder.build())
    }
}