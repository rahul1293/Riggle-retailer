package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class Earnings(
    @SerializedName("balance")
    val available_coins: Int,
    @SerializedName("statement")
    val earnings: List<CoinsEarning>
)