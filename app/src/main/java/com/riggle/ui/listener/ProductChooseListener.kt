package com.riggle.ui.listener

import com.riggle.data.models.response.SchemesBean

interface ProductChooseListener {
    fun itemUpdated(scheme: SchemesBean, pos: Int)

    fun onRefresh() {

    }
}