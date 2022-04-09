package com.riggle.utils

import android.util.Log
import com.riggle.BuildConfig

object RiggleLogger {
    private val LOG_ENABLE = BuildConfig.DEBUG

    @JvmStatic
    fun e(tag: String, message: String) {
        if (LOG_ENABLE) {
            Log.e("Riggle_LOGGER=: ", "$tag: $message")
        }
    }


    @JvmStatic
    fun e(tag: String, message: String, throwable: Throwable?) {
        if (LOG_ENABLE) {
            Log.e("Riggle_LOGGER=: ", "$tag: $message", throwable)
        }
    }


    @JvmStatic
    fun d(tag: String, message: String) {
        if (LOG_ENABLE) {
            Log.d("Riggle_LOGGER=: ", "$tag: $message")
        }
    }


    @JvmStatic
    fun i(tag: String, message: String) {
        if (LOG_ENABLE) {
            Log.i("Riggle_LOGGER=: ", "$tag: $message")
        }
    }


    @JvmStatic
    fun v(tag: String, message: String) {
        if (LOG_ENABLE) {
            Log.v("Riggle_LOGGER=: ", "$tag: $message")
        }
    }


    @JvmStatic
    fun w(tag: String, message: String) {
        if (LOG_ENABLE) {
            Log.w("Riggle_LOGGER=: ", "$tag: $message")
        }
    }


    @JvmStatic
    fun eLong(tag: String, message: String) {
        if (LOG_ENABLE) {
            val maxLogSize = 2000
            for (i in 0..message.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > message.length) message.length else end
                Log.e("Riggle_LOGGER=: ", tag + ": " + message.substring(start, end))
            }
        }
    }
}