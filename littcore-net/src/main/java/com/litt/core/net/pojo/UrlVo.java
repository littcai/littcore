package com.litt.core.net.pojo;

import java.util.HashMap;
import java.util.Map;

import com.litt.core.common.Utility;

/**
 * 
 * 
 * URL对象.
 * 
 * <pre><b>描述：</b>
 *    详细描述一个URL，包括其基本URL和参数的拆解 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-6-15
 * @version 1.0
 *
 */
public class UrlVo
{
	
	/** 基本URL. */
	private String baseUrl;
	
	/** 参数映射. */
	private Map<String, String> paramMap = new HashMap<String, String>();
	
	/**
	 * 构造函数拆解URL.
	 * 
	 * @param url url
	 */
	public UrlVo(String url)
	{
		int p = url.indexOf("?");	//检查是否存在参数
		if(p>0)	//拆分基本URL和参数
		{
			baseUrl = url.substring(0,p);
			String temp = url.substring(p+1);
			String[] params = Utility.splitStringAll(temp, "&");
			for(int i=0;i<params.length;i++)
			{
				String[] param = Utility.splitStringAll(params[i],"=");
				if(param.length!=2)
					throw new IllegalArgumentException("URL格式不正确！");
				
				paramMap.put(param[0], param[1]);
			}			
		}
	}
	
	/**
	 * 增加额外参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addParam(String name, String value)
	{
		paramMap.put(name, value);
	}
	
	/**
	 * 增加额外参数
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void addParam(Map<String, String> params)
	{
		paramMap.putAll(params);
	}	
	
	/**
	 * 获得参数.
	 * @param name 参数名称
	 * @return 参数值
	 */
	public String getParam(String name)
	{
		return paramMap.get(name);
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @return the paramMap
	 */
	public Map<String, String> getParamMap()
	{
		return paramMap;
	}

	

	
}
