package com.litt.core.util;

import java.nio.ByteBuffer;

import com.litt.core.common.Utility;

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
	/**
     * The high digits lookup table.
     */
    private static final byte[] highDigits;

    /**
     * The low digits lookup table.
     */
    private static final byte[] lowDigits;

    /**
     * Initialize lookup tables.
     */
    static {
        final byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };

        int i;
        byte[] high = new byte[256];
        byte[] low = new byte[256];

        for (i = 0; i < 256; i++) {
            high[i] = digits[i >>> 4];
            low[i] = digits[i & 0x0F];
        }

        highDigits = high;
        lowDigits = low;
    }
	
	
	/** 隐藏构造函数，避免生成实例 */
	private ByteUtils(){};
	
	/**
	 * 二进制字符串转字节数组.
	 * 
	 * @param binary
	 *            二进制字符串
	 * 
	 * @return 字节数组
	 */
	public static byte[] binaryStringToByteArray(String binary)
	{
		String[] strArray = Utility.splitStringAll(binary, 8);
		byte[] ret = new byte[strArray.length];
		for(int i=0;i<strArray.length;i++)
		{
			ret[i] = Byte.parseByte(strArray[i],2);
		}
		return ret;
	}
	
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
	public static String toHexString(byte[] byteArray) 
	{
		int size = byteArray.length;
		if(size==0)
			return "";
	    StringBuffer out = new StringBuffer(size);
	    //fill first
	    int byteValue = byteArray[0] & 0xFF;
        out.append((char) highDigits[byteValue]);
        out.append((char) lowDigits[byteValue]);        
	    
	    for (int i = 1; i < size; i++) 
	    {	    	 
	    	 byteValue = byteArray[i] & 0xFF;
	         out.append((char) highDigits[byteValue]);
	         out.append((char) lowDigits[byteValue]);
	            
	    }
	    return out.toString();
	}

	/**
	 * 把字节数组转换成16进制字符串.
	 * 
	 * @param byteArray
	 *            字节数组
	 * 
	 * @return 16进制字符串(当高位值为0时，字符串中补0)
	 */
	public static String toHexString(ByteBuffer buffer) 
	{		
		StringBuffer out = new StringBuffer(buffer.remaining() * 3 - 1);
		int mark = buffer.position();
		buffer.flip();	//跳回开始位置
		int size = buffer.remaining();
		if(size==0)
			return "";
		//fill the first
        int byteValue = buffer.get() & 0xFF;
        out.append((char) highDigits[byteValue]);
        out.append((char) lowDigits[byteValue]);
        size--;

        // and the others, too
        for (; size > 0; size--) 
        {           
            byteValue = buffer.get() & 0xFF;
            out.append((char) highDigits[byteValue]);
            out.append((char) lowDigits[byteValue]);
        }

        buffer.position(mark);

        return out.toString();
	}
	

	/**
	 * 字节转换成16进制字符串.
	 * 
	 * @param b 字节
	 * 
	 * @return 16进制字符串
	 */
	public static String toHexString(byte b) 
	{
		int byteValue = b & 0xFF;
		
		StringBuffer out = new StringBuffer();
		out.append((char) highDigits[byteValue]);
		out.append((char) lowDigits[byteValue]);

		return out.toString();
	}
	
	/**
     * Dumps an {@link IoBuffer} to a hex formatted string.
     * 
     * @param in the buffer to dump
     * @param lengthLimit the limit at which hex dumping will stop
     * @return a hex formatted string representation of the <i>in</i> {@link Iobuffer}.
     */
    public static String getHexdump(byte[] data) 
    {
    	if (data==null || data.length == 0) {
            return "";
        }
        int size = data.length;        
        
        StringBuffer out = new StringBuffer(size * 3 - 1);
        // fill the first
        int byteValue = data[0] & 0xFF;
        out.append((char) highDigits[byteValue]);
        out.append((char) lowDigits[byteValue]);


        // and the others, too
        for (int i=1;i<size; i++) {
            out.append(' ');
            byteValue = data[i] & 0xFF;
            out.append((char) highDigits[byteValue]);
            out.append((char) lowDigits[byteValue]);
        }
        return out.toString();
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
	 * 字节数组转LONG.
	 * 
	 * @param byteArray
	 *            字节数组
	 * 
	 * @return int 数值
	 */   
	public static long byteArrayToLong(byte[] byteArray) 
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
	 * BCD码二进制字符串转换成十进制.
	 * 
	 * @param bcdString
	 *            BCD编码二进制字符串
	 * 
	 * @return 十进制
	 */   
    public static int bcdStringToInt(String bcdString) 
    {    
        StringBuffer ret = new StringBuffer(4);    
        
        int lengthBCD = bcdString.length();    
        if (lengthBCD % 4 != 0) 
           throw new IllegalArgumentException("指定的编码不是BCD码！");    
        String[] bcdArray = Utility.splitStringAll(bcdString, 4);
        for(int i=0;i<bcdArray.length;i++)
        {
        	int v = Integer.parseInt(bcdArray[i], 2);   
        	if (v > 9) 
                throw new IllegalArgumentException("不是8421 BCD码,请检查");
        	ret.append(v);       
        }        
        return Integer.parseInt(ret.toString());    
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

	    if (len >= 2) {
	     len = len / 2;
	    }
	
	    byte[] bbt = new byte[len];
	    byte[] abt = asc.getBytes();
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

	    if (len >= 2) {
	     len = len / 2;
	    }
	
	    byte bbt[] = new byte[len];
	    byte[] abt = asc.getBytes();
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
	 * ByteBuffer扩容.
	 * 
	 * @param src 原缓冲区
	 * @param size 追加大小
	 * 
	 * @return ByteBuffer
	 */
	public static ByteBuffer extend(ByteBuffer src,int size)
	{
		int remain = src.remaining();		
		if(remain<size)
		{
			int newCapacity = src.capacity() + size - remain;
			
			ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
			src.flip();			
			newBuffer.put(src);			
			return newBuffer;
		}
		else
		{			
			return src;
		}		
	}	
	
	/**
	 * 追加ByteBuffer.
	 * @param src 原缓冲区
	 * @param add 新缓冲区
	 * @return ByteBuffer
	 */
	public static ByteBuffer append(ByteBuffer src,ByteBuffer add)
	{
		int remain = src.remaining();
		int addCapacity = add.capacity();
		if(remain<addCapacity)
		{
			int newCapacity = src.capacity() + addCapacity - remain;
			
			ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
			src.flip();
			add.flip();
			newBuffer.put(src);
			newBuffer.put(add);
			return newBuffer;
		}
		else
		{	
			add.flip();
			src.put(add);
			return src;
		}
		
	}
	
    /**
     * 截取字节某几位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param begin 开始位置（最高位计7）
     * @param end 结束位置
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(byte b,int begin,int end)
    {
    	if(begin>7||end<0||end>begin)
    		throw new IllegalArgumentException("起始位置和结束位置有误！");
    	
    	//byte在位移时将转换为int型处理
    	if(b==0)
    		return 0;
    	else
    	{
//    		int and = 0;
//    		for(int i=begin;i>=end;i--)
//    		{
//    			and += 1 << i;	    			
//    		}
    		int and = (2 << begin) - (1 << end);	//优化后的算法，不用循环     		
    		return (b & and) >>> end;  	
    	}    	
    }
    
    /**
     * 截取字节某几位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param begin 开始位置（最高位计7）
     * @param end 结束位置
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(int b,int begin,int end)
    {
    	if(begin>31||end<0||end>begin)
    		throw new IllegalArgumentException("起始位置和结束位置有误！");
    	if(b==0)
    		return 0;
    	else
    	{
    		int and = (2 << begin) - (1 << end);	//优化后的算法，不用循环     		
    		return (b & and) >>> end;	    	
    	}    	
    }   
    
    /**
     * 截取字节某几位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param begin 开始位置（最高位计7）
     * @param end 结束位置
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(short b,int begin,int end)
    {
    	if(begin>31||end<0||end>begin)
    		throw new IllegalArgumentException("起始位置和结束位置有误！");
    	if(b==0)
    		return 0;
    	else
    	{
    		int and = (2 << begin) - (1 << end);	//优化后的算法，不用循环     		
    		return (b & and) >>> end;	    	
    	}    	
    }      
    
    /**
     * 截取字节某1位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param index 索引位置（最高位计7）
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(byte b,int index)
    {
    	return getBitsValue(b, index, index);
    }
    
    /**
     * 截取字节某1位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param index 索引位置（最高位计7）
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(int b,int index)
    {
    	return getBitsValue(b, index, index);
    } 
   
    /**
     * 截取字节某1位的值.
     * 主要用于1个字节转换后的二进制代码每一位或几位代表一个业务含义。
     * 如截取字节值为5，二进制为0000 0101，其中从第2位到第1位在一起代表一个值，截取后为10，转换整型后为2
     * 
     * @param b 字节
     * @param index 索引位置（最高位计7）
     * 
     * @return 截取后的值
     */
    public static int getBitsValue(short b,int index)
    {
    	return getBitsValue(b, index, index);
    }  

	/**
	 * 内置测试方法
	 * @param args
	 */
	public static void main(String[] args)
	{
		// System.out.println(ByteUtils.hexStringToByte("68H"));

//		System.out.println(ByteUtils.byteToString((byte)0x68));	
//		
//		System.out.println(Integer.toBinaryString(68));
//		System.out.println(Byte.parseByte("01000100", 2));
		
//		ByteBuffer src = ByteBuffer.allocate(5);
//		src.putInt(1);
//		ByteBuffer add = ByteBuffer.allocate(1);
//		add.put((byte)9);
//		byte[] target = ByteUtils.append(src, add).array();
//		System.out.println(target.length);
//		System.out.println(target[4]);
		
		ByteBuffer abc = ByteBuffer.allocate(6);
		abc.put((byte)0x2A);
		abc.put((byte)0x17);

		abc.put((byte)0x1C);

		abc.put((byte)0x07);

		abc.put((byte)0x09);
		abc.put((byte)0x90);
		
		
//		System.out.println(ByteUtils.toHexString(abc));
//		System.out.println(ByteUtils.toHexString(new byte[]{11,12}));
//		System.out.println(ByteUtils.toHexString((byte)11));
		System.out.println(ByteUtils.byteArrayToLong(abc.array()));		
		//System.out.println(ByteUtils.hexStringToDisplay(ByteUtils.toHexString(abc)));
		System.out.println(ByteUtils.getBitsValue(1228, 10));
		
	}
	
}
