package com.team.mamba.atlascalendar.data.model.local;

public class ContinentLocation {

    double latitude;
    double longitude;
    String continent;


    public ContinentLocation(String continent,double latitude,double longitude){

        this.continent = continent;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getContinent() {
        return continent;
    }
}
