package com.team.mamba.atlascalendar.utils;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class AppSharedPrefs {


    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String USER_LOGGED_IN = "userLoggedIn";
    private static final String USER_CODE = "userCode";
    private static final String BUSINESS_ACCOUNT = "isIsOrgBus";
    private static final String BUSINESS_REP_ID = "businessRepId";
    public static final String FIREBASE_DEVICE_TOKEN = "fireBaseDeviceToken";


    private SharedPreferences sharedPreferences;

    @Inject
    public AppSharedPrefs(SharedPreferences preferences) {
        sharedPreferences = preferences;
    }

    public AppSharedPrefs() {

    }

    public void setUserId(String id){

        sharedPreferences
                .edit()
                .putString(USER_ID,id)
                .apply();
    }

    public String getUserId(){

        return sharedPreferences
                .getString(USER_ID,"");
    }

    public void setFirstName(String firstName){

        sharedPreferences.edit()
                .putString(FIRST_NAME,firstName)
                .apply();
    }

    public String getFirstName(){

        return sharedPreferences
                .getString(FIRST_NAME,"");
    }

    public void setLastName(String lastName){

        sharedPreferences
                .edit()
                .putString(LAST_NAME,lastName)
                .apply();
    }

    public String getLastName(){

        return sharedPreferences
                .getString(LAST_NAME,"");
    }

    public void setPhoneNumber(String phoneNumber){

        sharedPreferences.edit()
                .putString(PHONE_NUMBER,phoneNumber)
                .apply();
    }

    public String getPhoneNumber(){

        return sharedPreferences
                .getString(PHONE_NUMBER,"");
    }

    public boolean isUserLoggedIn(){

        return sharedPreferences
                .getBoolean(USER_LOGGED_IN,false);
    }

    public void setUserLoggedIn(boolean userLoggedIn){

        sharedPreferences
                .edit()
                .putBoolean(USER_LOGGED_IN,userLoggedIn)
                .apply();
    }

    public String getUserCode(){

        return sharedPreferences
                .getString(USER_CODE,"");
    }

    public void setUserCode(String userCode){

        sharedPreferences
                .edit()
                .putString(USER_CODE,userCode)
                .apply();
    }

    public boolean isBusinessAccount(){

        return sharedPreferences
                .getBoolean(BUSINESS_ACCOUNT,false);
    }

    public void setBusinessAccount(boolean businessAccount){

        sharedPreferences
                .edit()
                .putBoolean(BUSINESS_ACCOUNT,businessAccount)
                .apply();
    }


    public String getFirebaseDeviceToken(){

        return sharedPreferences
                .getString(FIREBASE_DEVICE_TOKEN,"");
    }

    public void setFirebaseDeviceToken(String token){

        sharedPreferences
                .edit()
                .putString(FIREBASE_DEVICE_TOKEN,token)
                .apply();
    }
}
