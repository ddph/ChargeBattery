package com.clound.battery.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/8/7.
 */
public class SharedPreferencesUtil {

    public static String User = "battery";
    public static SharedPreferencesUtil mInstance = null;
    public static SharedPreferences mSharedPreferences;
    private static Context mContext;

    public static SharedPreferencesUtil getInstance(Context context) {
        mContext = context;
        if (null == mInstance) {
            mInstance = new SharedPreferencesUtil();
            mSharedPreferences = context.getSharedPreferences(User, Context.MODE_PRIVATE);
        }
        return mInstance;
    }

    /**
     * 写入字符串型数据
     */
    public static void setString(String key, String value) {
        synchronized (mContext) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 获取字符串型数据
     */
    public static String getString(String key) {
        synchronized (mContext) {
            if(key.equals("SerialDeviceName")){
                return mSharedPreferences.getString(key,"/dev/ttyS2");//串口默认是ttyS2
            }
            return mSharedPreferences.getString(key, "");
        }
    }

    /**
     * 写入整型数据
     */
    public static void setInteger(String key, int value) {
        synchronized (mContext) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    /**
     * 获取整型数据
     */
    public static Integer getInteger(String key) {
        synchronized (mContext) {
            return mSharedPreferences.getInt(key, 120 * 6000);
        }
    }

    /**
     * 写入布尔型数据
     */
    public static void setBoolean(String key, boolean value) {
        synchronized (mContext) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    /**
     * 获取布尔型数据
     */
    public static boolean getBoolean(String key) {
        synchronized (mContext) {
            return mSharedPreferences.getBoolean(key, false);
        }
    }

    /**
     * 写入长整型数据
     */
    public static void setLong(String key, long value) {
        synchronized (mContext) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    /**
     * 获取长整型数据
     */
    public static long getLong(String key) {
        synchronized (mContext) {
            return mSharedPreferences.getLong(key, 0);
        }
    }
}