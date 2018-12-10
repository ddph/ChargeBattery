package com.clound.battery.model.mina.config;

import android.content.Context;

/**
 * 连接的配置文件
 * Created by lyf on 2017/5/20.
 */
public class MinaConnectConfig {

    private Context mContext;//上下文
    private String ip;//IP
    private int port;//端口
    private int readBufferSize; //缓存大小
    private long connectionTimeout;//连接超时时间

    public Context getmContext() {
        return mContext;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public static class Builder {
        private Context mContext;
        private String ip = "";
        private int port = 18306;
        private int readBufferSize = 10240; //缓存大小
        private long connectionTimeout = 10000;//连接超时时间

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setmContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public MinaConnectConfig bulid() {
            MinaConnectConfig minaConnectConfig = new MinaConnectConfig();
            minaConnectConfig.connectionTimeout = this.connectionTimeout;
            minaConnectConfig.ip = this.ip;
            minaConnectConfig.port = this.port;
            minaConnectConfig.mContext = this.mContext;
            minaConnectConfig.readBufferSize = this.readBufferSize;
            return minaConnectConfig;
        }
    }
}