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
public class Product40 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String code40 = "00";
        String order40 = data.substring(6, 16);
        String hl40 = data.substring(16, 18);
        String state40 = data.substring(18, 20);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("40", code40 + order40 + hl40 + state40));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String order40 = data.substring(2, 12);
        LogUtils.e(TAG, "订单号:" + order40);
        String hl40 = data.substring(12, 14);
        LogUtils.e(TAG, "回路:" + hl40);
        String time40 = data.substring(14, 18);
        LogUtils.e(TAG, "充电时间:" + time40);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("40", order40 + hl40 + time40));
        HlBean bean = ParseHex.getHlItemBattery(hl40 + time40);
        if (bean != null) {
            LogUtils.e(TAG, "bean:" + bean.toString());
            EventBus.getDefault().post(new HlEvent(bean));
        }
    }
}