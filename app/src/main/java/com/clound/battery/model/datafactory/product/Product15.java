package com.clound.battery.model.datafactory.product;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.util.LogUtils;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product15 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        //收到跳跳包，认为当前连接没有断开
    }
}