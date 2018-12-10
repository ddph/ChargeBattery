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
public class Product31 extends BaseProduct {

    public static volatile String hltime = null;

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String hlcode = "00";
        String order = data.substring(6, 16);
        LogUtils.e(TAG, "31---订单:" + order);
        String hl = data.substring(16, 18);
        LogUtils.e(TAG, "31---回路:" + hl);
        String hlstatu = data.substring(18, 20);
        LogUtils.e(TAG, "31---回路状态:" + hlstatu);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("31", hlcode + deviceid + order + hl + hlstatu));
        /** 等待子板命令回复成功时更新界面时间,若子板未收到或者Android主板未收到子板回复的命令则不更新界面的时间 **/
        if (hltime != null) {
            HlBean hlBean = ParseHex.getHlItemBattery(hltime);
            if (hlBean != null) {
                LogUtils.e(TAG, "hlBean:" + hlBean.toString());
                EventBus.getDefault().post(new HlEvent(hlBean));//通知界面更新回路时间
            }
            hltime = null;//界面时间更新完成后,记录的时间清空
        }
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        /**String deviceId = data.substring(2, 24);//设备ID
         LogUtils.e(TAG, "设备ID：" + deviceId);**/
        String orderNum = data.substring(24, 34);
        LogUtils.e(TAG, "订单号:" + orderNum);
        String hls = data.substring(34, 36);//回路
        LogUtils.e(TAG, "充电回路:" + hls);
        String hlstime = data.substring(36, 40);//充电时间
        LogUtils.e(TAG, "充电时间:" + hlstime);
        /** 记录当前充电时间,等待子板回复成功时显示界面时间 **/
        hltime = hls + hlstime;
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialData("31", orderNum + hls + hlstime));
    }
}