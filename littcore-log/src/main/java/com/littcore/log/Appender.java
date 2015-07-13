package com.littcore.log;

import java.util.concurrent.Callable;

/** 
 * 
 * 日志记录接口.
 * 
 * <pre><b>描述：</b>
 *    采用独立线程+缓存的形式记录日志. 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-1-12
 * @version 1.0
 *
 */
public interface Appender extends Callable<String>
{
	
	/**
	 * 设置停止标志.
	 *
	 * @param isStop 是否停止
	 */
	public void setStop(boolean isStop);
	
	/**
	 * @return the flushTime
	 */
	public long getFlushTime();
}
