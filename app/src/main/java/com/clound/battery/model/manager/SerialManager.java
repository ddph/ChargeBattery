package com.clound.battery.model.manager;

import android.os.SystemClock;
import com.clound.battery.manager.ThreadPoolManager;
import com.clound.battery.model.bean.SerialBean;
import com.clound.battery.model.data.SerialData;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.SharedPreferencesUtil;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android_serialport_api.SerialPortUtil;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class SerialManager {
    private static String TAG = "SerialManager";
    public static String serialDeviceName = SharedPreferencesUtil.getString("SerialDeviceName");//   "/dev/ttyS2"
    public static int baud = 57600;
    public static volatile boolean isSerialFlag = false;
    private static volatile SerialManager instance = null;
    private static BlockingQueue<SerialBean> queue = new LinkedBlockingQueue<>();           //发送的数据放入队列中,防止并发
    private static boolean isSerialConsumer = false;

    private SerialManager() {
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
        }
        LogUtils.e(TAG, "当前打开的串口是:" + serialDeviceName);
        boolean isSerial = SerialPortUtil.open(serialDeviceName, baud, 8);
        if (isSerial) {
            isSerialFlag = true;
            new SerialData();
            isSerialConsumer = true;
            ThreadPoolManager.getSingleInstance().execute(new SerialConsumer());
            LogUtils.e(TAG, "串口打开成功");
        } else {
            isSerialFlag = false;
            LogUtils.e(TAG, "串口打开失败");
        }
    }

    /**
     * 单一实例
     */
    public static SerialManager getInstance() {
        if (instance == null) {
            synchronized (SerialManager.class) {
                if (instance == null) {
                    instance = new SerialManager();
                }
            }
        }
        return instance;
    }

    /**
     * 发送数据到串口
     */
    public static void sendSerialData(int code, String data) {
        try {
            queue.put(new SerialBean(code, data));
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "数据添加过多：" + e.toString());
        }
    }

    /**
     * 队列中取出数据发送到串口
     */
    class SerialConsumer implements Runnable {
        @Override
        public void run() {
            while (isSerialConsumer) {
                if (!queue.isEmpty()) {
                    try {
                        SerialBean bean = queue.take();
                        sendSerialData(bean.code, bean.value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SystemClock.sleep(300);
            }
        }

        private void sendSerialData(int code, String data) {
            SerialPortUtil.sendString(code, data);
        }
    }

    /**
     * 关闭串口
     */
    public static void close() {
        isSerialFlag = false;
        if (queue != null) {
            queue.clear();
            queue = null;
        }
        SerialPortUtil.close();
    }

    /**
     * 释放单例
     */
    public static void release() {
        isSerialConsumer = false;
        if (instance != null) {
            close();
            instance = null;
        }
    }
}