package com.littcore.common;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * <b>标题：</b>SpringBean管理器，用来从容器外获取Bean的实例.
 * <pre><b>描述：</b>
 *  使用ApplicationContext，可直接读取配置文件，也可直接用容器部署的ApplicationContext替换
 *  使用类路径加载的时候默认配置文件名为applicationContext-*.xml
 * </pre> 
 * 
 * <pre><b>修改记录：</b>
 * 时间：2008-08-25
 * 变动：采用单例模式重构，由于ApplicationContext和ServletContext只会有一个实例，故不会有多线程问题，但是有可能会重复加载
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 时间：2008-08-26
 * 变动：采用全静态变量，去除不实用的单例模式。
 *      非Web程序可使用loadFromClassPath方法从类路径中加载配置文件
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 *  
 * @since 2008-08-25  
 * @version 2.0 beta
 * 
 * @since 2008-08-26  
 * @version 2.0
 * 
 */
public final class BeanManager
{
	private static ApplicationContext application;	//Spring
	private static ServletContext servletContext;	
    
    /**
     * 禁止生成BeanManager的实例.
     */    
	private BeanManager(){};    
    
    /**
	 * 根据类路径加载配置文件并进行容器初始化.
	 * 在Web项目下，由初始化监听器进行初始化并注入
	 * 
	 * @param configFileName
	 *            配置文件名称数组
	 */
	public static void loadFromClassPath(String[] configFileName)
	{	
		if(configFileName==null)
			configFileName = new String[]{"classpath:applicationContext-*.xml"};
		BeanManager.application = new ClassPathXmlApplicationContext(configFileName);	
	}
	
    /**
     * 从容器中获得一个Bean的实例.
     * @param beanName Bean的名称
     * @return Object Bean的实例
     */
	public static Object getBean(String beanName)
	{	
		return BeanManager.application.getBean(beanName);
	}
	
    /**
     * 从容器中获得一个Bean的实例.
     * @param beanName Bean的名称
     * @return Object Bean的实例
     */
	public static <T> T getBean(String beanName, Class<T> beanClass)
	{	
		return BeanManager.application.getBean(beanName, beanClass);
	}	
	
	   /**
     * 从容器中获得一个Bean的实例.
     * @param beanName Bean的名称
     * @return Object Bean的实例
     */
	public static <T> T getBean (Class<T> beanClass)
	{	
		return BeanManager.application.getBean(beanClass);
	}
	
    /**
     * 从容器中获得国际化消息.
     * 需配置messageSource，且ID=messageSource
     *
     * @param key 消息键值
     * @param locale 国际化方言
     * @return String 消息
     */
    public static String getMessage(String key, Locale locale)
    {   
        return BeanManager.getMessage(key, null, locale);
    }
    
    /**
     * 从容器中获得国际化消息.
     * 需配置messageSource，且ID=messageSource
     *
     * @param key 消息键值
     * @param param 参数值
     * @param locale 国际化方言
     * @return String 消息
     */
    public static String getMessage(String key, Object param, Locale locale)
    {   
        return BeanManager.getMessage(key, new Object[]{param}, locale);
    }    
    
    /**
     * 从容器中获得国际化消息.
     * 需配置messageSource，且ID=messageSource
     * 
     * @param key 消息键值
     * @param params 参数
     * @param locale 区域
     * 
     * @return String 消息
     */
    public static String getMessage(String key, Object[] params, Locale locale)
    {   
        return BeanManager.application.getMessage(key, params, locale);
    }    

	/**
	 * @return the application
	 */
	public static ApplicationContext getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	public static void setApplication(ApplicationContext application) {
		BeanManager.application = application;
	}

	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public static void setServletContext(ServletContext servletContext) {
		BeanManager.servletContext = servletContext;
	}


   

}