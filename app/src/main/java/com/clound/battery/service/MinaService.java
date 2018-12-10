package com.clound.battery.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.model.commend.IpCommend;
import com.clound.battery.model.event.ConnectServerResultsEvent;
import com.clound.battery.model.event.MinaActEvent;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.model.mina.config.MinaConnectConfig;
import com.clound.battery.model.manager.ConnectManager;
import com.clound.battery.model.mina.send.SendHeartBeatData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class MinaService extends Service {
    private static ConnectThred connectThred;
    private static SendHeartBeatData sendHeartBeatData = null;
    private String TAG = getClass().getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sendHeartBeatData = new SendHeartBeatData();
        connect();
    }

    //连接失败重连
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConnect(ConnectServerResultsEvent event) {
        switch (event.code) {
            case 200://连接成功
                break;
            case 400://连接失败
                EventBus.getDefault().post(new MinaActEvent(400, "连接失败"));
                connect();
                break;
        }
    }

    public static void connect() {
        if (connectThred != null) {
            connectThred.disConnection();
        }
        //使用子线程开启连接
        connectThred = new ConnectThred("mina", BatteryApp.ApplicationContext);
        connectThred.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy...");
        if (sendHeartBeatData != null) {
            sendHeartBeatData.disConnect();
        }
        MinaManager.getmInstance().release();
        connectThred.disConnection();
        connectThred = null;
        BatteryApp.minaService = null;
    }

    /**
     * 掉线重连
     */
    public static void reConnect() {
        EventBus.getDefault().post(new MinaActEvent(400, "连接失败"));
        if (connectThred != null) {
            connectThred.disConnection();
            connectThred = null;
        }
        connect();
    }

    /**
     * 负责调用connectmanager类来完成与服务器的连接
     */
    static class ConnectThred extends HandlerThread {
        boolean isConnection;
        ConnectManager mManager;

        public ConnectThred(String name, Context context) {
            super(name);
            MinaConnectConfig config = new MinaConnectConfig.Builder(context)
                    .setIp(IpCommend.getIp())
                    .setPort(IpCommend.getPort())
                    .setReadBufferSize(10240)           //数据接收最大字节
                    .setConnectionTimeout(5 * 1000)     //连接超时时长
                    .bulid();
            //创建连接的管理类
            mManager = new ConnectManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            //利用循环请求连接
            while (true) {
                if (mManager == null) {
                    MinaConnectConfig config = new MinaConnectConfig.Builder(BatteryApp.ApplicationContext)
                            .setIp("39.105.56.18")             //IP    39.105.56.18   https://api.3ccg.com
                            .setPort(18306)                     //Port  18306         18306
                            .setReadBufferSize(10240)           //数据接收最大字节
                            .setConnectionTimeout(10000)        //超时时长
                            .bulid();
                    //创建连接的管理类
                    mManager = new ConnectManager(config);
                }

                if (!ConnectManager.isFlagConentStatus) {//true连接成功  false连接失败
                    isConnection = mManager.connect();
                } else {
                    isConnection = true;
                }
                if (isConnection) {
                    //当请求成功的时候,跳出循环
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 断开连接
         */
        public void disConnection() {
            if (mManager != null) {
                mManager.disConnect();
            }
        }
    }
}