package com.team.mamba.atlascalendar.data;

import com.team.mamba.atlascalendar.data.appLevel.AppHelper;
import com.team.mamba.atlascalendar.data.remote.AtlasApiEndPoints;
import com.team.mamba.atlascalendar.data.remote.GooglePlacesApiEndPoints;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;
import javax.inject.Inject;

public class AppDataManager implements DataManager{

    private AppHelper appDataHelper;


    @Inject
    public AppDataManager(AppHelper appHelper){

        this.appDataHelper = appHelper;
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
}
