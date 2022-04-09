package com.riggle.di_Koin

import com.google.firebase.FirebaseApp
import com.riggle.data.DataManager
import com.riggle.data.DataManagerImpl
import com.riggle.data.firebase.FirebaseRemoteConfigUtil
import com.riggle.data.pref.SharedPreferencesUtil
import com.riggle.data.pref.SharedPreferencesUtilImpl
import com.riggle.utils.UserProfileSingleton
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
 //  viewModel { EditProfileViewModel() }
    single { SharedPreferencesUtilImpl(get()) as SharedPreferencesUtil }
    single { DataManagerImpl(get(), get()) as DataManager }
    single { UserProfileSingleton(get())  }

    single {
           FirebaseRemoteConfigUtil.instance?.fireBaseConfigValues
    }

    single{
        SharedPreferencesUtilImpl(androidContext())
    }

    single {
        FirebaseApp.initializeApp(androidContext())
    }
}