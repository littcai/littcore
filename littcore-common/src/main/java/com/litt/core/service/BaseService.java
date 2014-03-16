package com.litt.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.dao.BaseHibernateDao;
import com.litt.core.dao.BaseJdbcDao;
import com.litt.core.exception.NotLoginException;
import com.litt.core.shield.security.SecurityContext;
import com.litt.core.shield.security.SecurityContextHolder;
import com.litt.core.shield.vo.ILoginVo;

/**
 * 
 * <b>标题：</b> 业务层基类.
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-27
 * @version 1.0
 */
public class BaseService
{	
	/** 系统日志对象. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 基础Dao接口.
	 * 通过在业务层直接传入该实现以简化掉DAO层
	 */
	protected BaseHibernateDao hibernateDao;
	
	/**
	 * 基础Dao接口.
	 * 通过在业务层直接传入该实现以简化掉DAO层
	 */
	protected BaseJdbcDao jdbcDao;		

	/**
	 * Sets the hibernate dao.
	 * 
	 * @param hibernateDao the hibernateDao to set
	 */
	public void setHibernateDao(BaseHibernateDao hibernateDao)
	{
		this.hibernateDao = hibernateDao;
	}

	/**
	 * Gets the hibernate dao.
	 * 
	 * @return BaseHibernateDao
	 */
	public BaseHibernateDao getHibernateDao()
	{
		return hibernateDao;
	}

	/**
	 * @return the jdbcDao.
	 */
	public BaseJdbcDao getJdbcDao()
	{
		return jdbcDao;
	}

	/**
	 * @param jdbcDao the jdbcDao to set.
	 */
	public void setJdbcDao(BaseJdbcDao jdbcDao)
	{
		this.jdbcDao = jdbcDao;
	}
}
