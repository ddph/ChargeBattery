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
public class Product24 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String huilucode = "00";
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("24", huilucode + data.substring(6, data.length() - 2)));
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String huilu = data.substring(2, 4);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("24", huilu));
    }
}