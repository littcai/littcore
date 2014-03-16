package com.litt.core.util;

import java.util.HashMap;
import java.util.Map;

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
 * @since 2011-3-1
 * @version 1.0
 */
public class ParamUtilsTest extends TestCase {
	
	
	public void test_getBoolean()
	{
		Map paramMap = new HashMap();
		paramMap.put("a", true);
		paramMap.put("b", new Boolean(true));
		paramMap.put("c", 1);
		paramMap.put("d", "true");
		
		paramMap.put("e", false);
		paramMap.put("f", new Boolean(false));
		paramMap.put("g", 0);
		paramMap.put("h", "false");
		
		super.assertTrue(ParamUtils.getBoolean(paramMap, "a", false));
		super.assertTrue(ParamUtils.getBoolean(paramMap, "b", false));
		super.assertTrue(ParamUtils.getBoolean(paramMap, "c", false));
		super.assertTrue(ParamUtils.getBoolean(paramMap, "d", false));
		
		super.assertFalse(ParamUtils.getBoolean(paramMap, "e", true));
		super.assertFalse(ParamUtils.getBoolean(paramMap, "f", true));
		super.assertFalse(ParamUtils.getBoolean(paramMap, "g", true));
		super.assertFalse(ParamUtils.getBoolean(paramMap, "h", true));
	}

}
