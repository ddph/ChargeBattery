package com.clound.battery.model.data;

import com.clound.battery.model.datafactory.factory.BatteryFactory;
import com.clound.battery.model.event.MinaDataEvent;
import com.clound.battery.model.event.MinaSerialEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class MinaData {

    public BatteryFactory batteryFactory = null;

    public MinaData() {
        EventBus.getDefault().register(this);
        batteryFactory = new BatteryFactory();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMinaDataEvent(MinaDataEvent minaDataEvent) {
        String minaData = minaDataEvent.minaData;
        if (minaData.substring(0, 2).equals("66")) {//服务器返回的消息
            minaData = minaData.substring(2, minaData.length());//除66外把命令内容截取出来
            switch (minaData.substring(0, 2)) {
                case "10":      //登陆成功
                    batteryFactory.disposeData(10).proceServer(minaData);
                    break;
                case "12":      //安卓获取时间命令
                    batteryFactory.disposeData(12).proceServer(minaData);
                    break;
                case "15":      //心跳包命令
                    batteryFactory.disposeData(15).proceServer(minaData);
                    break;
                case "32":      //下发充电回路和充电时间后，10S检测回路后上传回路状态
                    batteryFactory.disposeData(32).proceServer(minaData);
                    break;
                case "35":      //上传充电结束命令
                    batteryFactory.disposeData(35).proceServer(minaData);
                    break;
                case "36":      //用户第一次刷卡，查询余额
                    batteryFactory.disposeData(36).proceServer(minaData);
                    break;
                case "37":      //回路检测正常，用户刷卡充电（上传卡号，后台要判断余额）
                    batteryFactory.disposeData(37).proceServer(minaData);
                    break;
                case "41":      //用户刷卡收到服务器发送的充电时间或电量，开始充电 0x41
                    batteryFactory.disposeData(41).proceServer(minaData);
                    break;
                case "42":      //火灾报警
                    batteryFactory.disposeData(42).proceServer(minaData);
                    break;
            }
        } else {//服务器主动下发的消息
            switch (minaData.substring(0, 2)) {
                case "13":      //服务器设置安卓时间命令
                    batteryFactory.disposeData(13).proceServer(minaData);
                    break;
                case "16":      //服务器获取安卓状态信息
                    batteryFactory.disposeData(16).proceServer(minaData);
                    break;
                case "17":      //服务器设置安卓的连接参数
                    batteryFactory.disposeData(17).proceServer(minaData);
                    break;
                case "18":      //服务器查询安卓位置信息
                    batteryFactory.disposeData(18).proceServer(minaData);
                    break;
                case "20":      //查询电表用电电量命令
                    batteryFactory.disposeData(20).proceServer(minaData);
                    break;
                case "21":      //查询电表当前电压电流功率参数命令
                    batteryFactory.disposeData(21).proceServer(minaData);
                    break;
                case "22":      //查询设备回路状态命令
                    batteryFactory.disposeData(22).proceServer(minaData);
                    break;
                case "23":      //查询设备温度命令
                    batteryFactory.disposeData(23).proceServer(minaData);
                    break;
                case "24":      //查询设备回路功率命令
                    batteryFactory.disposeData(24).proceServer(minaData);
                    break;
                case "25":      //设置设备过温保护温度
                    batteryFactory.disposeData(25).proceServer(minaData);
                    break;
                case "26":      //查询设备过温保护温度
                    batteryFactory.disposeData(26).proceServer(minaData);
                    break;
                case "27":      //禁用或开启设备命令
                    batteryFactory.disposeData(27).proceServer(minaData);
                    break;
                case "28":      //查询设备是否被禁用命令
                    batteryFactory.disposeData(28).proceServer(minaData);
                    break;
                case "29":      //查询设备程序版本
                    batteryFactory.disposeData(29).proceServer(minaData);
                    break;
                case "30":      //查询设备某回路充电时间或充电电量
                    batteryFactory.disposeData(30).proceServer(minaData);
                    break;
                case "31":      //微信扫码后，下发用户选择的充电回路
                    batteryFactory.disposeData(31).proceServer(minaData);
                    break;
                case "34":      //远程断开设备某回路的充电：（同时显示屏更新充电状态）
                    batteryFactory.disposeData(34).proceServer(minaData);
                    break;
                case "40":      //远程开启设备某回路的充电：（同时显示屏更新充电状态）
                    batteryFactory.disposeData(40).proceServer(minaData);
                    break;
                case "38":      //设置设备充电参数
                    batteryFactory.disposeData(38).proceServer(minaData);
                    break;
                case "39":      //查询设备充电参数
                    batteryFactory.disposeData(39).proceServer(minaData);
                    break;
                case "45":      //广告更新了
                    batteryFactory.disposeData(45).proceServer(minaData);
                    break;
                case "46":      //Android版本App远程更新
                    batteryFactory.disposeData(46).proceServer(minaData);
                    break;
                case "47":      //Android版本App远程更新
                    batteryFactory.disposeData(47).proceServer(minaData);
                    break;
            }
        }
    }

    /**
     * 后台控制Android去设备或查询子板
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketSerialData(MinaSerialEvent serialPortData) {
        String data = serialPortData.msg;
        switch (serialPortData.code) {
            case "22"://所有回路状态
                batteryFactory.disposeData(22).proceSerial(data);
                break;
            case "38"://设置充电参数
                batteryFactory.disposeData(38).proceSerial(data);
                break;
            case "39"://查询充电参数
                batteryFactory.disposeData(39).proceSerial(data);
                break;
            case "23"://查询设备温度
                batteryFactory.disposeData(23).proceSerial(data);
                break;
            case "24"://查询设备回路功率
                batteryFactory.disposeData(24).proceSerial(data);
                break;
            case "25"://设置过温保护温度
                batteryFactory.disposeData(25).proceSerial(data);
                break;
            case "26"://查询设备过温保护温度
                batteryFactory.disposeData(26).proceSerial(data);
                break;
            case "27"://设置禁用或开启设备，设备被禁用时，在显示屏上显示禁用提示，同时扫码不能使用
                batteryFactory.disposeData(27).proceSerial(data);
                break;
            case "29"://查询设备程序版本
                batteryFactory.disposeData(29).proceSerial(data);
                break;
            case "30"://查询设备某回路充电时间
                batteryFactory.disposeData(30).proceSerial(data);
                break;
            case "20"://查询电表用电量
                batteryFactory.disposeData(20).proceSerial(data);
                break;
            case "31"://微信扫码后下发用户选择的充电回路.
                batteryFactory.disposeData(31).proceSerial(data);
                break;
            case "34"://远程断开设备某回路的充电
                batteryFactory.disposeData(34).proceSerial(data);
                break;
            case "40"://远程开启设备某回路的充电
                batteryFactory.disposeData(40).proceSerial(data);
                break;
            case "35"://上传充电结束（充电主板主动发送）
                batteryFactory.disposeData(35).proceSerial(data);
                break;
            case "36"://用户第一次刷卡，查询余额
                batteryFactory.disposeData(36).proceSerial(data);
                break;
            case "42"://火灾报警
                batteryFactory.disposeData(42).proceSerial(data);
                break;
            case "32"://下发充电回路和充电时间后，10S检测回路后上传回路状态
                batteryFactory.disposeData(32).proceSerial(data);
                break;
            case "21"://查询电表当前电压电流功率参数命令
                batteryFactory.disposeData(21).proceSerial(data);
                break;
            case "28"://查询所有回路充电时间
                batteryFactory.disposeData(28).proceSerial(data);
                break;
            case "41":
                batteryFactory.disposeData(41).proceSerial(data);
                break;
        }
    }

    public void close() {
        EventBus.getDefault().unregister(this);
    }

}