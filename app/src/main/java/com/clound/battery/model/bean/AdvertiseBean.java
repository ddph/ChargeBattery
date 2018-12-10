package com.clound.battery.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */
public class AdvertiseBean {

    /**
     * 请求状态标识
     * */
    private String state;

    /**
     * 广告图片集合
     **/
    private List<String> imageList;

    /**
     * 广告视频集合
     **/
    private List<String> videoList;

    /**
     * Html集合
     * */
    private List<String> htmlList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }

    public List<String> getHtmlList() {
        return htmlList;
    }

    public void setHtmlList(List<String> htmlList) {
        this.htmlList = htmlList;
    }

    @Override
    public String toString() {
        return "AdvertiseBean{" +
                "state='" + state + '\'' +
                ", imageList=" + imageList +
                ", videoList=" + videoList +
                ", htmlList=" + htmlList +
                '}';
    }
}