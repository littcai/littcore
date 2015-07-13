package com.littcore.dao.datasource;

/**
 * 数据源路由条件.
 * 
 * <pre><b>描述：</b>
 *    动态数据源选择所依据的条件
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
public class RoutingCondition {
	
	/** 租户ID. */
	private int tenantId;

	/**
	 * @return the tenantId
	 */
	public int getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

}
