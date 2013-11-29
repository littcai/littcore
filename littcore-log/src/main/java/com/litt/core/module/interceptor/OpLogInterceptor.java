package com.litt.core.module.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.litt.core.common.Utility;
import com.litt.core.log.Appender;
import com.litt.core.log.OpLogVo;
import com.litt.core.log.impl.JDBCAppender;
import com.litt.core.module.annotation.Func;
import com.litt.core.shield.vo.ILoginVo;
import com.litt.core.util.StringUtils;
import com.litt.core.web.interceptor.BaseControllerInterceptor;

/** 
 * 
 * 操作日志拦截器.
 * 
 * <pre><b>描述：</b>
 *    基于annotation的操作日志拦截器，拦截于控制层。入库采用阻塞队列缓存的方式。 
 *    
 * HandlerInterceptor说明：
 * 	发起请求,进入拦截器链，运行所有拦截器的preHandle方法，
 *  1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。 
 *  2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法. 当有拦截器抛出异常时,会从当前拦截器往回执行所有拦截器的afterCompletion方法   
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2013-02-05 [bugfix] 从URL中获取methodName的方法有误，导致没有取到方法名
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-1-12
 * @version 1.0
 *
 */
public class OpLogInterceptor extends BaseControllerInterceptor implements MethodInterceptor, HandlerInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(OpLogInterceptor.class);	
	
	/** 日志缓存队列. */
	private static final BlockingQueue<OpLogVo> logCacheQueue = new LinkedBlockingQueue<OpLogVo>(1000);
	
	private Appender appender; 
	
	/** 数据源. */
	private DataSource dataSource;
	
	/** 日志表名. */
	private String tableName;
	
	/** 单线程用于异步数据存储. */
	private ExecutorService exec = Executors.newSingleThreadExecutor(new ThreadFactory(){

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		}
		
	});
	
	/** 分配任务的Future. */
	private Future<String> future;
	
	public OpLogInterceptor()
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Operation Log Interceptor initialized...");
		}
	}	

	public void init()
	{
		if(Utility.isEmpty(tableName))
			this.appender = new JDBCAppender(logCacheQueue,dataSource);
		else
			this.appender = new JDBCAppender(logCacheQueue,dataSource,tableName);
		this.future = exec.submit(appender);			
	}

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable
	{		
		Method method = invocation.getMethod();   
		String methodName= method.getName();	//TODO 这里可以增加匹配特定方法名的限制，以避免检查所有方法
		boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解		
		if(isFunction)
		{
			Func function = method.getAnnotation(Func.class);				
			boolean isEnableLog = function.enableLog();
			Object obj = null;
			try
			{
				obj = invocation.proceed();	
				if(isEnableLog)	//记录操作日志
				{				
					this.addLog(function,"",true);
				}
			}
			catch(Throwable e)
			{
				boolean isLogFail = function.enableLogFail();		
				if(isEnableLog&&isLogFail)//需要记录失败日志时才记录
				{					
					this.addLog(function,e.getMessage(),false);
				}
				throw e;
			}
			return obj;
		}	
		else
		{
			return invocation.proceed();	
		}
	}
	
	/**
	 * 进入Controller前调用，用于记录SpringMVC中控制器的操作日志.
	 *
	 * @param request the request
	 * @param response the response
	 * @param handler the handler
	 * @return true:表示继续流程（如调用下一个拦截器或处理器）,false:表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应.
	 * @throws Exception the exception
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String requestURI = request.getRequestURI();
		logger.debug("请求的URI：{}", new Object[]{requestURI});
		//取最后一个斜线与最后斜线到第一个点之间的字符串，即为方法名	
		String packageName = handler.getClass().getPackage().getName();
		logger.debug("包名：{}", new Object[]{packageName});		
		//按规则，包名最后去掉Web即为实际包名；
		packageName = packageName.substring(0, packageName.lastIndexOf("."));
		packageName = packageName.substring(packageName.lastIndexOf(".")+1);
		logger.debug("实际父包名：{}", new Object[]{packageName});	
		
		String className = handler.getClass().getSimpleName();	//类名
		logger.debug("类名：{}", new Object[]{className});
		//类名去掉Controller并且首字母小写即为映射路径
		className = className.substring(0, className.lastIndexOf("Controller"));
		className = StringUtils.uncapitalize(className);
		logger.debug("映射类名：{}", new Object[]{className});
		//从requestURI找到路径映射，从而截取出后缀字符串到.为方法名
		String methodName = StringUtils.substringBetween(requestURI, packageName+"/"+className+"/", ".");
		logger.debug("方法名：{}", new Object[]{methodName});
		Method method = this.getMethod(handler.getClass(), methodName);
		if(method==null)	//没找到方法
		{
			logger.error("未在控制器：{}中找到方法：{}", new Object[]{handler.getClass().getName(), methodName});
			return true;
		}
		
		boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解		
		if(isFunction)
		{
			Func function = method.getAnnotation(Func.class);				
			boolean isEnableLog = function.enableLog();
			if(isEnableLog)	//记录操作日志
			{				
				try
				{
					this.addLog(function,"",true);
				}
				catch (Throwable e)
				{
					throw new Exception("Record opLog error.", e);
				}
			}						
		}			
		return true;
	}
	
	/**
	 * 完成Controller方法后调用.
	 *
	 * @param request the request
	 * @param response the response
	 * @param handler the handler
	 * @param modelAndView the model and view
	 * @throws Exception the exception
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		logger.debug("postHandle");
	}
	
	/**
	 * 最后调用.
	 * 当方法执行失败抛出异常时，能在此处捕获到
	 * 
	 *
	 * @param request the request
	 * @param response the response
	 * @param handler the handler
	 * @param e the e
	 * @throws Exception the exception
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception
	{
		logger.debug("afterCompletion");
		if(e!=null)
		{
			logger.error("error:", e);
		}
	}

	
	
	/**
	 * 记录
	 * @param opLog 日志描述对象
	 * @param status 操作结果
	 */
	private void addLog(Func func,String content,boolean status) throws Throwable
	{				
		OpLogVo opLogVo = new OpLogVo();		
		opLogVo.setFuncType(func.funcType());
		opLogVo.setModuleCode(func.moduleCode());
		opLogVo.setFuncCode(func.funcCode());
		opLogVo.setContent(content);
		opLogVo.setOpDatetime(new Date());
		opLogVo.setStatus(status);
		ILoginVo loginVo = this.getLoginVo();
		if(loginVo!=null)
		{
			opLogVo.setOpId(loginVo.getOpId());
			opLogVo.setOpName(loginVo.getOpName());
			opLogVo.setOpIp(loginVo.getLoginIp());
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("插入一条操作日志.");
		}
		logCacheQueue.put(opLogVo);
	}
	
	/**
	 * 拦截器关闭时同时停止日志线程
	 *
	 */
	public void destroy()
	{		
		/*
		 * 尝试关闭线程池，并拒绝新任务
		 */
		exec.shutdown();			
		/*
		 * 尝试关闭运行中任务
		 */
		try {
						
			/*
			 * 设置停止标志，等待返回结果，超时则强制定制，丢失部分数据
			 * 注：等待时间为appender的一个刷新周期，已确保缓存数据被入库
			 */
			try {
				appender.setStop(true);
				future.get(appender.getFlushTime()+5000, TimeUnit.MILLISECONDS);	
			} catch (ExecutionException e) {
				logger.error("Get OpLog Appender ret failed.", e);
			} catch (TimeoutException e) {
				logger.error("Get OpLog Appender ret timeout.", e);
			}
			
			//检查任务是否完成，未完成则强制停止
			if(!future.isDone())
			{
				logger.warn("OpLog Appender did't stop smoothly, force stop.");
				future.cancel(true);			
			}		
			if(!exec.isTerminated())
			{
			     // Wait a while for existing tasks to terminate
			     if (!exec.awaitTermination(60, TimeUnit.SECONDS)) 
			     {
			    	 exec.shutdownNow();	//force shutdown		    	 
			    	// Wait a while for tasks to respond to being cancelled
			         if (!exec.awaitTermination(60, TimeUnit.SECONDS))
			             logger.error("OpLog Thread Pool did not terminate.");
			     }
			}
		} catch (InterruptedException ie) {
		     // (Re-)Cancel if current thread also interrupted
			 exec.shutdownNow();
		     // Preserve interrupt status
		     Thread.currentThread().interrupt();
	    }
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}	
	
	public static void main(String[] args) throws Exception{
		OpLogInterceptor inceptor = new OpLogInterceptor();
		inceptor.init();
		Thread.currentThread().sleep(1000);
		inceptor.destroy();
	}

}
