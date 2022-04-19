package com.riggle.data.network

import com.riggle.BuildConfig

class APIUrlConstants {
    companion object {
        const val LOGIN_PHONE: String = BuildConfig.BASE_URL + "user/auth/send_otp/"

        const val RE_SEND_OTP: String = BuildConfig.BASE_URL + "user/auth/resend_otp/"

        const val VERIFY_OTP: String = BuildConfig.BASE_URL + "user/auth/verify_otp/"

        const val EDIT_PROFILE: String = BuildConfig.BASE_URL + "/users/editProfile"

        //const val GET_MEMBER_LIST: String = BuildConfig.BASE_URL + "/users/memberList"
        const val GET_MEMBER_LIST: String = BuildConfig.BASE_URL + "user/users"

        const val REMOVE_MEMBER: String = BuildConfig.BASE_URL + "/users/removeMember"

        const val ADD_MEMBER: String = BuildConfig.BASE_URL + "/users/addMember"

        //const val GET_PRODUCTS: String = BuildConfig.BASE_URL + "/product/getProducts"
        const val GET_PRODUCTS: String = BuildConfig.BASE_URL + "core/products/"

        const val GET_BRANDS: String = BuildConfig.BASE_URL + "/product/getBrands"

        const val GET_TOTAL_RIGGLE_COINS: String = BuildConfig.BASE_URL + "users/getRiggleCoins"

        const val GET_CATEGORIES: String = BuildConfig.BASE_URL + "/product/getCategories"

        const val GET_VARIANTS: String = BuildConfig.BASE_URL + "/product/getVariants"

        const val ADD_CARTS: String = BuildConfig.BASE_URL + "/cart/addCart"

        const val FETCH_CART: String = BuildConfig.BASE_URL + "user/retailers/{id}/cart/"
        const val creditLineStatus: String = BuildConfig.BASE_URL + "user/retailers/{id}/credit_line_status/"

        const val EDIT_CART: String = BuildConfig.BASE_URL + "/cart/editCart"

        const val DELIVERY_SLOTS: String = BuildConfig.BASE_URL + "/product/getTimeSlots"

        const val ADD_ORDER: String = BuildConfig.BASE_URL + "/cart/addOrder"

        const val PRODUCT_DETAIL: String = BuildConfig.BASE_URL + "core/products/{id}/"

        const val MY_ORDERS:String = BuildConfig.BASE_URL + "core/orders"

        const val ORDER_DETAIL:String = BuildConfig.BASE_URL + "/cart/orderDetail"

        const val SEARCH:String = BuildConfig.BASE_URL + "/product/search"

        const val userDetails:String = BuildConfig.BASE_URL + "/users/user_details"

        const val myEarnings:String = BuildConfig.BASE_URL + "user/retailers/{id}/riggle_coin_statement"/*"/cart/myEarnings"*/
        const val topSearches:String = BuildConfig.BASE_URL + "/product/topSearches"

        const val uploadFile:String = BuildConfig.BASE_URL + "/users/uploadDocs"

        const val getRegion:String = BuildConfig.BASE_URL + "core/regions"
        const val updateRetails:String = BuildConfig.BASE_URL + "user/retailers/{id}/"
        const val getCategoryList:String = BuildConfig.BASE_URL + "core/categories"
        const val pingDetails:String = BuildConfig.BASE_URL + "user/retailers/{id}/"
        //const val pingDetails:String = BuildConfig.BASE_URL + "user/auth/ping"
        const val addToCart:String = BuildConfig.BASE_URL + "user/retailers/{id}/cart/"

        const val placeOrder:String = BuildConfig.BASE_URL + "core/orders/"
        const val addUser:String = BuildConfig.BASE_URL + "user/users/"
        const val coreConstants:String = BuildConfig.BASE_URL + "user/constants/"

    }
}