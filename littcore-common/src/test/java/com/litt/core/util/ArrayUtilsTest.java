package com.litt.core.util;

import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase
{
	public void test_fillWith()
	{
		String[] a = new String[10];
		a = ArrayUtils.fillWith(a, "1");
		for(int i=0;i<a.length;i++)
			assertEquals("1", a[i]);
	}
}
