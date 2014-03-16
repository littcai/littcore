package com.litt.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.litt.core.util.ValidateUtils;


/**
 * 
 * <b>标题：</b>基础功能类.
 * <pre><b>描述：</b>
 *    常用数据转换、日期处理、日期比较工具
 * </pre>
 * 
 * <pre><b>变更日志：</b>
 *    增加changeTimeZone方法用于时区转换
 * </pre>
 * 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 * 
 * @since 2008-09-17
 * @version 1.1
 * 
 * @since 2013-07-19
 * @version 2.0
 */
public final class Utility 
{	
    
    /** 日志工具. */
    private static final Log logger = LogFactory.getLog(Utility.class);
    
    public static final char EXTENSION_SEPARATOR = '.';

	/** 文件分隔符. */
	public static final String SEPARATOR = File.separator;
	
	/**	换行符. */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
    /**
     * The Unix separator character.
     */
	public static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
	public static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
	public static final char SYSTEM_SEPARATOR = File.separatorChar;
	

	/** 是否调试标志（可用作系统开发和发布的标志）. */
	public static boolean IS_DEBUG = true; 
	
	/**
	 * 隐藏构造器，确保该工具类不能被实例化.
	 */
	private Utility(){}	
	
	/**
	 * 获取字符的实际长度(中文算2个).
	 * @param value
	 * @return
	 */
	public static int ansiLength(String value)
	{
		if(ValidateUtils.isEmpty(value))
			return 0;
		int length = 0;		
		for (int i = 0; i < value.length(); i++)
		{
			if (value.charAt(i) > 127)
				length = length + 2;
			else
				length++;
		}
		return length;
	}
	
	/**
	 * 从JAR包中获取文件绝对路径.
	 * 
	 * @param jarFileName
	 *            JAR包中文件的相对路径
	 * 
	 * @return String JAR包中文件的绝对路径
	 * 
	 * @throws FileNotFoundException
	 *             文件不存在时抛出此异常
	 */
	public static String getJarFilePath(String jarFileName) throws FileNotFoundException
	{
		URL url = Utility.class.getResource(jarFileName);	//有可能文件不存在		
		if(url!=null)
			return url.getFile();
		throw new FileNotFoundException();
	}
	
	/**
	 * 从JAR包中加载文件.
	 * 
	 * @param jarFileName
	 *            JAR包中文件的相对路径
	 * 
	 * @return InputStream  JAR包中文件的输入流
	 * 
	 * @throws FileNotFoundException
	 *            文件不存在时抛出此异常
	 */
	public static InputStream loadJarFile(String jarFileName) throws FileNotFoundException
	{
		InputStream is = Utility.class.getResourceAsStream(jarFileName);	//有可能文件不存在		
		if(is!=null)
			return is;
		throw new FileNotFoundException();
	}	
	
	
	
    /**
	 * 获取系统运行时的绝对路径. 
	 * 该方法获取的值需在项目启动时通过代码设置
	 * 
	 * @return String 系统运行时的绝对路径，如：D:\TOMCAT\webapps\project
	 */
	public static String getRootPath()
	{
		return CoreConstants.ROOT_PATH;
	}

	
	/**
	 * 读取系统类路径下的配置文件并加载到系统属性列表中. 
	 * 
	 * @param propertiesFilePath
	 *            属性配置文件；路径
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void initProps(String propertiesFilePath) throws IOException
	{
		System.getProperties().load(new FileInputStream(propertiesFilePath));
	}
	
	/**
	 * 从System属性对象中获得属性值.
	 * 
	 * @param keyValue
	 *            属性唯一索引值
	 * 
	 * @return String 属性值
	 */
	public static String getSystemProperty(String keyValue)
	{
		return System.getProperties().getProperty(keyValue);		
	}	
	
	/**
	 * 获取windows登陆用户名.
	 * 
	 * @return String windows登陆用户名
	 */
	public static String getWindowsUser()
	{
		return System.getProperty("user.name") ;
	}
	
    /**
	 * 将ISO-8859-1编码的字符转换成UTF-8.
	 * 该方法主要用于转换URL中的ISO字符串
	 * 
	 * @param source
	 *            需要编码转换的字符串
	 * 
	 * @return String 转换过后的字符串
	 */
	public static String ecodeISOString(String source)
    {
        return Utility.ecodeString(source, "ISO-8859-1", "UTF-8");
    }
	
    /**
	 * 将ecodeA编码的字符转换成ecodeB.
	 * 
	 * @param ecodeB
	 *            目标编码类型
	 * @param ecodeA
	 *            原编码类型
	 * @param source
	 *            需要编码转换的字符串
	 * 
	 * @return String 转换过后的字符串
	 */
	public static String ecodeString(String source,String ecodeA,String ecodeB)
    {
		 if(ValidateUtils.isEmpty(source))
	        	return source;
		String ret = Utility.trimNull(source);		
        try
        {
        	ret = new String(ret.getBytes(ecodeA), ecodeB);
        }
        catch (Exception e)
        {
            logger.error("编码转换失败", e);
        }
        return ret;
    }	
	
	/**
	 * 字符串分割.
	 * 使用StringTokenizer,不支持空字符串（不推荐使用，目前统一使用spitStringAll方法）.
	 * 
	 * @param string
	 *            需要分割的字符串
	 * @param spit
	 *            分隔符
	 * 
	 * @return String[] 分割后的字符串数组
	 * 
	 * @deprecated
	 * 
	 * 
	 */
	public static String[] tokenString(String string,String spit)
	{
		StringTokenizer spitToker = new StringTokenizer(string, spit);	
		int count = spitToker.countTokens();		
		String[] ret = new String[count];			
		int i=0;
		while(spitToker.hasMoreTokens())
		{
			ret[i] = spitToker.nextToken();				
			i++;
		}
		return ret;
	}
	
	/**
	 * 将字符串按指定分隔符转换成数组.
	 * 空字符串将被忽略
	 * 如Utility.splitStringAll(",2,3", ",")将被分割成["2", "3"]
	 * 
	 * @param string 需要分割的字符串
	 * @param split 分隔符
	 * 
	 * @return String[] 分割后的字符串数组
	 */
	public static String[] splitString(String string,String split)
	{	
		if(string==null)
			return new String[0];
		if(Utility.isEmpty(split))
			throw new IllegalArgumentException("分隔符不能为空！");
		if(Utility.isEmpty(string))	//如果是空字符串，则返回长度为1的字符串数组（这是由于下面的方法返回String[0]添加的）。
			return new String[]{""};
		return StringUtils.splitByWholeSeparator(string,split);	
		
	}
	
	/**
	 * 将字符串按指定分隔符分割成数组.
	 * 使用common-lang的StringUtils，支持空字符串.	 
	 * 如Utility.splitStringAll(",2,3", ",")将被分割成["", "2", "3"]
	 * 
	 * @param string 需要分割的字符串
	 * @param split 分隔符
	 * @return String[] 分割后的字符串数组
	 */
	public static String[] splitStringAll(String string,String split)
	{	
		if(string==null)
			return new String[0];
		if(Utility.isEmpty(split))
			throw new IllegalArgumentException("分隔符不能为空！");
		if(Utility.isEmpty(string))	//如果是空字符串，则返回长度为1的字符串数组（这是由于下面的方法返回String[0]添加的）。
			return new String[]{""};
		return StringUtils.splitByWholeSeparatorPreserveAllTokens(string,split);			
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
		if(ValidateUtils.isEmpty(string))
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
	
	
	
	/**
	 * 将数组转换成用逗号分割的字符串.
	 * 该方法主要用于SQL语句多字段的拼接
	 * 
	 * @param strings 需要合并的字符串数组(空字符串将不会被合并)
	 * 
	 * @return String 合并后的字符串(如果为空则返回NULL)
	 */
	public static String joinString(String[] strings)
	{
		StringBuffer ret = new StringBuffer();
		if(strings==null)
			return "";
		else
		{
			for(int i=0;i<strings.length;i++)
			{
				if(!ValidateUtils.isEmpty(strings[i]))
				{
					ret.append(strings[i]);
					ret.append(',');
				}
			}	
		}	
		//如果有数据且最后一个是逗号，要去掉逗号
		int point = ret.lastIndexOf(",");
		if(point>0)
			return ret.substring(0,point);	
		else
			return ret.toString();
	}
	
	/**
	 * 将字符串数组按指定分隔符拼接成一个字符串(StringUtils).
	 * 该方法主要用于SQL语句多字段的拼接
	 * 
	 * @param strings 需要合并的字符串数组
	 * @param split 分隔符
	 * 
	 * @return String 合并后的字符串
	 * 
	 * @see StringUtils
	 */
	public static String joinString(Object[] strings,String split)
	{
		return StringUtils.join(strings,split);
	}
	
	/**
	 * 字符串截取，主要用于超链文字过长的截取，后面加...
	 * (注意：...也包含在截取后字符串长度之中)
	 * 
	 * @param string 需要被截取的字符串
	 * @param len 被截取后剩余的长度(必须大于等于4)	  
	 * 
	 * @return String 被截取后的字符串
	 */
	public static String abbreviate(String string,int len)
	{		
		if(ValidateUtils.isEmpty(string))
			return string;
		if(string.length()<=len)
			return string;
		else
			return StringUtils.abbreviate(string, len);
	}
	
	/**
	 * 字符串截取，主要用于文字过长的截取，后面加...(StringUtils).
	 * (注意：...也包含在截取后字符串长度之中)
	 * 
	 * TODO 该方法有待明确含义
	 * 
	 * @param string 需要被截取的字符串
	 * @param offset 偏移量
	 * @param len 被截取后剩余的长度(必须大于等于4)	 
	 * 
	 * @return String 被截取后的字符串
	 * 
	 * @see StringUtils
	 */
	public static String abbreviate(String string,int offset, int len)
	{		
		if(ValidateUtils.isEmpty(string))
			return string;
		if(string.length()<=len)
			return string;
		else
			return StringUtils.abbreviate(string, offset, len);
	}	
	
	/**
	 * 首字母大写
	 * @param string 待处理字符串
	 * @return 首字母大写的字符串
	 * 
	 * @since 2008-11-13
	 */
	public static String capitalizeFirst(String string)
	{
		return StringUtils.capitalize(string);
	}
	
	/**
	 * 首字母小写
	 * @param string 待处理字符串
	 * @return 首字母小写的字符串
	 * 
	 * @since 2008-11-13
	 */
	public static String uncapitalizeFirst(String string)
	{
		return StringUtils.uncapitalize(string);
	}	
	
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
        return StringUtils.isEmpty(string);	//string==null||string.trim().equals("")
    }
	
	/**
	 * 如果对象为NULL则返回空字符串.
	 * 该方法常用于避免页面null值显示
	 * 
	 * @param o 可转变成String类型的对象
	 * @return String 非空字符串对象
	 */
	public static String trimNull(Object o)
	{
		if (o == null)
			return "";
		return o.toString();
	}	
    
	/**
	 * 如果对象为NULL则返回默认字符串.
	 * @param o 可转变成String类型的对象
	 * @param defaultValue 默认值
	 * @return String 非空字符串对象
	 */
	public static String trimNull(Object o, String defaultValue)
	{
		if (o == null)
			return defaultValue;
		return o.toString();
	}
	
	/**
	 * 如果对象为NULL则返回默认整型数.
	 * @param o 可转变成String类型的对象
	 * @param defaultValue  默认值
	 * @return String 非空字符串对象
	 */
	public static int trimNull(Object o, int defaultValue)
	{
		if (o == null)
			return defaultValue;
		return Integer.valueOf(o.toString().trim()).intValue();
	}	
	
	/**
	 * 将多行字符串转换为单行.
	 * @param string 字符串
	 * @return 单行字符串
	 */
	public static String trimLine(String string)
	{
		
		if(ValidateUtils.isEmpty(string))
			return string;
		
		return  StringUtils.replace(StringUtils.replace(string,"\n",""),"\r\n","");
	}
	
	/**
	 * 将字符串转换成boolean类型（可以是1、0、yes、no、true、false）.
	 * @param string 可转换成boolean型的字符串
	 * @return boolean 转换后的值(默认值false,转换失败也返回false)
	 */
	public static boolean parseBoolean(String string)
	{
		if(ValidateUtils.isEmpty(string))
            return false;
        if(string.equals("1"))
            return true;
        else if(string.equals("0"))
            return false;
        else if(string.equalsIgnoreCase("yes"))
        	return true;
        else if(string.equalsIgnoreCase("no"))
        	return false;
		
		try
		{
			return Boolean.valueOf(string).booleanValue();
		}
		catch (Exception e)
		{			
			logger.error(e);
            return false;
		}
	}
	
	/**
	 * 将字符串转换成boolean类型（可以是1、0、yes、no、true、false）.
	 * 
	 * @param string 可转换成boolean型的字符串
	 * @param defaultValue 默认值
	 * @return boolean 转换后的值(失败则返回默认值)
	 */
	public static boolean parseBoolean(String string, boolean defaultValue)
	{
        if(ValidateUtils.isEmpty(string))
            return defaultValue;
        if(string.equals("1"))
            return true;
        else if(string.equals("0"))
            return false;
        else if(string.equalsIgnoreCase("yes"))
        	return true;
        else if(string.equalsIgnoreCase("no"))
        	return false;
		try
		{
			return Boolean.valueOf(string).booleanValue();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成Boolean类型（可以是1、0、yes、no、true、false）.
	 * 
	 * @param string  可转换成boolean型的字符串
	 * @param defaultValue  默认值
	 * @return Boolean  转换后的值(失败则返回默认值)
	 */
	public static Boolean parseBoolean(String string, Boolean defaultValue)
	{
		if(ValidateUtils.isEmpty(string))
            return defaultValue;
        if(string.equals("1"))
            return Boolean.valueOf(true);
        else if(string.equals("0"))
            return Boolean.valueOf(false);
        else if(string.equalsIgnoreCase("yes"))
        	return Boolean.valueOf(true);
        else if(string.equalsIgnoreCase("no"))
        	return Boolean.valueOf(false);
		try
		{
			return Boolean.valueOf(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}	
	
	/**
	 * 将字符串转换成byte类型,默认值0.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the byte
	 */
	public static byte parseByte(String string)
	{
		try
		{
			return Byte.parseByte(string);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 将字符串转换成byte类型,默认值defaultValue.
	 * 
	 * @param defaultValue
	 *            默认值
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the byte
	 */
	public static byte parseByte(String string,byte defaultValue)
	{
		try
		{
			return Byte.parseByte(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成Byte类型,默认值defaultValue.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the Byte
	 */
	public static Byte parseByte(String string,Byte defaultValue)
	{
		try
		{
			return Byte.valueOf(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}	
	
	/**
	 * 将字符串转换成short类型,默认值0.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the short
	 */
	public static short parseShort(String string)
	{
		try
		{
			return Short.parseShort(string);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 将字符串转换成short类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the short
	 */
	public static short parseShort(String string, short defaultValue)
	{
		try
		{
			return Short.parseShort(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成Short类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the Short
	 */
	public static Short parseShort(String string, Short defaultValue)
	{
		try
		{
			return Short.valueOf(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}	
	
	
	/**
	 * 将字符串转换成int类型,默认值0.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the int
	 */
	public static int parseInt(String string)
	{
		try
		{
			return Integer.parseInt(string);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 将Integer型转换成int型，默认值0.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the int
	 */
	public static int parseInt(Integer string)
	{
		try
		{
			return (string==null?0:string.intValue());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 将Integer型转换成int型，默认值defaultValue.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the int
	 */
	public static int parseInt(Integer string, int defaultValue)
	{
		try
		{
			return (string==null?0:defaultValue);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}	
	
	/**
	 * 将字符串转换成int类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the int
	 */
	public static int parseInt(String string, int defaultValue)
	{
		try
		{
			return Integer.parseInt(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 将字符串转换成Integer类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the integer
	 */
	public static Integer parseInt(String string, Integer defaultValue)
	{
		try
		{
			return new Integer(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 将字符串转换成long类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the long
	 */

	public static long parseLong(String string)
	{
		try
		{
			return Long.parseLong(string);
		}
		catch (Exception e)
		{
			return 0L;
		}
	}	
	
	
	/**
	 * 将字符串转换成long类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the long
	 */
	public static long parseLong(String string, long defaultValue)
	{
		try
		{
			return Long.parseLong(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成long类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the long
	 */
	public static Long parseLong(String string, Long defaultValue)
	{
		try
		{
			return Long.valueOf(string);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}	
	
	/**
	 * 将字符串转换成double类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the double
	 */
	public static double parseDouble(String string)
	{
		try 
		{
			return Double.parseDouble(string);
		}
		catch(Exception e)
		{
			return 0.0D;
		}
	}	
	
	/**
	 * 将字符串转换成double类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the double
	 */
	public static double parseDouble(String string,double defaultValue)
	{
		try 
		{
			return Double.parseDouble(string);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成double类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the double
	 */
	public static Double parseDouble(String string,Double defaultValue)
	{
		try 
		{
			return Double.valueOf(string);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}	

	
	/**
	 * 将字符串转换成float类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the float
	 */
	public static float parseFloat(String string)
	{
		try 
		{
			return Float.parseFloat(string);
		}
		catch(Exception e)
		{
			return 0;
		}
	}	
	
	/**
	 * 将字符串转换成float类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the float
	 */
	public static float parseFloat(String string,float defaultValue)
	{
		try 
		{
			return Float.parseFloat(string);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成BigDecimal类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * 
	 * @return the big decimal
	 */
	public static BigDecimal parseBigDecimal(String string)
	{
		try 
		{
			return new java.math.BigDecimal(string);	
		}
		catch(Exception e)
		{
			return new BigDecimal(0);
		}
	}	
	
	/**
	 * 将字符串转换成BigDecimal类型.
	 * 
	 * @param string
	 *            需要转换的字符串
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return the big decimal
	 */
	public static BigDecimal parseBigDecimal(String string,BigDecimal defaultValue)
	{
		try 
		{
			return new java.math.BigDecimal(string);	
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * Decode a {@link java.math.BigInteger} from a {@link String} value.
	 * Supports decimal, hex and octal notation.
	 * @see BigInteger#BigInteger(String, int)
	 */
	private static BigInteger decodeBigInteger(String value) {
		int radix = 10;
		int index = 0;
		boolean negative = false;

		// Handle minus sign, if present.
		if (value.length() > 0 && value.charAt(0) == '-') {
			negative = true;
			index++;
		}

		// Handle radix specifier, if present.
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		}
		else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		}
		else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}

		BigInteger result = new BigInteger(value.substring(index), radix);
		return (negative ? result.negate() : result);
	}
	
	/**
	 * 将字符串转换为指定数字类型的对象
	 * @param string 需要转换的字符串
	 * @param targetClass 目标类型
	 * @return Number对象
	 */
	public static Number parseNumber(String string, Class targetClass) 
	{
		String trimmed = string.trim();

		if (targetClass.equals(Byte.class)) {
			return Byte.decode(trimmed);
		}
		else if (targetClass.equals(Short.class)) {
			return Short.decode(trimmed);
		}
		else if (targetClass.equals(Integer.class)) {
			return Integer.decode(trimmed);
		}
		else if (targetClass.equals(Long.class)) {
			return Long.decode(trimmed);
		}
		else if (targetClass.equals(BigInteger.class)) {
			return decodeBigInteger(trimmed);
		}
		else if (targetClass.equals(Float.class)) {
			return Float.valueOf(trimmed);
		}
		else if (targetClass.equals(Double.class)) {
			return Double.valueOf(trimmed);
		}
		else if (targetClass.equals(BigDecimal.class) || targetClass.equals(Number.class)) {
			return new BigDecimal(trimmed);
		}
		else {
			throw new IllegalArgumentException(
					"Cannot convert String [" + string + "] to target class [" + targetClass.getName() + "]");
		}
	}
	
	/**
	 * 时区转换为系统时区.
	 *
	 * @param date 日期
	 * @param timeZone 原时区
	 * @return 转换后的时间对象
	 */
	public static Date changeTimeZone(Date date, TimeZone timeZone)
	{
		//计算跟系统时区的时差		
		return changeTimeZone(date, timeZone, TimeZone.getDefault());
	}
	
	/**
	 * 时区转换.
	 *
	 * @param date 日期
	 * @param timeZoneFrom 原时区
	 * @param timeZoneTo 目标时区
	 * @return 转换后的时间对象
	 */
	public static Date changeTimeZone(Date date, TimeZone timeZoneFrom, TimeZone timeZoneTo)
	{
		//计算跟系统时区的时差
		int timeOffset = timeZoneFrom.getRawOffset() - timeZoneTo.getRawOffset();		
		return new DateTime(date).minusMillis(timeOffset).toDate();
	}
	
	/**
	 * 将系统标准格式日期转换成DATE型.
	 * 
	 * @param date
	 *            日期字符串(格式：yyyy-MM-dd)
	 * 
	 * @return Date(转换失败则返回NULL)
	 */
	public static Date parseDate(String date)
	{
		if(ValidateUtils.isEmpty(date))
			return null;
		return DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(date).toDate();
	}
	
	/**
	 * 将整型日期转换成DATE型.
	 * 
	 * @param date
	 *            日期字符串(格式：yyyyMMdd)
	 * 
	 * @return Date 转换失败则返回NULL
	 */
	public static Date parseDate(int dateInt)
	{
		int year = dateInt / 10000;
		int month = (dateInt / 100) % 100; 
		int day = dateInt % 100; 
		
		DateTime dt = new DateTime(year, month, day, 0, 0, 0, 0);
		return dt.toDate();
	}	
	
	/**
	 * 将long型时间转换成DATE.
	 * 
	 * @param datetime 格式："20080808"
	 * 
	 * @return Date 转换失败则返回NULL
	 */
	public static Date parseDate(long dateLong)
	{
		return parseDate((int)dateLong);
	}	
	
	/**
	 * 将系统标准格式日期转换成DATE型.
	 * 
	 * @param date
	 *            日期字符串(格式：yyyy-MM-dd)
	 * @param defaultValue
	 *            当转换出错时的默认时间
	 * 
	 * @return Date(转换失败则返回默认值)
	 */
	public static Date parseDate(String date, Date defaultValue)
	{
		if(ValidateUtils.isEmpty(date))
			return defaultValue;
		return DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(date).toDate();
	}
	
	/**
	 * 按指定格式日期转换成DATE型.
	 * 
	 * @param datetime
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * 
	 * @return Date (转换失败则返回NULL)
	 */
	public static Date parseDate(String datetime, String pattern)
	{
		if(ValidateUtils.isEmpty(datetime))
			return null;
		
		return DateTimeFormat.forPattern(pattern).parseDateTime(datetime).toDate();
		
	}	
	
	/**
	 * 根据日期时间的毫秒数转换为Date型
	 * @param timeMillis
	 * @return Date
	 */
	public static Date parseTimeMillis(long timeMillis)
	{
		return new Date(timeMillis);
	}
	
	/**
	 * 将系统标准格式时间转换成DATE型.
	 * 
	 * @param datetime
	 *            日期时间字符串(格式：yyyy-MM-dd HH:mm:ss)
	 * 
	 * @return Date(转换失败则返回NULL)
	 */
	public static Date parseDateTime(String datetime)
	{
		if(ValidateUtils.isEmpty(datetime))
			return null;		
		return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(datetime).toDate();
	}
	
	/**
	 * 将LONG型格式时间转换成DATE型.
	 * 
	 * @param datetime 格式："20080808120101"
	 * 
	 * @return Date (转换失败则返回NULL)
	 */
	public static Date parseDateTime(long datetime)
	{
		int year = (int)(datetime / 10000000000L);
		int month = (int)((datetime / 100000000L) % 100); 
		int day = (int)((datetime / 1000000L) % 100);  
		int hour = (int)((datetime / 10000L) % 100);  
		int minute = (int)((datetime / 100L) % 100);  
		int second = (int)(datetime % 100);  		
		
		DateTime dt = new DateTime(year, month, day, hour, minute, second, 0);
		return dt.toDate();

	}
	
	
	
	/**
	 * 将系统标准格式时间转换成DATE型.
	 * 
	 * @param datetime
	 *            日期时间字符串(格式：yyyy-MM-dd HH:mm:ss)
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return Date(转换失败则返回默认值)
	 */
	public static Date parseDateTime(String datetime,Date defaultValue)
	{
		if(ValidateUtils.isEmpty(datetime))
			return defaultValue;
		return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(datetime).toDate();
	}	
	
	/**
	 * 按指定格式时间转换成DATE型.
	 * 
	 * @param datetime
	 *            日期时间字符串
	 * @param pattern
	 *            日期时间格式
	 * 
	 * @return Date(转换失败则返回NULL)
	 */
	public static Date parseDateTime(String datetime,String pattern)
	{
		if(ValidateUtils.isEmpty(datetime))
			return null;
		return DateTimeFormat.forPattern(pattern).parseDateTime(datetime).toDate();
	}	
	
	
	/**
	 * 获得URL中的文件名.
	 * 
	 * @param url
	 *            URL
	 * 
	 * @return String 文件名
	 */
	public static String getUrlFileName(String url)
	{
		String ret = "";
		try
		{	
			int point = url.lastIndexOf("/") + 1;	
			ret = url.substring(point);
			point = ret.indexOf("?");	//如果有参数，还要去除问号后面的参数			
			if(point>0)
				ret = ret.substring(0,point);
			return ret;
		}
		catch(Exception e)
		{			
            logger.error("获取文件名失败",e);
            return null;
		}
	}
	
	/**
	 * 获得本地文件全路径名中的文件名.
	 * 
	 * 20090923:需要根据文件名的操作系统实际情况截取
	 * 
	 * @param fileName
	 *            文件全路径名
	 * 
	 * @return String 文件名(失败返回NULL)
	 */
	public static String getSimpleFileName(String fileName)
	{
		return FilenameUtils.getName(fileName);	    
	}	
    
    /**
	 * 获取无后缀文件名.
	 * 
	 * @param fileName
	 *            不带路径文件名
	 * 
	 * @return String 无后缀文件名
	 */
    public static String getFileName(String fileName)
    {
        int point = fileName.lastIndexOf(".");    
        if(point>0)            	
         	return fileName.substring(0,point);
        else
           	return fileName;        
    }    
	
	/**
	 * 获取文件后缀名.
	 * 
	 * @param fileName
	 *            全文件名
	 * 
	 * @return String 后缀名(无后缀名文件返回空字符串)
	 */
	public static String getFileNameSuffix(String fileName)
	{
		int point = fileName.lastIndexOf(".");	
		if(point>0)
		{ 
			return fileName.substring(fileName.lastIndexOf(".")+1); 
		} 
		else 
			return "";		
	}
	
    /**
	 * 字符串替换(jdk1.3中使用)
	 * 
	 * @param source
	 *            原字符串
	 * @param toReplace
	 *            需要替换的内容
	 * @param replacement
	 *            替换的内容
	 * 
	 * @return String 替换后的内容
	 * 
	 * @deprecated
	 */ 
    public static String replaceAll(
        String source,
        String toReplace,
        String replacement) {
        int idx = source.lastIndexOf(toReplace);
        if (idx != -1) {
            StringBuffer ret = new StringBuffer(source);
            ret.replace(idx, idx + toReplace.length(), replacement);
            while ((idx = source.lastIndexOf(toReplace, idx - 1)) != -1) {
                ret.replace(idx, idx + toReplace.length(), replacement);
            }
            source = ret.toString();
        }
        return source;
    } 
    
    
    


    
    public static void main(String[] args)
    {
    	byte b = -78;
//    	long a = System.currentTimeMillis();
//    	for(int i=0;i<1000000;i++)
//    		Utility.getBitsValue(b, 3, 3);
//    	System.out.println(System.currentTimeMillis() - a);
//    	
//    	a = System.currentTimeMillis();
//    	for(int i=0;i<1000000;i++)
//    		Utility.getBitsValue2(b, 3, 3);
//    	System.out.println(System.currentTimeMillis() - a);    	
    	//System.out.println(Utility.getBitsValue(b, 7, 7));   
    	//System.out.println(Integer.toBinaryString(-78));
    	//System.out.println(Integer.toBinaryString(Integer.parseInt("10110010", 2)));
    	//System.out.println(Utility.getBitsValue(b, 7,5));
    	//System.out.println(Utility.getBitsValue(b, 4));
    	//System.out.println(Utility.getBitsValue(b, 3,0));
    	
    	//System.out.println(Utility.ansiLength("12中文"));
    	System.out.println(Integer.parseInt("10011001100", 2));
    	
    }
}
