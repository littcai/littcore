package com.littcore.util;

import java.math.BigDecimal;
import java.util.Arrays;

import com.littcore.common.Utility;

/** 
 * 
 * 数组操作辅助类.
 * 
 * <pre><b>描述：</b>
 *    增强ArrayUtils的一些辅助函数.
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2010-12-02 v1.1 
 *    	1、增加字符串数组直接转其他类型数组的方法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-11
 * @version 1.0,1,1
 *
 */
public class ArrayUtils extends org.apache.commons.lang.ArrayUtils
{
	
	/**
	 * 将一个数组中的所有元素填充为指定值.
	 * 
	 * @param array 字符串数组
	 * @param content 内容
	 * 
	 * @return 类型为T的数组
	 */
	public static <T> T[] fillWith(T[] array, T content)
	{
		if(array==null)
			return array;
		for(int i=0;i<array.length;i++)
		{
			array[i] = content;
		}
		return array;
	}
	
	/**
	 * 字符串数组转Long数组.
	 * 转换失败则对应位置置NULL
	 * 
	 * @param array 字符串数组
	 * @return Long数组
	 */
	public static Long[] toLong(String[] array)
	{		
		if(array==null || array.length==0)
			return new Long[0];
		Long[] ret = new Long[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseLong(array[i], null);
		}
		return ret;
	}
	
	/**
	 * 字符串数组转long数组.
	 * 转换失败则对应位置置0
	 * 
	 * @param array 字符串数组
	 * @return long数组
	 */
	public static long[] toLongPrimitive(String[] array)
	{		
		if(array==null || array.length==0)
			return new long[0];
		long[] ret = new long[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseLong(array[i]);
		}
		return ret;
	}
	
	/**
	 * 字符串数组转Integer数组.
	 * 转换失败则对应位置置NULL
	 * 
	 * @param array 字符串数组
	 * @return Integer数组
	 */
	public static Integer[] toInteger(String[] array)
	{		
		if(array==null || array.length==0)
			return new Integer[0];
		Integer[] ret = new Integer[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseInt(array[i], null);
		}
		return ret;
	}
	
	/**
	 * 字符串数组转int数组.
	 * 转换失败则对应位置置0
	 * 
	 * @param array 字符串数组
	 * @return int数组
	 */
	public static int[] toIntPrimitive(String[] array)
	{		
		if(array==null || array.length==0)
			return new int[0];
		int[] ret = new int[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseInt(array[i]);
		}
		return ret;
	}	
	
	/**
	 * 字符串数组转Short数组.
	 * 转换失败则对应位置置NULL
	 * 
	 * @param array 字符串数组
	 * @return Short数组
	 */
	public static Short[] toShort(String[] array)
	{		
		if(array==null || array.length==0)
			return new Short[0];
		Short[] ret = new Short[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseShort(array[i], null);
		}
		return ret;
	}
	
	/**
	 * 字符串数组转short数组.
	 * 转换失败则对应位置置0
	 * 
	 * @param array 字符串数组
	 * @return long数组
	 */
	public static short[] toShortPrimitive(String[] array)
	{		
		if(array==null || array.length==0)
			return new short[0];
		short[] ret = new short[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseShort(array[i]);
		}
		return ret;
	}	
	
	/**
	 * 字符串数组转BigDecimal数组.
	 * 转换失败则对应位置置NULL
	 * 
	 * @param array 字符串数组
	 * @return BigDecimal数组
	 */
	public static BigDecimal[] toBigDecimal(String[] array)
	{		
		if(array==null || array.length==0)
			return new BigDecimal[0];
		BigDecimal[] ret = new BigDecimal[array.length];
		for(int i=0;i<array.length;i++)
		{
			ret[i] = Utility.parseBigDecimal(array[i], null);
		}
		return ret;
	}
	
	/**
   * Checks if is same array.
   * 注：数组中不允许有null值，否则无法排序
   *
   * @param a the a
   * @param b the b
   * @return true, if is same array
   */
  public static boolean isSame(Integer[] a, Integer[] b)
  {
    if(a==null && b==null)
      return true;
    if(a==null || b==null) //这里有一个null肯定就不等了
      return false;
    if(a.length!=b.length)
      return false;
    if(ArrayUtils.contains(a, null) || ArrayUtils.contains(b, null)) //null无法排序
      return false;
    Arrays.sort(a);
    Arrays.sort(b);
    return Arrays.equals(a, b);
  }
  
  public static boolean isSame(int[] a, int[] b)
  {
    if(a==null && b==null)
      return true;
    if(a==null || b==null) //这里有一个null肯定就不等了
      return false;
    if(a.length!=b.length)
      return false;

    Arrays.sort(a);
    Arrays.sort(b);
    return Arrays.equals(a, b);
  }
	
  public static boolean isSame(String[] a, String[] b)
  {
    if(a==null && b==null)
      return true;
    if(a==null || b==null) //这里有一个null肯定就不等了
      return false;
    if(a.length!=b.length)
      return false;
    if(ArrayUtils.contains(a, null) || ArrayUtils.contains(b, null)) //null无法排序
      return false;
    Arrays.sort(a);
    Arrays.sort(b);
    return Arrays.equals(a, b);
  }
  
  /**
   * 将一个数组按指定长度切分成一个二维数组.
   *
   * @param array the array
   * @param count the count
   * @return the int[][]
   */
  public static int[][] split(int[] array, int count)
  {
    int length = array.length;
    int tail = length%count;
    int splitSize = length/count;
    int[][] b = new int[splitSize + (tail>0?1:0)][];
    for(int i=0; i<array.length; i++) {
       int m = i/count;
       int n = i%count;
       if(b[m]==null)
         b[m] = new int[m<splitSize?count:tail];
       b[m][n] = array[i];
    }
    return b;
  }
  
  public static String[][] split(String[] array, int count)
  {
    int length = array.length;
    int tail = length%count;
    int splitSize = length/count;
    String[][] b = new String[splitSize + (tail>0?1:0)][];
    for(int i=0; i<array.length; i++) {
       int m = i/count;
       int n = i%count;
       if(b[m]==null)
         b[m] = new String[m<splitSize?count:tail];
       b[m][n] = array[i];
    }
    return b;
  }
  
  public static Integer[][] split(Integer[] array, int count)
  {
    int length = array.length;
    int tail = length%count;
    int splitSize = length/count;
    Integer[][] b = new Integer[splitSize + (tail>0?1:0)][];
    for(int i=0; i<array.length; i++) {
       int m = i/count;
       int n = i%count;
       if(b[m]==null)
         b[m] = new Integer[m<splitSize?count:tail];
       b[m][n] = array[i];
    }
    return b;
  }
}
