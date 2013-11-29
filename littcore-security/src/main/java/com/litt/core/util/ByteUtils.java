package com.litt.core.util;


/**
 * 
 * <b>标题：</b>字节操作.
 * <pre><b>描述:</b>
 *    二进制,字节数组,字符,十六进制,BCD,BIN
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-07-18
 * @version 1.0
 * 
 */
public final class ByteUtils 
{
	
	/** 隐藏构造函数，避免生成实例 */
	private ByteUtils(){};
	
	
	/** 
	 * 把16进制字符串转换成字节数组
	 * @param hex 16进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexStringToByteArray(String hex) 
	{
	    int len = (hex.length() / 2);
	    byte[] result = new byte[len];
	    char[] achar = hex.toCharArray();
	    for (int i = 0; i < len; i++) {
	     int pos = i * 2;
	     result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1]));
	    }
	    
	    return result;
	}
	

	/**
	 * 字符转byte.
	 * 
	 * @param c 字符
	 * 
	 * @return 字节
	 */
	public static byte charToByte(char c) 
	{
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);
	    return b;
	}
	
	/**
	 * 把字节数组转换成16进制字符串.
	 * 
	 * @param byteArray
	 *            字节数组
	 * 
	 * @return 16进制字符串(当高位值为0时，字符串中补0)
	 */
	public static String byteArrayToHexString(byte[] byteArray) {
	    StringBuffer sb = new StringBuffer(byteArray.length);
	    String sTemp;
	    for (int i = 0; i < byteArray.length; i++) {
	     sTemp = Integer.toHexString(0xFF & byteArray[i]);
	     if (sTemp.length() < 2)
	      sb.append(0);
	     sb.append(sTemp.toUpperCase());
	    }
	    return sb.toString();
	}

	/**
	 * 字节转换成字符串.
	 * 
	 * @param b 字节
	 * 
	 * @return 字符串
	 */
	public static String byteToString(byte b) 
	{
		byte high, low;
		byte maskHigh = (byte) 0xf0;
		byte maskLow = 0x0f;

		high = (byte) ((b & maskHigh) >> 4);
		low = (byte) (b & maskLow);

		StringBuffer buf = new StringBuffer();
		buf.append(findHex(high));
		buf.append(findHex(low));

		return buf.toString();
	}

	/**
	 * 字节转换为16进制字符.
	 * 
	 * @param b 字节
	 * 
	 * @return 16进制字符
	 */
	private static char findHex(byte b) {
		int t = new Byte(b).intValue();
		t = t < 0 ? t + 16 : t;

		if ((0 <= t) && (t <= 9)) {
			return (char) (t + '0');
		}

		return (char) (t - 10 + 'A');
	}    

	/**
	 * int转byte.
	 * 
	 * @param value
	 *            int型数值
	 * 
	 * @return byte[] 4字节数组
	 */
	public static byte[] intToByteArray(int value) 
	{
        byte[] b = new byte[4];
        //使用4个byte表示int
        for (int i = 0; i < 4; i++) 
        {
            int offset = (b.length - 1 - i) * 8;  // 偏移量
            b[i] = (byte) ((value >> offset) & 0xFF); //每次取8bit
        }
        return b;
    }


    /**
	 * 字节数组转INT.
	 * 
	 * @param byteArray
	 *            字节数组
	 * 
	 * @return int 数值
	 */   
	public static int byteArrayToInt(byte[] byteArray) 
	{
        int value = 0;
        for (int i = 0; i < byteArray.length; i++) 
        {
            int shift = (byteArray.length - 1 - i) * 8;
            value += (byteArray[i] & 0xFF) << shift;
        }
        return value;
    } 
	
	/**
	 * BCD码字节数组转为10进制.
	 * 
	 * @param byteArray
	 *            BCD码字节数组
	 * 
	 * @return 10进制
	 */
	public static int bcdByteArrayToInt(byte[] byteArray)
	{
	    StringBuffer temp = new StringBuffer(byteArray.length*2);
	    for(int i=0;i<byteArray.length;i++)
	    {
		     temp.append((byte)((byteArray[i]& 0xf0)>>>4));
		     temp.append((byte)(byteArray[i]& 0x0f));
	    }
	    return Integer.parseInt((temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString()));
	}
	
	/**
	 * BCD码字节数组转为10进制.
	 * 
	 * @param b 字节
	 * 
	 * @return 10进制数
	 */
	public static byte bcdToInt(byte b)
	{
		return (byte)(((b& 0xf0)>>>4) * 10 + (b& 0x0f));
	}

	
	/**
	 * 10进制转为BCD编码字节数组.
	 * 
	 * @param i
	 *            10进制数
	 * 
	 * @return BCD码字节数组
	 */
	public static byte[] intToBcdByteArray(int i) 
	{
	    String asc = String.valueOf(i);
		int len = asc.length();
	    int mod = len % 2;
	
	    if (mod != 0) {
	     asc = "0" + asc;
	     len = asc.length();
	    }
	
	    byte abt[] = new byte[len];
	    if (len >= 2) {
	     len = len / 2;
	    }
	
	    byte bbt[] = new byte[len];
	    abt = asc.getBytes();
	    int j, k;
	
	    for (int p = 0; p < asc.length()/2; p++) {
	     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
	      j = abt[2 * p] - '0';
	     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
	      j = abt[2 * p] - 'a' + 0x0a;
	     } else {
	      j = abt[2 * p] - 'A' + 0x0a;
	     }
	
	     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
	      k = abt[2 * p + 1] - '0';
	     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
	      k = abt[2 * p + 1] - 'a' + 0x0a;
	     }else {
	      k = abt[2 * p + 1] - 'A' + 0x0a;
	     }
	
	     int a = (j << 4) + k;
	     byte b = (byte) a;
	     bbt[p] = b;
	    }
	    return bbt;
	}
	
	/**
	 * 10进制转为BCD编码字节数组.
	 * 
	 * @param i
	 *            10进制数
	 * 
	 * @return BCD码字节数组
	 */
	public static byte[] byteToBcdByteArray(byte i) 
	{
	    String asc = String.valueOf(i);
		int len = asc.length();
	    int mod = len % 2;
	
	    if (mod != 0) {
	     asc = "0" + asc;
	     len = asc.length();
	    }
	
	    byte abt[] = new byte[len];
	    if (len >= 2) {
	     len = len / 2;
	    }
	
	    byte bbt[] = new byte[len];
	    abt = asc.getBytes();
	    int j, k;
	
	    for (int p = 0; p < asc.length()/2; p++) {
	     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
	      j = abt[2 * p] - '0';
	     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
	      j = abt[2 * p] - 'a' + 0x0a;
	     } else {
	      j = abt[2 * p] - 'A' + 0x0a;
	     }
	
	     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
	      k = abt[2 * p + 1] - '0';
	     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
	      k = abt[2 * p + 1] - 'a' + 0x0a;
	     }else {
	      k = abt[2 * p + 1] - 'A' + 0x0a;
	     }
	
	     int a = (j << 4) + k;
	     byte b = (byte) a;
	     bbt[p] = b;
	    }
	    return bbt;
	}	
	
	
	
	/**
	 * BCD字节数组转ASCII字节数组.
	 * 
	 * @param bcdByteArray
	 *            bcd编码的字节数组
	 * 
	 * @return ascii编码的字节数组
	 */
	public static byte[] bcdToAscii(byte[] bcdByteArray) 
	{
		byte[] ascByteArray = new byte[bcdByteArray.length * 2];

		for (int i = 0; i < bcdByteArray.length; i++) 
		{
			ascByteArray[i * 2] = (byte) ((bcdByteArray[i] >> 4) & 0x0f);
			ascByteArray[i * 2 + 1] = (byte) (bcdByteArray[i] & 0x0f);

		}
		return ascByteArray;
	} 

	/**
	 * 内置测试方法
	 * @param args
	 */
	public static void main(String[] args)
	{
		// System.out.println(ByteUtils.hexStringToByte("68H"));

		System.out.println(ByteUtils.byteToString((byte)0x68));	
		
		System.out.println(Integer.toBinaryString(68));
		System.out.println(Byte.parseByte("01000100", 2));
	}
	
}
