package com.zhilai.pcb.utils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferWrapper;

/**
 * @ClassName ByteBufferTest
 * @Description
 * @Author zhouhang
 * @Date 2019/1/15
 * @Company 深圳市智莱科技股份有限公司
 */
public class ByteBufferTest extends IoBufferWrapper {

    public ByteBufferTest(IoBuffer buf) {
        super(buf);
    }

    public static void main(String[] args) {
        ByteBufferTest byteBufferTest = new ByteBufferTest(null);
    }
}
