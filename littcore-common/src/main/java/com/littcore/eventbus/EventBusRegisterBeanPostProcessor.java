package com.littcore.eventbus;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring事件总线管理器.
 * 
 * <pre>
 * <b>描述：</b>
 * 		将事件总线与Spring整合
 * 注：由于一个EventBus实例只有一个消息通道，实际上所有的监听器都会收到发布的消息
 * </pre>
 * 
 * <pre>
 * <b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-04-15
 * @version 1.0
 */
public class EventBusRegisterBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
	
	private ApplicationContext context;
	
	private IEventBus eventBus;	
	
	private Object preInitializedBean;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		this.preInitializedBean = bean;
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof IEventBusListener) 
		{			
			/*
			 * 如果是经过AOP代理的类，这里获取到的是Proxy对象，将导致无法获取到原生对象的注解
			 */
            if (AopUtils.isAopProxy(bean))
            {  
            	registerToEventBus((IEventBusListener)preInitializedBean);
            }
            else
            {	
            	registerToEventBus((IEventBusListener)bean);
            }
        }
		return bean;
	}
	
	/**
	 * 监听器注册到EventBus.
	 * @param bean
	 */
	private void registerToEventBus(IEventBusListener bean) {
		eventBus.register(bean);
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;		
	}

	/**
	 * @param eventBus the eventBus to set
	 */
	public void setEventBus(SpringEventBus eventBus) {
		this.eventBus = eventBus;
	}

}
