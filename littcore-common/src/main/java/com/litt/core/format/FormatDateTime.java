package com.litt.core.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * 格式化日期.
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 * 
 */
public class FormatDateTime
{
	public static final String STYLE_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String STYLE_DATE = "yyyy-MM-dd";

	public static final String STYLE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String STYLE_DATE_TIME_CN = "yyyy年MM月dd日 ";

	public static final String STYLE_DATE_CN = "yyyy年MM月dd日";

	public static final String STYLE_TIME_CN = "HH时mm分ss秒";
	
	/**
	 * 按指定样式格式化日期时间.
	 * 
	 * @param datetime Date对象
	 * @param format 格式，例如："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return String 指定样式的时间格式的串,例如：返回'2006-08-30 16:00'
	 */
	public static String format(Date datetime, String format)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		String date = dateFormat.format(datetime);
		return date;
	}
	
	/**
	 * 按指定样式格式化日期时间.
	 *
	 * @param datetime Date对象
	 * @param format 格式，例如："yyyy-MM-dd HH:mm:ss"
	 * @param locale 本地化 
	 * @return String 指定样式的时间格式的串,例如：返回'2006-08-30 16:00'
	 */
	public static String format(Date datetime, String format, Locale locale)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
		String date = dateFormat.format(datetime);
		return date;
	}
	
	/**
	 * 格式化日期时间(系统标准时间)
	 * 格式："yyyy-MM-dd HH:mm:ss".
	 * 
	 * @param datetime Date对象
	 * 
	 * @return String para的标准时间格式的串,例如：返回'2006-08-30 16:00:00'
	 */
	public static String formatDateTime(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(STYLE_DATE_TIME);
		String date = dateFormat.format(datetime);
		return date;
	}

	/**
	 * 格式化日期时间(系统标准时间)
	 * 格式："yyyy-MM-dd".
	 * 
	 * @param datetime Date对象
	 * 
	 * @return String 标准时间格式的串,例如：返回'2006-08-30'
	 */
	public static String formatDate(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(FormatDateTime.STYLE_DATE);
		String date = dateFormat.format(datetime);
		return date;
	}

	/**
	 * 格式化日期成整型
	 * 格式："yyyyMMdd".
	 * 
	 * @param datetime Date对象
	 * 
	 * @return int para的标准时间格式的串,例如：返回'20060830'
	 */
	public static String formatDateNum(Date datetime)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");		
		return dateFormat.format(datetime);
	}

	/**
	 * 格式化日期时间(整型)
	 * 格式："yyyyMMddHHmmss".
	 * 
	 * @param datetime Date对象
	 * 
	 * @return yyyyMMddHHmmss的时间格式的串,例如：返回'20060830123001'
	 * 
	 * @since 2008-11-12
	 */
	public static String formatDateTimeNum(Date datetime)
	{
		if (datetime == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(datetime);
	}

	/**
	 * 格式化时间(系统标准时间)
	 * 格式："HH:mm:ss"
	 * @return String 标准时间格式的串,例如：返回'16:00:00'
	 */
	public static String formatTime(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String date = dateFormat.format(datetime);
		return date;
	}

	/**
	 * 格式化成中文日期时间
	 * 格式："yyyy年MM月dd日 HH时mm分ss秒"
	 * @return String 中文格式化时间日期格式的串,例如：返回'2006年08月30日 16时00分00秒'
	 */
	public static String formatDateTimeCn(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		String date = dateFormat.format(datetime);
		return date;
	}

	/**
	 * 格式化成中文日期.
	 * 格式："yyyy年MM月dd日"
	 * @return String 中文格式化日期格式的串,例如：返回'16时00分00秒'
	 */
	public static String formatDateCn(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String date = dateFormat.format(datetime);
		return date;
	}

	/**
	 * 格式化成中文时间
	 * 格式："HH时mm分ss秒"
	 * @return String 中文格式化时间格式的串,例如：返回'16:00:00'
	 */
	public static String formatTimeCn(Date datetime)
	{
		if (datetime == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH时mm分ss秒");
		String date = dateFormat.format(datetime);
		return date;
	}

}
