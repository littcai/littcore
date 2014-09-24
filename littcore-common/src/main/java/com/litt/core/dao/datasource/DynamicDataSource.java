
package com.litt.core.dao.datasource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * 动态数据源.
 * 
 * <pre><b>描述：</b>
 *    根据访问数据源时提交的用户属性，动态路由选择对应的数据源.
 * 用途：1、应用级管理数据库的load-balance、failover  
 *     2、SaaS模式中动态数据源的切换 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-6-7
 * @version 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private Logger logger = Logger.getLogger(DynamicDataSource.class.getName());

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() {		
		return RoutingDataSourceContextHolder.getContext();
	}
	
}
