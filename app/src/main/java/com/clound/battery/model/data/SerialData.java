package com.clound.battery.model.data;

import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.event.MinaSerialEvent;
import com.clound.battery.model.event.SerialHlStateEvent;
import com.clound.battery.model.event.SettingSerialEvent;
import com.clound.battery.util.LogUtils;
import org.greenrobot.eventbus.EventBus;
import android_serialport_api.SerialPortUtil;

import static com.clound.battery.model.commend.BatteryCommend.MAIN_CODE;
import static com.clound.battery.model.commend.BatteryCommend.MINADATA_CODE;
import static com.clound.battery.model.commend.BatteryCommend.SETTINGACT_CODE;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class SerialData implements SerialPortUtil.SerialCallBack {

    private String TAG = getClass().getSimpleName();

    public SerialData() {
        SerialPortUtil.setSerialCallBack(this);
    }

    @Override
    public void onSerialData(int code, String serialPortData) {
        LogUtils.e(TAG, "onSerialData:" + serialPortData);
        switch (serialPortData.substring(4, 6)) {
            case "22"://所有回路状态
                setSerial(code, "22", serialPortData);
                break;
            case "38"://设置充电参数
                setSerial(code, "38", serialPortData);
                break;
            case "39"://查询充电参数
                setSerial(code, "39", serialPortData);
                break;
            case "23"://查询设备温度
                setSerial(code, "23", serialPortData);
                break;
            case "24"://查询设备回路功率
                setSerial(code, "24", serialPortData);
                break;
            case "25"://设置过温保护温度
                setSerial(code, "25", serialPortData);
                break;
            case "26"://查询设备过温保护温度
                setSerial(code, "26", serialPortData);
                break;
            case "27"://设置禁用或开启设备，设备被禁用时，在显示屏上显示禁用提示，同时扫码不能使用
                setSerial(code, "27", serialPortData);
                break;
            case "29"://查询设备程序版本
                setSerial(code, "29", serialPortData);
                break;
            case "30"://查询设备某回路充电时间
                setSerial(code, "30", serialPortData);
                break;
            case "20"://查询电表用电量
                setSerial(code, "20", serialPortData);
                break;
            case "31"://微信扫码后下发用户选择的充电回路
                setSerial(code, "31", serialPortData);
                break;
            case "14"://功率检测正常下发支付后充电命令
                setSerial(code, "14", serialPortData);
                break;
            case "34"://远程断开设备某回路的充电
                setSerial(code, "34", serialPortData);
                break;
            case "40"://远程开启设备某回路的充电
                setSerial(code, "40", serialPortData);
                break;
            case "35"://上传充电结束（充电主板主动发送）
                setSerial(BatteryCommend.MINADATA_CODE, "35", serialPortData);
                break;
            case "36"://用户第一次刷卡，查询余额
                setSerial(BatteryCommend.MINADATA_CODE, "36", serialPortData);
                break;
            case "19"://刷卡后收到服务器发送的充电时间，选择过充电回路，开始充电
                setSerial(code, "41", serialPortData);
                break;
            case "42"://火灾报警
                setSerial(BatteryCommend.MINADATA_CODE, "42", serialPortData);
                break;
            case "32"://下发充电回路和充电时间后，10S检测回路后上传回路状态
                setSerial(BatteryCommend.MINADATA_CODE, "32", serialPortData);
                break;
            case "21"://查询电表当前电压电流功率参数命令
                setSerial(code, "21", serialPortData);
                break;
            case "28"://查询所有回路充电时间
                setSerial(code, "28", serialPortData);
                break;
            case "41"://刷卡后收到服务器发送的充电时间，选择过充电回路，开始充电
                setSerial(BatteryCommend.MINADATA_CODE, "41", serialPortData);
                break;
            case "44"://查询所有回路时间
                setSerial(code, "44", serialPortData);
                break;
        }
    }

    /**
     * 指令从哪里发过来,回哪里去
     *
     * @param code           从...发送的code值
     * @param serialPortData 指令内容
     */
    private void setSerial(int code, String serialCode, String serialPortData) {
        switch (code) {
            case MAIN_CODE:
                LogUtils.d(TAG, "数据回调到MainActivity：" + serialPortData);
                EventBus.getDefault().post(new SerialHlStateEvent(serialCode, serialPortData));
                break;
            case SETTINGACT_CODE:
                LogUtils.d(TAG, "数据回调到SettingActivity：" + serialPortData);
                EventBus.getDefault().post(new SettingSerialEvent(serialCode, serialPortData));
                break;
            case MINADATA_CODE:
                LogUtils.d(TAG, "数据回调到MinaData：" + serialPortData);
                EventBus.getDefault().post(new MinaSerialEvent(serialCode, serialPortData));
                break;
        }
    }
}