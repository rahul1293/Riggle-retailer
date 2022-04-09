package com.riggle.di.modules;

import android.content.Context;

import com.riggle.baseClass.RiggleApplication;
import com.riggle.di.qualifiers.ApplicationContext;
import com.riggle.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    final RiggleApplication riggleApplication;

    public AppModule(RiggleApplication riggleApplication) {
        this.riggleApplication = riggleApplication;
    }

    @Provides
    @ApplicationScope
    RiggleApplication provideRiggleApplication() {
        return riggleApplication;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    Context getApplicationContext(RiggleApplication riggleApplication) {
        return riggleApplication.getApplicationContext();
    }
}
