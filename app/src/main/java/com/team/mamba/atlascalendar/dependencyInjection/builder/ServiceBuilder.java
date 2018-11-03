package com.team.mamba.atlascalendar.dependencyInjection.builder;

import com.team.mamba.atlascalendar.service.CurrentLocationService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract CurrentLocationService bindCurrentLocationService();
}
