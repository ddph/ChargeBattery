package com.clound.battery.util.download;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class DownLoadFileUtil {
    public static String TAG = "DownLoadFileUtil";
    public static final String SDCARD = Constant.PATH_PROJECT;

    /**
     * 判断sdcard是否可用
     */
    public static boolean isMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 初始化检测对应文件夹是否存在
     */
    public static void ismKards() {
        File filePicture = new File(Constant.PATH_IMAGE);//图片缓存路径
        File fileMov = new File(Constant.PATH_VIDEO);//视频缓存路径
        File fileHtml = new File(Constant.PATH_HTML);//html缓存路径
        File fileCache = new File(Constant.PATH_CACHE);//崩溃日志缓存路径
        File fileApk = new File(Constant.PATH_APK);//apk下载地址
        File fileLog = new File(Constant.PATH_LOG);//正常日志缓存路径
        File fileDeviceId = new File(Constant.PATH_SAVE_DEVICE);//保存设备ID
        File fileQrCode = new File(Constant.PATH_QRCODE);//保存二维码
        File fileRate = new File(Constant.PATH_RATE);//存放收费标准,以防App更新缓存在SharedPreference内的收费标准丢失,所以写在本地txt日志内
        if (!fileRate.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileRate");
            fileRate.mkdirs();
        }
        if (!fileQrCode.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileQrCode");
            fileQrCode.mkdirs();
        }
        if (!filePicture.exists()) {
            LogUtils.e(TAG, "文件夹不存在filePicture");
            filePicture.mkdirs();
        }
        if (!fileMov.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileMov");
            fileMov.mkdirs();
        }
        if (!fileHtml.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileHtml");
            fileHtml.mkdirs();
        }
        if (!fileCache.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileCache");
            fileCache.mkdirs();
        }
        if (!fileApk.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileApk");
            fileApk.mkdirs();
        }
        if (!fileLog.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileLog");
            fileLog.mkdirs();
        }
        if (!fileDeviceId.exists()) {
            LogUtils.e(TAG, "文件夹不存在fileDeviceId");
            fileDeviceId.mkdirs();
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 在SD卡的指定目录上创建文件
     *
     * @param fileName
     */
    public static File createFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 读图片
     */
    public static Bitmap readImage(String url) {
        if (!isMounted()) {
            return null;
        }
        File file = new File(SDCARD + "/" + url);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * 清空缓存目录
     */
    public static void clearCaches(String Path) {
        File dir = new File(Path);
        if (dir.exists()) {
            File[] allfiles = dir.listFiles();
            for (File file : allfiles) {
                file.delete();
                //deletePic(file.getAbsolutePath());
            }
        }
    }

    private static void deletePic(String path) {
        if (!TextUtils.isEmpty(path)) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = BatteryApp.ApplicationContext.getContentResolver();//cutPic.this是一个上下文
            String url = MediaStore.Images.Media.DATA + "='" + path + "'";
            //删除图片
            contentResolver.delete(uri, url, null);
        }
    }

    /**
     * @param context  上下文环境
     * @param fileName 存放在assets文件夹下的文件的文件名
     * @param savePath 在sd卡的存放路径
     */
    public static void copyAssetToSD(Context context, String fileName, String savePath) {
        InputStream inputStream;
        File targetFile = new File(savePath + fileName);
        if (targetFile.exists()) {
            return;
        } else {
            boolean isSucce = targetFile.mkdir();
            Log.e(TAG, "copyAssetToSD: 路径：：" + targetFile.getAbsolutePath());
            Log.e(TAG, "copyAssetToSD: 创建目录：：" + isSucce);
        }
        try {
            inputStream = context.getResources().getAssets().open(fileName);// assets文件夹下的文件
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);// 保存到本地的文件夹下的文件
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("TAG", "copy: " + e.toString());
            e.printStackTrace();
        }
    }

    //Constant.Picture,Constant.MovFile，两个参数可选
    public static List<String> getLocalFileName(String path) {
        List<String> fileNameList = new ArrayList<>();
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                fileNameList.add(file.getName());
            }
        }
        return fileNameList;
    }

    private static String getFileMD5(String path) {
        String md5 = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            System.out.println("文件md5值：" + bigInt.toString(16));
            md5 = bigInt.toString(16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }
}