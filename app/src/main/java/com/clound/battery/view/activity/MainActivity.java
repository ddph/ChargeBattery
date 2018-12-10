package com.clound.battery.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.clound.battery.R;
import com.clound.battery.application.BatteryApp;
import com.clound.battery.model.bean.HlBean;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.SerialDataUtil;
import com.clound.battery.model.event.BalanceEvent;
import com.clound.battery.model.event.HlEvent;
import com.clound.battery.model.event.MinaActEvent;
import com.clound.battery.model.event.SerialHlStateEvent;
import com.clound.battery.model.manager.SerialManager;
import com.clound.battery.util.Constant;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.ParseHex;
import com.clound.battery.view.adatper.GridViewAdapter;
import com.clound.battery.view.dialog.BalanceDialog;
import com.clound.battery.view.setting.WebViewSetting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnLongClickListener, View.OnTouchListener {

    @ViewInject(R.id.webView)
    public WebView mWebViewBanner;
    @ViewInject(R.id.ll_splash)
    public LinearLayout mLlSplash;//初始化页
    @ViewInject(R.id.gridView)
    public GridView gridView;
    @ViewInject(R.id.imgView)
    public ImageView mImgView;
    @ViewInject(R.id.ll_main)
    public LinearLayout mLlMain;//电桩状态界面
    @ViewInject(R.id.ll_hint)
    public LinearLayout mLlHint;//点击这里跳转到设置界面
    @ViewInject(R.id.tv_rates)
    public TextView mTvRates;

    private GridViewAdapter gridViewAdapter;
    private BalanceDialog balanceDialog = null;
    private int DIALOG_WHAT = 101;
    private int SERIAL_WHAT = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        gridView.setOnTouchListener(this);
        mLlHint.setOnLongClickListener(this);
        gridViewAdapter = new GridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);
        WebViewSetting.getInstance(MainActivity.this).setWebView(mWebViewBanner);//设置WebView
        TextPaint tp = mTvRates.getPaint();
        tp.setFakeBoldText(true);
        mTvRates.setText("收费标准暂未更新,请耐心等待...");
    }

    /**
     * 开启设备后收到所有回路状态处理并展示
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSerialHlStateEvent(SerialHlStateEvent serialHlStateEvent) {
        String data = serialHlStateEvent.serialData;
        switch (serialHlStateEvent.code) {
            case "22":
                /**
                 * 数据初始化完成之后显示主layout,隐藏初始化layout
                 * */
                LogUtils.e(TAG, "回路状态:" + data);
                final List<HlBean> hlBeanStates = ParseHex.getHlStateListData(data);
                /** 更新界面 **/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLlSplash.setVisibility(View.GONE);
                        mLlMain.setVisibility(View.VISIBLE);
                        gridView.setNumColumns(hlBeanStates.size() / (hlBeanStates.size() / 5));
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) gridView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
                        if (hlBeanStates.size() == 10) {
                            linearParams.height = 260;// 控件的宽强制设成260
                        }
                        if (hlBeanStates.size() == 20) {
                            linearParams.height = 520;// 控件的宽强制设成520
                        }
                        gridView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        gridViewAdapter.setDataAll(hlBeanStates);
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(hlBeanStates.size() / 2 * 1000);
                            SerialManager.sendSerialData(BatteryCommend.MAIN_CODE, SerialDataUtil.getOnSendSerialData("44", ""));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case "44":
                /**
                 * 刷新界面回路时间
                 * */
                LogUtils.e(TAG, "查询所有回路时间:" + data);
                //33(包头) 07(长度) 10(命令字) 01(按时间) 2C10(时间) 87
                List<HlBean> hlBeanTimes = ParseHex.getHlListBatteryListData(data);
                if ((hlBeanTimes != null) && (hlBeanTimes.size() > 0)) {
                    for (int index = 0; index < hlBeanTimes.size(); index++) {
                        final HlBean hlBean = hlBeanTimes.get(index);
                        if ((hlBean.getHl_time() != null) && (!hlBean.getHl_time().equals("null")) && (!hlBean.getHl_time().equals(""))) {
                            if (Integer.parseInt(hlBean.getHl_time()) > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.e(TAG, "刷新界面当前回路时间:" + hlBean.getHl_time());
                                        View view = gridView.getChildAt(Integer.parseInt(hlBean.getHl_number()));
                                        gridViewAdapter.notifyItemChanged(Integer.parseInt(hlBean.getHl_number()), hlBean, view);//刷新单个回路状态
                                    }
                                });
                            }
                        }
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onHlEvent(final HlEvent hlEvent) {
        if (hlEvent.hlBean != null) {
            LogUtils.e(TAG, "onHlEvent: " + hlEvent.hlBean.toString());
            if ((gridViewAdapter != null) && (gridViewAdapter.hlBeanList != null)) {
                if ((gridViewAdapter.hlBeanList.size() == 10) || (gridViewAdapter.hlBeanList.size() == 20)) {
                    if (gridViewAdapter.hlBeanList.size() > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View view = gridView.getChildAt(Integer.parseInt(hlEvent.hlBean.getHl_number()));
                                gridViewAdapter.notifyItemChanged(Integer.parseInt(hlEvent.hlBean.getHl_number()), hlEvent.hlBean, view);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 服务端数据回调到这里
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMinaActEvent(MinaActEvent minaActEvent) {
        switch (minaActEvent.code) {
            case 200://网络连接成功,切换二维码图标
                Log.e(TAG, "onMinaActEvent: 网络连接成功,显示该设备的二维码");
                ImageOptions imageOptions = new ImageOptions.Builder()
                        .setCircular(false)
                        .setFadeIn(true)
                        .setFailureDrawableId(R.mipmap.network_fail)
                        .build(); //淡入效果
                x.image().bind(mImgView, "file:" + minaActEvent.minaData, imageOptions);
                break;
            case 400://网络连接失败,切换网络断开提示
                mImgView.setImageResource(R.mipmap.network_fail);
                break;
            case 201:
            case 202:
            case 203:
            case 204:
                WebViewSetting.getInstance(MainActivity.this).setLoadWebView(mWebViewBanner, Constant.PATH_HTML + "index.html");
                break;
            case 205://更新费率
                String[] strings = minaActEvent.minaData.split(",");
                String rateData = "";
                for (int size = 0; size < strings.length; size += 2) {
                    if (size == (strings.length - 2)) {
                        rateData = rateData + strings[size] + "W:每小时" + strings[size + 1] + "元";
                        break;
                    }
                    rateData = rateData + strings[size] + "W:每小时" + strings[size + 1] + "元\n";
                }
                mTvRates.setText(rateData);
                break;
            default:
                break;
        }
    }

    /**
     * 查询余额显示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBalanceEvent(BalanceEvent event) {
        if (balanceDialog != null) {
            balanceDialog.dismiss();
            balanceDialog = null;
        }
        balanceDialog = new BalanceDialog(MainActivity.this, event.balanceBean);
        balanceDialog.show();
        handler.sendEmptyMessageDelayed(DIALOG_WHAT, 1500);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101://关闭余额弹窗
                    if (balanceDialog != null) {
                        balanceDialog.dismiss();
                        balanceDialog = null;
                    }
                    break;
                case 102:
                    if (SerialManager.isSerialFlag) {
                        SerialManager.sendSerialData(BatteryCommend.MAIN_CODE, SerialDataUtil.getOnSendSerialData("22", ""));
                    } else {
                        handler.sendEmptyMessageDelayed(SERIAL_WHAT, 3000);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.ll_hint://跳转至设置界面
                startActivity(SettingActivity.getIntent(MainActivity.this));
                break;
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.gridView:
                return MotionEvent.ACTION_MOVE == motionEvent.getAction() ? true//禁止滑动条上下滚动
                        : false;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(SERIAL_WHAT, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy...");
        EventBus.getDefault().unregister(this);
        if (BatteryApp.serialService != null) {
            stopService(new Intent(MainActivity.this, BatteryApp.serialService.getClass()));
        }
        if (BatteryApp.minaService != null) {
            stopService(new Intent(MainActivity.this, BatteryApp.minaService.getClass()));
        }
    }
}