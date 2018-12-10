package com.clound.battery.model.datapackaging;

import com.clound.battery.util.ByteUtil;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class SerialDataUtil {

    /**
     * 封装成要发送到Android充电主板的数据格式
     *
     * @param commandKey   命令字
     * @param commandValue 命令内容
     *
     * @return 要发送到Android充电桩子板的数据
     */
    public static String getOnSendSerialData(String commandKey, String commandValue) {
        String header = "CC";
        int length = 4 + commandValue.length() / 2;
        String leng = Integer.toHexString(length);//长度转为十六进制高低位
        if (leng.length() == 1) {
            leng = "0" + leng;
        }
        String data = header + leng + commandKey + commandValue;
        String crc = ByteUtil.getCRC_16CheckSum(data);
        return (data + crc).toUpperCase();
    }

    /**
     * 封装成要返回发送到Android充电主板的数据格式
     *
     * @param commandKey   命令字
     * @param commandValue 命令内容
     *
     * @return 要发送到Android充电桩子板的数据
     */
    public static String getOnSendSerialDataBack(String commandKey, String commandValue) {
        String header = "33";
        int length = 4 + commandValue.length() / 2;
        String leng = Integer.toHexString(length);//长度转为十六进制高低位
        if (leng.length() == 1) {
            leng = "0" + leng;
        }
        String data = header + leng + commandKey + commandValue;
        String crc = ByteUtil.getCRC_16CheckSum(data);
        return (data + crc).toUpperCase();
    }

}