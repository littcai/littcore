package com.litt.core.format;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.litt.core.common.Utility;

public class FormatDatetimeTest {
	
	@Test
	public void test_format()
	{
		Date date = Utility.parseDateTime("2010-09-10 11:12:13");	
		
		Assert.assertEquals("10-9-10 11:12:13", new DateTime(date).toString("yy-M-dd hh:mm:ss")); 		
		System.out.println(new DateTime(date).toDateTime(DateTimeZone.forOffsetHours(8)).toString("yy-M-dd hh:mm:ss", Locale.CHINESE)); 
		System.out.println(new DateTime(date).toDateTime(DateTimeZone.forOffsetHours(2)).toString("yy-M-dd hh:mm:ss", Locale.CHINESE)); 
		System.out.println(new DateTime(date).toDateTime(DateTimeZone.forOffsetHours(3)).toString("yy-M-dd hh:mm:ss", Locale.CHINESE)); 
		System.out.println(new DateTime(date).toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+8"))).toString("yy-M-dd hh:mm:ss", Locale.CHINESE));
		
		System.out.println(FormatDateTime.formatDateTime(new DateTime(date).toDateTime(DateTimeZone.forOffsetHours(2)).toDate()));
	}

}
