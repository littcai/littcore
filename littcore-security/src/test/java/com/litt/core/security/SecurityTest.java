package com.litt.core.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.litt.core.security.algorithm.RSATool;

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
 * @since 2014-1-14
 * @version 1.0
 */
public class SecurityTest {
	
	@Before
	public void init() throws Exception
	{
		RSATool tool = SecurityFactory.genRSA("shgc");
		tool.store("C:\\priKey.key", "C:\\pubKey.key");
	}
	
	@Test
	public void test() throws Exception
	{
		String source = "This is a test.";
		
		ISecurityEncoder encoder = SecurityFactory.genRSAEncoder("C:\\priKey.key");
		String encoded = encoder.encrypt(source);			
		System.out.println(encoded);		
		
		ISecurityDecoder decoder = SecurityFactory.genRSADecoder("C:\\pubKey.key");
		String decoded = decoder.decrypt(encoded);
		System.out.println(decoded);		
		Assert.assertEquals(source, decoded);
		
		String sign = encoder.sign(source);		
		System.out.println(sign);
		boolean result = decoder.verify(source, sign);
		Assert.assertTrue(result);
	}

}
