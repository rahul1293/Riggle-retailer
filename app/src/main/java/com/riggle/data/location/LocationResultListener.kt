package com.riggle.data.location

import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}