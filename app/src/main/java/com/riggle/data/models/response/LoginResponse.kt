package com.riggle.data.models.response

data class LoginResponse(
    val contact_support: String, // not coming in latest api, coming in old api response
    val already_exists: Boolean
)