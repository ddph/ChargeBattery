package com.clound.battery.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clound.battery.R;
import com.clound.battery.manager.ActManager;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.event.SettingSerialEvent;
import com.clound.battery.model.datapackaging.SerialDataUtil;
import com.clound.battery.model.manager.SerialManager;
import com.clound.battery.util.LogUtils;
import com.clound.battery.util.SaveDeviceMessageInfo;
import com.clound.battery.util.SharedPreferencesUtil;
import com.clound.battery.util.system.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import android_serialport_api.SerialPortFinder;

/**
 * author cowards
 * created on 2018\10\18 0018
 **/
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_select_device)
    public TextView mTvSelectDevice;       //选择串口
    @ViewInject(R.id.btn_open)
    public Button mBtnOpen;                //测试按钮
    @ViewInject(R.id.tv_log)
    public TextView mTvLog;                //界面log输出
    @ViewInject(R.id.ed_device)
    public EditText mEdDevice;             //ID输入框
    @ViewInject(R.id.btn_save)
    public Button mBtnSave;                //保存ID
    @ViewInject(R.id.btn_close_activity)
    public Button mBtnCloseActivity;       //关闭当前界面
    @ViewInject(R.id.btn_close_procedure)
    public Button mBtnCloseProcedure;      //退出程序
    @ViewInject(R.id.tv_version_code)
    public TextView mTvVersionCode;        //显示版本号

    private StringBuilder stringBuilder = new StringBuilder();  //记录输出日志信息
    private SerialPortFinder serialPortFinder;                  //获取所有串口节点
    private AlertDialog alertDialog;
    private String serial = null;                               //记录当前选择的串口

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        serialPortFinder = new SerialPortFinder();
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        mBtnOpen.setOnClickListener(this);
        mTvSelectDevice.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnCloseActivity.setOnClickListener(this);
        mBtnCloseProcedure.setOnClickListener(this);
        mEdDevice.setText(SaveDeviceMessageInfo.readDeviceId());
        mEdDevice.setSelection(mEdDevice.getText().toString().length());
        if (SerialManager.isSerialFlag) {//如果串口打开的话把串口名显示到界面上
            mTvSelectDevice.setText(SerialManager.serialDeviceName);
            stringBuilder.append("打开" + SerialManager.serialDeviceName + "成功" + "\n");
            mTvLog.setText(stringBuilder.toString());
        }
        mTvVersionCode.setText("版本号:" + SystemUtil.getVersionName());//显示当前程序版本号
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
                SerialManager.sendSerialData(BatteryCommend.SETTINGACT_CODE, SerialDataUtil.getOnSendSerialData("22", ""));
                break;
            case R.id.tv_select_device:
                openSerialDialog();
                break;
            case R.id.btn_save:
                saveDeviceId();
                break;
            case R.id.btn_close_activity:
                finish();
                break;
            case R.id.btn_close_procedure:
                ActManager.getAppManager().AppExit(SettingActivity.this);
                break;
        }
    }

    /**
     * 设置设备ID
     */
    private void saveDeviceId() {
        String device = mEdDevice.getText().toString().trim();
        if (TextUtils.isEmpty(device)) {
            Toast.makeText(this, "请输入设备ID", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isSave = SaveDeviceMessageInfo.saveDeviceId(device);//设置好之后保存到本地文件内
        if (isSave) {
            showToast("保存成功");
        } else {
            showToast("保存失败");
        }
    }

    /**
     * 选择并打开串口
     */
    public void openSerialDialog() {
        if (SerialManager.isSerialFlag) {
            showToast("串口已经打开");
        }
        try {
            if (serialPortFinder.getAllDevicesPath() == null) {
                showToast("当前设备没有串口");
                return;
            }
        } catch (NullPointerException e) {
            showToast("当前设备没有串口");
            return;
        }
        final String[] strings = serialPortFinder.getAllDevicesPath();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择串口");
        alertBuilder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                serial = strings[index];
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferencesUtil.setString("SerialDeviceName", serial);//   "/dev/ttyS2"
                mTvLog.setText(stringBuilder);
                alertDialog.dismiss();
                showToast("设置成功,重启生效");
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * 测试串口过来的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingEvent(SettingSerialEvent serialPortData) {
        LogUtils.e(TAG, "读取到串口的数据:" + serialPortData.getMsg());
        stringBuilder.append(serialPortData.getMsg() + "\n");
        mTvLog.setText(stringBuilder);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtils.e(TAG, "onDestroy...");
    }
}