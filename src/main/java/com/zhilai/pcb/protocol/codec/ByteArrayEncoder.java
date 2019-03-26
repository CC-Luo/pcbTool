package com.zhilai.pcb.protocol.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * @ClassName ByteArrayEncoder
 * @Description 编码器
 * @Author zhouhang
 * @Date 2019/1/14
 * @Company 深圳市智莱科技股份有限公司
 */
public class ByteArrayEncoder extends ProtocolEncoderAdapter {
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        byte[] returnBytes = (byte[]) message;
        //根据报文长度开辟空间
        IoBuffer buff = IoBuffer.allocate(returnBytes.length);
        //设置为可自动扩展空间
        buff.setAutoExpand(true);
        //将报文中的信息添加到buff中
        buff.put(returnBytes);
        buff.flip();
        //将报文发送出去
        out.write(buff);
    }
}
