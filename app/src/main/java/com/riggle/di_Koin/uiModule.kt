package com.riggle.di_Koin

import com.riggle.ui.other.search.SearchActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        SearchActivityViewModel(get())
    }
}