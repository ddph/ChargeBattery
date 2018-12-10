package com.clound.battery.util.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.clound.battery.application.BatteryApp;

/**
 * created by Administrator on 2018/9/21 16:05
 */
public class PhoneInfoInstance {

    private TelephonyManager telephonyManager = null;
    //移动运营商编号
    private String NetworkOperator;
    private static volatile PhoneInfoInstance instance = null;

    public PhoneInfoInstance(){
        telephonyManager = (TelephonyManager) BatteryApp.ApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static PhoneInfoInstance getIntance(){
        if(instance == null){
            synchronized (PhoneInfoInstance.class){
                if(instance == null){
                    instance = new PhoneInfoInstance();
                }
            }
        }
        return instance;
    }

    //获取sim卡iccid
    @SuppressLint("MissingPermission")
    public String getIccid() {
        String iccid = "N/A";
        iccid = telephonyManager.getSimSerialNumber();
        return iccid;
    }

    //获取电话号码
    @SuppressLint("MissingPermission")
    public String getNativePhoneNumber() {
        String nativePhoneNumber = "N/A";
        nativePhoneNumber = telephonyManager.getLine1Number();
        return nativePhoneNumber;
    }

    //获取手机服务商信息
    public String getProvidersName() {
        String providersName = "N/A";
        NetworkOperator = telephonyManager.getNetworkOperator();
        //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
//        Flog.d(TAG,"NetworkOperator=" + NetworkOperator);
        if (NetworkOperator.equals("46000") || NetworkOperator.equals("46002")) {
            providersName = "中国移动";//中国移动
        } else if (NetworkOperator.equals("46001")) {
            providersName = "中国联通";//中国联通
        } else if (NetworkOperator.equals("46003")) {
            providersName = "中国电信";//中国电信
        }
        return providersName;

    }

    @SuppressLint("MissingPermission")
    public String getPhoneInfo() {
        StringBuffer sb = new StringBuffer();

        sb.append("\nLine1Number = " + telephonyManager.getLine1Number());
        sb.append("\nNetworkOperator = " + telephonyManager.getNetworkOperator());//移动运营商编号
        sb.append("\nNetworkOperatorName = " + telephonyManager.getNetworkOperatorName());//移动运营商名称
        sb.append("\nSimCountryIso = " + telephonyManager.getSimCountryIso());
        sb.append("\nSimOperator = " + telephonyManager.getSimOperator());
        sb.append("\nSimOperatorName = " + telephonyManager.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + telephonyManager.getSimSerialNumber());
        sb.append("\nSubscriberId(IMSI) = " + telephonyManager.getSubscriberId());
        return sb.toString();
    }
}