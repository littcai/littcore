package com.littcore.dao.ql;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.oro.text.perl.Perl5Util;

import com.littcore.dao.ql.IQLResult;
import com.littcore.dao.ql.PageParam;
import com.littcore.dao.ql.QLCondBuilder;
import com.littcore.util.StringUtils;

/** 
 * 
 * QL组件测试.
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
 * @since 2010-9-14
 * @version 1.0
 *
 */
public class QlTest extends TestCase
{
	public void testCond()
	{
		PageParam pageParam = new PageParam(1, 10);
		pageParam.addCond("loginId", "admin");
		String dynamicHql = "select operator from Operator operator"
			+"-- and roleId={roleId}"
			+"-- and loginId like {%loginId%}"
			+"-- and opName like {%opName%}"			
			;
		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		assertEquals("select operator from Operator operator WHERE 1=1 and loginId like ?", listResult.generate());
		assertEquals("SELECT COUNT(*) from Operator operator WHERE 1=1 and loginId like ?", listResult.generateCount());
		
	}
	
	public void testSort() throws Exception
	{
		PageParam pageParam = new PageParam(1, 10);
		
		String dynamicHql = "select operator from Operator operator"
				+"-- and roleId={roleId}"
				+"-- and loginId like {%loginId%}"
				+"-- and opName like {%opName%}"
				+"-- order by opId asc"
				;

		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		String orderSql = listResult.getOrderQl();
		super.assertEquals(" order by opId asc", orderSql);
	}
	
	public void testSort2() throws Exception
	{
		PageParam pageParam = new PageParam(1, 10, "opName", "desc");
		
		String dynamicHql = "select operator from Operator operator"
				+"-- and roleId={roleId}"
				+"-- and loginId like {%loginId%}"
				+"-- and opName like {%opName%}"
				+"-- order by opId asc"
				;

		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		String orderSql = listResult.getOrderQl();
		super.assertEquals(" ORDER BY opName desc", orderSql);
	}
	
	public void testSort3() throws Exception
	{
		PageParam pageParam = new PageParam(1, 10, "loginId", "asc");
		
		String dynamicHql = "select operator from Operator operator"
				+"-- and roleId={roleId}"
				+"-- and loginId like {%loginId%}"
				+"-- and opName like {%opName%}"
				;

		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		String orderSql = listResult.getOrderQl();
		super.assertEquals(" ORDER BY loginId asc", orderSql);
	}
	
	public void testSortMulti() throws Exception
	{
		PageParam pageParam = new PageParam(1, 10, "opId", "asc");
		pageParam.addSort("opName", "desc");
		
		String dynamicHql = "select operator from Operator operator"
				+"-- and roleId={roleId}"
				+"-- and loginId like {%loginId%}"
				+"-- and opName like {%opName%}"
				;

		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		String orderSql = listResult.getOrderQl();
		super.assertEquals(" ORDER BY opId asc, opName desc", orderSql);
	}
	
	public void testLike()
	{
		PageParam pageParam = new PageParam(1, 10);
		pageParam.addCond("name", "测试");
		String dynamicHql = "SELECT OBJ.*, O.OP_NAME FROM CUSTOMER OBJ LEFT JOIN OPERATOR O ON OBJ.CHARGE_OP=O.OP_ID"
			+ "-- AND OBJ.NAME LIKE {%name%}"
			;
		IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);	
		assertEquals("SELECT OBJ.*, O.OP_NAME FROM CUSTOMER OBJ LEFT JOIN OPERATOR O ON OBJ.CHARGE_OP=O.OP_ID WHERE 1=1 AND OBJ.NAME LIKE ?", listResult.generate());
	}
}
