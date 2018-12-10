package com.clound.battery.model.datafactory.product;

import com.clound.battery.http.XUtilsManager;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.download.DownLoadFileUtil;
import com.clound.battery.util.system.SystemUtil;
import com.clound.battery.util.update.SilentInstall;

import java.math.BigInteger;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product46 extends BaseProduct implements XUtilsManager.HttpCallBack {

    @Override
    public void proceSerial(String data) {
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("46", "00"));
        String integer = String.valueOf(new BigInteger(data.substring(2, 6), 16));//服务器上已发布的App版本
        int teger = SystemUtil.getVersionCode();//当前软件运行版本
        LogUtils.e(TAG, "App当前服务器上发布的版本号:" + integer + ",当前程序的版本号：" + teger);
        if (Integer.valueOf(integer) > teger) {
            DownLoadFileUtil.clearCaches(Constant.PATH_APK);//清空原有的安装包
            LogUtils.e(TAG, "有最新版本App要下载...");
            XUtilsManager.getInstance().getDownLoad(200, this, Constant.PATH_APK + "battery.apk", BatteryCommend.appUpdateUrl);
        } else {
            LogUtils.e(TAG, "未查到App最新版本");
        }
    }

    @Override
    public void onResponse(int what, String response) {
        LogUtils.e(TAG, "下载完成,准备安装:" + response);
        SilentInstall.install(response);
    }

    @Override
    public void onFailure(int what, String error) {
        LogUtils.e(TAG, "下载失败,重新下载:" + error);
        XUtilsManager.getInstance().getDownLoad(200, this, Constant.PATH_APK + "battery.apk", BatteryCommend.appUpdateUrl);
    }
}