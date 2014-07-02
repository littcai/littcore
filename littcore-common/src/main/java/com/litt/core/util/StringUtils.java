package com.litt.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/** 
 * 
 * 字符串辅助工具类.
 * 
 * <pre><b>描述：</b>
 *    各种字符串的辅助处理 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-10-30
 * @version 1.0
 *
 */
public class StringUtils extends org.apache.commons.lang.StringUtils
{
	static final char DELIM_START = '{';

	static final char DELIM_STOP = '}';

	static final String DELIM_STR = "{}";

	private static final char ESCAPE_CHAR = '\\';
	
	/**
	 * 效果同assembleString.
	 * @param patternString 需组装的字符串
	 * @param arrayString 参数数组
	 * @return 组装好的字符串
	 */
	public static String format(String patternString, Object[] arrayString)
	{
		return assembleString(patternString, arrayString);
	}

	/**
	 * 字符串组装.
	 * 将字符串中的动态内容以参数化方式组装到原始内容中.
	 * 如:assembleString("我的名字叫{}，今年{}岁", new String[]{"蔡源", "29"});将返回:"我的名字叫蔡源，今年29岁"。
	 * 使用该方法将有效避免使用StringBuffer手工拼装编写大量代码的问题
	 * 
	 * @param patternString 需组装的字符串
	 * @param arrayString 参数数组
	 * 
	 * @return the string
	 * 
	 * 
	 */
	public static String assembleString(String patternString, Object[] arrayString)
	{

		if (arrayString==null || arrayString.length==0)
		{
			return patternString;
		}
		int i = 0;
		int j;
		StringBuffer sbuf = new StringBuffer(patternString.length() + 50);

		for (int index = 0; index < arrayString.length; index++)
		{
			j = patternString.indexOf(DELIM_STR, i);

			if (j == -1)
			{
				// no more variables
				if (i == 0)
				{ // this is a simple string
					return patternString;
				}
				else
				{ // add the tail string which contains no variables and return
					// the result.
					sbuf.append(patternString.substring(i, patternString.length()));
					return sbuf.toString();
				}
			}
			else
			{
				if (isEscapedDelimeter(patternString, j))
				{
					if (!isDoubleEscaped(patternString, j))
					{
						index--; // DELIM_START was escaped, thus should not be incremented
						sbuf.append(patternString.substring(i, j - 1));
						sbuf.append(DELIM_START);
						i = j + 1;
					}
					else
					{
						// The escape character preceding the delemiter start is
						// itself escaped: "abc x:\\{}"
						// we have to consume one backward slash
						sbuf.append(patternString.substring(i, j - 1));
						sbuf.append(arrayString[index]);
						i = j + 2;
					}
				}
				else
				{
					// normal case
					sbuf.append(patternString.substring(i, j));
					sbuf.append(arrayString[index]);
					i = j + 2;
				}
			}
		}
		// append the characters following the last {} pair.
		sbuf.append(patternString.substring(i, patternString.length()));
		return sbuf.toString();
	}
	
	/**
	 * 将字符串按指定长度分割成数组.
	 * 
	 * @param string 需要分割的字符串
	 * @param count 需要分割的长度
	 * @return String[] 分割后的字符串数组(如果为空则返回NULL)
	 */
	public static String[] splitStringAll(String string,int count)
	{	
		if(StringUtils.isEmpty(string))
			return new String[0];
		if(count<=0)
			throw new IllegalArgumentException("分割长度必须大于1");   
		int length = string.length();
		if(length<count)
			return new String[]{string};
		
		int n = length / count ;
		int point = (length % count)>0?1:0 ;	//小数位
		int total = n+point;
		String[] ret = new String[total];		
		for(int i=0;i<total;i++)
		{
			ret[i] = StringUtils.substring(string,i*count, (i+1)*count);
		}	
		return ret;			
	}		

	final static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex)
	{

		if (delimeterStartIndex == 0)
		{
			return false;
		}
		char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
		return potentialEscape == ESCAPE_CHAR;
	}

	final static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex)
	{
		return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR;
	}
	
	/**
	 * 是否包含条件数组中的某个字符串.
	 * 
	 * @param str 原始字符串
	 * @param condArray 条件字符串数组
	 * 
	 * @return 包含任意一个返回true，否则返回false
	 */
	public static boolean contains(String str, String[] condArray)
	{
		if(condArray==null || condArray.length==0)
			return false;
		for(int i=0;i<condArray.length;i++)
		{
			boolean isContain = StringUtils.contains(str, condArray[i]);
			if(isContain)
				return true;
		}
		return false;
	}
	
	/**
	 * 获得条件数组中的每个字符串在原字符串中的位置.
	 * 
	 * @param str 原始字符串
	 * @param condArray 条件字符串数组
	 * 
	 * @return 条件数组为NULL或长度为0返回NULL，正常返回位置索引数组
	 */
	public static int[] indexOf(String str, String[] condArray)
	{
		if(condArray==null || condArray.length==0)
			return new int[0];
		int[] ret = new int[condArray.length];
		for(int i=0;i<condArray.length;i++)
		{
			ret[i] = StringUtils.indexOf(str, condArray[i]);			
		}
		return ret;
	}	

	/**
	 * 去字符串中的换行符.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String chompAll(String string)
	{
		Pattern p = Pattern.compile("\\*|\r|\n");    
	    Matcher m = p.matcher(string);    
	    String after = m.replaceAll("");    
	    return after;
	}
}
