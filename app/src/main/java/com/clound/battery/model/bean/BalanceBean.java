package com.clound.battery.model.bean;

/**
 * author cowards
 * created on 2018\11\22 0022
 **/
public class BalanceBean {

    /**
     * 卡号
     * */
    private String cardNo;

    /**
     * 余额
     * */
    private String balance;

    /**
     * 充电卡类型
     * */
    private String cardNoType;

    /**
     * 是否可充电
     * */
    private String charger;

    /**
     * 充电时间
     * */
    private String batteryTime;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCardNoType() {
        return cardNoType;
    }

    public void setCardNoType(String cardNoType) {
        this.cardNoType = cardNoType;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    public String getBatteryTime() {
        return batteryTime;
    }

    public void setBatteryTime(String batteryTime) {
        this.batteryTime = batteryTime;
    }

    @Override
    public String toString() {
        return "BalanceBean{" +
                "cardNo='" + cardNo + '\'' +
                ", balance='" + balance + '\'' +
                ", cardNoType='" + cardNoType + '\'' +
                ", charger='" + charger + '\'' +
                ", batteryTime='" + batteryTime + '\'' +
                '}';
    }
}
