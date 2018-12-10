package com.clound.battery.model.datafactory.product;

import android.util.Log;

import com.clound.battery.model.data.MinaData;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.manager.MinaManager;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product16 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        Log.e(TAG, "proceSerial: " + data);
    }

    @Override
    public void proceServer(String data) {
        Log.e(TAG, "proceServer: " + data);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("16",MinaDataUtil.MinaData_16()));
    }
}