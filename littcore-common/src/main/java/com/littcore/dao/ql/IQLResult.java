package com.littcore.dao.ql;

public interface IQLResult
{

	/**
	 * 生成最终的SQL语句
	 * @return SQL字符串
	 */
	public String generate();

	/**
	 * 生成最终的SQL语句.
	 * 
	 * @param baseQl 基础SQL语句
	 * 
	 * @return SQL字符串
	 */
	public String generate(String baseQl);

	/**
	 * 生成最终的SQL语句.
	 * 
	 * @param baseQl 基础SQL语句
	 * @param orderQl 排序条件
	 * 
	 * @return SQL字符串
	 */
	public String generate(String baseQl, String orderQl);

	/**
	 * 自动生成统计总行数SQL(基于替换). 
	 * 
	 * @return SQL字符串
	 */
	public String generateCount();

	/**
	 * @return the params
	 */
	public Object[] getParams();

	/**
	 * @return the baseQl
	 */
	public String getBaseQl();

	/**
	 * @return the condQl
	 */
	public String getCondQl();

	/**
	 * @return the orderQl
	 */
	public String getOrderQl();

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex();

	/**
	 * @return the pageSize
	 */
	public int getPageSize();

}