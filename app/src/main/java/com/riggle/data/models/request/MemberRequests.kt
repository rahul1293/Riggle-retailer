package com.riggle.data.models.request

data class AddMember(val mobile: String, val role: String?)

data class AddMembers(
    val first_name: String,
    val last_name: String,
    val mobile: String,
    val retailer: Int,
    val role: String?
)