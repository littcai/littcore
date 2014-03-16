package com.litt.core.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.litt.core.util.Assert;
import com.litt.core.util.StringUtils;

/** 
 * 
 * <b>标题：</b>格式化数字、金额等.
 * <pre><b>描述：</b>
 *    默认显示保留小数点后2位，只能用做显示，不能参与计算
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 * 
 */
public class FormatNumber 
{
	
	private static final int DEFAULT_SCALE = 2;	//默认保留的小数位,百分比则为百分比的小数位数
	
	/**
	 * 将金额格式化成系统标准字符串
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @return 例如：1,234.56 
	 */
	public static String format(BigDecimal value)
	{			
		return format(value,DEFAULT_SCALE);
	}
	
	/**
	 * 将金额格式化成系统标准字符串.
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @param scale 保留小数的位数
	 * @return 例如：1,234.5678 
	 */
	public static String format(BigDecimal value,int scale)
	{
		StringBuffer p = new StringBuffer(); 
		if(scale>0)	//有小数位的时候加上小数点
			p.append('.');
		else if(scale<0)
		{
	        throw new IllegalArgumentException("The scale must be a positive integer or zero");
	    }
		for(int i=0;i<scale;i++)
			p.append('#');
		
		DecimalFormat  df = new DecimalFormat(",###"+p.toString());		
		return df.format(value);
	}
	
	/**
	 * 本地化.
	 * @param value
	 * @param locale
	 * @return
	 */
	public static String format(BigDecimal value, Locale locale)
	{
		NumberFormat df = DecimalFormat.getInstance(locale);
		return df.format(value);
	}
	
	/**
	 * 将金额格式化成系统标准字符串
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @return 例如：1,234.56 
	 */
	public static String format(Double value)
	{	
		return format(value, DEFAULT_SCALE);
	}
	
	/**
	 * 将金额格式化成系统标准字符串
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @return 例如：1,234.56 
	 */
	public static String format(Double value,int scale)
	{
		if(value==null)
			return "";
		return format(value,scale);
	}
	
	/**
	 * 将金额格式化成系统标准字符串
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @return 例如：1,234.56 
	 */
	public static String format(double value)
	{	
		return format(value,DEFAULT_SCALE);
	}
	
	/**
	 * 将金额格式化成系统标准字符串
	 * 整数部分每三位用逗号分隔，小数部分默认取两位
	 * @param value 小数
	 * @param scale 保留小数的位数
	 * @return 例如：1,234.5678 
	 */
	public static String format(double value,int scale)
	{
		StringBuffer p = new StringBuffer(); 
		if(scale>0)	//有小数位的时候加上小数点
			p.append('.');
		else if(scale<0)
		{
	        throw new IllegalArgumentException("The scale must be a positive integer or zero");
	    }
		for(int i=0;i<scale;i++)
			p.append('#');
		
		DecimalFormat  df = new DecimalFormat(",###"+p.toString());		
		return df.format(value);
	}
	
	/**
	 * 格式化百分数，默认取小数点后2位
	 * @param value 小数
	 * @return String 默认格式化百分数
	 */
	public static String formatPercent(double value)
	{
		return formatPercent(value,DEFAULT_SCALE);
	}
	
	/**
	 * 格式化百分数
	 * @param value  小数
	 * @param scale 保留小数的位数
	 * @return String 格式化百分数
	 */	
	public static String formatPercent(double value,int scale)
	{
		 DecimalFormat df = (DecimalFormat)DecimalFormat.getPercentInstance();
	
		 StringBuffer p = new StringBuffer(); 
		 if(scale>0)	//有小数位的时候加上小数点
			p.append('.');
		 else if(scale<0)
		 {
		    throw new IllegalArgumentException("The scale must be a positive integer or zero");
		 }
		 for(int i=0;i<scale;i++)
			p.append('#');
		 df.applyPattern(p+"%");
		 return df.format(value);
	}
	
	/**
	 * 格式化百分数.
	 *
	 * @param value  小数
	 * @param scale 保留小数的位数
	 * @param locale 国际化
	 * @return String 格式化百分数
	 */	
	public static String formatPercent(double value,int scale, Locale locale)
	{
		 DecimalFormat df = (DecimalFormat)DecimalFormat.getPercentInstance(locale);	
		 if(scale<0)
		 {
		    throw new IllegalArgumentException("The scale must be a positive integer or zero");
		 }
		 return df.format(value);
	}
	
	/**
	 * 格式化百分数，默认取小数点后2位
	 * @param value 小数
	 * @return String 默认格式化百分数
	 */
	public static String formatPercent(BigDecimal value)
	{
		 return formatPercent(value.doubleValue(), DEFAULT_SCALE);
	}	
	
	/**
	 * 格式化百分数，默认取小数点后2位
	 * @param value 小数
	 * @return String 默认格式化百分数
	 */
	public static String formatPercent(BigDecimal value,int scale)
	{
		return formatPercent(value.doubleValue(), scale);
	}
	
	
	/**
	 * 将整型数格式化为指定长度的字符串，长度不足左补0.
	 * @param value 值
	 * @param length 字符串长度
	 * @return String
	 */
	public static String formatZero(int value, int length)
	{
		Assert.state(value>=0, "value must be a positive integer or zero");
		Assert.state(length>0, "length must be a positive integer");
//		StringBuffer sb = new StringBuffer(length);
//		for(int i=0; i<length; i++)
//		{
//			sb.append("0");
//		}
//		DecimalFormat format = new DecimalFormat(sb.toString());		
//		return format.format(value);
		return StringUtils.leftPad(String.valueOf(value), length, '0');		//DecimalFormat内部使用了同步，性能差
	}
	
	/**
	 * 内置测试函数
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(format(new BigDecimal(1113.1231)));
		System.out.println(format(new BigDecimal(1113.1231),5));
		System.out.println(format(new Double(1113.1231)));
		System.out.println(format(new Double(1113.1231),5));
		System.out.println(formatPercent(new BigDecimal(11.43551)));
		System.out.println(formatZero(0,3));
	}

}
