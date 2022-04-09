package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class RiggleCoinsResponse(
    @SerializedName("riggle_coins_balance")
    var riggleCointBalance : Int
    )