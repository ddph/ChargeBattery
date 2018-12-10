package com.clound.battery.model.event;

/**
 * author cowards
 * created on 2018\10\18 0018
 * 硬件回路状态数据SerialData到MainActivity
 **/
public class SerialHlStateEvent {
    public String code;
    public String serialData;

    public SerialHlStateEvent(String code, String serialData) {
        this.code = code;
        this.serialData = serialData;
    }

    public String getSerialData() {
        return serialData;
    }
}