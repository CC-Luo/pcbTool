package com.zhilai.pcb.protocol.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @ClassName ByteArrayCodecFactory
 * @Description
 * @Author zhouhang
 * @Date 2019/1/14
 * @Company 深圳市智莱科技股份有限公司
 */
public class ByteArrayCodecFactory implements ProtocolCodecFactory {
    private ByteArrayDecoder decoder;
    private ByteArrayEncoder encoder;

    public ByteArrayCodecFactory() {
        encoder = new ByteArrayEncoder();
        decoder = new ByteArrayDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
