package com.clound.battery.util.system;

import android.content.Context;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * author cowards
 * created on 2018\11\20 0020
 *
 * 修改系统时间:系统需root
 **/
public class SystemTime {
    private static String TAG = "SystemTime";

    public static void setSystemTime(final Context cxt, String datetimes) {
        /**
         * 可用busybox 修改时间
         * 时间格式:        yyyyMMdd.HHmmss
         */
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            //          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            String datetime = ""; // 测试的设置的时间【时间格式
            datetime = datetimes.toString(); // yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}