package com.littcore.module.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.littcore.common.Utility;
import com.littcore.exception.BusiCodeException;
import com.littcore.exception.BusiException;
import com.littcore.module.annotation.Func;
import com.littcore.module.annotation.Permission;
import com.littcore.shield.vo.ILoginVo;
import com.littcore.web.interceptor.BaseControllerInterceptor;

/** 
 * 
 * 权限拦截器.
 * 
 * <pre><b>描述：</b>
 *    通过方法级别的注解检查当前登陆操作员的操作权限    
 * 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    Date:2013-02-05 支持新的Func注解，在有Func注解的情况下优先使用；否则检查是否有Permission注解 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-7-1
 * @version 1.0
 *
 * 
 */
public class PermissionInterceptor extends BaseControllerInterceptor implements MethodInterceptor, HandlerInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
	
	public PermissionInterceptor()
	{
		if(logger.isInfoEnabled())
		{
			logger.info("Permission Interceptor initialized...");
		}
	}		

	/**
	 * 权限校验拦截(用于Service层).
	 * 
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		Method method = invocation.getMethod();   
		//String methodName= method.getName();	//TODO 这里可以增加匹配特定方法名的限制，以避免检查所有方法
		this.checkPermission(method);		//校验权限
		return invocation.proceed();
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String methodName = Utility.trimNull(request.getParameter("method"), "unspecified");
		Method method = this.getMethod(handler.getClass(), methodName);
		this.checkPermission(method);		//校验权限
		return true;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{		
		
	}


	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception
	{		
		
	}

	
	/**
	 * 校验权限.
	 * @param method
	 * @throws BusiException
	 */
	private void checkPermission(Method method) throws BusiException
	{
		boolean isFunction = method.isAnnotationPresent(Func.class);	//是否找到功能注解			
		if(isFunction)
		{
			Func function = method.getAnnotation(Func.class);				
			boolean isEnablePermission = function.enablePermission();
			if(isEnablePermission)	//检查方法执行权限
			{				
				this.checkPermission(function.moduleCode(), function.funcCode());
			}			
		}
		else
		{		
			boolean isPermission = method.isAnnotationPresent(Permission.class);	//是否找到权限注解		
			if(isPermission)
			{
				Permission permission = method.getAnnotation(Permission.class);	
				boolean isEnablePermission = permission.enablePermission();
				if(isEnablePermission)	//检查方法执行权限
				{				
					this.checkPermission(permission.moduleCode(), permission.funcCode());
				}
			}		
		}
	}	
	
	/**
	 * 检查权限
	 *
	 */
	private void checkPermission(String moduleCode, String funcCode) throws BusiException
	{
		ILoginVo loginVo = this.getLoginVo();
		if(loginVo==null)
			throw new BusiCodeException("error.permission.denied");
		else
		{
			boolean isPermitted = loginVo.withPermission(moduleCode + "." + funcCode);
			if(!isPermitted)
			{
				logger.error("Operator:{} access module:{} func:{} without permission.", new Object[]{loginVo.getLoginId(), moduleCode, funcCode});
				throw new BusiCodeException("error.permission.denied");
			}			
		}
	}	
	
}
