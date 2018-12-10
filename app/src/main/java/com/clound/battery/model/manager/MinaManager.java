package com.clound.battery.model.manager;

import android.os.SystemClock;

import com.clound.battery.manager.ThreadPoolManager;
import com.clound.battery.util.LogUtils;

import org.apache.mina.core.session.IoSession;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * session管理类,通过ioSession与服务器通信
 * Created by lyf on 2018/10/21.
 */
public class MinaManager {

    private String TAG = getClass().getSimpleName();
    private static volatile MinaManager mInstance = null;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();          //发送的数据放入队列中,防止并发
    private boolean isMinaConsumer = false;

    /**
     * 最终与服务器 通信的对象
     **/
    private IoSession ioSession;

    public static MinaManager getmInstance() {
        if (mInstance == null) {
            synchronized (MinaManager.class) {
                if (mInstance == null) {
                    mInstance = new MinaManager();
                }
            }
        }
        return mInstance;
    }

    private MinaManager() {
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
        }
        isMinaConsumer = true;
        ThreadPoolManager.getSingleInstance().execute(new MinaConsumer());
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    /**
     * 将对象发送到服务器
     */
    public void writeToServer(String msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            LogUtils.e(TAG, "数据添加过多：" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void closeSession() {
        if (ioSession != null) {
            ioSession.closeOnFlush();
            ioSession = null;
        }
        if (queue != null) {
            queue.clear();
        }
    }

    public void release(){
        closeSession();
        isMinaConsumer = false;
        if(mInstance == null){
            mInstance = null;
        }
    }

    /**
     * 队列中取出数据发送到服务端
     */
    class MinaConsumer implements Runnable {
        @Override
        public void run() {
            while (isMinaConsumer) {
                if (!queue.isEmpty()) {
                    try {
                        sendSession(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SystemClock.sleep(300);
            }
        }

        private void sendSession(String msg) {
            if ((ioSession != null) && (ioSession.isConnected())) {
                ioSession.write(msg);
                LogUtils.e(TAG, "App---服务器:" + msg);
            } else {
                LogUtils.e(TAG, "发送失败,App---服务器:" + msg);
            }
        }
    }
}