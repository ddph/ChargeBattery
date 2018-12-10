package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import util.ByteUtil;

/**
 * Created by Administrator on 2018/5/31.
 */
public class SerialPortUtil {

    public static String TAG = "SerialPortUtil";

    /**
     * 标记当前串口状态(true:打开,false:关闭)
     **/
    public static boolean isFlagSerial = false;
    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static SerialCallBack serialCallBack;
    private static int code;

    public static void setSerialCallBack(SerialCallBack callBack) {
        serialCallBack = callBack;
    }

    /**
     * 打开串口
     */
    public static boolean open(String pathname, int baud, int flags) {
        if (isFlagSerial) {
            return isFlagSerial;
        }
        try {
            serialPort = new SerialPort(new File(pathname), baud, flags);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
//            receive();
            receiveCopy();
            isFlagSerial = true;
        } catch (IOException e) {
            e.printStackTrace();
            isFlagSerial = false;
        }
        return isFlagSerial;
    }

    /**
     * 关闭串口
     */
    public static boolean close() {
        if (isFlagSerial) {
            return false;
        }
        boolean isClose = false;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isClose = true;
            isFlagSerial = false;//关闭串口时，连接状态标记为false
        } catch (IOException e) {
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }

    /**
     * 发送16进制串口指令
     */
    public static void sendString(int codes, String data) {
        code = codes;
        if (!isFlagSerial) {
            return;
        }
        try {
            outputStream.write(ByteUtil.hex2byte(data));
            outputStream.flush();
            Log.e(TAG, "App--->串口:" + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] buffer = new byte[128];
    private static int activeLength = 0;

    /**
     * 接收串口数据的方法
     * 此方法已经废弃,现在用receiveCopy()方法代替此方法
     */
    @Deprecated
    public static void receive() {
        if (receiveThread != null && !isFlagSerial) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (isFlagSerial) {
                    try {
                        byte[] readData = new byte[1];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && isFlagSerial) {
                            try {
                                System.arraycopy(readData, 0, buffer, activeLength, size);
                                activeLength += size;
                                String receStr = ByteUtil.byteToStr(buffer, activeLength);//拼接好的数据转字串
                                if (receStr.length() > 6) {
                                    if ((receStr.substring(4, 6).equals("32")) && (receStr.length() == 26)) {
                                        Log.d(TAG, "原数据32:" + receStr);
                                        if (activeLength > 3) {
                                            byte[] bytes = new byte[activeLength - 1];
                                            System.arraycopy(buffer, 0, bytes, 0, bytes.length);
                                            String str = ByteUtil.byteToStr(bytes, bytes.length);
                                            String crc_16 = ByteUtil.getCRC_16CheckSum(str);//计算好的最后一个CRC字节
                                            crc_16 = str + crc_16;
                                            if (receStr.equals(crc_16.toUpperCase())) {
                                                String rightStr = ByteUtil.byteToStr(buffer, activeLength);
                                                Log.d(TAG, "校验成功的数据:" + rightStr);
                                                serialCallBack.onSerialData(code, rightStr);
                                                activeLength = 0;
                                            }
                                        }
                                    } else {
                                        Log.d(TAG, "原数据32:" + receStr);
                                        if (activeLength > 3) {
                                            byte[] bytes = new byte[activeLength - 1];
                                            System.arraycopy(buffer, 0, bytes, 0, bytes.length);
                                            String str = ByteUtil.byteToStr(bytes, bytes.length);
                                            String crc_16 = ByteUtil.getCRC_16CheckSum(str);//计算好的最后一个CRC字节
                                            crc_16 = str + crc_16;
                                            if (receStr.equals(crc_16.toUpperCase())) {
                                                String rightStr = ByteUtil.byteToStr(buffer, activeLength);
                                                Log.d(TAG, "校验成功的数据:" + rightStr);
                                                serialCallBack.onSerialData(code, rightStr);
                                                activeLength = 0;
                                            }
                                        }
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.e(TAG, "串口数据读取异常：" + e.toString());
                                activeLength = 0;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveCopy() {
        if (receiveThread != null && !isFlagSerial) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (isFlagSerial) {
                    try {
                        byte[] readData = new byte[1];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && isFlagSerial) {
                            try {
                                System.arraycopy(readData, 0, buffer, activeLength, size);
                                activeLength += size;
                                String receStr = ByteUtil.byteToStr(buffer, activeLength);//拼接好的数据转字串
                                Log.d(TAG, "原始数据:" + receStr);
                                if (receStr.length() > 6) {
                                    switch (receStr.substring(4, 6)) {
                                        case "22":
                                            if (receStr.length() == 48) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            } else if (receStr.length() == 88) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "38":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "39":
                                            if (receStr.length() == 16) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "23":
                                            if (receStr.length() == 12) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "24":
                                            if (receStr.length() == 14) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "25":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "26":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "27":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "29":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "30":
                                            if (receStr.length() == 14) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "28":
                                            if (receStr.length() == 10) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "20":
                                            if (receStr.length() == 14) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "31":
                                            if (receStr.length() == 22) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "34":
                                            if (receStr.length() == 30) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "40":
                                            if (receStr.length() == 22) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "44":
                                            if ((receStr.length() == 68)) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            } else if (receStr.length() == 128) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "21":
                                            if (receStr.length() == 22) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "35":
                                            if (receStr.length() == 30) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "36":
                                            if (receStr.length() == 16) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "41":
                                            if (receStr.length() == 22) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "42":
                                            if (receStr.length() == 14) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        case "32":
                                            if (receStr.length() == 26) {
                                                if (activeLength > 3) {
                                                    verify(receStr);
                                                }
                                            }
                                            break;
                                        default:
                                            activeLength = 0;
                                            break;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.e(TAG, "串口数据读取异常：" + e.toString());
                                activeLength = 0;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }

    private static void verify(String receStr) {
        byte[] bytes = new byte[activeLength - 1];
        System.arraycopy(buffer, 0, bytes, 0, bytes.length);
        String str = ByteUtil.byteToStr(bytes, bytes.length);
        String crc_16 = ByteUtil.getCRC_16CheckSum(str);//计算好的最后一个CRC字节
        crc_16 = str + crc_16;
        if (receStr.equals(crc_16.toUpperCase())) {
            String rightStr = ByteUtil.byteToStr(buffer, activeLength);
            Log.d(TAG, "校验成功的数据:" + rightStr);
            serialCallBack.onSerialData(code, rightStr);
            activeLength = 0;
        }
    }

    /**
     * 串口数据回调
     */
    public interface SerialCallBack {
        void onSerialData(int code, String serialPortData);
    }
}