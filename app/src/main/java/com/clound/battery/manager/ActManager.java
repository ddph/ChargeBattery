package com.clound.battery.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.util.LogUtils;

import java.util.Stack;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class ActManager {
    private static Stack<Activity> activityStack;
    private static ActManager instance = null;

    private ActManager() {
    }

    /**
     * 单一实例
     */
    public static ActManager getAppManager() {
        if (instance == null) {
            synchronized (ActManager.class) {
                if (instance == null) {
                    instance = new ActManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启应用程序
     */
    public void restart(Context context) {
        try {
            finishAllActivity();
            handler.sendEmptyMessageDelayed(200,2000);
        } catch (Exception e) {
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent LaunchIntent = BatteryApp.ApplicationContext.getPackageManager().getLaunchIntentForPackage(BatteryApp.ApplicationContext.getPackageName());
            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            BatteryApp.ApplicationContext.startActivity(LaunchIntent);
        }
    };

}