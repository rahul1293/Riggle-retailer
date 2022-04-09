package com.riggle.ui.listener

import com.riggle.data.models.response.ProductsData

interface ProductVariantListener {
    fun itemUpdated(pos: ProductsData)
}