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
    val differenceValue: Float?,
    @SerializedName("lenders")
    val lenders: List<Lenders>
)

data class Lenders(
    val credit_upto: Int,
    val document_list: List<String>,
    val late_fee_text: String,
    val name: String,
    val onboarding_link: String,
    val processing_fee_text: String,
    val roi_0_7_days: String,
    val roi_8_14_days: String
)