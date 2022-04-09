package com.riggle.di.modules;

import android.content.Context;

import com.riggle.data.pref.SharedPreferencesUtil;
import com.riggle.data.pref.SharedPreferencesUtilImpl;
import com.riggle.di.qualifiers.ApplicationContext;
import com.riggle.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilityModule {

    @Provides
    @ApplicationScope
    SharedPreferencesUtil provideSharedPreference(@ApplicationContext Context context) {
        return new SharedPreferencesUtilImpl(context);
    }
}
