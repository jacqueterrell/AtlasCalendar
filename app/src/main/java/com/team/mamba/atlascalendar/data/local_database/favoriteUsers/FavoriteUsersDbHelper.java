package com.team.mamba.atlascalendar.data.local_database.favoriteUsers;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public interface FavoriteUsersDbHelper {


    LiveData<List<FavoriteUsersEntity>> watchFavoriteUsers();

    Single<List<FavoriteUsersEntity>> getFavoriteUsers();

    void deleteFavoriteUser(FavoriteUsersEntity favoriteUsersEntity);

    void insertFavoriteUser(FavoriteUsersEntity favoriteUsersEntity);

    void deleteAllUsers();
}
