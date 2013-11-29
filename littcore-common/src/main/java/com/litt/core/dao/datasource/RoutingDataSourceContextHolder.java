package com.litt.core.dao.datasource;

import com.litt.core.util.Assert;

/**
 * 租户上下文信息.
 * 
 * <pre><b>描述：</b>
 *    维护线程级的租户上下文信息。
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
public class RoutingDataSourceContextHolder {
	
	private static final ThreadLocal<RoutingCondition> context = new ThreadLocal<RoutingCondition>();// 线程本地环境
	
	/**
	 * 设置数据源类型.
	 *
	 * @param routingCondition 路由条件
	 */
    public static void setContext(RoutingCondition routingCondition){
    	Assert.notNull(routingCondition, "Routing condition can't be null.");
    	context.set(routingCondition);
    }
    
    /**
     * 获取数据源类型 
     * @return
     */
    public static RoutingCondition getContext(){
        return context.get();
    }
    
    /**
     * 清除数据源类型 .
     */
    public static void remove(){
    	context.remove();
    }

}
