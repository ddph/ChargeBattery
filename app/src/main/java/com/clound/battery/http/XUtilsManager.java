package com.clound.battery.http;

import android.support.v7.widget.ViewUtils;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * author cowards
 * created on 2018\12\8 0008
 **/
public class XUtilsManager {

    private static volatile XUtilsManager instance = null;

    public XUtilsManager() {
    }

    public static XUtilsManager getInstance() {
        if (instance == null) {
            synchronized (XUtilsManager.class) {
                if (instance == null) {
                    instance = new XUtilsManager();
                }
            }
        }
        return instance;
    }

    /**
     * xUtils  GET异步请求
     *
     * @param what         标签
     * @param httpCallBack
     * @param httpUrl      接口地址
     */
    public void getAsynHttp(final int what, final HttpCallBack httpCallBack, final String httpUrl) {
        RequestParams params = new RequestParams(httpUrl);
        params.setCacheMaxAge(1000 * 10);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return true;
            }

            @Override
            public void onSuccess(String result) {
                //如果服务返回304或onCache选择了信任缓存,这时result为null
                if (result != null) {
                    httpCallBack.onResponse(what, result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFailure(what, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * xUtils  POST异步请求
     *
     * @param what         标签
     * @param httpCallBack
     * @param httpUrl      接口地址
     * @param map          以key-value形式上传参数
     */
    public void postAsynHttp(final int what, final HttpCallBack httpCallBack, final String httpUrl, Map<String, String> map) {
        RequestParams params = new RequestParams(httpUrl);
        for (String key : map.keySet()) {
            params.addBodyParameter(key, map.get(key));
        }
        params.setCacheMaxAge(1000 * 10);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                httpCallBack.onResponse(what, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFailure(what, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 上传文件
     *
     * @param what         标签
     * @param httpCallBack
     * @param filePath     上传的本地文件路径
     * @param httpUrl      接口地址
     */
    public void uplodeFile(final int what, final HttpCallBack httpCallBack, String filePath, String httpUrl) {
        RequestParams params = new RequestParams(httpUrl);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(filePath));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                httpCallBack.onResponse(what, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFailure(what, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 下载文件
     *
     * @param what         标签
     * @param httpCallBack
     * @param filePath     下载到的本地文件路径
     * @param httpUrl      文件网络地址
     */
    public void getDownLoad(final int what, final HttpCallBack httpCallBack, String filePath, String httpUrl) {
        RequestParams params = new RequestParams(httpUrl);
        params.setSaveFilePath(filePath);
        params.setAutoRename(false);//自动为文件命名
        params.setAutoResume(true);//自动断点续传
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                httpCallBack.onResponse(what, result.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpCallBack.onFailure(what, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(XUtilsManager.class.getSimpleName(), "取消下载...");
            }

            @Override
            public void onFinished() {
                Log.e(XUtilsManager.class.getSimpleName(), "下载结束...");
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                Log.e(XUtilsManager.class.getSimpleName(), "下载等待中...");
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                Log.e(XUtilsManager.class.getSimpleName(), "开始下载...");
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                Log.e(XUtilsManager.class.getSimpleName(), "current：" + current + "，total：" + total);
            }
        });
    }

    public interface HttpCallBack {

        /**
         * 访问成功回调接口
         */
        void onResponse(int what, String response);

        /**
         * 访问成错误回调接口
         */
        void onFailure(int what, String error);
    }
}
