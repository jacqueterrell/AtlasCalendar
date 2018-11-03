package com.team.mamba.atlascalendar.utils;

import com.team.mamba.atlascalendar.BuildConfig;

public class AppConstants {

    private AppConstants() {
        //disable new object creation
    }

    public static boolean APP_DEBUG = BuildConfig.DEBUG;
    public static final String USERS_COLLECTION = "users";
    public static final String BUSINESSES_COLLECTION = "businesses";
    public static final String CONNECTIONS_COLLECTION = "connections";
    public static final String BUS_NOTES_COLLECTION = "busNotes";
    public static final String USER_NOTES_COLLECTION = "userNotes";
    public static final String ANNOUNCEMENTS_COLLECTION = "announcements";
    public static String TEST_USER_ID = "S0URPfcKiVanC5NhHd4n9ejcEWZ2";
    public static final String BASE_PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";

    public static final int REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int REQUEST_CAMERA_PERMISSIONS = 3;
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 4;
    public static final int REQUEST_PHONE_PERMISSIONS = 5;
    public static final int REQUEST_WRITE_CONTACTS_PERMISSIONS = 6;
    public static final int REQUEST_READ_CALENDAR_PERMISSIONS = 7;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 8;

    public static final String NOTIFICATION_NEW_CONNECTION = "connRequest";
    public static final String NOTIFICATION_NEW_ANNOUNCEMENT = "announcement";
}
