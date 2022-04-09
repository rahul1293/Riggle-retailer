package com.riggle.services.eventbus

import com.riggle.data.models.response.ProductsData

/**
 * Created by anshulpatro on 09/04/18.
 */
class EventBusEvents {
    class LogoutUser(val isLogout: Boolean)
    class UpdateProdOnHome(val productsData: ProductsData)
    class OnOrderConfirmed(val isConfirmed: Boolean)
}