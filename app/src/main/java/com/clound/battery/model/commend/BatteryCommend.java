package com.clound.battery.model.commend;

import com.clound.battery.util.SaveDeviceMessageInfo;

/**
 * author cowards
 * created on 2018\11\20 0020
 **/
public class BatteryCommend {

    public final static int MAIN_CODE = 1;
    public final static int MINADATA_CODE = 2;
    public final static int SETTINGACT_CODE = 3;

    public static String http = "http://47.92.150.230/";

    /**
     * 获取广告信息
     **/
    public static String httlUrl = "http://39.105.56.18:8081/Android/adInfo/getAndroidAdInfo?code=" + "4A000000011";//SaveDeviceId.readDeviceId()

    /**
     * 获取二维码
     **/
    public static String qrCodeUrl = http + "Android/" + SaveDeviceMessageInfo.readDeviceId() + ".jpg";

    /**
     * App版本更新
     */
    public static String appUpdateUrl = http + "AndroidApk/battery.apk";

}