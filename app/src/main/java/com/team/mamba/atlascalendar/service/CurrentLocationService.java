package com.team.mamba.atlascalendar.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;
import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;

public class CurrentLocationService extends Service {


    private boolean isRunning = false;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationReceiver locationReceiver = new LocationReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isRunning){

            locationReceiver.setAlarm(getApplicationContext());
            isRunning = true;
        }

        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        isRunning = false;
        locationReceiver.cancelAlarm(getApplicationContext());
        super.onDestroy();

    }

}
