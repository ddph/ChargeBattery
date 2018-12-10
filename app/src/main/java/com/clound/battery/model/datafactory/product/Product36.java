package com.clound.battery.model.datafactory.product;

import com.clound.battery.model.bean.BalanceBean;
import com.clound.battery.model.commend.BatteryCommend;
import com.clound.battery.model.datapackaging.MinaDataUtil;
import com.clound.battery.model.datapackaging.SerialDataUtil;
import com.clound.battery.model.event.BalanceEvent;
import com.clound.battery.model.manager.MinaManager;
import com.clound.battery.model.manager.SerialManager;
import com.clound.battery.util.ByteUtil;
import com.clound.battery.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * author cowards
 * created on 2018\11\26 0026
 **/
public class Product36 extends BaseProduct {

    @Override
    public void proceSerial(String data) {
        LogUtils.e(TAG, "proceSerial: " + data);
        String cardNo = data.substring(6, 14);
        MinaManager.getmInstance().writeToServer(MinaDataUtil.ClintGoToServer("36", cardNo));
    }

    @Override
    public void proceServer(String data) {
        BalanceBean balanceBean = new BalanceBean();
        LogUtils.e(TAG, "proceServer: " + data);
        String cardNoCode = String.valueOf(ByteUtil.hexStringToAlgorism(data.substring(2, 4)));//返回码
        LogUtils.e(TAG, "36...返回码:" + cardNoCode);
        String cardNo = data.substring(4, 12);//卡号
        LogUtils.e(TAG, "36...卡号:" + cardNo);
        String balance = String.valueOf(ByteUtil.hexStringToAlgorism(ByteUtil.getBalanceShort(data.substring(12, 18))) / 100.00);//   普通卡:余额/月卡:次数
        LogUtils.e(TAG, "36...余额:" + balance);
        String cardNoType = String.valueOf(ByteUtil.hexStringToAlgorism(data.substring(18, 20)));//充电卡类型
        LogUtils.e(TAG, "36...返回充电卡类型码:" + cardNoType);
        cardNoType = ByteUtil.toBinaryString(cardNoType);
        String charger = cardNoType.substring(3, 4);
        cardNoType = cardNoType.substring(4, 8);
        String batteryTime = String.valueOf(ByteUtil.hexStringToAlgorism(ByteUtil.getShort(data.substring(20, 24))));//充电时间
        balanceBean.setBalance(balance);
        balanceBean.setCardNo(cardNo);
        balanceBean.setCharger(charger);
        balanceBean.setCardNoType(cardNoType);
        balanceBean.setBatteryTime(batteryTime);
        EventBus.getDefault().post(new BalanceEvent(balanceBean));
        LogUtils.e(TAG, "36...充电时间:" + batteryTime);
        SerialManager.sendSerialData(BatteryCommend.MINADATA_CODE,
                SerialDataUtil.getOnSendSerialDataBack("36",
                        cardNo + data.substring(12, 18) + data.substring(18, 20) + data.substring(20, 24)));
    }
}