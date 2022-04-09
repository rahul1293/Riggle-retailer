package com.riggle.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class APICommonResponse<T> {
    /*@SerializedName("success")
    @Expose
    var isSuccess = false

    @SerializedName("msg")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }*/
    @SerializedName("success")
    @Expose
    var isSuccess = false

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }

    @SerializedName("session_id")
    @Expose
    var session_id: String? = null

    @SerializedName("user")
    @Expose
    var user: T? = null
        private set

    fun setUser(data: T) {
        this.user = data
    }

    @SerializedName("results")
    @Expose
    var results: T? = null
        private set

    fun setResults(data: T) {
        this.results = data
    }

    @SerializedName("product")
    @Expose
    var product: T? = null
        private set

    fun setProduct(data: T) {
        this.product = data
    }

    @SerializedName("quantity")
    var quantity: Int = 0

}