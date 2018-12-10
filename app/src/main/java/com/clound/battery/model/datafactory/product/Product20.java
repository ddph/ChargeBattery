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
public class Product20 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String energy = data.substring(6, 12);//电量
        String energycode = "00";//查询成功 00，非0 失败
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("20", energycode + energy));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("20", ""));
    }
}