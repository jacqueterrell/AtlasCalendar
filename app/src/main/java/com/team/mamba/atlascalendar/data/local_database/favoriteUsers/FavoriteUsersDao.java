package com.team.mamba.atlascalendar.data.local_database.favoriteUsers;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface FavoriteUsersDao {

    @Query("SELECT * FROM FavoriteUsers")
    LiveData<List<FavoriteUsersEntity>> watchFavoriteUsers();

    @Query("SELECT * FROM FavoriteUsers")
    Single<List<FavoriteUsersEntity>> getFavoriteUsers();

    @Delete
    void delete(FavoriteUsersEntity favoriteUsersEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(FavoriteUsersEntity favoriteUsersEntity);

    @Query("DELETE FROM FavoriteUsers")
    void deleteAllUsers();
}
