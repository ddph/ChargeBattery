package com.clound.battery.model.mina.coder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CodecFactory implements ProtocolCodecFactory{
	private final ReadDecoder readDecoder;
	private final Encoder encoder;
	
	public CodecFactory(){
		readDecoder = new ReadDecoder();
		encoder = new Encoder();
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return readDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
}