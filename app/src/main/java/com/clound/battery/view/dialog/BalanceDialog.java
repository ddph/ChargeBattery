package com.clound.battery.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.clound.battery.R;
import com.clound.battery.model.bean.BalanceBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * author cowards
 * created on 2018\10\17 0017
 * <p>
 * 刷卡弹出余额等信息
 **/
@ContentView(R.layout.dialog_situation)
public class BalanceDialog extends Dialog {

    private BalanceBean balanceBean;
    private Context mContext;
    @ViewInject(R.id.tv_cardno)
    public TextView mTvCardno;          //卡号
    @ViewInject(R.id.tv_balance)
    public TextView mTvBalance;         //余额
    @ViewInject(R.id.tv_cardno_type)
    public TextView mTvCardnoType;      //卡类型
    @ViewInject(R.id.tv_charger)
    public TextView mTvCharger;         //是否可充电
    @ViewInject(R.id.tv_battery_time)
    public TextView mTvBatteryTime;     //充电时间

    public BalanceDialog(Context context, BalanceBean bean) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.balanceBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        x.view().inject(this,null);
        setText();
    }

    /**
     * 设置显示内容
     */
    public void setText() {
        mTvCardno.setText(balanceBean.getCardNo());
        if (balanceBean.getCardNoType().equals("0001")) {
            mTvCardnoType.setText("标准卡");
        }
        if (balanceBean.getCardNoType().equals("0011")) {
            mTvCardnoType.setText("包月卡");
        }
        if (balanceBean.getCharger().equals("0")) {
            mTvCharger.setText("可充电");
        }
        if (balanceBean.getCharger().equals("1")) {
            mTvCharger.setText("不可充电");
        }
        mTvBatteryTime.setText(balanceBean.getBatteryTime() + "分钟");
        mTvBalance.setText(balanceBean.getBalance() + "元");
    }

    /**
     * 弹出
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * 关闭
     */
    @Override
    public void dismiss() {
        super.dismiss();
    }
}