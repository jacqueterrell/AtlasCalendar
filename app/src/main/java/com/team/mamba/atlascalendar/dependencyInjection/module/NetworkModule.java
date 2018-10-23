package com.team.mamba.atlascalendar.dependencyInjection.module;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.remote.AtlasApiEndPoints;
import com.team.mamba.atlascalendar.data.remote.GooglePlacesApiEndPoints;
import com.team.mamba.atlascalendar.dependencyInjection.qualifiers.GooglePlace;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public  class NetworkModule {

    @Provides
    @Singleton
    Gson provideGson() {

        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter(Gson gson) {

        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        return new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory callAdapterFactory() {

        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, RxJava2CallAdapterFactory callAdapterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()

                .baseUrl(BuildConfig.GOOGLE_PLACE_URL)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converter)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @GooglePlace
    Retrofit provideGooglePlacesRetrofit(Converter.Factory converter, RxJava2CallAdapterFactory callAdapterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()

                .baseUrl(BuildConfig.GOOGLE_PLACE_URL)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converter)
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    AtlasApiEndPoints provideAtlasApiEndPoints(Retrofit retrofit) {
        return retrofit.create(AtlasApiEndPoints.class);
    }

    @Provides
    @Singleton
    GooglePlacesApiEndPoints provideGooglePlacesApiEndPoints(@GooglePlace Retrofit retrofit) {
        return retrofit.create(GooglePlacesApiEndPoints.class);
    }
}
