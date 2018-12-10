package com.clound.battery.model.event;

/**
 * author cowards
 * created on 2018\10\23 0023
 * 收到的串口数据SerialData发送到SocketData
 **/
public class MinaSerialEvent {
    public String msg;
    public String code;

    public MinaSerialEvent(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}