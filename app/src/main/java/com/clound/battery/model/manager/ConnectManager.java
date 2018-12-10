package com.clound.battery.model.manager;

import android.content.Context;

import com.clound.battery.model.event.ConnectServerResultsEvent;
import com.clound.battery.model.mina.coder.CodecFactory;
import com.clound.battery.model.mina.config.MinaConnectConfig;
import com.clound.battery.model.data.MinaData;
import com.clound.battery.model.mina.recive.ReceiveData;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * 连接的管理类
 * Created by lyf on 2017/5/20.
 */
public class ConnectManager {
    private static String TAG = "ConnectManager";
    private MinaData minaData;
    private MinaConnectConfig mConfig;//配置文件
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSessioin;
    private InetSocketAddress mAddress;
    public static volatile boolean isFlagConentStatus = false;//在线状态为true,离线状态为false

    public ConnectManager(MinaConnectConfig mConfig) {
        this.mConfig = mConfig;
        this.mContext = new WeakReference<Context>(mConfig.getmContext());
        init();
    }

    private void init() {
        minaData = new MinaData();
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤
        mConnection.getSessionConfig().setBothIdleTime(80);
        mConnection.getFilterChain().addLast("logger", new LoggingFilter());
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        //设置过滤器链
        DefaultIoFilterChainBuilder ioFilterChainBuilder = new DefaultIoFilterChainBuilder();
        ioFilterChainBuilder.addFirst("executor", new ExecutorFilter());
        ioFilterChainBuilder.addAfter("executor", "mdcInjectionFilter", new MdcInjectionFilter(MdcInjectionFilter.MdcKey.remoteAddress));
        ioFilterChainBuilder.addAfter("mdcInjectionFilter", "codecFilter", new ProtocolCodecFilter(new CodecFactory()));
        mConnection.setFilterChainBuilder(ioFilterChainBuilder);
        mConnection.getSessionConfig().setReadBufferSize(102400);
//		单一过滤器
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(new CodecFactory()));
        //设置连接监听
        mConnection.setHandler(new ReceiveData(mContext.get()));
    }

    /**
     * 与服务器连接的方法
     *
     * @return
     */
    public boolean connect() {
        try {
            if (mConnection == null) {
                return false;
            }
            ConnectFuture future = mConnection.connect();
            future.awaitUninterruptibly();//等待与服务器创建连接
            mSessioin = future.getSession();
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ConnectServerResultsEvent(400));
            return false;
        }
        return mSessioin == null ? false : true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect() {
        if (mConnection != null) {
            mConnection.dispose();
            mConnection = null;
        }
        if (minaData != null) {
            minaData.close();
        }
        if (mSessioin != null) {
            mSessioin.closeNow();
            mSessioin = null;
        }
        if (mAddress != null) {
            mAddress = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }
}