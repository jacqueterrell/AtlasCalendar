package com.team.mamba.atlascalendar.dependencyInjection.module;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.team.mamba.atlascalendar.dependencyInjection.builder.ActivityBuilder;
import com.team.mamba.atlascalendar.dependencyInjection.builder.FragmentBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Module(includes = {AndroidInjectionModule.class,AndroidSupportInjectionModule.class,
        DatabaseModule.class, NetworkModule.class, ActivityBuilder.class, FragmentBuilder.class,
        } )
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }


    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application){
        return PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }
}
