package com.riggle.data.models.response


import com.google.gson.annotations.SerializedName

data class CreditResponse(
    @SerializedName("order_value")
    val orderValue: Float?,
    @SerializedName("percentage")
    val percentage: Float?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("difference_value")
    val differenceValue: Float?
)