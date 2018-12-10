package com.clound.battery.model.datafactory.product;

import com.clound.battery.model.bean.HlBean;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.datapackaging.SerialDataUtil;
import com.clound.battery.model.event.HlEvent;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.model.manager.SerialManager;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.ParseHex;

import org.greenrobot.eventbus.EventBus;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product35 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String orderNumber = data.substring(6, 16);
        LogUtils.e(TAG, "35...订单号:" + orderNumber);
        String Hl = data.substring(16, 18);
        LogUtils.e(TAG, "35...回路:" + Hl);
        String endState = data.substring(18, 20);
        LogUtils.e(TAG, "35...停止充电状态:" + endState);
        String maxPower = data.substring(20, 24);
        LogUtils.e(TAG, "35...充电过程最大功率:" + maxPower);//00423A35C7    014B230CE3   00423A35C7
        String nowPower = data.substring(24, 28);
        LogUtils.e(TAG, "35...本次充电所用电量:" + nowPower);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("35", orderNumber + Hl + endState + maxPower + nowPower));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String codes = data.substring(2, 4);
        LogUtils.e(TAG, "充电结束返回码：" + codes);
        String orderNum = data.substring(4, 14);
        LogUtils.e(TAG, "充电结束订单号：" + orderNum);
        String hl = data.substring(14, 16);
        LogUtils.e(TAG, "充电结束回路:" + hl);
        String hlstate = data.substring(16, 18);
        LogUtils.e(TAG, "充电结束状态:" + hlstate);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE,
                SerialDataUtil.getOnSendSerialDataBack("35", orderNum + hl + hlstate));
        HlBean hlBean = ParseHex.getHlItemEndBattery(hl);
        EventBus.getDefault().post(new HlEvent(hlBean));
    }
}