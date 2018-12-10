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
public class Product41 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String cardNo41 = data.substring(6, 14);
        LogUtils.e(TAG, "卡号:" + cardNo41);
        String hl41 = data.substring(14, 16);
        LogUtils.e(TAG, "回路:" + hl41);
        String time41 = data.substring(16, 20);
        LogUtils.e(TAG, "时间:" + time41);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("41", deviceid + cardNo41 + hl41 + time41));
        HlBean bean41 = ParseHex.getHlItemBattery(hl41 + time41);
        if (bean41 != null) {
            LogUtils.e(TAG, "bean41:" + bean41.toString());
            EventBus.getDefault().post(new HlEvent(bean41));
        }
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String code41 = data.substring(2, 4);//返回码
        LogUtils.e(TAG, "返回码:" + code41);
        String order41 = data.substring(26, 36);//订单号
        LogUtils.e(TAG, "订单号:" + order41);
        String hl41 = data.substring(36, 38);//回路
        LogUtils.e(TAG, "回路:" + hl41);
        String orderState = data.substring(38, 40);
        LogUtils.e(TAG, "订单状态:" + orderState);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialDataBack("41", order41 + hl41 + orderState));
    }
}