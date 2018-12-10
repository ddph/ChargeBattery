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
public class Product30 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String hltimecode = "00";
        String hltime = data.substring(6, 12);
        HlBean hlBean = ParseHex.getHlItemBattery(hltime);
        if (hlBean != null) {
            LogUtils.e(TAG, "hlBean:" + hlBean.toString());
            EventBus.getDefault().post(new HlEvent(hlBean));
        }
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("30", hltimecode + hltime));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String hl = data.substring(2, 4);
        LogUtils.e(TAG, "回路:" + hl);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("30", hl));
    }
}