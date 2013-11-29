package com.litt.core.common;

import java.util.Date;

import org.joda.time.DateTimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.litt.core.format.FormatDateTime;

/**
 * Utility测试类
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @date 2006-08-30
 * @version 1.0
 */
public class UtilityTest extends TestCase
{

    public void test_spitString()        
    {
        String[] result = Utility.splitString(",2,3", ",");
        assertEquals("2", result[0]);
        assertEquals(2,result.length);
        result = Utility.splitString("1\\2\\3", "\\");   //测试WINDOWS路径分割
        assertEquals("1", result[0]);
        assertEquals(3,result.length);
        result = Utility.splitString("1\\\\3", "\\");   //测试WINDOWS路径分割
        assertEquals("1", result[0]);
        assertEquals("3", result[1]);
        assertEquals(2,result.length);
        
        result = Utility.splitString("1/2/3", "/"); //测试UNIX路径分割
        assertEquals(3,result.length);
    }
    
	public void test_splitStringAll()
	{
		String[] ret = Utility.splitStringAll("1,2,3", ",");
		super.assertEquals(ret.length, 3);
		ret = Utility.splitStringAll(",2,3", ",");
		super.assertEquals(ret.length, 3);
		ret = Utility.splitStringAll(",2,", ",");
		super.assertEquals(ret.length, 3);
		ret = Utility.splitStringAll(",2,3", ",");
		super.assertEquals(ret[0], "");
		ret = Utility.splitStringAll("", ",");	//空字符串
		super.assertEquals(1, ret.length);
		ret = Utility.splitStringAll(",", ",");	
		super.assertEquals(2, ret.length);
	}
	
	public void test_splitStringAllInt()
	{
		String[] ret = Utility.splitStringAll("00010010", 4);
		super.assertEquals(ret.length, 2);
		super.assertEquals("0001",ret[0]);
		super.assertEquals("0010",ret[1]);
		
		ret = Utility.splitStringAll("00010010123", 4);
		super.assertEquals(ret.length, 3);
		super.assertEquals("0001",ret[0]);
		super.assertEquals("0010",ret[1]);
	}	
	
	
	public void test_joinString()
	{
		String ret = Utility.joinString(new String[]{"id","name","age"});
		super.assertEquals(ret,"id,name,age");
	}    
	
	public void test_joinString2()
	{
		String ret = Utility.joinString(new String[]{"id","name","age"}, ",");
		super.assertEquals(ret,"id,name,age");
		ret = Utility.joinString(new String[]{"id","name","age"}, "|");
		super.assertEquals(ret,"id|name|age");
	}
	
	public void test_abbreviate()
	{
		String ret = Utility.abbreviate("测试长标题多余的内容", 5);
		super.assertEquals(ret,"测试...");
		
		ret = Utility.abbreviate("测试长标题多余的内容ABCD", 6, 10);
		super.assertEquals(ret,"...余的内容...");
	}
	
	public void test_capitalizeFirst()
	{
		String ret = Utility.capitalizeFirst("aBc");
		super.assertEquals(ret,"ABc");
		
		ret = Utility.capitalizeFirst("ABc");
		super.assertEquals(ret,"ABc");
	}
	
	public void test_uncapitalizeFirst()
	{
		String ret = Utility.uncapitalizeFirst("aBc");
		super.assertEquals(ret,"aBc");
		
		ret = Utility.uncapitalizeFirst("ABc");
		super.assertEquals(ret,"aBc");
	}	
	
    public void test_isEmpty()
    {
    	String value = null;
    	assertEquals(true,Utility.isEmpty(value));
    	value = "";
    	assertEquals(true,Utility.isEmpty(value));
    	value = "   ";
    	assertEquals(false,Utility.isEmpty(value));
    	value = "abc";
    	assertEquals(false,Utility.isEmpty(value));
    }
    
    public void test_trimNull()
    {
    	String value = null;
    	assertEquals("",Utility.trimNull(value));
    	value = "";
    	assertEquals("",Utility.trimNull(value));
    	value = "abc";
    	assertEquals("abc",Utility.trimNull(value)); 
    }
    
    public void test_trimNull_String()
    {
    	String value = null;
    	assertEquals("abc",Utility.trimNull(value,"abc"));
    	
    	value = null;
    	assertEquals(1,Utility.trimNull(value,1));
    }
    
    public void test_trimNull_int()
    {
    	String value = null; 	
    	
    	assertEquals(1,Utility.trimNull(value,1));
    } 
    
    public void test_parseInt()
    {
    	assertEquals(8, Integer.parseInt("008"));
    	assertEquals(8, Utility.parseInt("08"));
    	assertEquals(8, Utility.parseInt("08", 10));
    }
    
    public void test_parseBoolean()
    {
    	String value = null;
    	assertEquals(false,Utility.parseBoolean(value));
    	value = "";
    	assertEquals(false,Utility.parseBoolean(value));
    	value = "1";
    	assertEquals(true,Utility.parseBoolean(value));
    	value = "0";
    	assertEquals(false,Utility.parseBoolean(value));
    	value = "yes";
    	assertEquals(true,Utility.parseBoolean(value));
    	value = "no";
    	assertEquals(false,Utility.parseBoolean(value));
    	value = "true";
    	assertEquals(true,Utility.parseBoolean(value));
    	value = "false";
    	assertEquals(false,Utility.parseBoolean(value));
    	
    	value = "abc";
    	assertEquals(false,Utility.parseBoolean(value));
    }
    
    public void test_parseBoolean_boolean()
    {
    	String value = null;
    	assertEquals(true,Utility.parseBoolean(value,true));
    	value = "";
    	assertEquals(true,Utility.parseBoolean(value,true));
    	value = null;
    	assertEquals(false,Utility.parseBoolean(value,false));
    	value = "";
    	assertEquals(false,Utility.parseBoolean(value,false));
    	
    	value = "1";
    	assertEquals(true,Utility.parseBoolean(value,false));
    	value = "0";
    	assertEquals(false,Utility.parseBoolean(value,true));
    	value = "yes";
    	assertEquals(true,Utility.parseBoolean(value,false));
    	value = "no";
    	assertEquals(false,Utility.parseBoolean(value,true));
    	value = "true";
    	assertEquals(true,Utility.parseBoolean(value,false));
    	value = "false";
    	assertEquals(false,Utility.parseBoolean(value,true));
    }  
    
    public void test_parseByte()
    {
    	byte ret = Utility.parseByte("127");
    	assertEquals(127,ret);
    	ret = Utility.parseByte("128");
    	assertEquals(0,ret);
    	ret = Utility.parseByte("-127");
    	assertEquals(-127,ret);
    	ret = Utility.parseByte("-128");
    	assertEquals(-128,ret);    	
    	ret = Utility.parseByte("a");
    	assertEquals(0,ret);    	
    }
    
    public void test_parseDate()
    {
    	Date date = Utility.parseDate(20090101);
    	assertEquals("2009-01-01", FormatDateTime.formatDate(date));
    	assertEquals("2010-01-01", FormatDateTime.formatDate(Utility.parseDate("2010-1-1", "yyyy-M-d")));
    	assertEquals("2010-10-10", FormatDateTime.formatDate(Utility.parseDate("2010-10-10", "yyyy-M-d")));
    } 
    
    public void test_parseDateTime()
    {
    	Date date = Utility.parseDateTime(20090101010203L);
    	assertEquals("2009-01-01 01:02:03", FormatDateTime.formatDateTime(date));
    	assertEquals("2010-01-01 01:01:01", FormatDateTime.formatDateTime(Utility.parseDate("2010-1-1 1:1:1", "yyyy-M-d h:m:s")));
    	assertEquals("2010-10-10 10:10:10", FormatDateTime.formatDateTime(Utility.parseDate("2010-10-10 10:10:10", "yyyy-M-d h:m:s")));
    	assertEquals("2010-05-17 10:10:10", FormatDateTime.formatDateTime(Utility.parseDate("2010-5-17 10:10:10", "yyyy-M-d h:m:s")));
    }  
    
    public void test_getFilePrefix()
    {
        String prefix = Utility.getFileName("test.txt");
        assertEquals("test",prefix);
    }
    
    public void test_getSimpleFileName()
    {
    	String srcFileName = "/var/usr/test.dat";    	
    	String name = Utility.getSimpleFileName(srcFileName);
    	assertEquals("test.dat",name);
    	
    	srcFileName = "/var/usr/test";    	
    	name = Utility.getSimpleFileName(srcFileName);
    	assertEquals("test",name);
    	
    	srcFileName = "D:\\temp\\test.dat";    	
    	name = Utility.getSimpleFileName(srcFileName);
    	assertEquals("test.dat",name);
    	
    	srcFileName = "D:\\temp\\test";    	
    	name = Utility.getSimpleFileName(srcFileName);
    	assertEquals("test",name);
    }
    
    public void test_changeTimeZone()
    {
    	Date date = Utility.parseDateTime("2010-09-10 11:12:13");    	
    	Date newDate = Utility.changeTimeZone(date, DateTimeZone.forOffsetHours(7).toTimeZone());	//由于系统是+8时区，这里偏移一个时区做测试
    	Assert.assertEquals("2010-09-10 12:12:13", FormatDateTime.formatDateTime(newDate));
    	
    }
    

}