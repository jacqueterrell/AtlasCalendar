package com.team.mamba.atlascalendar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.team.mamba.atlascalendar.data.AppDataManager;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class CurrentLocationService extends Service {

    @Inject
    AppDataManager dataManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AndroidInjection.inject(this);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //todo get location every five minutes

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }
}
