package com.clound.battery.model.event;

import com.clound.battery.model.bean.BalanceBean;

/**
 * author cowards
 * created on 2018\11\22 0022
 **/
public class BalanceEvent {
    public BalanceBean balanceBean;
    public BalanceEvent(BalanceBean bean){
        this.balanceBean = bean;
    }
}
