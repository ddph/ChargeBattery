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
public class Product38 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String code38 = data.substring(6, 8);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("38", code38));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String maxpower = data.substring(2, 6);//最大功率
        LogUtils.e(TAG, "38...最大功率:" + maxpower);
        String ziting = data.substring(6, 8);//充满自停功率
        LogUtils.e(TAG, "38...充满自停功率:" + ziting);
        String yanshi = data.substring(8, 10);//充满延时时间
        LogUtils.e(TAG, "38...充满延时时间:" + yanshi);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("38", maxpower + ziting + yanshi));
    }
}