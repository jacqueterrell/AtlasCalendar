package com.team.mamba.atlascalendar.data.local_database.favoriteUsers;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteUsersEntity.class},version = 1)
public abstract class FavoriteUsersDatabase extends RoomDatabase {

    public abstract FavoriteUsersDao favoriteUsersDao();
}
