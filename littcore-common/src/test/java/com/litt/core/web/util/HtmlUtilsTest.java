package com.litt.core.web.util;



import junit.framework.TestCase;

/** 
 * 
 * TODO.
 * 
 * <pre><b>描述：</b>
 *    TODO 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    TODO
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-9
 * @version 1.0
 *
 */
public class HtmlUtilsTest extends TestCase
{
	public void test_escapeScript() throws Exception
	{
		String content = "<script>alert('123');</script><input onclick='alert();'>";
		String ret = HtmlUtils.escapeScript(content);
		assertEquals("&ltscript>alert('123');&lt/script><input >", ret);
	}
}
