package com.team.mamba.atlascalendar.data.local_database.favoriteUsers;

import android.annotation.SuppressLint;

import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressLint("CheckResult")
public class FavoriteUsersDb implements FavoriteUsersDbHelper {


    private FavoriteUsersDatabase database;

    @Inject
    public FavoriteUsersDb(FavoriteUsersDatabase favoriteUsersDatabase){

        this.database = favoriteUsersDatabase;
    }


    @Override
    public LiveData<List<FavoriteUsersEntity>> watchFavoriteUsers() {
        return database.favoriteUsersDao().watchFavoriteUsers();
    }

    @Override
    public Single<List<FavoriteUsersEntity>> getFavoriteUsers() {
        return database.favoriteUsersDao().getFavoriteUsers();
    }

    @Override
    public void deleteFavoriteUser(FavoriteUsersEntity favoriteUsersEntity) {

        Completable.fromCallable(()->{
            database.favoriteUsersDao().delete(favoriteUsersEntity);
            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    Logger.i("deleted user successfully");
                },throwable -> {
                    Logger.i(throwable.getLocalizedMessage());
                });
    }

    @Override
    public void insertFavoriteUser(FavoriteUsersEntity favoriteUsersEntity) {

        Completable.fromCallable(()->{
            database.favoriteUsersDao().insertUser(favoriteUsersEntity);
            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    Logger.i("inserted user successfully");
                },throwable -> {
                    Logger.i(throwable.getLocalizedMessage());
                });
    }

    @Override
    public void deleteAllUsers() {

        Completable.fromCallable(()->{
            database.favoriteUsersDao().deleteAllUsers();
            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    Logger.i("All users deleted");
                },throwable -> {
                    Logger.i(throwable.getLocalizedMessage());
                });
    }
}
