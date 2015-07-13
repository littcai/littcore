package com.littcore.dao.ql;



public interface IQLCondBuilder
{
	/**
	 * 获得参数值.
	 * 
	 * @param paramName 参数名
	 * 
	 * @return the param
	 */
	public Object getParam(String paramName);

	/**
	 * 增加静态的QL查询条件
	 * @param condQl
	 */
	public void addStaticCond(String staticCondQl);

	/**
	 * 生成QL结果对象.
	 * 
	 * @return IQLResult
	 */
	public IQLResult generate();
	
}