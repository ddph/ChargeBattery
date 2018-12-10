package com.clound.battery.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.clound.battery.manager.WIFIManager;
import com.clound.battery.service.SerialService;
import com.clound.battery.service.MinaService;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.SharedPreferencesUtil;
import com.clound.battery.util.download.DownLoadFileUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class BatteryApp extends Application {
    private String TAG = getClass().getSimpleName();
    public static Context ApplicationContext;
    public static MinaService minaService;
    public static SerialService serialService;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContext = getContext();
        x.Ext.init(this);
        x.Ext.setDebug(false);//开启会影响性能
        SharedPreferencesUtil.getInstance(ApplicationContext);
        WIFIManager.init(ApplicationContext);
        LogUtils.init();
        DownLoadFileUtil.ismKards();
        CrashReport.initCrashReport(getApplicationContext(), "8bbd0ba763", true);//开发阶段设置true,上架设为false
        minaService = new MinaService();
        serialService = new SerialService();
        startService(new Intent(ApplicationContext, minaService.getClass()));
        startService(new Intent(ApplicationContext, serialService.getClass()));
    }

    /**
     * 获取上下文
     */
    public Context getContext() {
        return this.getApplicationContext();
    }

}