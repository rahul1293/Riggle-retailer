package com.riggle.data.models.response

data class LockedKeys(
    val address: Boolean,
    val city: Boolean,
    val g_latitude: Boolean,
    val g_longitude: Boolean,
    val gstn_no: Boolean,
    val mobile: Boolean,
    val pincode: Boolean,
    val state: Boolean,
    val store_name: Boolean,
    val user_type: Boolean
)