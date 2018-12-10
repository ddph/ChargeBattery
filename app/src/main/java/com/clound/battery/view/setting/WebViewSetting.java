package com.clound.battery.view.setting;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.download.DownLoadFileUtil;

import java.util.List;

/**
 * author cowards
 * created on 2018\11\23 0023
 **/
public class WebViewSetting {

    public String TAG = getClass().getSimpleName();
    public static volatile WebViewSetting instance = null;
    private WebSettings mWebSet;

    public WebViewSetting() {
    }

    public static WebViewSetting getInstance(Context context) {
        if (instance == null) {
            synchronized (WebViewSetting.class) {
                if (instance == null) {
                    instance = new WebViewSetting();
                }
            }
        }
        return instance;
    }

    /**
     * 对webView进行设置
     */
    public void setWebView(WebView mWebViewBanner) {
        mWebViewBanner.clearCache(true);
        mWebViewBanner.clearHistory();
        mWebViewBanner.setDrawingCacheEnabled(true);
        mWebViewBanner.buildDrawingCache();
        mWebViewBanner.buildLayer();
        mWebSet = mWebViewBanner.getSettings();    //对webview的设置和配置
        mWebSet.setJavaScriptEnabled(true);         //设置支持JavaScript交互
        mWebSet.setSupportZoom(false);               //是否支持缩放
        mWebSet.setBuiltInZoomControls(true);       //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSet.setDisplayZoomControls(false);      //隐藏原生的缩放控件
        mWebSet.setCacheMode(mWebSet.LOAD_NO_CACHE);
        mWebSet.setDomStorageEnabled(true);     //时间未倒计时  加入这句话
        mWebSet.setAppCacheEnabled(false);
        mWebSet.setUseWideViewPort(true);           //将图片调整到适合webview的大小
        mWebSet.setLoadWithOverviewMode(true);      // 缩放至屏幕的大小
        mWebSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置webview自适应屏幕
        mWebSet.setJavaScriptCanOpenWindowsAutomatically(true);
        setLoadWebView(mWebViewBanner, Constant.PATH_HTML + "picvideo.html");
    }

    /**
     * 加载网页
     **/
    public void setLoadWebView(final WebView mWebViewBanner, final String uriHtml) {
        LogUtils.e(TAG, "加载的html文件:" + uriHtml);
        mWebSet.setMediaPlaybackRequiresUserGesture(false);//控制自动播放
        mWebViewBanner.loadUrl("file://" + uriHtml);
        mWebViewBanner.setWebViewClient(new WebViewClient() {
            /**
             * 页面开始加载前
             * */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 页面加载成功后
             * */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.e(TAG, "页面加载完成");
                setRenderIcon(mWebViewBanner);
            }
        });
    }

    /**
     * 加载资源文件
     */
    public void setRenderIcon(final WebView mWebViewBanner) {
        List<String> imgList = DownLoadFileUtil.getLocalFileName(Constant.PATH_IMAGE);
        String finalPic = "";
        if (imgList.size() > 0) {
            for (String uri : imgList) {
                finalPic = finalPic + "file://" + Constant.PATH_IMAGE + uri + ",";
            }
        }
        final String pic = finalPic;
        List<String> videoList = DownLoadFileUtil.getLocalFileName(Constant.PATH_VIDEO);
        String finalVideo = "";
        if (videoList.size() > 0) {
            for (String uri : videoList) {
                finalVideo = finalVideo + "file://" + Constant.PATH_VIDEO + uri + ",";
            }
        }
        final String video = finalVideo;
        mWebViewBanner.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.e(TAG, "传入图片参数  " + pic);
                LogUtils.e(TAG, "传入视频参数  " + video);
                //mWebViewBanner.loadUrl("javascript:setAd" + "('" + pic + "')");
                mWebViewBanner.loadUrl("javascript:setAd" + "('" + pic + "','" + video + "')");
            }
        });
    }
}