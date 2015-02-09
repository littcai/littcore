package com.litt.core.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.CoreConstants;
import com.litt.core.converter.beanutils.DateConverter;

/**
 *  
 * <b>标题:</b> 系统初始化任务.
 * <pre><b>描述:</b> 
 *   配置文件读取、数据缓存等
 * </pre>
 * 
 * <pre><b>修改记录:</b> 
 *   2008-09-22 增加读取系统配置文件方法
 *   2010-08-06 1、去除读取系统配置文件方法，由业务系统自行扩展
 *   			2、继承于BeanLoaderListener，整合成单一初始化监听器
 * </pre> 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-3-29
 * @version 1.0
 *
 */
public class InitSystemListener extends BeanLoaderListener {

    private static final Log logger = LogFactory.getLog(InitSystemListener.class);

	/**
	 * 执行容器销毁相关任务.
	 * 
	 * @param event 容器事件对象
	 */
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}


	/**
	 * 执行容器初始化相关任务.
	 * 1、设置系统运行根目录为运行时绝对目录
	 * 2、校验license
	 * 	2.1、从Web容器初始参数中获取证书文件路径
	 *  2.2、将证书校验结果存入容器中
	 *  2.3、在各过滤器、监听器中可监控该校验结果
	 * 
	 * @param event 容器事件对象
	 *            
	 */
	public void contextInitialized(ServletContextEvent event)
	{
		//初始化系统配置文件
		logger.info("System initializing...");	
		super.contextInitialized(event);
		ServletContext application = event.getServletContext();
		CoreConstants.ROOT_PATH = application.getRealPath("/");	
		logger.info("Run-time root path - "+CoreConstants.ROOT_PATH);		
		//register date converter
		ConvertUtils.register(new DateConverter(), java.util.Date.class); 
	}
}
