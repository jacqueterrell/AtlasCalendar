package com.team.mamba.atlascalendar.data.appLevel;

import android.content.Context;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDbHelper;
import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersEntity;
import com.team.mamba.atlascalendar.data.remote.AtlasApiEndPoints;
import com.team.mamba.atlascalendar.data.remote.GooglePlacesApiEndPoints;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public class AppHelper implements DataHelper {

    private final AppSharedPrefs sharedPrefs;
    private final Context appContext;
    private final  AtlasApiEndPoints atlasApiEndPoints;
    private final GooglePlacesApiEndPoints googlePlacesApiEndPoints;

    @Inject
    public AppHelper(Context appContext, AppSharedPrefs sharedPrefs,
                     AtlasApiEndPoints atlasApiEndPoints, GooglePlacesApiEndPoints googlePlacesApiEndPoints){

        this.appContext = appContext;
        this.sharedPrefs = sharedPrefs;
        this.atlasApiEndPoints = atlasApiEndPoints;
        this.googlePlacesApiEndPoints = googlePlacesApiEndPoints;
    }

    @Override
    public AppSharedPrefs getSharedPrefs() {
        return sharedPrefs;
    }

    @Override
    public AtlasApiEndPoints getApiEndPoints() {
        return atlasApiEndPoints;
    }

    @Override
    public GooglePlacesApiEndPoints getGooglePlacesApiEndPoint() {
        return googlePlacesApiEndPoints;
    }

}
