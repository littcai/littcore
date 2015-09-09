package com.littcore.security;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.littcore.security.ISecurity;
import com.littcore.security.ISecurityDecoder;
import com.littcore.security.ISecurityEncoder;
import com.littcore.security.SecurityFactory;
import com.littcore.security.algorithm.RSATool;

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
		RSATool tool = SecurityFactory.genRSA("jfqmt");
		tool.store("E:\\jfqmt-pri.key", "E:\\jfqmt-pub.key");
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
	
	public void testRSASign()throws Exception
	{
		String ori = "mobile=15800379850&content=测试发送短消息&appCode=xwremote&username=xwremote&password=xwremote4855";
		String signed = "MOrmTxQC2T7FfaD6YwE%2BORANYXJYM9VcZIF7adkeg8gjDu67aN7ohYpv1sp0MEmLn%2B4gMCrjI%2BLjpAxrRdYW0jIuro4BYNHFpvUArnB%2F3Hmb5T%2FSStv90sxO%2F40UpvbNiXuFlENhQUJveWCZuVamKFoIvmgUkIiIc8HgZ%2FpBKho%3D";
				
		File file = new File("c:\\xwremote-pub.key");			
		ISecurityDecoder decoder = SecurityFactory.genRSADecoder(file.getAbsolutePath());
			
		boolean isValid = decoder.verify(ori, signed);
		Assert.assertTrue(isValid);						
	}
	
	@Test
	public void testDes() throws Exception
	{
		ISecurity security = SecurityFactory.genDES();
		String encrypted = security.encrypt("This is a test.");
		System.out.println(encrypted);
		String decrypted = security.decrypt(encrypted);
		
		Assert.assertEquals("This is a test.", decrypted);		
	}
	
	@Test
	public void testDes2() throws Exception
	{
		ISecurity security = SecurityFactory.genDES();		
		String ret = String.format("%s;%s;%s;%s;%s;%s",
				"123", "192.168.1.1", "littcai@sina.com", "123456",
				new Date().getTime(), 1);	
		System.out.println(ret);		
		ret = "30bb657f-8e47-4dda-adb1-fa60713a2527;0:0:0:0:0:0:0:1;littcai@sina.com;E10ADC3949BA59ABBE56E057F20F883E;1391592484218;1209600000";
		String encrypted = security.encrypt(ret);	
		
		ISecurity security2 = SecurityFactory.genDES();
		
		String decrypted = security2.decrypt(encrypted);
		Assert.assertEquals(ret, decrypted);		
	}

}
