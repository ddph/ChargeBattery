package com.clound.battery.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.clound.battery.util.LogUtils;
import com.clound.battery.util.system.SystemUtil;

/**
 * Created by Administrator on 2018/9/30.
 */
public class UpdateReceiver extends BroadcastReceiver {
    private static final String TAG = UpdateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {//接收升级广播
            LogUtils.e(TAG, "onReceive:升级了一个安装包，重新启动此程序");
            if (packageName.equals("package:" + SystemUtil.getPackageInfo().packageName)) {
                SystemUtil.reBootDevice();
            }
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {//接收安装广播
            LogUtils.e(TAG, "onReceive:安装了" + packageName);
            if (packageName.equals("package:" + SystemUtil.getPackageInfo().packageName)) {
                //SystemUtil.reBootDevice();
            }
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) { //接收卸载广播
            LogUtils.e(TAG, "onReceive:卸载了" + packageName);
        }
    }
}