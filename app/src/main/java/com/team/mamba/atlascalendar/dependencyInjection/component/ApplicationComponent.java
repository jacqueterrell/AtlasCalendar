package com.team.mamba.atlascalendar.dependencyInjection.component;


import android.app.Application;
import com.team.mamba.atlascalendar.dependencyInjection.module.AppModule;
import com.team.mamba.atlascalendar.dependencyInjection.module.DatabaseModule;
import com.team.mamba.atlascalendar.userInterface.AtlasApplication;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

        @Singleton
        @Component(modules = {AppModule.class,DatabaseModule.class})

        public interface ApplicationComponent {

        void inject(AtlasApplication app);

        @Component.Builder
        interface Builder{

        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
        }
        }
