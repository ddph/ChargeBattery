package com.clound.battery.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * created by Administrator on 2018/8/28 19:37
 */
public class SaveDeviceMessageInfo {

    public static String fileName = "id.txt";//保存设备ID
    public static String rateName = "rate.txt";//保存设备收费标准

    /**
     * 保存设备ID
     *
     * @param deviceId 输入的设备ID
     */
    public static boolean saveDeviceId(String deviceId) {
        File file = new File(Constant.PATH_SAVE_DEVICE + fileName);
        if (file.exists()) {
            FileUtil.delete(Constant.PATH_SAVE_DEVICE + fileName);
        }
        return writeTxtToFile(Base64Util.encode(deviceId), Constant.PATH_SAVE_DEVICE, fileName);//对保存的设备ID加密保存
    }

    /**
     * 保存收费标准
     *
     * @param rate 输入的收费标准
     */
    public static boolean saveRate(String rate) {
        File file = new File(Constant.PATH_RATE + rateName);
        if (file.exists()) {
            FileUtil.delete(Constant.PATH_RATE + rateName);
        }
        return writeTxtToFile(rate, Constant.PATH_RATE, rateName);//对保存的收费标准
    }

    /**
     * 字符串写入本地txt
     *
     * @param strcontent 文件内容
     * @param filePath   文件地址
     * @param fileName   文件名
     * @return 写入结果
     */
    private static boolean writeTxtToFile(String strcontent, String filePath, String fileName) {
        boolean isSavaFile = false;
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
            isSavaFile = true;
        } catch (Exception e) {
            isSavaFile = false;
            Log.e("TestFile", "Error on write File:" + e);
        }
        return isSavaFile;
    }

    /**
     * 生成文件
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     */
    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 生成文件夹
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    /**
     * 读取本地文件
     */
    public static String readDeviceId() {
        String path = Constant.PATH_SAVE_DEVICE + fileName;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        if (file.isDirectory()) {
            Log.e("TestFile", "The File doesn't not exist.");
            return "";
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.e("TestFile", "The File doesn't not exist.");
                return "";
            } catch (IOException e) {
                Log.e("TestFile", e.getMessage());
                return "";
            }
        }
        return Base64Util.decode(stringBuilder.toString());//对读到的设备ID解密
    }

    /**
     * 读取本地文件
     */
    public static String readRate() {
        String path = Constant.PATH_RATE + rateName;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        if (file.isDirectory()) {
            Log.e("TestFile", "The File doesn't not exist.");
            return "";
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.e("TestFile", "The File doesn't not exist.");
                return "";
            } catch (IOException e) {
                Log.e("TestFile", e.getMessage());
                return "";
            }
        }
        return stringBuilder.toString();//对读到的设备ID解密
    }
}