package com.riggle.ui.profile

import com.riggle.ui.credit.CreditViewModel
import com.riggle.ui.profile.earnings.MyEarningsViewModel
import com.riggle.ui.profile.editprofile.DocumentsItemListViewModel
import com.riggle.ui.profile.editprofile.EditProfileViewModel
import com.riggle.ui.profile.editprofile.ProfileRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel {  EditProfileViewModel( get()) }
    viewModel {  MyEarningsViewModel( get()) }
    viewModel {  CreditViewModel( get()) }
}