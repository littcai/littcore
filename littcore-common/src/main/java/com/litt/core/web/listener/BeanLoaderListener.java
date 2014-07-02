package com.litt.core.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.litt.core.common.BeanManager;


/**
 * <b>标题：</b>Spring Bean加载监听器.
 * <pre><b>描述</b>
 *    取得web容器内的context环境，通过单例模式提供给系统的其他调度者，需要与BeanManager配合使用 
 * </pre>
 * 
 * <pre><b>修改记录:</b> 
 * 2012-10-11  增加约束，禁用同名Bean覆盖和循环依赖
 * </pre> 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 */
public class BeanLoaderListener extends ContextLoaderListener {	
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoader#customizeContext(javax.servlet.ServletContext, org.springframework.web.context.ConfigurableWebApplicationContext)
	 */
	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {	
		super.customizeContext(servletContext, applicationContext);	
		XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext)applicationContext;
		boolean allowBeanDefinitionOverriding  = false;
		boolean allowCircularReferences = false;
		if("true".equals(servletContext.getInitParameter("allowBeanDefinitionOverriding")))
		{
			allowBeanDefinitionOverriding = true;			
		}
		if("true".equals(servletContext.getInitParameter("allowCircularReferences")))
		{
			allowCircularReferences = true;		
		}	
		xmlWebApplicationContext.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);	//禁用同名Bean定义覆盖
		xmlWebApplicationContext.setAllowCircularReferences(allowCircularReferences);			//禁用循环依赖
	}

    /**
     * 进行容器初始化，并将上下文传播到BeanManager中.
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext context = event.getServletContext();
        context.log("Attempt to inject WebApplicationContext into [com.litt.core.common.BeanManager]...");
        BeanManager.setApplication(WebApplicationContextUtils
                .getRequiredWebApplicationContext(context));
        BeanManager.setServletContext(event.getServletContext());
    }

}
