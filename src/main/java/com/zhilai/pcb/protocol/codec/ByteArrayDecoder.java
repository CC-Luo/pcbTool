package com.zhilai.pcb.protocol.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @ClassName ByteArrayDecoder
 * @Description
 * @Author zhouhang
 * @Date 2019/1/14
 * @Company 深圳市智莱科技股份有限公司
 */
public class ByteArrayDecoder extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        /*//记录初始位置
        int start = in.position();
        //获取第一个byte
        byte current = in.get();
        if(0x4F == current){
            if(in.remaining() < 5){
                in.position(in.limit());
                return false;
            }else{
                //消息内容足够
                in.position(start);// 重置恢复position位置到操作前
                writeBuffer(6,in,out);
                return true;
            }

        }else if(0x50 == current){
            if(in.remaining() < 13){
                in.position(start);
                return false;
            }else{
                in.position(start);
                int length = 0;
                if(0x41==in.get(13)){
                    length = 16;
                }else{
                    length = 14;
                }
                writeBuffer(length,in,out);
            }
        }else if(0x47 == current){
            if(in.remaining() < 20){
                in.position(start);
                return false;
            }else{
                in.position(start);
                writeBuffer(21,in,out);
            }
        }

        in.position(start+1);
        //这个地方要不要写死一个标识，作为格式出错后传到业务层处理
        return false;// 处理成功，让父类进行接收下个包*/
        writeBuffer(in,out);
        return true;
    }

    public void writeBuffer(IoBuffer in, ProtocolDecoderOutput out){
        int length = in.limit();
        byte[] packArr = new byte[length];
        in.get(packArr, 0, length);

        IoBuffer buffer = IoBuffer.allocate(length);
        buffer.put(packArr);
        buffer.flip();
        out.write(buffer);
        buffer.free();
    }
}
