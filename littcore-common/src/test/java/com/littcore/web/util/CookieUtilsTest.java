package com.littcore.web.util;

import org.junit.Assert;
import org.junit.Test;

import com.littcore.exception.CheckedBusiException;
import com.littcore.web.util.CookieUtils;

/**
 * .
 * 
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-9-6
 * @version 1.0
 */
public class CookieUtilsTest {
	
	@Test
	public void test_encryptCookie() throws Exception
	{
		String securityKey = "abcde";
		String encryptValue = CookieUtils.encryptCookie(securityKey, "userId=1", "192.168.0.1", 100);
		String cookieValue = CookieUtils.decryptCookie(securityKey, encryptValue, "192.168.0.1");
		Assert.assertEquals("userId=1", cookieValue);		
	}
	
	@Test
	public void test_encryptCookieWithWrongip() throws Exception
	{
		String securityKey = "abcde";
		String encryptValue = CookieUtils.encryptCookie(securityKey, "userId=1", "192.168.0.1", 100);
		try {
			String cookieValue = CookieUtils.decryptCookie(securityKey, encryptValue, "192.168.0.2");
			Assert.fail("Cookie from another ip");
		} catch (CheckedBusiException e) {
			
		}
	}
	
	@Test
	public void test_encryptCookieWithExpired() throws Exception
	{
		String securityKey = "abcde";
		String encryptValue = CookieUtils.encryptCookie(securityKey, "userId=1", "192.168.0.1", 1);
		try {
			Thread.sleep(2000);
			String cookieValue = CookieUtils.decryptCookie(securityKey, encryptValue, "192.168.0.2");
			Assert.fail("Cookie is expired");
		} catch (CheckedBusiException e) {
			
		}
	}
}
