package com.litt.core.web.query;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import org.junit.Test;

import com.litt.core.common.Utility;

public class QueryFilterTest {
	
	@Test
	public void test_query()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("q_code", "X0001");
		paramMap.put("q_age_int", 18);
		paramMap.put("q_birthday_date", "2010-10-10");
		paramMap.put("q_null", null);
		paramMap.put("q_empty", "   ");
		paramMap.put("abc", "abc");
		paramMap.put("question", "what?");
		
		QueryFilter filter = new QueryFilter(paramMap);
		
		Assert.assertEquals(3, filter.getParamMap().size());
		Assert.assertEquals("X0001", filter.getParamMap().get("code"));
		Assert.assertEquals(18, filter.getParamMap().get("age"));
		Assert.assertEquals(Utility.parseDate("2010-10-10"), filter.getParamMap().get("birthday"));
		Assert.assertNull(filter.getParamMap().get("null"));
		Assert.assertNull(filter.getParamMap().get("empty"));
	}

	
	@Test
	public void test_sort()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("s_code_order", "asc");
		paramMap.put("s_age_order", "desc");
		paramMap.put("s_null", null);
		paramMap.put("s_empty", "   ");
		paramMap.put("abc", "abc");
		paramMap.put("question", "what?");
		
		QueryFilter filter = new QueryFilter(paramMap);
		
//		Assert.assertEquals(2, filter.getSortMap().size());
//		Assert.assertEquals("asc", filter.getSortMap().get("code_order"));
//		Assert.assertEquals("desc", filter.getSortMap().get("age_order"));
//		Assert.assertNull(filter.getSortMap().get("null"));
//		Assert.assertNull(filter.getSortMap().get("empty"));
	}
}
