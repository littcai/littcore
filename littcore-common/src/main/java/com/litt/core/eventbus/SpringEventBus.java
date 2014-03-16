package com.litt.core.eventbus;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.eventbus.EventBus;

public class SpringEventBus extends EventBus implements IEventBus {
	
	/* (non-Javadoc)
	 * @see com.transoft.nms.eventbus.IEventBus#publish(java.lang.Object)
	 */
	@Override
	public void publish(Object event)
	{		
		//如果是CompleteEvent，则不会立即触发，而是在事务正确提交后发布
		if(event instanceof ICompleteEvent)
		{
			if (!TransactionSynchronizationManager.isSynchronizationActive())
				super.post(event);
			else
			{			
	//			TransactionSynchronizationManager.registerSynchronization(new EventBusTransactionSynchronization(pool,
	//                    subscriber, event));
			}
		}
		else {
			this.post(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.transoft.nms.eventbus.IEventBus#register(com.transoft.nms.eventbus.IEventBusListener)
	 */
	@Override
	public void register(IEventBusListener listener) {
		super.register(listener);		
	}

}
