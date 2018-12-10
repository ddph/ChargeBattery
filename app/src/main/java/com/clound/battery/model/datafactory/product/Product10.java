package com.clound.battery.model.datafactory.product;

import android.os.Handler;

import com.clound.battery.http.XUtilsManager;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.event.MinaActEvent;
import com.clound.battery.model.manager.ConnectManager;
import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.SaveDeviceMessageInfo;
import com.clound.battery.util.download.DownLoadFileUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product10 extends BaseProduct implements XUtilsManager.HttpCallBack {
    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        //登录成功,说明当前网络可用,通知界面,切换显示二维码,恢复正常使用
        //开始发送心跳包
        ConnectManager.isFlagConentStatus = true;
        List<String> videoList = DownLoadFileUtil.getLocalFileName(Constant.PATH_QRCODE);
        if (videoList.size() > 0) {//二维码已经下载过
            LogUtils.e(TAG, "二维码已经下载过:" + videoList.get(0));
            //10秒后通知界面显示二维码
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String strings = SaveDeviceMessageInfo.readRate();//读取本地缓存的费率信息更新界面
                    if (strings.length() > 6) {
                        EventBus.getDefault().post(new MinaActEvent(205, strings));
                    }
                    EventBus.getDefault().post(new MinaActEvent(200, Constant.PATH_QRCODE + "qrcode.jpg"));
                }
            }, 10000);
        } else {
            LogUtils.e(TAG, "设备第一次登录,需要显示界面的二维码");
            XUtilsManager.getInstance().getDownLoad(200, this, Constant.PATH_QRCODE + "qrcode.jpg", BatteryCommend.qrCodeUrl);
        }
    }

    @Override
    public void onResponse(int what, String response) {
        LogUtils.e(TAG, "下载成功:" + response);
        EventBus.getDefault().post(new MinaActEvent(200, Constant.PATH_QRCODE + "qrcode.jpg"));
    }

    @Override
    public void onFailure(int what, String error) {
        LogUtils.e(TAG, "下载失败:" + error);
        XUtilsManager.getInstance().getDownLoad(200, this, Constant.PATH_QRCODE + "qrcode.jpg", BatteryCommend.qrCodeUrl);
    }
}