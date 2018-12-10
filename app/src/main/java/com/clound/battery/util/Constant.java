package com.clound.battery.util;

import android.os.Environment;
import java.io.File;

/**
 * Created by Administrator on 2018/10/18.
 */
public class Constant {

    /**
     * 项目缓存信息根路径
     * */
    public static String PATH_PROJECT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/";

    /**
     * h5文件存放地址
     */
    public static String PATH_HTML = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/h5/";

    /**
     * 广告视频存放地址
     */
    public static String PATH_VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/h5/video/";

    /**
     * 下载app存放地址
     */
    public static String PATH_APK = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/apk/";

    /**
     * 下载二维码存放地址
     */
    public static String PATH_QRCODE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/qrcode/";

    /**
     * 广告图片下载保存地址
     */
    public static String PATH_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/h5/images/";

    /**
     * 设备ID保存地址
     */
    public static String PATH_SAVE_DEVICE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/deviceId/";

    /**
     * 崩溃日志缓存路径
     * */
    public static String PATH_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/cache/";

    /**
     * 正常日志缓存目录
     * */
    public static String PATH_LOG = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/log/";

    /**
     * 收费标准存放地址
     * */
    public static String PATH_RATE = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "ChargeBattery/rate/";

}