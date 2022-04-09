package com.riggle.baseClass

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.riggle.BuildConfig
import com.riggle.data.firebase.FirebaseRemoteConfigUtil
import com.riggle.data.pref.SharedPreferencesUtilImpl
import com.riggle.di_Koin.appModule
import com.riggle.di_Koin.networkModule
import com.riggle.di_Koin.repositoriesModule
import com.riggle.di_Koin.uiModule
import com.riggle.ui.profile.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class RiggleApplication : MultiDexApplication() {
    // @JvmField
    //@Inject
    //var sharedPreferencesUtil: SharedPreferencesUtil? = null
    //var component: ApplicationComponent? = null
    //private set

    override fun onCreate() {
        super.onCreate()

        instance = this

        //SharedPreferencesUtilImpl(this)
        //FirebaseApp.initializeApp(this)

        //  initializeDagger()
        FirebaseRemoteConfigUtil.instance?.fetchConfig()

        //initializing network spectator
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidLogger()
            androidContext(this@RiggleApplication)
            modules(
                appModule,
                networkModule,
                profileModule,
                repositoriesModule,
                uiModule
            )
        }


    }

    /*private fun initializeDagger() {
        component = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .utilityModule(UtilityModule())
            .build()
        component?.inject(this)
    }*/

    companion object {
        var TAG = "RIGGLE_APP"

        @get:Synchronized
        var instance: RiggleApplication? = null
            private set
    }
}