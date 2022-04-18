package com.riggle.utils

class Constants {
    object DataKeys {
        const val KEY_POSITION = "position"
        const val KEY_PAGE_TYPE = "page_type"
        val PICK_IMAGE_GALLERY: Int = 1001
        const val DATE_TIME_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
        const val CREDIT_PENDING = "pending"
        const val CREDIT_INITIATED = "initiated"
        const val CREDIT_ACTIVE = "active"

    }

    enum class PageTypes(private val stringValue: String) {
        UNDEFINED(""), BRAND_PAGE("brand"), CATEGORY_PAGE("category");

        override fun toString(): String {
            return stringValue
        }
    }
}