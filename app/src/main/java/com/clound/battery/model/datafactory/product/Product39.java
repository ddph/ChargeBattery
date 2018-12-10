package com.clound.battery.model.datafactory.product;

import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.datapackaging.SerialDataUtil;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.model.manager.SerialManager;
import com.clound.battery.util.LogUtils;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product39 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String code = "00";
        String maxpoer = data.substring(6, 10);
        String ziting = data.substring(10, 12);
        String yanshi = data.substring(12, 14);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("39", code + maxpoer + ziting + yanshi));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("39", ""));
    }
}