package com.riggle.data.network

import com.riggle.data.models.ApiError

interface ApiResponseListener<T> {
    fun onSuccess(response: T)
    fun onError(apiError: ApiError??)
}