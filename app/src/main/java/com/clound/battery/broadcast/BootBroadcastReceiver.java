package com.clound.battery.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clound.battery.view.activity.MainActivity;

/**
 * Created by Administrator on 2018/1/9.
 * 实现开机自动启动
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Thread.sleep(2000L);
            Intent service = new Intent(context, MainActivity.class);
            service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(service);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}