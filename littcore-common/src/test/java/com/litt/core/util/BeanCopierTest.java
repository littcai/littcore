package com.litt.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * BeanCopierTest.
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
 * @since 2014年3月30日
 * @version 1.0
 */
public class BeanCopierTest extends TestCase {
	
	public void test_getChangedFields()
	{
		User user = new User();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", new Integer(2));
		paramMap.put("name", "abc");
		
		Map<String, Object> srcMap = BeanCopier.getChangedFields(paramMap, user);
		super.assertEquals(1, srcMap.get("id"));
		super.assertEquals("cai", srcMap.get("name"));
	}
	
	public class User {
		private int id = 1;
		private String name = "cai";
		private Date birthday = new Date();
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the birthday
		 */
		public Date getBirthday() {
			return birthday;
		}
		/**
		 * @param birthday the birthday to set
		 */
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
	}

}
