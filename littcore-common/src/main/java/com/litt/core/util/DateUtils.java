package com.litt.core.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

import com.litt.core.common.Utility;
import com.litt.core.format.FormatDateTime;

/** 
 * 
 * 日期处理的辅助类.
 * 
 * <pre><b>描述：</b>
 *    处理日期相关业务 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    1.0 由于对日期类型的处理日益复杂，故增加该辅助类，而不再继续在Utility类中堆砌
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-7-14
 * @version 1.0
 *
 */
public class DateUtils
{
	/**
	 * 将Date转换成Timestamp类型.
	 * 
	 * @param date
	 *            日期时间
	 * 
	 * @return Timestamp 转换失败则返回NULL
	 */
	public static Timestamp convertDate2Timestamp(Date date)
	{
		if(date==null)
			return null;
		try
		{
			return new Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 将Date转换成Timestamp类型.
	 * 
	 * @param date
	 *            日期时间
	 * @param defaultValue
	 *            默认值
	 * 
	 * @return Timestamp 转换失败则返回defaultValue
	 */
	public static Timestamp convertDate2Timestamp(Date date,Date defaultValue)
	{
		if(date==null)
			return new Timestamp(defaultValue.getTime());
		try
		{
			return new Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			return new Timestamp(defaultValue.getTime());
		}
	}	
	
	/**
	 * 将java.util.Date型时间转换成java.sql.Date.
	 * 
	 * @param datetime
	 *            java.util.Date
	 * 
	 * @return java.sql.Date 转换失败则返回NULL
	 */
	public static java.sql.Date convertDate2SqlDate(Date datetime)
	{
		try
		{
			java.sql.Date date = new java.sql.Date(datetime.getTime()); 
		    return date;		
		}
		catch(Exception e)
		{
			return null;
		}
	}	

	/**
	 * 将java.sql.Date型时间转换成java.util.Date.
	 * 
	 * @param datetime
	 *            java.sql.Date(年月日)
	 * 
	 * @return Date 转换失败则返回NULL
	 */
	public static Date convertSqlDate2Date(java.sql.Date datetime)
	{
		try
		{
		    Date date = new Date(datetime.getTime());
		    return date;		
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 将java.sql.Time型时间转换成java.util.Date.
	 * 
	 * @param datetime
	 *            java.sql.Time(时分秒)
	 * 
	 * @return Date 转换失败则返回NULL
	 */
	public static Date convertSqlTimeToDate(java.sql.Time datetime)
	{
		try
		{
		    Date date = new Date(datetime.getTime());
		    return date;		
		}
		catch(Exception e)
		{
			return null;
		}
	}	
	
	/**
	 * 将java.sql.Timestamp型时间转换成java.util.Date.
	 * 
	 * @param datetime
	 *            java.sql.Timestamp
	 * 
	 * @return Date 转换失败则返回NULL
	 */
	public static Date convertSqlTimestampToDate(java.sql.Timestamp datetime)
	{
		try
		{
		    Date date = new Date(datetime.getTime());
		    return date;		
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 得到今天的日期（时分秒为0）.
	 * 
	 * @return Date 当前日期
	 */
	public static Date getCurrentDate() 
	{
		return Utility.parseDate(FormatDateTime.formatDate(new Date()));	  
	}	
	
	/**
	 * 得到今天的标准格式表示，例如：返回'2006-08-30'.
	 * 
	 * @return String 当前日期的标准格式表示
	 */
	public static String getCurrentDateString() 
	{
		return FormatDateTime.formatDate(new Date());	  
	}	
	
	/**
	 * 得到今天的整型表示，例如：返回'20060830'.
	 * 
	 * @return int 当前日期的整型表示
	 */
	public static int getCurrentDateInt() 
	{
		return Utility.parseInt(FormatDateTime.formatDateNum(new Date()));	  
	}
	
    /**
     * 格式化日期成整型
     * 格式："yyyyMMdd".
     * 
     * @param datetime Date对象
     * 
     * @return int para的标准时间格式的串,例如：返回'20060830'
     */
  public static int getDateInt(Date datetime)
  {
	  return Utility.parseInt(FormatDateTime.formatDateNum(datetime));	
  } 
	
	/**
	 * 获得当前时间的标准格式表示.
	 * 例如：返回'13:01:20'
	 * 
	 * @return String
	 */
	public static String getCurrentTimeString()
	{
		return FormatDateTime.formatTime(new Date()); 
	}	
	
	/**
	 * 获得当前时间的整型表示.
	 * 例如：返回'130120'
	 * 
	 * @return int
	 */
	public static int getCurrentTimeInt()
	{
		DateTime dt = new DateTime();
		return dt.getHourOfDay()*10000 + dt.getMinuteOfHour()*100  + dt.getSecondOfMinute();
	}
	
	/**
	 * 获得当前小时的整型表示.
	 * 例如：返回'13'
	 * 
	 * @return int
	 */
	public static int getCurrentHourInt()
	{
		DateTime dt = new DateTime();
		return dt.getHourOfDay();
	}	

    /**
	 * 得到当前的年月.
	 * 
	 * @return 以整型输出(格式:YYYYMM)
	 */
    public static int getCurrentYeatMonthInt()
    {       
        Calendar dStart = Calendar.getInstance();
        dStart.setTime(new Date());
        return dStart.get(Calendar.YEAR) * 100 + (dStart.get(Calendar.MONTH) + 1);
    }
    
    /**
	 * 得到整型年月.
	 * 
	 * @return 以整型输出(格式:YYYYMM)
	 */
    public static int getYeatMonthInt(Date date)
    {       
    	DateTime dt = new DateTime(date);
    	return dt.getYear() *100 + dt.getMonthOfYear();        
    }   
    
    /**
	 * 得到当前的年份.
	 * 
	 * @return 以整型输出(格式:YYYY)
	 */
    public static int getCurrentYearInt()
    {       
        Calendar dStart = Calendar.getInstance();
        dStart.setTime(new Date());
        return dStart.get(Calendar.YEAR) ;
    }
    
    /**
	 * 得到当前的月份.
	 * 
	 * @return 以整型输出(格式:MM)
	 */
    public static int getCurrentMonthInt()
    { 
        Calendar dStart = Calendar.getInstance();
        dStart.setTime(new Date());
        return dStart.get(Calendar.MONTH) + 1;
    }
	
	/**
     * 得到当前时刻的时间字符串,例如：返回'2006-08-30 16:00:00'.
     * 
     * @return String 当前时间的标准格式表示
     */
	public static String getCurrentDateTimeString() 
	{
	    return FormatDateTime.formatDateTime(new Date()); 	 
	}
	
	/**
     * 得到当前时刻的时间字符串,例如：返回'2006-08-30 16:00:00'.
     * 
     * @return String 当前时间的标准格式表示
     * 
     * @since 2008-11-12
     */
	public static Long getCurrentDateTimeLong() 
	{
	    return Utility.parseLong(FormatDateTime.formatDateTimeNum(new Date())); 	 
	}	
	
	/**
     * 得到当前时刻的时间字符串,例如：返回'2006-08-30 16:00:00.001'.
     * 
     * @return Timestamp 当前系统时间的Timestamp表示
     */
	public static Timestamp getCurrentTimestamp() 
	{	  
	  return new Timestamp(System.currentTimeMillis());
	}	
	
	/**
     * 得到当前时刻的指定格式字符串 例如：样式'yyyy-MM-dd HH:mm',返回'2006-08-30 16:00'.
     * 
     * @param format 显示格式
     * 
     * @return String 当前时间指定样式的表示
     */
	public static String getCurrentDateTime(String format) 
	{
        return FormatDateTime.format(new Date(), format);	  
	}
	
	/**
     * 得到当前时间的中文字符串，例如：返回'2006年08月30日 16时00分00秒'.
     * 
     * @return String 当前时间的中文表示
     */
	public static String getCurrentDateTimeCn() 
	{    
	  return FormatDateTime.formatDateTimeCn(new Date());	  
	}
    
    /**
	 * 得到当前日期的中文格式化输出，格式:YYYY年MM月DD日.
	 * 
	 * @return 当前日期的中文表示
	 */
    public static String getCurrentDateCn()
    {               
        return FormatDateTime.formatDateCn(new Date());
    } 
	  
	/**
	 * 判断指定日期所在的年份是否为闰年.
	 * 
	 * @param date
	 *            指定日期
	 * 
	 * @return boolean 是闰年返回true，不是返回false
	 */
	public static boolean isLeapYear(Date date)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);		 
		return calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR));
	}
	
	/**
	 * 得到当前时间的前或后N月.
	 * 
	 * @param n
	 *            加减月数，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfMonth(int n) 
	{		
	   return getBeAfMonth(new Date(),n);
	}	
	
	/**
	 * 得到指定时间的前或后N月.
	 * 
	 * @param n
	 *            加减月数，正数加，负数减
	 * @param date
	 *            the date
	 * 
	 * @return Date
	 */
	public static Date getBeAfMonth(Date date,int n) 
	{
		if(n==0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,n);		
	    return calendar.getTime();
	}
	
	/**
	 * 得到当前时间的前或后N星期.
	 * 
	 * @param n 加减星期数，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfWeek(int n) 
	{
		return getBeAfWeek(new Date(), n);
	}
	
	/**
	 * 得到指定时间的前或后N星期.
	 * 
	 * @param n
	 *            加减星期数，正数加，负数减
	 * @param date
	 *            the date
	 * 
	 * @return Date
	 */
	public static Date getBeAfWeek(Date date,int n) 
	{
		if(n==0)
			return date;
		DateTime dt = new DateTime(date);	
	    return dt.plusWeeks(n).toDate();
	}	
	  
	/**
	 * 得到当前时间的前或后N天.
	 * 
	 * @param n
	 *            加减天数，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfDay(int n) 
	{		
	   return getBeAfDay(new Date(),n);
	}		  

	/**
	 * 得到指定时间的前或后N天.
	 * 
	 * @param n
	 *            加减天数，正数加，负数减
	 * @param date
	 *            the date
	 * 
	 * @return Date
	 */
	public static Date getBeAfDay(Date date,int n) 
	{
		if(n==0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,n);		
	    return calendar.getTime();
	}	
	  
	/**
	 * 得到当前时间的前或后N小时.
	 * 
	 * @param n
	 *            加减小时，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfHour(int n) 
	{		
		return getBeAfHour(new Date(),n);
	}		  
	  
	/**
	 * 得到指定时间的前或后N小时.
	 * 
	 * @param date
	 *            指定时间
	 * @param n
	 *            加减小时，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfHour(Date date,int n) 
	{
		if(n==0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR,n);		
	    return calendar.getTime();
	}	
	  
	/**
	 * 得到当前时间的前或后N分钟.
	 * 
	 * @param n
	 *            加减分钟，正数加，负数减
	 * 
	 * @return Date
	*/
	public static Date getBeAfMinute(int n) 
	{		
		return getBeAfMinute(new Date(),n);
	}		  
	  
	/**
	 * 得到指定时间的前或后N分钟.
	 * 
	 * @param date
	 *            指定时间
	 * @param n
	 *            加减分钟，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfMinute(Date date,int n) 
	{
		if(n==0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE,n);		
	    return calendar.getTime();
	}	
	  
	/**
	 * 得到当前时间的前或后N秒.
	 * 
	 * @param n
	 *            加减秒，正数加，负数减
	 * 
	 * @return Date
	 */
	public static Date getBeAfSecond(int n) 
	{		
		return getBeAfSecond(new Date(),n);
	}		  
	  
	/**
	 * 得到指定时间的前或后N秒.
	 * 
	 * @param date
	 *            指定时间
	 * @param n
	 *            加减秒，正数加，负数减
	 * 
	 * @return Date 返回值
	 */
	public static Date getBeAfSecond(Date date,int n) 
	{
		if(n==0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND,n);		
	    return calendar.getTime();
	}	  
	  
	/**
	 * 比较当前时间是否在两个时间当中.
	 * 
	 * @param endDateTime
	 *            结束日期
	 * @param beginDateTime
	 *            开始日期
	 * 
	 * @return boolean 在返回true，不在返回false
	 */
    public static boolean isBetween(Date beginDateTime, Date endDateTime)
    {
        java.util.Date currentDate = new java.util.Date();
        return currentDate.after(beginDateTime) && currentDate.before(endDateTime);
    }
    
	/**
	 * 得到两个日期间相差的月数.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 月数
	 */
    public static int getBetweenMonths(Date date1, Date date2)
    {    	
        DateTime dt1 = new DateTime(date1);
        DateTime dt2 = new DateTime(date2);
        
        int years = dt2.getYear() - dt1.getYear();
        int months = dt2.getMonthOfYear() - dt1.getMonthOfYear();
        
        return years*12 + months; 
    }	    
		
	/**
	 * 得到两个日期间相差的天数,包括了小时、分秒，如果两个时间相差不超过一天按0天算.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 天数
	 */
    public static int getBetweenDaysFull(Date date1, Date date2)
    {
        long diff = date2.getTime() - date1.getTime();
        long days = diff / 86400000L;
        return new Long(days).intValue();
    }
		
	/**
	 * 得到两个日期间相差的天数,只按天算，即使实际时间间隔小于1天.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 天数
	 */
    public static int getBetweenDays(Date date1, Date date2)
    { 
    	DateTime dt1 = new DateTime(date1);
    	DateTime dt2 = new DateTime(date2);
    	
        long diff = dt2.toDateMidnight().getMillis() - dt1.toDateMidnight().getMillis();
        long days = diff / 86400000L;
        return (int)days;
    }		
		
	/**
	 * 得到两个日期间相差的小时数.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 小时数
	 */
    public static int getBetweenHours(Date date1, Date date2)
    {
        long diff = date2.getTime() - date1.getTime();
        long days = diff / 3600 / 1000;
        return new Long(days).intValue();
    }
		
	/**
	 * 得到两个日期间相差的分钟数.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 分钟数
	 */
    public static long getBetweenMinutes(Date date1, Date date2)
    {
        if(date1==null||date2==null)
            return 0;
        long diff = date2.getTime() - date1.getTime();
        long minutes = diff / 60 / 1000;
        return minutes;
    }
		
	/**
	 * 得到两个日期间相差的秒数.
	 * 
	 * @param date1
	 *            开始日期
	 * @param date2
	 *            结束日期
	 * 
	 * @return int 秒数
	 */
    public static long getBetweenSeconds(Date date1, Date date2)
    {
        if(date1==null||date2==null)
            return 0;
        long diff = date2.getTime() - date1.getTime();
        long seconds = diff / 1000;
        return seconds;
    }	
    
    /**
     * 获得当前日在月中的位置
     * @return int day
     */
    public static int getDayOfMonth()
    {
    	return getDayOfMonth(new Date());
    }
    
    /**
     * 获得日在月中的位置.
     * 
     * @param date 日期
     * 
     * @return int day
     */
    public static int getDayOfMonth(Date date)
    {
    	DateTime dt = new DateTime(date);    	
    	return dt.getDayOfMonth();
    }
    
    /**
     * 获得指定天的开始00:00:00.
     * 
     * @param date 指定日期
     * 
     * @return Date
     */
    public static Date getStartOfDay(Date date)
    {
    	DateTime dt = new DateTime(date);
    	dt = dt.withTime(0, 0, 0, 0);
    	return dt.toDate();
    }     
    
    
    /**
     * 获得指定天的23:59:59.
     * 
     * @param date 指定日期
     * 
     * @return Date
     */
    public static Date getEndOfDay(Date date)
    {
    	DateTime dt = new DateTime(date);
    	dt = dt.withTime(23, 59, 59, 0);
    	return dt.toDate();
    }  
    
    /**
     * 获得当月第一天的.
     * @return Date
     */
    public static Date getFirstOfMonth()
    {
    	return getFirstOfMonth(new Date());
    }

    
    /**
     * 获得当月第一天的.
     * 
     * @param date 指定日期
     * 
     * @return Date
     */
    public static Date getFirstOfMonth(Date date)
    {
    	//得到下个月的第一天，再减一
    	DateTime dt = new DateTime(date);    	
    	dt = dt.withDayOfMonth(1);
    	return dt.toDate();
    }     
    
    /**
     * 获得当月最后一天的.
     * @return Date
     */
    public static Date getLastOfMonth()
    {
    	return getLastOfMonth(new Date());
    }

    
    /**
     * 获得当月最后一天的.
     * 
     * @param date 指定日期
     * 
     * @return Date
     */
    public static Date getLastOfMonth(Date date)
    {
    	//得到下个月的第一天，再减一
    	DateTime dt = new DateTime(date);    	
    	dt = dt.plusMonths(1);	//先加1个月
    	DateTime nextFirstDate = new DateTime(dt.getYear(), dt.getMonthOfYear(), 1,23,59,59,0);	//取得这个月的第一天
    	nextFirstDate = nextFirstDate.minusDays(1);	//再减1天
    	return nextFirstDate.toDate();
    } 
    
    /**
     * 获得当前日期所在周的第一天(星期一).
     * 
     * @return Date
     */
    public static Date getFirstOfWeek()
    {
    	return getFirstOfWeek(new Date());
    }
    
    /**
     * 获得日期所在周的第一天(星期一).
     * @param date 日期
     * @return Date
     */
    public static Date getFirstOfWeek(Date date)
    {
    	DateTime dt = new DateTime(date);  
    	dt = dt.withDayOfWeek(1);
    	return dt.toDate();
    }
    
    /**
     * 获得当前日期所在周的最后一天(星期天).
     * 
     * @return Date
     */
    public static Date getLastOfWeek()
    {
    	return getLastOfWeek(new Date());
    }
    
    /**
     * 获得日期所在周的最后一天(星期天).
     * @param date 日期
     * @return Date
     */
    public static Date getLastOfWeek(Date date)
    {
    	DateTime dt = new DateTime(date);    	
    	dt = dt.withDayOfWeek(7);
    	return dt.toDate();
    }    
    
    /**
     * 是否是今天.
     * 
     * @param date 日期
     * 
     * @return true, if checks if is today
     */
    public static boolean isToday(Date date)
    {    	
    	int dateInt = getDateInt(date);
    	return DateUtils.isToday(dateInt);
    }
    
    /**
     * 是否是今天.
     * 
     * @param date 日期
     * 
     * @return true, if checks if is today
     */
    public static boolean isToday(int dateInt)
    {
    	int today = DateUtils.getCurrentDateInt();
    	return today == dateInt;
    } 
    
    /**
     * 日期1的时间是否早于日期2
     * @param date1 日期1
     * @param date2 日期2
     * @return 是返回true;否返回false
     */
    public static boolean isTimeBefore(Date date1, Date date2)
    {
    	DateTime dt1 = new DateTime(date1);
    	DateTime dt2 = new DateTime(date2);
    	return dt1.getMillisOfDay()<dt2.getMillisOfDay();
    }
    
    /**
     * 转换为Calendar类型.
     * @param date
     * @return Calendar
     */
    public static Calendar toCalendar(Date date)
    {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	return calendar;
    }
    
    /**
     * 设置年份.
     * 
     * @param date the date
     * @param amount the amount
     * 
     * @return the date
     */
    public static Date setYears(Date date, int amount) {
    	return set(date, Calendar.YEAR, amount);
    }
        
    /**
     * 设置月份.
     * 
     * @param date the date
     * @param amount the amount
     * 
     * @return the date
     */
    public static Date setMonths(Date date, int amount)
	{
		return set(date, Calendar.MONTH, amount);
	}

	/**
	 * 设置天.
	 * 
	 * @param date the date
	 * @param amount the amount
	 * 
	 * @return the date
	 */
	public static Date setDays(Date date, int amount)
	{
		return set(date, Calendar.DAY_OF_MONTH, amount);
	}

	/**
	 * 设置小时.
	 * 
	 * @param date the date
	 * @param amount the amount
	 * 
	 * @return the date
	 */
	public static Date setHours(Date date, int amount)
	{
		return set(date, Calendar.HOUR_OF_DAY, amount);
	}

	/**
	 * 设置分钟.
	 * 
	 * @param date the date
	 * @param amount the amount
	 * 
	 * @return the date
	 */
	public static Date setMinutes(Date date, int amount)
	{
		return set(date, Calendar.MINUTE, amount);
	}

	/**
	 * 设置秒.
	 * 
	 * @param date the date
	 * @param amount the amount
	 * 
	 * @return the date
	 */
	public static Date setSeconds(Date date, int amount)
	{
		return set(date, Calendar.SECOND, amount);
	}

	/**
	 * 设置毫秒.
	 * 
	 * @param date the date
	 * @param amount the amount
	 * 
	 * @return the date
	 */
	public static Date setMilliseconds(Date date, int amount)
	{
		return set(date, Calendar.MILLISECOND, amount);
	}

	/**
	 * 设置日期属性.
	 * @param date 日期对象
	 * @param calendarField 字段
	 * @param amount 值
	 * @return Date
	 */
	private static Date set(Date date, int calendarField, int amount)
	{
		if (date == null)
			throw new IllegalArgumentException("The date must not be null");
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.set(calendarField, amount);
		return c.getTime();
	}
    
}
