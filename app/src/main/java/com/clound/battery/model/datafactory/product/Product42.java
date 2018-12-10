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
public class Product42 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String maxWen = data.substring(6, 7);
        String DeviceWen = data.substring(7, 8);
        String smoke = data.substring(8, 9);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("42", maxWen + DeviceWen + smoke));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialDataBack("42", ""));
    }
}