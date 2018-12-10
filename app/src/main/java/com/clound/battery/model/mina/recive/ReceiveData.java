package com.clound.battery.model.mina.recive;

import android.content.Context;
import android.os.SystemClock;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.model.event.MinaDataEvent;
import com.clound.battery.model.event.MessageModel;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.manager.ConnectManager;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.service.MinaService;
import com.clound.battery.util.LogUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;

/**
 * author cowards
 * created on 2018\10\22 0022
 **/
public class ReceiveData extends IoHandlerAdapter {

    private Context context;

    private String TAG = getClass().getSimpleName();

    public ReceiveData(Context context) {
        this.context = context;
    }

    /**
     * 创建会话成功时被调用
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LogUtils.d(TAG, "与服务器会话创建成功！");
    }

    /**
     * 连接成功时回调的方法
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //当与服务器连接成功时,将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
        LogUtils.e(TAG, "连接成功");
        MinaManager.getmInstance().setIoSession(session);
        /**
         * 开始登陆
         * */
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("10", MinaDataUtil.minaDataDeviceId()));
    }

    /**
     * 接收到消息时回调的方法
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        MessageModel model = (MessageModel) message;
        EventBus.getDefault().post(new MinaDataEvent(model.getCommandKey() + model.getCommandValue()));
    }

    /**
     * 消息发送完成时回调
     *
     * @param message
     * @param session
     * @throws Exception
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        LogUtils.d(TAG, "发送数据到服务端:" + message.toString());
    }

    /**
     * 连接关闭时回调
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        LogUtils.e(TAG, "连接关闭...");
        if (session != null) {
            ConnectManager.isFlagConentStatus = false;
            session.closeOnFlush();
            MinaManager.getmInstance().closeSession();

            /** 10秒后重新连接 **/
            SystemClock.sleep(10 * 1000);
            if (BatteryApp.minaService != null) {
                MinaService.reConnect();
            }
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        LogUtils.e(TAG, "空闲时调用...");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LogUtils.e(TAG, "异常捕捉：" + cause.toString());
    }
}