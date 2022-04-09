package com.riggle.data.models

import com.google.gson.annotations.SerializedName

class ApiError {
    @SerializedName("statusCode")
    var statusCode = 0

    @SerializedName("success")
    var isSuccess = false

    @SerializedName("msg")
     var message: String? = ""

    @SerializedName("body")
    var body: String? = null

    constructor() {}

    /**
     * Constructor
     *
     * @param statusCode status code of api error response
     * @param message    message of api error response
     */
    constructor(statusCode: Int, message: String?, success: Boolean, body: String?) {
        this.message = message
        this.body = body
        this.statusCode = statusCode
        isSuccess = success
    }


}