package com.litt.core.shield.vo;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

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
 * @since 2013-9-29
 * @version 1.0
 */
public class AutoLoginTokenTest {
	
	@Test
	public void test()
	{
		AutoLoginToken token = new AutoLoginToken("192.168.1.1", "littcai@hotmail.com", "password");
		String value = token.toString();
		String uuid = token.getId();
		Date createDatetime = token.getCreateDatetime();
		
		token = AutoLoginToken.fromString(value);
		Assert.assertEquals(uuid, token.getId());
		Assert.assertEquals(createDatetime.getTime(), token.getCreateDatetime().getTime());
		Assert.assertEquals(0, token.getExpiredTime());
		
		Assert.assertEquals("192.168.1.1", token.getAutoLoginIp());
		Assert.assertEquals("littcai@hotmail.com", token.getLoginId());
		Assert.assertEquals("password", token.getEncryptedPassword());
	}

}
