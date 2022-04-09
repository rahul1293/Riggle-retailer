package com.riggle.ui.base.connector

import android.os.Bundle

/**
 * Created by Anshul Patro
 */
interface CustomAppViewConnector {
    fun setView(): Int
    fun initializeViews(savedInstanceState: Bundle?)
}