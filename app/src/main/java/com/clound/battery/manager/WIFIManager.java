package com.clound.battery.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.clound.battery.util.LogUtils;

import static android.content.Context.WIFI_SERVICE;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class WIFIManager {

    public static String TAG = "WIFIManager";
    private static Context context;
    public static TelephonyManager telephonyManager = null;
    public static void init(Context count){
        context = count;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 读取数据网络信号强度
     */
    public static void getDataNetworkRssi() {
        final int type = telephonyManager.getNetworkType();//获取网络类型联通3g,移动3g,电信3g...
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override   //信号状态改变时回调
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                String strength = String.valueOf(signalStrength
                        .getGsmSignalStrength());//信号强度
                LogUtils.e(TAG, "当前网络类型:" + type + ",信号强度:" + strength);
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);//监听网络信号事件
    }

    /**
     * 获取当前wifi信号强度(-50~0:信号较好,-70~-50:信号较差,-100~-50:信号最差)
     */
    public static int getWifiRssi() {
        int strength = 0;
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getRssi() <= 0) {
            strength = info.getRssi();
        }
        return strength;
    }
}