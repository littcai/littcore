package com.littcore.dao.ql;

import org.apache.commons.lang.StringUtils;

import com.littcore.util.ValidateUtils;


/**
 * 通过QlBuilder生成的结果对象.
 * 
 * <pre><b>描述：</b>
 * 	包含了组装好的SQL语句和参数数组
 *  自动生成WHERE 1=1，代码中无需编写
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-11-28
 * @version 1.0
 */
public class QLResult implements IQLResult
{	
	
	/**
	 * 简化首个AND判断的WHERE 1=1.
	 */
	public static final String WHERE11 = " WHERE 1=1";
	
	/** 基本SQL. */
	private String baseQl;
	
	/** 查询条件. */
	private String condQl;
	
	/** 排序条件. */
	private String orderQl;
	
	/** 查询条件对象. */
	private Object[] params;	
	
	/** 分页页码.	 */
	private int pageIndex;
	
    /**
     * 分页大小.
     */
    private int pageSize;
	
	/**
	 * 生成最终的SQL语句
	 * @return SQL字符串
	 */
	public String generate()
	{
		StringBuffer ret = new StringBuffer(200).append(baseQl)
												.append(WHERE11)
												.append(condQl);		
		
		if(!ValidateUtils.isEmpty(orderQl))
			ret.append(orderQl);
		return ret.toString();
	}
	
	/**
	 * 生成最终的SQL语句.
	 * 
	 * @param baseQl 基础SQL语句
	 * 
	 * @return SQL字符串
	 */
	public String generate(String baseQl)
	{
		this.baseQl = baseQl;
		StringBuffer ret = new StringBuffer(200).append(baseQl)
												.append(WHERE11)
												.append(condQl);

		if(!ValidateUtils.isEmpty(orderQl))
			ret.append(orderQl);
		return ret.toString();
	}
	
	/**
	 * 生成最终的SQL语句.
	 * 
	 * @param baseQl 基础SQL语句
	 * @param orderQl 排序条件
	 * 
	 * @return SQL字符串
	 */
	public String generate(String baseQl,String orderQl)
	{
		this.baseQl = baseQl;
		StringBuffer ret = new StringBuffer(200).append(baseQl)
												.append(WHERE11)
												.append(condQl)
												.append(orderQl);
		return ret.toString();
	}		
	
	/**
	 * 自动生成统计总行数SQL(基于替换). 
	 * 
	 * @return SQL字符串
	 */
	public String generateCount()
	{
		String upperCase = this.baseQl.toUpperCase();
		//将FROM前内容替换为SELECT COUNT(*)		
		int index = StringUtils.indexOf(upperCase, "FROM");
		String baseCountQl = this.baseQl.substring(index);		
		return StringUtils.join(new Object[]{"SELECT COUNT(*) ", baseCountQl, WHERE11, this.condQl});				
	}

	/**
	 * @return the params
	 */
	public Object[] getParams()
	{
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params)
	{
		this.params = params;
	}

	/**
	 * @return the baseQl
	 */
	public String getBaseQl()
	{
		return baseQl;
	}

	/**
	 * @param baseQl the baseQl to set
	 */
	public void setBaseQl(String baseQl)
	{
		this.baseQl = baseQl;
	}

	/**
	 * @return the condQl
	 */
	public String getCondQl()
	{
		return condQl;
	}

	/**
	 * @param condQl the condQl to set
	 */
	public void setCondQl(String condQl)
	{
		this.condQl = condQl;
	}

	/**
	 * @return the orderQl
	 */
	public String getOrderQl()
	{
		return orderQl;
	}

	/**
	 * @param orderQl the orderQl to set
	 */
	public void setOrderQl(String orderQl)
	{
		this.orderQl = orderQl;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex()
	{
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
}
