package com.team.mamba.atlascalendar.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.AppSharedPrefs;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LocationReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        getCurrentLocation(context);
    }

    public void setAlarm(Context context){

        Calendar calendar = Calendar.getInstance();
        long interval = 1000 * 60 * 5; // 5 minutes in milliseconds

        Intent intent = new Intent(context,LocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                interval,
                pendingIntent);
    }


    public void cancelAlarm(Context context){

        Intent intent = new Intent(context,LocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,1,intent,0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }



    @SuppressLint("MissingPermission")
    private void getCurrentLocation(Context context){

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        try{

            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful() && task.getResult() != null){

                            Location location = task.getResult();
                            postCurrentLocation(context,location.getLatitude(),location.getLongitude());

                        } else {

                            Logger.w("Failed to get location " + task.getException().getLocalizedMessage());
                        }
                    });

        } catch (Exception e){

            Logger.e(e.getMessage());
        }
    }

    @SuppressLint("CheckResult")
    private void postCurrentLocation(Context context,double latitude,double longitude){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Map<String,String> locationMap = new HashMap<>();
        locationMap.put("latitude",String.valueOf(latitude));
        locationMap.put("longitude",String.valueOf(longitude));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = preferences.getString(AppSharedPrefs.USER_ID,"");

        if (TextUtils.isEmpty(userId)){

            return;
        }

        Observable.fromCallable(()->{

            //add the announcement to the businesses announcement list
            db.collection(AppConstants.USERS_COLLECTION)
                    .document(userId)
                    .update("lastLocation",locationMap)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Logger.i("successfully posted location");
                           // Toast.makeText(context, "Location updated"     , Toast.LENGTH_LONG).show();
//                            Vibrator v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//                            v.vibrate(500);

                        } else {

                            if (task.getException() != null) {

                                Logger.e(task.getException().getMessage());
                            }
                        }
                    });

            return false;

        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }
}

