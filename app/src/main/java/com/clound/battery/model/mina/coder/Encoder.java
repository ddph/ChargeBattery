package com.clound.battery.model.mina.coder;

import com.clound.battery.model.mina.kit.HexKit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class Encoder extends ProtocolEncoderAdapter {
    private String TAG = getClass().getSimpleName();

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        //传输的二进制信息都在IoBuffer中。
        IoBuffer buf = IoBuffer.allocate(800).setAutoExpand(true);
        //message是一个完整的16进制包
        byte[] content = HexKit.hex2Bytes(message.toString());
        buf.put(content);
        buf.flip();
        out.write(buf);
    }
}