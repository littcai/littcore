package com.litt.core.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

public class FormatNumberTest {
	
	@Test
	public void test_format()
	{
		Assert.assertEquals("12,345.679", FormatNumber.format(new BigDecimal(12345.6789), Locale.CHINESE));
		
		DecimalFormat df = (DecimalFormat)DecimalFormat.getPercentInstance();		
		System.out.println(df.format(0.10));
		
		DecimalFormat df2 = (DecimalFormat)DecimalFormat.getPercentInstance(Locale.FRENCH)	;	
		System.out.println(df2.format(0.10));
	}

}
