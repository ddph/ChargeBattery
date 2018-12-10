package com.clound.battery.model.mina.coder;

import com.clound.battery.model.event.MessageModel;
import com.clound.battery.model.mina.kit.HexKit;
import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ReadDecoder extends CumulativeProtocolDecoder {
    /**
     * 服务端发过来的数据源
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        while (in.remaining() > 0) {
            //因为包头是2个字节，所以此处收到的数据包长度最小也得是2，此处判断收到的数据包长度小于2时，异常，返回false
            if (in.remaining() < 2) {
                return false;
            }
            in.mark();
            //1.截取并判断是否是【包头】部分
            byte[] headPart = new byte[2];
            in.get(headPart);
            //判断前两个字节是0X55和0xaa时，表示当前位置是一个新报文的开始时，继续往下截取报文剩余部分
            if (headPart[0] == (byte) 0x55 && headPart[1] == (byte) 0xaa) {
                //2.截取【包长度】部分
                //此处判断管道中剩余的内容长度是否满足要读取的长度，如果不满足，异常，返回false
                if (in.remaining() < 2) {
                    return false;
                }
                byte[] lengthPart = new byte[2];
                in.get(lengthPart);
                //将包长度转换为数字，表示整包长度的字节数
                int totalLength = HexKit.toInt(lengthPart);
                //此处判断读取到的包长度小于【包头】长度+【包长度】长度+【命令字】长度+【CRC】长度，或者管道中没有剩余的内容时，异常，返回false
                if (totalLength < (2 + 2 + 1 + 1) || !in.hasRemaining()) {
                    return false;
                }
                //3.截取【命令字】部分
                byte commandPart = in.get();
                //4.截取【命令内容】部分（命令内容总长度=整包长度-【包头】长度-【包长度】长度-【命令字】长度-【CRC】长度）
                byte[] commandBodyPart = new byte[totalLength - 2 - 2 - 1 - 1];

                if (commandBodyPart.length > in.remaining()) {
                    in.reset();
                    return false;
                } else {
                    in.get(commandBodyPart);
                    //5.截取最后的CRC校验位
                    byte crcPart = in.get();
                    //6.计算整个报文的累计和，并与CRC校验位的值进行匹配，以校验报文是否完整
                    ByteBuffer buffer = ByteBuffer.allocate(totalLength);
                    buffer.put(headPart).put(lengthPart).put(commandPart).put(commandBodyPart);
                    String crcLocal = HexKit.getHexSumForCRC(HexKit.bytes2Hex(buffer.array()));
                    String crcRemote = HexKit.bytes2Hex(new byte[]{crcPart});
                    if (!crcLocal.equalsIgnoreCase(crcRemote)) {
                        //校验和不相等时，不处理
                        return true;
                    }
                    //7.封装各部分数据
                    MessageModel message = new MessageModel();
                    message.setHead(HexKit.bytes2Hex(headPart));
                    message.setLength(HexKit.bytes2Hex(lengthPart));
                    message.setCommandKey(HexKit.bytes2Hex(commandPart));
                    message.setCommandValue(HexKit.bytes2Hex(commandBodyPart));
                    message.setCrc(HexKit.bytes2Hex(crcPart));
                    //向下传递交给各个协议处理类
                    out.write(message);
                    return true;
                }
            }
        }
        return false;
    }
}