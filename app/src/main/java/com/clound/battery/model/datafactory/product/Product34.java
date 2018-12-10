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
public class Product34 extends BaseProduct {
    public String TAG = getClass().getSimpleName();

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String code34 = "00";
        String order34 = data.substring(6, 16);//订单编号
        String hl34 = data.substring(16, 18);//回路
        String state34 = data.substring(18, 20);//状态
        String maxPower34 = data.substring(20, 24);//最大功率
        String power34 = data.substring(24, 28);//本次充电所用电量
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("34", code34 + order34 + hl34 + state34 + maxPower34 + power34));
        if (state34.equals("01")) {
            HlBean bean35 = ParseHex.getHlItemEndBattery(hl34);//结束充电
            EventBus.getDefault().post(new HlEvent(bean35));
        } else if (state34.equals("04")) {
            HlBean bean37 = ParseHex.getHlItemFaultBattery(hl34);//故障
            EventBus.getDefault().post(new HlEvent(bean37));
        }
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String order = data.substring(2, 12);
        String hL = data.substring(12, 14);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("34", order + hL));
    }
}