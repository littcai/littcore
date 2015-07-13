package com.littcore.module.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.littcore.common.Utility;
import com.littcore.exception.BusiException;
import com.littcore.shield.vo.ILoginVo;
import com.littcore.web.interceptor.BaseControllerInterceptor;
import com.littcore.log.Appender;
import com.littcore.log.OpLogVo;
import com.littcore.log.impl.JDBCAppender;
import com.littcore.module.annotation.Func;

/** 
 * 
 * 功能项拦截器.
 * 
 * <pre><b>描述：</b>
 *    基于annotation的功能项拦截器，拦截于控制层。
 * 整合了操作日志和权限校验的功能，精简处理逻辑和方法调用
 * 注：不能与OpLogInterceptor和PermissionInterceptor同时使用
 * 
 * 注：默认会往request中注入__moduleCode和__funcCode，用于UI端处理
 *    
 * HandlerInterceptor说明：
 * 	发起请求,进入拦截器链，运行所有拦截器的preHandle方法，
 *  1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。 
 *  2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法. 当有拦截器抛出异常时,会从当前拦截器往回执行所有拦截器的afterCompletion方法   
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-02-05
 * @version 1.0
 *
 */
public class FuncInterceptor extends BaseControllerInterceptor implements HandlerInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(FuncInterceptor.class);	
	
	/** 日志缓存队列. */
	private static final BlockingQueue<OpLogVo> logCacheQueue = new LinkedBlockingQueue<OpLogVo>(1000);
	
	private Appender appender; 
	
	/** 数据源. */
	private DataSource dataSource;
	
	/** 日志表名. */
	private String tableName;
	
	/** 是否启用日志记录. */
	private boolean logEnabled = true;
	
	/** 单线程用于异步数据存储. */
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	
	/** 分配任务的Future. */
	private Future<String> future;
	
	public FuncInterceptor()
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Func Interceptor initialized...");
		}
	}	

	public void init()
	{
		if(Utility.isEmpty(tableName))
			this.appender = new JDBCAppender(logCacheQueue,dataSource);
		else
			this.appender = new JDBCAppender(logCacheQueue,dataSource,tableName);
		future = exec.submit(appender);		
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
		logger.debug("Request URI:{}", new Object[]{requestURI});
		String methodName = getMethodNameFromURI(handler, requestURI);
		Method method = this.getMethod(handler.getClass(), methodName);
		if(method==null)	//没找到方法
		{
			logger.error("Can't find method:{} in controller:{}", new Object[]{methodName, handler.getClass().getName()});
			return true;
		}
		
		boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解		
		if(isFunction)
		{
			Func function = method.getAnnotation(Func.class);	
			//1、检查操作权限
			if(function.enablePermission())
			{
				this.checkPermission(function.moduleCode(), function.funcCode());
			}
			//2、记录操作日志
//			boolean isEnableLog = function.enableLog();
//			if(isEnableLog)	
//			{				
//				try
//				{
//					this.addLog(function,"",true);
//				}
//				catch (Exception e)
//				{
//					boolean isLogFail = function.enableLogFail();		
//					if(isEnableLog&&isLogFail)//需要记录失败日志时才记录
//					{					
//						this.addLog(function,e.getMessage(),false);
//					}
//					throw e;
//				}
//			}						
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
		String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
		logger.debug("Request URI:{}", new Object[]{lookupPath});
		String methodName = getMethodNameFromURI(handler, lookupPath);
		Method method = this.getMethod(handler.getClass(), methodName);
		if(method==null)	//没找到方法
		{
			logger.error("Can't find method:{} in controller:{}", new Object[]{methodName, handler.getClass().getName()});		
			return;
		}
		
		boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解		
		if(isFunction)
		{
			Func function = method.getAnnotation(Func.class);		
			//request注入
			request.setAttribute("__moduleCode", function.moduleCode());
			request.setAttribute("__funcCode", function.funcCode());
			
			//2、记录操作日志
			boolean isEnableLog = function.enableLog();
			if(logEnabled && isEnableLog)	
			{				
				try
				{
					this.addLog(function, "", true);
				}
				catch (Exception e)
				{
					boolean isLogFail = function.enableLogFail();		
					if(isLogFail)//需要记录失败日志时才记录
					{					
						this.addLog(function, e.getMessage(), false);
					}
					throw e;
				}
			}						
		}			
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
		/*
		 * 发生异常，记录异常日志
		 */
		if(e!=null)
		{
			String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
			logger.debug("Request URI:{}", new Object[]{lookupPath});
			String methodName = getMethodNameFromURI(handler, lookupPath);
			Method method = this.getMethod(handler.getClass(), methodName);
			if(method==null)	//没找到方法
			{
				logger.error("Can't find method:{} in controller:{}", new Object[]{methodName, handler.getClass().getName()});	
				return;
			}
			
			boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解		
			if(isFunction)
			{
				Func function = method.getAnnotation(Func.class);				
				//2、记录操作日志
				boolean isEnableLog = function.enableLog();
				boolean isLogFail = function.enableLogFail();		
				if(logEnabled && isEnableLog && isLogFail)	
				{				
					try
					{
						this.addLog(function, e.getMessage(), false);
					}
					catch (Exception ex)
					{						
						this.addLog(function, ex.getMessage(), false);								
					}
				}						
			}			
		}
	}

	
	
	/**
	 * 记录
	 * @param opLog 日志描述对象
	 * @param status 操作结果
	 */
	private void addLog(Func func,String content,boolean status) throws Exception
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
			logger.debug("Insert one OpLog.");
		}
		logCacheQueue.put(opLogVo);		
	}
		
	/**
	 * 检查权限
	 *
	 */
	private void checkPermission(String moduleCode, String funcCode) throws BusiException
	{
		ILoginVo loginVo = this.getLoginVo();
		if(loginVo==null)
			throw new BusiException("User not login.");
		else
		{
			boolean isPermitted = loginVo.withPermission(moduleCode+"."+funcCode);
			if(!isPermitted)
			{
				logger.error("Operator:{} access module:{} func:{} without permission.", new Object[]{loginVo.getLoginId(), moduleCode, funcCode});
				throw new BusiException("Permission denied.");
			}			
		}
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

	/**
	 * @return the logEnabled
	 */
	public boolean isLogEnabled() {
		return logEnabled;
	}

	/**
	 * @param logEnabled the logEnabled to set
	 */
	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}	

}
