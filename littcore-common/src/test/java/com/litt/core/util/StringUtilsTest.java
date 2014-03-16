package com.litt.core.util;

import junit.framework.TestCase;

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
 * @since 2011-8-9
 * @version 1.0
 */
public class StringUtilsTest extends TestCase {
	
	
	public void test_chompAll()
	{
		assertEquals("abc", StringUtils.chompAll("a\r\nb\nc"));
		
		assertEquals("a b	c", StringUtils.chompAll("a \r\nb\n	c"));
	}

}
