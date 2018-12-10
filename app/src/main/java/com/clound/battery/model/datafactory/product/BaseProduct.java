package com.clound.battery.model.datafactory.product;

import com.clound.battery.util.ByteUtil;
import com.clound.battery.util.SaveDeviceMessageInfo;

public abstract class BaseProduct {

    public String TAG = getClass().getSimpleName();

    public String deviceid = ByteUtil.strTo16(SaveDeviceMessageInfo.readDeviceId());

    /**
     * 串口过来的数据
     */
    public abstract void proceSerial(String data);

    /**
     * 服务器过来的数据
     */
    public abstract void proceServer(String data);

}