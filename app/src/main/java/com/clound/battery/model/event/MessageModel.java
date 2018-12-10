package com.clound.battery.model.event;

import java.io.Serializable;

/**
 * 接收和发送的报文模型<br/>
 * 数据包结构：<br/>
 * 头标识：固定值55AA，2个字节<br/>
 * 数据长度：所有内容（包含CRC）的字节总长度，2个字节<br/>
 * 命令字：1个字节<br/>
 * 命令内容：根据具体协议而定<br/>
 * CRC，前边所有内容16进制累加和，1个字节
 *
 * @author cowards
 */
public class MessageModel implements Serializable {
    private static final long serialVersionUID = 1L;
    //接收到的原始消息内容
    private String message;
    //包头部分
    private String head;
    //包长度部分
    private String length;
    //命令字部分
    private String commandKey;
    //命令内容部分
    private String commandValue;
    //CRC部分
    private String crc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCommandKey() {
        return commandKey;
    }

    public void setCommandKey(String commandKey) {
        this.commandKey = commandKey;
    }

    public String getCommandValue() {
        return commandValue;
    }

    public void setCommandValue(String commandValue) {
        this.commandValue = commandValue;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                ", head='" + head + '\'' +
                ", length='" + length + '\'' +
                ", commandKey='" + commandKey + '\'' +
                ", commandValue='" + commandValue + '\'' +
                ", crc='" + crc + '\'' +
                '}';
    }
}