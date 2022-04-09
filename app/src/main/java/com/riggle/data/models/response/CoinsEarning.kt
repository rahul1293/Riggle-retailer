package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class CoinsEarning(
    val cart_id: Int,
    val created_by: Any,
    val id: Int,
    val is_deleted: Int,
    val modified: String,
    val on_event: String,
    val riggle_coins_bal: Int,
    val status: String,
    val user_id: Int,

    // updated response
    @SerializedName("riggle_coins")
    val coins: Int,
    @SerializedName("delivered_at")
    val created: String,
    val redeemed_riggle_coins: Int
)