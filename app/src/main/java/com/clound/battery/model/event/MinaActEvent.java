package com.clound.battery.model.event;

/**
 * author cowards
 * created on 2018\10\29 0029
 * 服务端数据回来要通知界面更新数据
 **/
public class MinaActEvent {

    public int code;
    public String minaData;

    public MinaActEvent(int code, String minaData) {
        this.code = code;
        this.minaData = minaData;
    }
}