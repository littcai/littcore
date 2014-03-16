package com.litt.core.uid;



/**
 * 请求序列ID生成器.
 * 
 * <pre><b>描述：</b>
 * 注：SID值采用short型存储，仅用于分布式系统间并发请求的ID生成，不适用于高并发的请求.
 * 
 * 每次调用都生成一个唯一的SID。
 * 区别于一般唯一序号生成器的地方是序列号可复用，即调用了remove方法后该序列号下次能重复使用。因此序列号的体积也是非常小的。
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-3-26
 * @version 1.0
 */
public class SidGenerator
{
	
	/** The sid size. */
	short sidSize = (short)10000;
	
	/** The sid array. */
	byte[] sidArray = new byte[sidSize];
	
	private final static SidGenerator instance = new SidGenerator();
	
	/**
	 * 禁止外部生成实例类.
	 */
	private SidGenerator(){};
	
	public static short getSid()
	{
		return instance.generateSid();
	}
	
	
	/**
	 * 生成序列号.
	 * 
	 * @return the short
	 */
	public short generateSid()
	{		
		synchronized(sidArray)
		{ 
			
			for(short i=1;i<sidSize;i++)
			{
				if(sidArray[i]==0)
				{
					sidArray[i] = 1;
					return i;
				}
			}
			//如果数组满了，则扩展数组容量
			if(sidSize>=(Short.MAX_VALUE-10))	//如果超出容量，可能变为负数，重置
			{
				sidSize = 10000;
				sidArray = new byte[sidSize];
				return 1;
			}
			else 
			{
				byte[] newUidArray = new byte[sidSize+10];		
				System.arraycopy(sidArray, 0, newUidArray, 0, sidSize);		
				newUidArray[sidSize] = 1;
				short uid = sidSize; 
				sidSize += 10; 
				sidArray = newUidArray;
				return uid;
			}
			
		}
	}
	
	/**
	 * 重置SID，使其可以复用.
	 * @param sid 序列号
	 */
	public void resetSid(short sid)
	{
		synchronized(sidArray)
		{
			if(sid<sidArray.length)
				sidArray[sid] = 0;
		}
	}
	
	/**
	 * 重置全部SID
	 *
	 */
	public void resetAll()
	{
		synchronized(sidArray)
		{
			for(short i=0;i<sidArray.length;i++)
			{
				sidArray[i] = 0;
			}
		}
	}
	
	/**
	 * 移除已使用的SID.
	 * 
	 * @param sid 序列号
	 */
	public static void removeSid(short sid)
	{
		instance.resetSid(sid);
	}
	
	/**
	 * 移除已使用的SID.
	 */
	public static void removeAll()
	{
		instance.resetAll();
	}
	
	public static void main(String args[]) {
		for(int i=0;i<32770;i++)
			System.out.println(SidGenerator.getSid());
	}
	
}
