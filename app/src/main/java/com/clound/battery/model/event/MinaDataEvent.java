package com.clound.battery.model.event;

/**
 * author cowards
 * created on 2018\10\22 0022
 * 接收到的服务端数据ReceiveHandler发送到MinaData
 **/
public class MinaDataEvent {
    public String minaData;

    public MinaDataEvent(String minaData){
        this.minaData = minaData;
    }

}