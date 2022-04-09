package com.riggle.di.modules;


import android.content.Context;

import com.riggle.data.DataManagerImpl;
import com.riggle.data.network.ApiService;
import com.riggle.di.qualifiers.ApplicationContext;
import com.riggle.di.scopes.ApplicationScope;
import com.riggle.data.DataManager;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DataMangerModule {

    @Provides
    @ApplicationScope
    DataManager getDataManger(ApiService apiInterface, Retrofit retrofit, @ApplicationContext Context context) {
        return new DataManagerImpl(apiInterface, retrofit);
    }
}
