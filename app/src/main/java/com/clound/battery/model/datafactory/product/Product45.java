package com.clound.battery.model.datafactory.product;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.clound.battery.http.XUtilsManager;
import com.clound.battery.model.bean.AdvertiseBean;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.event.MinaActEvent;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.util.Constant;
import com.clound.battery.util.JsonUtil;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.download.DownLoadFileUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product45 extends BaseProduct implements XUtilsManager.HttpCallBack {
    private Handler handler = new Handler();
    private int IMG = 0;//图片计数
    private int VIDEO = 0;//视频计数
    private int HTML = 0;//html文件计数

    private int IMG_SIZE = 0;//图片计数
    private int VIDEO_SIZE = 0;//视频计数
    private int HTML_SIZE = 0;//html文件计数

    private List<String> imgList = new ArrayList<>();//存放图片集合
    private List<String> videoList = new ArrayList<>();//存放视频集合
    private List<String> htmlList = new ArrayList<>();//存放h5文件集合

    @Override
    public void proceSerial(String data) {
    }

    @Override
    public void proceServer(String data) {
        LogUtils.e(TAG, "proceServer: " + data);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServerBack("45", "00"));
        if (data.substring(2, 4).equals("01")) {//Android广告
            switch (data.substring(4, 6)) {
                case "01"://更新图片
                    LogUtils.e(TAG, "更新图片...");
                    DownLoadFileUtil.clearCaches(Constant.PATH_IMAGE);
                    break;
                case "02"://更新视频
                    LogUtils.e(TAG, "更新视频...");
                    DownLoadFileUtil.clearCaches(Constant.PATH_VIDEO);
                    break;
                case "03"://更新HTML
                    LogUtils.e(TAG, "更新HTML...");
                    DownLoadFileUtil.clearCaches(Constant.PATH_HTML);
                    break;
                case "04"://全部更新
                    break;
                default:
                    break;
            }
        }
        XUtilsManager.getInstance().getAsynHttp(201, this, BatteryCommend.httlUrl);//拉取广告信息
    }

    private class DownLoadRunnable implements Runnable {
        private int type;

        public DownLoadRunnable(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            switch (type) {
                case 202://图片
                    if (IMG < imgList.size()) {
                        LogUtils.e(TAG, "准备下载图片:" + imgList.get(IMG));
                        XUtilsManager.getInstance().getDownLoad(202, Product45.this, Constant.PATH_IMAGE + imgList.get(IMG), BatteryCommend.http + imgList.get(IMG));
                    } else {//更新完成
                        EventBus.getDefault().post(new MinaActEvent(201, "广告更新完成"));
                        IMG = 0;
                        IMG_SIZE = 0;
                    }
                    break;
                case 203://视频
                    if (VIDEO < videoList.size()) {
                        LogUtils.e(TAG, "准备下载视频:" + videoList.get(VIDEO));
                        XUtilsManager.getInstance().getDownLoad(203, Product45.this, Constant.PATH_VIDEO + videoList.get(VIDEO), BatteryCommend.http + videoList.get(VIDEO));
                    } else {//更新完成
                        EventBus.getDefault().post(new MinaActEvent(201, "广告更新完成"));
                        VIDEO = 0;
                        VIDEO_SIZE = 0;
                    }
                    break;
                case 204://html
                    if (HTML < htmlList.size()) {
                        LogUtils.e(TAG, "准备下载HTML模板:" + htmlList.get(HTML));
                        XUtilsManager.getInstance().getDownLoad(204, Product45.this, Constant.PATH_HTML + (HTML + ".html"), BatteryCommend.http + imgList.get(HTML));
                    } else {//更新完成
                        EventBus.getDefault().post(new MinaActEvent(201, "广告更新完成"));
                        HTML = 0;
                        HTML_SIZE = 0;
                    }
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResponse(int what, String response) {
        switch (what) {
            case 201://请求广告数据
                LogUtils.e(TAG, "广告数据请求成功:" + response);
                AdvertiseBean advertiseBean = JsonUtil.getAdvertise(response);
                if (advertiseBean == null) {
                    return;
                }
                if (advertiseBean.getState().equals("0")) {//成功
                    imgList = advertiseBean.getImageList();
                    videoList = advertiseBean.getVideoList();
                    htmlList = advertiseBean.getHtmlList();
                    if (imgList != null) {
                        if (imgList.size() > 0) {
                            IMG_SIZE = imgList.size();
                            handler.postDelayed(new DownLoadRunnable(202), 2000);
                        }
                    }
                    if (videoList != null) {
                        if (videoList.size() > 0) {
                            VIDEO_SIZE = videoList.size();
                            handler.postDelayed(new DownLoadRunnable(203), 5000);
                        }
                    }
                    if (htmlList != null) {
                        if (htmlList.size() > 0) {
                            HTML_SIZE = htmlList.size();
                            handler.postDelayed(new DownLoadRunnable(204), 5000);
                        }
                    }
                } else {
                    LogUtils.e(TAG, "广告数据有误");
                }
                Log.e(TAG, "onResponse: " + advertiseBean.toString());
                break;
            case 202://下载图片、视频、h5文件
                //下载成功
                LogUtils.e(TAG, "onResponse:" + response);
                IMG++;
                handler.postDelayed(new DownLoadRunnable(202), 1000);
                break;
            case 203:
                VIDEO++;
                handler.postDelayed(new DownLoadRunnable(203), 1000);
                break;
            case 204:
                HTML++;
                handler.postDelayed(new DownLoadRunnable(204), 1000);
                break;
        }
    }

    @Override
    public void onFailure(int what, String error) {
        switch (what) {
            case 201://请求广告数据
                XUtilsManager.getInstance().getAsynHttp(201, this, BatteryCommend.httlUrl);
                break;
            case 202://下载图片
                handler.postDelayed(new DownLoadRunnable(202), 1000);
                break;
            case 203://下载视频
                handler.postDelayed(new DownLoadRunnable(203), 1000);
                break;
            case 204://下载h5文件
                handler.postDelayed(new DownLoadRunnable(204), 1000);
                break;
        }
        LogUtils.e(TAG, "onResponse:" + error);
    }
}