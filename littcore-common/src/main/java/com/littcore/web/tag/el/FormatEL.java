package com.littcore.web.tag.el;

import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.littcore.common.Utility;
import com.littcore.format.FormatDateTime;
import com.littcore.util.IPUtils;
import com.littcore.util.ValidateUtils;
import com.littcore.web.util.HtmlUtils;

/** 
 * 
 * 格式化EL.
 * 
 * <pre><b>描述：</b>
 *    自定义格式化EL表达式
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-11-25
 * @version 1.0
 *
 */
public class FormatEL
{	
	/**
	 * 格式化日期.
	 * 
	 * @param date 日期对象
	 * 
	 * @return 格式："yyyy-MM-dd"
	 */
	public static String formatDate(Date date)
	{
		return FormatDateTime.formatDate(date);
	}
	
	/**
	 * 格式化为中文日期.
	 * 
	 * @param date 日期对象
	 * 
	 * @return 格式："yyyy年MM月dd日"
	 */
	public static String formatDateCn(Date date)
	{
		return FormatDateTime.formatDateCn(date);
	}	
	
	/**
	 * 格式化为指定格式.
	 * 
	 * @param date 日期对象
	 * @param style 显示格式
	 * 
	 * @return 指定格式
	 */
	public static String formatDateLike(Date date, String style)
	{
		return FormatDateTime.format(date, style);
	}
	
	/**
	 * 格式化日期时间.
	 * 
	 * @param date 日期对象
	 * 
	 * @return 格式："yyyy-MM-dd HH:mm:ss"
	 */
	public static String formatDateTime(Date date)
	{
		return FormatDateTime.formatDateTime(date);
	}	
	
	/**
	 * 格式化整型的日期时间.
	 * 
	 * @param datetime 日期时间
	 * 
	 * @return 格式："yyyy-MM-dd HH:mm:ss"
	 */
	public static String formatLongDateTime(Long datetime)
	{
		if(datetime==null || datetime.longValue()<19700101000001L)
			return "";
		else
			return FormatDateTime.formatDateTime(Utility.parseDateTime(datetime.longValue()));
	}
	
	/**
	 * 格式化整型的日期.
	 * 
	 * @param date 日期
	 * 
	 * @return 格式："yyyy-MM-dd"
	 */
	public static String formatLongDate(Long date)
	{
		if(date==null || date.longValue()<19700101)
			return "";
		else
			return FormatDateTime.formatDate(Utility.parseDate(date.longValue()));
	}
	
	/**
	 * 格式化长字符串，截取指定长度大小（常用于标题）.
	 * @param string 原字符串
	 * @param substringLen 截取的长度
	 * @return 截取后的字符串
	 */
	public static String formatLongString(String string,int substringLen)
	{
		if(ValidateUtils.isEmpty(string))
			return "";
		if(string.length()<=substringLen)
			return string;
		else
			return Utility.abbreviate(string, substringLen);			
	}
	
	/**
	 * 将整型IP地址转换为可读形式.
	 *
	 * @param longIp 整型IP
	 * @return the string
	 */
	public static String formatLongIp(Long longIp)
	{
		if(longIp==null || longIp.longValue()<0)
			return null;
		else
			return IPUtils.num2ip(longIp);
	}
	
	/**
	 * 输出HTML安全的字符串.
	 * 
	 * 将字符串中的HTML转义.
	 * @param string
	 * @return 安全输出的字符串
	 */
	public static String out(String string)
	{
		return StringEscapeUtils.escapeHtml(string);
	}
	
	/**
	 * HTML清理.
	 * 去除字符串中的HTML标签
	 * @param string 原字符串
	 * @return 转义后的字符串
	 */
	public static String escapeHTML(String string)
	{
		return HtmlUtils.htmlEscape(string);
	}	
	
	
	/**
	 * 脚本清理.
	 * 去除字符串中的脚本标签
	 * @param string 原字符串
	 * @return 转义后的字符串
	 */
	public static String escapeScript(String string)
	{
		return HtmlUtils.escapeScript(string);
	}	
	
	/**
	 * Textarea内容转移.
	 * 将内容转移成DIV可以模拟输出的类型.
	 * 
	 * 
	 * @param string 原字符串
	 * @return 转义后的字符串
	 */
	public static String escapeTextarea(String string)
	{
		//先转移所有的HTML标签，避免放入DIV后被当成HTML格式显示
		String ret = HtmlUtils.htmlEscape(string);
		
		//将换行转移成<br/>，空格转换为html控制	
		ret = ret.replaceAll(" ", "&nbsp;");
		ret = ret.replaceAll("[\r\n]", "<br/>");
		
		return ret;
	}	
	
	/**
	 * 根据条件输出内容.
	 * 
	 * @param flag 标志
	 * @param str 为true输出
	 * @param str2 为false输出
	 * 
	 * @return string
	 */
	public static String switchOut(boolean flag, String str, String str2)
	{
		if(flag)
			return str;
		else
			return str2;
	}	
	
	/**
	 * 将字节为单位的文件大小格式化为用户友好的呈现.
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	public static String formatFileSize(Long bytes)
	{
	  int unit = 1024;        
    if (bytes==null || bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    DecimalFormat df = new DecimalFormat("#.##");       
    return new StringBuilder()
      .append(df.format(bytes / Math.pow(unit, exp)))
      .append(" ")
      .append("KMGTPE".charAt(exp-1))
      .append("B").toString();
	}
	
	
}
