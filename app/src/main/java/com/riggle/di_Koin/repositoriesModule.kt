package com.riggle.di_Koin

import com.riggle.ui.profile.editprofile.ProfileRepository
import org.koin.dsl.module

var  repositoriesModule  = module{
    factory {  ProfileRepository(get()) }
}