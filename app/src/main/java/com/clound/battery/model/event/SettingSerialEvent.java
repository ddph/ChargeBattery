package com.clound.battery.model.event;

/**
 * author cowards
 * created on 2018\10\23 0023
 * 硬件数据SerialData回调到SettingActivity
 **/
public class SettingSerialEvent {
    public String code;
    public String msg;

    public SettingSerialEvent(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}