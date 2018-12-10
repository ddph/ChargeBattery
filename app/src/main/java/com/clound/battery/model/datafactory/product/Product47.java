package com.clound.battery.model.datafactory.product;

import android.util.Log;

import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.event.MinaActEvent;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.SaveDeviceMessageInfo;
import com.clound.battery.util.download.DownLoadFileUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product47 extends BaseProduct {

    @Override
    public void proceSerial(String data) {}

    /**
     * 收费标准
     *
     * @param data
     */
    @Override
    public void proceServer(String data) {
        Log.e(TAG, "proceServer: " + data);
        String str = data.substring(2, data.length());
        StringBuilder rateData = new StringBuilder();
        LogUtils.e(TAG, "str:" + str);
        if (str.length() > 6) {
            DownLoadFileUtil.clearCaches(Constant.PATH_RATE);
            for (int size = 0; size < str.length() / 6; size++) {
                String p = str.substring(size * 6, (size + 1) * 6);
                String power = getTen(p.substring(0, 4));//功率
                LogUtils.e(TAG, "功率" + size + ":" + power);
                String rate = String.valueOf(Double.parseDouble(getTen(p.substring(4, 6))) / 100.00);//费率
                LogUtils.e(TAG, "费率" + size + ":" + rate);
                rateData.append(power + "," + rate + ",");
            }
            LogUtils.e(TAG, "rateData：" + rateData);
            SaveDeviceMessageInfo.saveRate(rateData.toString());
            EventBus.getDefault().post(new MinaActEvent(205, rateData.toString()));
        }
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("47", "00"));//00：成功(状态值)
    }

    private String getTen(String data) {
        int ret = Integer.parseInt(data, 16);
        ret = ((ret & 0x8000) > 0) ? (ret - 0x10000) : (ret);
        return String.valueOf(ret);
    }
}