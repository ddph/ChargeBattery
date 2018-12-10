package com.clound.battery.model.event;

import com.clound.battery.model.bean.HlBean;

/**
 * author cowards
 * created on 2018\11\22 0022
 **/
public class HlEvent {
    public HlBean hlBean;
    public HlEvent(HlBean bean){
        this.hlBean = bean;
    }
}
