package com.clound.battery.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.model.manager.SerialManager;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class SerialService extends Service {
    private String TAG = getClass().getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SerialManager.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy...");
        SerialManager.release();
        BatteryApp.serialService = null;
    }
}