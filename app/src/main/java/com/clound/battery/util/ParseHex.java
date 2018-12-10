package com.clound.battery.util;

import android.content.Intent;

import com.clound.battery.model.bean.HlBean;
import com.clound.battery.view.adatper.GridViewAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * author cowards
 * created on 2018\10\19 0019
 **/
public class ParseHex {

    /**
     * 把子板回来的各回路状态封装成界面可显示的回路状态数据
     */
    public static List<HlBean> getHlStateListData(String data) {
        final List<HlBean> hlBeans = new ArrayList<>();
        String hlstate = data.substring(6, data.length() - 2);
        for (int i = 0; i < hlstate.length() / 4; i++) {
            String str = hlstate.substring(i * 4, (i + 1) * 4);
            HlBean hlBean = new HlBean();
            hlBean.setHl_number(ByteUtil.getHexToTen(str.substring(0, 2)));
            hlBean.setHl_state(str.substring(2, 4));
            hlBeans.add(hlBean);
        }
        return hlBeans;
    }

    /**
     * 把子板回来的各回路时间封装成界面可显示的回路电量数据
     */
    public static List<HlBean> getHlListBatteryListData(String data) {
        final List<HlBean> hlBeans = GridViewAdapter.hlBeanList;
        String hlstate = data.substring(6, data.length() - 2);
        for (int i = 0; i < hlstate.length() / 6; i++) {
            String str = hlstate.substring(i * 6, (i + 1) * 6);
            HlBean hlBean = GridViewAdapter.hlBeanList.get(i);
            String time = ByteUtil.getShort(str.substring(2, 6));
            hlBean.setHl_time(String.valueOf(ByteUtil.hexStringToAlgorism(time)));
            if (ByteUtil.hexStringToAlgorism(time) > 1) {
                hlBean.setHl_state("02");
            }
            hlBeans.set(i, hlBean);
        }
        return hlBeans;
    }

    /**
     * 把微信下发单个回路充电时长解析到界面
     */
    public static HlBean getHlItemBattery(String data) {
        if ((GridViewAdapter.hlBeanList.size() != 10) && (GridViewAdapter.hlBeanList.size() != 20)) {
            return null;
        }
        if ((Integer.parseInt(data.substring(0, 2)) - 1) > GridViewAdapter.hlBeanList.size()) {
            return null;
        }
        String hlstate = "02";//充电中
        HlBean hlBean = GridViewAdapter.hlBeanList.get(Integer.parseInt(data.substring(0, 2)));//拿到当前回路
        String time = data.substring(2, 6);//当前回路充电时间
        hlBean.setHl_time(String.valueOf(ByteUtil.hexStringToAlgorism(ByteUtil.getShort(time))));
        hlBean.setHl_state(hlstate);
        hlBean.setHl_number(ByteUtil.getHexToTen(data.substring(0, 2)));
        return hlBean;
    }

    public static HlBean getHlItemEndBattery(String data) {
        if ((GridViewAdapter.hlBeanList.size() != 10) && (GridViewAdapter.hlBeanList.size() != 20)) {
            return null;
        }
        String hlstate = "01";//充电结束
        String time = "0";
        BigInteger hl = new BigInteger(data, 16);
        HlBean hlBean = GridViewAdapter.hlBeanList.get(ByteUtil.hexStringToAlgorism(data));
        hlBean.setHl_state(hlstate);
        hlBean.setHl_time(String.valueOf(ByteUtil.hexStringToAlgorism(ByteUtil.getShort(time))));
        hlBean.setHl_number(String.valueOf(hl));
        return hlBean;
    }

    public static HlBean getHlItemFaultBattery(String data) {
        if ((GridViewAdapter.hlBeanList.size() != 10) && (GridViewAdapter.hlBeanList.size() != 20)) {
            return null;
        }
        String hlstate = "00";//回路故障
        String time = "0";
        BigInteger hl = new BigInteger(data, 16);
        HlBean hlBean = GridViewAdapter.hlBeanList.get(ByteUtil.hexStringToAlgorism(data));
        hlBean.setHl_state(hlstate);
        hlBean.setHl_time(String.valueOf(ByteUtil.hexStringToAlgorism(ByteUtil.getShort(time))));
        hlBean.setHl_number(String.valueOf(hl));
        return hlBean;
    }
}