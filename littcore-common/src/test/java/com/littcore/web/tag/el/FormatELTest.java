
package com.littcore.web.tag.el;

import com.littcore.web.tag.el.FormatEL;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 */
public class FormatELTest extends TestCase {
	
	public void test_escapeTextarea()
	{
		String content = "First\nSecond <b>html</b>\r\nThird";
		
		String ret = FormatEL.escapeTextarea(content);
		
		super.assertEquals("First<br/>Second &lt;b&gt;html&lt;/b&gt;<br/>Third", ret);
	}

}
