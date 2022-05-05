package com.riggle.data.models.response


import com.google.gson.annotations.SerializedName

data class CouponBean(
    @SerializedName("brand")
    val brand: Int?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discount_amount")
    val discountAmount: Double?,
    @SerializedName("expiry")
    val expiry: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("min_amount")
    val minAmount: Double?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)