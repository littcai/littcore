package com.litt.core.eventbus;

/**
 * 事件总线接口.
 * 
 * <pre>
 * <b>描述：</b>
 * 		开发标准接口.
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
public interface IEventBus {
	
	/**
	 * 发布事件.
	 *
	 * @param event the event
	 */
	public void publish(Object event);
	
	/**
	 * 注册订阅监听器.
	 *
	 * @param listener the listener
	 */
	public void register(IEventBusListener listener);

}
