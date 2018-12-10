package com.clound.battery.model.datapackaging;

import com.clound.battery.manager.WIFIManager;
import com.clound.battery.util.ByteUtil;
import com.clound.battery.util.SaveDeviceMessageInfo;

/**
 * author cowards
 * created on 2018\10\17 0017
 **/
public class MinaDataUtil {

    public static String MinaData_16() {
        String CODE = "00";//命令返回码,00成功,非00失败
        String ICCID = ByteUtil.strTo16("000000000000000");//SIM卡的ICCID  PhoneInfoInstance.getIntance().getIccid()
        String IMEI = ByteUtil.strTo16("000000000000000"); //SystemUtil.getIMEI(BatteryApp.ApplicationContext)
        String STANDBY = "00";//备用字节
        String TPDTM = "00000000";//当月话费
        String MB = "00000000";//话费余额
        String MTF = "00000000";//当月总流量
        String TCMHUT = "00000000";//当月已使用流量
        String TCTFTM = "0000";//当月总通话时间
        String TTFTM = "0000";//当月已通话时间
        String SCFTM = "0000";//当月总短信条数
        String ASCNWSTM = "0000";//当月已发送短信条数
        return CODE + ICCID + IMEI + STANDBY + TPDTM + MB + MTF + TCMHUT + TCTFTM + TTFTM + SCFTM + ASCNWSTM;
    }

    /**
     * 心跳包
     *
     * @return 4G(测试阶段用wifi)信号强度, 强度大小:0-100
     */
    public static String minaData4G() {
        int wifi = WIFIManager.getWifiRssi() + 100;
        return Integer.toHexString(wifi);
    }

    /**
     * 客户端登录调用
     *
     * @return DeviceId
     */
    public static String minaDataDeviceId() {
        return ByteUtil.strTo16(SaveDeviceMessageInfo.readDeviceId()) + "01" + "01";//0101分别是协议版本和程序版本
    }

    /**
     * 封装成要主动发送到服务端的数据格式
     *
     * @param commandKey   命令字
     * @param commandValue 命令内容
     * @return 要发送到服务端的数据
     */
    public static String ClintGoToServer(String commandKey, String commandValue) {
        String header = "55AA";//包头
        int length = 6 + commandValue.length() / 2; //长度
        String leng = ByteUtil.getShort(Integer.toHexString(length));//长度转为十六进制高低位
        String data = header + leng + commandKey + commandValue;
        String crc = ByteUtil.getCRC_16CheckSum(data);
        return (data + crc).toUpperCase();
    }

    /**
     * 封装成要返回给服务端的数据格式
     *
     * @param commandKey   命令字
     * @param commandValue 命令内容
     * @return 要发送到服务端的数据
     */
    public static String ClintGoToServerBack(String commandKey, String commandValue) {
        String header = "55AA";//包头
        String str = "66";
        int length = 7 + commandValue.length() / 2; //长度
        String leng = ByteUtil.getShort(Integer.toHexString(length));//长度转为十六进制高低位
        String data = header + leng + str + commandKey + commandValue;
        String crc = ByteUtil.getCRC_16CheckSum(data);
        return (data + crc).toUpperCase();
    }
}