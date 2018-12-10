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
public class Product32 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String orderNum = data.substring(6, 16);//订单号
        LogUtils.e(TAG, "32.....充电订单号:" + orderNum);
        String hls = data.substring(16, 18);
        LogUtils.e(TAG, "32.....充电回路:" + hls);
        String hlsState = data.substring(18, 20);
        LogUtils.e(TAG, "32.....充电状态：" + hlsState);
        String hlsPower = data.substring(20, 24);
        LogUtils.e(TAG, "32.....充电功率:" + hlsPower);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("32", deviceid + orderNum + hls + hlsState + hlsPower));
        if (hlsState.equals("02") || hlsState.equals("03")) {
            HlBean bean = ParseHex.getHlItemEndBattery(hls);//回路未检测到功率或功率为0，断开充电
            EventBus.getDefault().post(new HlEvent(bean));
        } else if (hlsState.equals("04") || hlsState.equals("05")) {
            HlBean bean37 = ParseHex.getHlItemFaultBattery(hls);//故障
            EventBus.getDefault().post(new HlEvent(bean37));
        }
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        String code = data.substring(2, 4);
        LogUtils.e(TAG, "状态码:" + code);
        String device = data.substring(4, 26);
        LogUtils.e(TAG, "终端ID：" + device);
        String ordernum = data.substring(26, 36);
        LogUtils.e(TAG, "订单号:" + ordernum);
        String Hl = data.substring(36, 38);
        LogUtils.e(TAG, "回路:" + Hl);
        String orderState = data.substring(38, 40);
        LogUtils.e(TAG, "订单状态：" + orderState);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE, SerialDataUtil.getOnSendSerialDataBack("32", ordernum + Hl + orderState));
        if (code.equals("01")) {
            LogUtils.e(TAG, "充电成功");
        }
        if (code.equals("02")) {
            LogUtils.e(TAG, "充电失败---回路异常或订单号异常，订单结束或订单不存在");
            HlBean bean = ParseHex.getHlItemEndBattery(Hl);
            EventBus.getDefault().post(new HlEvent(bean));
        }
    }
}
