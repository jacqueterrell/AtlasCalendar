package com.team.mamba.atlascalendar.data;

import com.team.mamba.atlascalendar.data.appLevel.AppHelper;
import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDbHelper;
import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersEntity;
import com.team.mamba.atlascalendar.data.remote.AtlasApiEndPoints;
import com.team.mamba.atlascalendar.data.remote.GooglePlacesApiEndPoints;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public class AppDataManager implements DataManager{

    private AppHelper appDataHelper;
    private FavoriteUsersDbHelper favoriteUsersDbHelper;


    @Inject
    public AppDataManager(AppHelper appHelper,FavoriteUsersDbHelper favoriteUsersDbHelper){

        this.appDataHelper = appHelper;
        this.favoriteUsersDbHelper = favoriteUsersDbHelper;
    }

    @Override
    public AppSharedPrefs getSharedPrefs() {
        return appDataHelper.getSharedPrefs();
    }

    @Override
    public AtlasApiEndPoints getApiEndPoints() {
        return appDataHelper.getApiEndPoints();
    }

    @Override
    public GooglePlacesApiEndPoints getGooglePlacesApiEndPoint() {
        return appDataHelper.getGooglePlacesApiEndPoint();
    }


    @Override
    public LiveData<List<FavoriteUsersEntity>> watchFavoriteUsers() {
        return favoriteUsersDbHelper.watchFavoriteUsers();
    }

    @Override
    public Single<List<FavoriteUsersEntity>> getFavoriteUsers() {
        return favoriteUsersDbHelper.getFavoriteUsers();
    }

    @Override
    public void deleteFavoriteUser(FavoriteUsersEntity favoriteUsersEntity) {

        favoriteUsersDbHelper.deleteFavoriteUser(favoriteUsersEntity);
    }

    @Override
    public void insertFavoriteUser(FavoriteUsersEntity favoriteUsersEntity) {

        favoriteUsersDbHelper.insertFavoriteUser(favoriteUsersEntity);
    }

    @Override
    public void deleteAllUsers() {

        favoriteUsersDbHelper.deleteAllUsers();
    }
}
