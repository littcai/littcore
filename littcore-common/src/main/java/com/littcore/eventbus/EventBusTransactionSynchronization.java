package com.littcore.eventbus;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import com.google.common.eventbus.EventBus;

/**
 * 事务同步器.
 * 
 * <pre>
 * <b>描述：</b>
 * 		在事务提交后发布消息
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
public class EventBusTransactionSynchronization extends TransactionSynchronizationAdapter {
	
	private EventBus eventBus;
	
	private Object event;
	
	public EventBusTransactionSynchronization(EventBus eventBus, Object event)
	{
		this.eventBus = eventBus;
		this.event = event;
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#afterCommit()
	 */
	@Override
	public void afterCommit() {
		super.afterCommit();
		eventBus.post(event);
	}



}
