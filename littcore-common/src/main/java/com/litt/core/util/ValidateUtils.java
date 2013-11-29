package com.litt.core.util;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.litt.core.common.Utility;

/** 
 * 
 * 数据校验组件.
 * 
 * <pre><b>描述：</b>
 *    用来校验数据是否符合应有的规则. 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-3-17
 * @version 1.0
 *
 */
public class ValidateUtils
{
	/**
	 * 判断传入的字符串对象是否为NULL或空.
	 * 
	 * @param string
	 *            传入的字符串对象
	 * 
	 * @return boolean 是则返回true，否则返回false
	 */
    public static boolean isEmpty(String string)
    {	
        return StringUtils.isEmpty(string);
    }
    
    /**
     * 
	 * 判断传入的字符串对象是否为NULL或空或者是内容为空或者空白符(whitespace)的字符串.
	 * 
	 * @param string
	 *            传入的字符串对象
	 * 
	 * @return boolean 是则返回true，否则返回false
     */
    public static boolean isBlank(String string)
    {
    	return StringUtils.isBlank(string);
    }
	
	
	/**
	 * 是否是日期格式.
	 * 标准格式(yyyy-MM-dd hh:mm:ss,SSS)
	 * 
	 * @param value 字符串
	 * 
	 * @return true, if is date
	 */
	public static boolean isDate(String value)
	{
		if(isEmpty(value))
			return false;
		if(value.length()!=23)
			return false;
		if(!RegexUtils.validate(value, "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}$"))
			return false;
		Date date = Utility.parseDate(value, "yyyy-MM-dd hh:mm:ss,SSS");		
		if(date==null)
			return false;
		return true;
	}
	
	
	/**
	 * 检查文件后缀名是否匹配.
	 * 
	 * @param file 文件
	 * @param suffix 后缀名
	 * 
	 * @return true, if successful
	 */
	public static boolean isMatchSuffix(File file, String suffix)
	{
		if(file==null)
			return false;
		if(isEmpty(suffix))
			return false;
		Assert.notNull(file);
		Assert.notNull(suffix);
		return suffix.equals(Utility.getFileNameSuffix(file.getName()));
	}
	
	/**
	 * 校验IP地址格式的有效性.
	 * 
	 * @param ip IP地址
	 * 
	 * @return true, if validate
	 */
	public static boolean isIp(String ip)
	{
		if(Utility.isEmpty(ip))
			return false;
		//检查是否只包含数字和小数点
		if(RegexUtils.validateSoft(ip, RegexUtils.IP_REGEXP))
			return true;
		
		return false;		
	}
	
	/**
	 * 校验IP域地址格式的有效性.
	 * 
	 * @param ip IP域地址，格式：192.168.*.*
	 * 
	 * @return true, if validate
	 */
	public static boolean isDomain(String ip)
	{
		if(Utility.isEmpty(ip))
			return false;
		//先将IP中的*号用数字0替换，则转换后的IP为标准IP格式
		ip = ip.replaceAll("[*]", "0");
		//检查是否只包含数字和小数点
		if(RegexUtils.validateSoft(ip, RegexUtils.IP_REGEXP))
			return true;
		
		return false;		
	}
}
