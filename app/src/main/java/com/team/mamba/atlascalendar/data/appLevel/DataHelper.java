package com.team.mamba.atlascalendar.data.appLevel;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersDbHelper;
import com.team.mamba.atlascalendar.data.remote.AtlasApiEndPoints;
import com.team.mamba.atlascalendar.data.remote.GooglePlacesApiEndPoints;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;

public interface DataHelper{

    AppSharedPrefs getSharedPrefs();
    AtlasApiEndPoints getApiEndPoints();
    GooglePlacesApiEndPoints getGooglePlacesApiEndPoint();
}
