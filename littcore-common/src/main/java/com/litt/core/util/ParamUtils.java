package com.litt.core.util;

import java.util.Map;

import com.litt.core.common.Utility;

/**
 * 参数辅助工具.
 * 
 * <pre><b>描述：</b>
 *    用于各种参数的处理
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2011-3-1
 * @version 1.0
 */
public class ParamUtils 
{
	public static short getShort(Map paramMap, String key, short defaultValue)
	{
		if(paramMap==null) return defaultValue;
		
		Object obj = paramMap.get(key);
		if(obj!=null && obj instanceof String)
		{
			return Utility.parseShort((String)obj, defaultValue);
		}
		else if(obj!=null)
		{
			return ((Number)obj).shortValue();
		}
		return defaultValue;
	}
	
	public static int getInt(Map paramMap, String key, int defaultValue)
	{
		if(paramMap==null) return defaultValue;
		
		Object obj = paramMap.get(key);
		if(obj!=null && obj instanceof String)
		{
			return Utility.parseInt((String)obj, defaultValue);
		}
		else if(obj!=null)
		{
			return ((Number)obj).intValue();
		}
		return defaultValue;
	}
	
	public static long getLong(Map paramMap, String key, long defaultValue)
	{
		if(paramMap==null) return defaultValue;
		
		Object obj = paramMap.get(key);
		if(obj!=null && obj instanceof String)
		{
			return Utility.parseLong((String)obj, defaultValue);
		}
		else if(obj!=null)
		{
			return ((Number)obj).longValue();
		}
		return defaultValue;
	}
	
	public static String getString(Map paramMap, String key, String defaultValue)
	{
		if(paramMap==null) return defaultValue;
		
		Object obj = paramMap.get(key);
		if(obj!=null && obj instanceof String)
		{
			return (String)obj;
		}
		else if(obj!=null)
		{
			return obj.toString();
		}
		return defaultValue;
	}
	
	public static boolean getBoolean(Map paramMap, String key, boolean defaultValue)
	{
		if(paramMap==null) return defaultValue;
		
		Object obj = paramMap.get(key);
		if(obj!=null && obj instanceof String)
		{
			return Utility.parseBoolean((String)obj, defaultValue);
		}
		if(obj!=null && obj instanceof Boolean)
		{
			return ((Boolean)obj).booleanValue();
		}
		else if(obj!=null)
		{
			return Utility.parseBoolean(obj.toString(), defaultValue);
		}
		return defaultValue;
	}
	
}
