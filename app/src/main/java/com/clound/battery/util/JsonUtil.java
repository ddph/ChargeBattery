package com.clound.battery.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.clound.battery.model.bean.AdvertiseBean;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static String TAG = "JsonUtil";
    public static JSONObject jsonObject = null;
    public static JSONArray jsonArray = null;

    /**
     * 获取广告信息
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static AdvertiseBean getAdvertise(String result) {
        AdvertiseBean advertiseBean = new AdvertiseBean();
        if (result == null || result.equals(null) || result.equals("") || result == "") {
            return null;
        } else {
            List<String> imgList = new ArrayList<>();
            List<String> videoList = new ArrayList<>();
            List<String> htmlList = new ArrayList<>();
            try {
                JSONObject jsonObject = JSONObject.parseObject(result);
                advertiseBean.setState(jsonObject.getString("state"));
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray arrayImage = data.getJSONArray("image");
                JSONArray arrayVideo = data.getJSONArray("video");
                JSONArray arrayHtml = data.getJSONArray("html");
                if (arrayImage.size() > 0) {
                    for (Object o : arrayImage) {
                        imgList.add(String.valueOf(o));
                    }
                    advertiseBean.setImageList(imgList);
                }
                if (arrayVideo.size() > 0) {
                    for (Object o : arrayVideo) {
                        videoList.add(String.valueOf(o));
                    }
                    advertiseBean.setVideoList(videoList);
                }
                if (arrayHtml.size() > 0) {
                    for (Object o : arrayHtml) {
                        htmlList.add(String.valueOf(o));
                    }
                    advertiseBean.setHtmlList(htmlList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "解析异常:" + e.toString());
            }
        }
        return advertiseBean;
    }
}