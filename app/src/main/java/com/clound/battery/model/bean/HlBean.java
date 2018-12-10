package com.clound.battery.model.bean;

/**
 * author cowards
 * created on 2018\10\18 0018
 **/
public class HlBean {

    /**
     * 回路号
     **/
    private String hl_number;

    /**
     * 回路状态
     **/
    private String hl_state;

    /**
     * 回路时间
     **/
    private String hl_time;

    /**
     * 0：线上卡 1：微信
     **/
    private int payType;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getHl_number() {
        return hl_number;
    }

    public void setHl_number(String hl_number) {
        this.hl_number = hl_number;
    }

    public String getHl_state() {
        return hl_state;
    }

    public void setHl_state(String hl_state) {
        this.hl_state = hl_state;
    }

    public String getHl_time() {
        return hl_time;
    }

    public void setHl_time(String hl_time) {
        this.hl_time = hl_time;
    }

    @Override
    public String toString() {
        return "HlBean{" +
                "hl_number='" + hl_number + '\'' +
                ", hl_state='" + hl_state + '\'' +
                ", hl_time='" + hl_time + '\'' +
                ", payType=" + payType +
                '}';
    }
}
