package com.clound.battery.model.mina.send;

import android.util.Log;

import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.manager.ConnectManager;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.service.MinaService;
import com.clound.battery.util.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author cowards
 * created on 2018\10\17 0017
 * 心跳包
 **/
public class SendHeartBeatData {

    private String TAG = "SendHeartBeatData";
    private Timer timer = null;
    private TimerTask timerTask = null;
    private int PERIOD = 20 * 1000;

    public SendHeartBeatData() {
        if ((timer != null) && (timerTask != null)) {
            return;
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (ConnectManager.isFlagConentStatus) {//当前连接成功
                    MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("15", MinaDataUtil.minaData4G()));
                } else {
                    LogUtils.e(TAG, "当前设备未连接");
                }
            }
        };
        timer.schedule(timerTask, 0, PERIOD);
    }

    public void disConnect() {
        Log.e(TAG, "reconnetion: colse");
        ConnectManager.isFlagConentStatus = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}