package com.clound.battery.view.adatper;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clound.battery.R;
import com.clound.battery.model.bean.HlBean;
import com.clound.battery.util.LogUtils;
import com.clound.battery.view.countdown.CountdownView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * author cowards
 * created on 2018\10\18 0018
 **/
public class GridViewAdapter extends BaseAdapter {
    public String TAG = getClass().getSimpleName();
    public Context context;
    public static volatile List<HlBean> hlBeanList = new ArrayList<>();
    private LayoutInflater inflater;

    public GridViewAdapter(Context cont) {
        this.context = cont;
        inflater = LayoutInflater.from(cont);
    }

    public void setDataAll(List<HlBean> list) {
        if (hlBeanList.size() >= 20) {
            hlBeanList.clear();
        }
        hlBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hlBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return hlBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHodler holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hl, null);
            holder = new ViewHodler(convertView);
            TextPaint tp = holder.mTvHlNumber.getPaint();
            tp.setFakeBoldText(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }
        HlBean bean = hlBeanList.get(position);
        LogUtils.e(TAG, "刷新界面:" + bean.toString());
        holder.mTvHlNumber.setText(bean.getHl_number());
        int state = Integer.parseInt(bean.getHl_state());
        //0：粘连故障  1：空闲     2：充电      3：断开故障（如继电器坏了，闭合不了）
        if (state == 1) {
            holder.mTvHlState.setVisibility(View.VISIBLE);
            holder.countdownView.setVisibility(View.GONE);
            holder.mLyHlBg.setBackgroundResource(R.mipmap.bg_leisure);
            holder.mTvHlState.setText("空闲");
            holder.countdownView.stop();
        } else if (state == 2) {
            holder.countdownView.setVisibility(View.VISIBLE);
            holder.mLyHlBg.setBackgroundResource(R.mipmap.bg_charge);
            holder.mTvHlState.setVisibility(View.GONE);
            if (bean.getHl_time() == null) {
            } else if (bean.getHl_time().equals("null")) {
            } else {
                holder.countdownView.start(Integer.parseInt(bean.getHl_time()) * 60 * 1000);
            }
        } else if (state == 0 || state == 3) {
            holder.mTvHlState.setVisibility(View.VISIBLE);
            holder.countdownView.setVisibility(View.GONE);
            holder.mLyHlBg.setBackgroundResource(R.mipmap.bg_leisure);
            holder.mTvHlState.setTextColor(Color.RED);
            holder.mTvHlState.setText("故障");
        }
        return convertView;
    }

    /**
     * 局部更新数据，调用一次getView()方法，刷新指定item
     *
     * @param position 要更新的位置
     */
    public void notifyItemChanged(int position, HlBean hlBean, View view) {
        hlBeanList.set(position, hlBean);
        getView(position, view, null);
    }

    class ViewHodler {
        @ViewInject(R.id.lv_hl_bg)
        public LinearLayout mLyHlBg;
        @ViewInject(R.id.tv_hl_number)
        public TextView mTvHlNumber;
        @ViewInject(R.id.tv_hl_state)
        public TextView mTvHlState;
        @ViewInject(R.id.cv_time)
        public CountdownView countdownView;

        public ViewHodler(View view) {
            x.view().inject(this,view);
        }
    }
}