package com.clound.battery.util;

import android.os.Environment;
import android.util.Log;

import com.clound.battery.application.BatteryApp;
import com.clound.battery.util.system.SystemUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2018/8/17.
 */
public class LogUtils {

    public static boolean CHEAK = true;                     //开发阶段设置为true
    private static String SDCARD = Constant.PATH_LOG;       //缓存根路径
    private static String TAG = "LogUtil";
    private static String logPath = null;                   //log日志存放路径
    private static final char VERBOSE = 'v';
    private static final char INFO = 'i';
    private static final char DEBUG = 'd';
    private static final char WARN = 'w';
    private static final char ERROR = 'e';
    public static String log = SDCARD + "/";

    /**
     * 初始化，须在使用之前设置，最好在Application创建时调用,获得文件储存路径,在后面加"/log"建立子文件夹
     */
    public static void init() {
        logPath = getFilePath() + "/log/";
        File fileLog = new File(log);
        if (!fileLog.exists()) {
            fileLog.mkdirs();
            Log.e(TAG, "文件夹不存在fileLog");
        }
    }

    /**
     * 判断sdcard是否可用
     */
    public static boolean isMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得文件存储路径
     *
     * @return
     */
    private static String getFilePath() {
        if (isMounted()) {
            return SDCARD;
        } else {
            CHEAK = true;
            return SDCARD;
        }
    }

    public static void v(String tag, String msg) {
        if (CHEAK) {
            Log.v(tag, msg);
        } else {
            writeToFile(VERBOSE, tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (CHEAK) {
            Log.d(tag, msg);
        } else {
            writeToFile(DEBUG, tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (CHEAK) {
            Log.i(tag, msg);
        } else {
            writeToFile(INFO, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (CHEAK) {
            Log.w(tag, msg);
        } else {
            writeToFile(WARN, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (CHEAK) {
            Log.e(tag, msg);
        } else {
            writeToFile(ERROR, tag, msg);
        }
    }

    /**
     * 将log信息写入文件中
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void writeToFile(char type, String tag, String msg) {
        if (null == logPath) {
            Log.e(TAG, "logPath == null ，未初始化LogToFile");
            return;
        }
        String fileName = logPath + "/debug_log" + ".txt";
        clearCaches(fileName);
        if (!new File(logPath).exists()) {
            new File(logPath).mkdirs();
        }
        String log = DateUtil.getDate() + "...VersionCode:" + SystemUtil.getVersionCode() + "------" + type + " " + tag + " " + msg + "\n";
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(log);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空缓存目录
     */
    public static void clearCaches(String Path) {
        File file = new File(Path);
        if (file.length() / 1024 > 1024 * 8) {
            file.delete();
        }
    }
}