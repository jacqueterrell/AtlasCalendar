package com.team.mamba.atlascalendar.dependencyInjection.module;


import android.content.Context;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDatabase;
import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDb;
import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDbHelper;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {


    /*****Set Up FavoriteUsers Database***/

    @Provides
    @Singleton
    FavoriteUsersDatabase provideFavoriteUsersDatabase(Context context){

        return Room.databaseBuilder(context,FavoriteUsersDatabase.class,"FavoriteUsers.db")
                .build();
    }

    @Provides
    FavoriteUsersDbHelper providesFavoriteUsersDbHelper(FavoriteUsersDb favoriteUsersDb){

        return favoriteUsersDb;
    }
}
