package com.zhilai.pcb.utils;

public class NumberUtil {
	// 异或校验
	public static byte getXor(byte[] datas) {
		byte temp = datas[0];
		for (int i = 1; i < datas.length-1; i++) {
			temp ^= datas[i];
		}
		return temp;
	}

	// 使用1字节就可以表示b
	public static String numToHex(int b) {
		String num = String.format("%02x", b);// 2表示需要两个16进制数
		if(num.length()>2)
			num = num.substring(num.length()-2);
		return num;
	}
	
	//byte转int
	public final static int getInt(byte[] buf) {
		if (buf == null) {
			throw new IllegalArgumentException("byte array is null!");
		}
		if (buf.length > 4) {
			throw new IllegalArgumentException("byte array size > 4 !");
		}
		int r = 0;
		for (int i = 0; i < buf.length; i++) {
			r <<= 8;
			r |= (buf[i] & 0x000000ff);
		}
		return r;
	}
	
	//获取一个字节的位
	public static byte[] getBooleanArray(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;  
    }

    public static String byteToString(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<bytes.length;i++){
			sb.append((char) bytes[i]);
		}
		return sb.toString().trim();
	}

	public static String byteToHexString(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<bytes.length;i++){
			sb.append(NumberUtil.numToHex(bytes[i]));
			sb.append(" ");
		}
		return sb.toString().trim();
	}

}
