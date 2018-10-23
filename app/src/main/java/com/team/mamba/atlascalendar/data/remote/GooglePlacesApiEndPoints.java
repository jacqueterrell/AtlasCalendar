package com.team.mamba.atlascalendar.data.remote;

import com.team.mamba.atlascalendar.data.model.api.googlePlaces.GoogleLocations;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GooglePlacesApiEndPoints {

    @GET("place/autocomplete/json")
    Single<GoogleLocations> requestGoogleAutoComplete(@QueryMap Map<String, String> queryMap);
}
