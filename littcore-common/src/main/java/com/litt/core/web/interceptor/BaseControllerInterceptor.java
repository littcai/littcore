package com.litt.core.web.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.litt.core.shield.security.SecurityContext;
import com.litt.core.shield.security.SecurityContextHolder;
import com.litt.core.shield.vo.ILoginVo;
import com.litt.core.util.StringUtils;

/** 
 * 
 * SpringMVC拦截器基础类.
 * 
 * <pre><b>描述：</b>
 *   
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
public abstract class BaseControllerInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseControllerInterceptor.class);	
	
	/**
	 * 方法缓存.
	 * 无需考虑并发，同名情况覆盖也不会有问题
	 * KEY : className.methodName
	 * VALUE : Method对象
	 */
	private Map<String, Method> methodCache = new HashMap<String, Method>();	
	
	/**
	 * 根据方法名获得方法.
	 * 由于SpringMVC不支持同构函数，因此一个名字只会对应一个方法
	 * @param classObj 类对象
	 * @param methodName 方法名
	 * @return
	 */
	public Method getMethod(Class classObj, String methodName)
	{
		String className = classObj.getName();
		String key = new StringBuffer().append(className).append(".").append(methodName).toString();
		Method method = this.methodCache.get(key);
		if(method!=null)
			return method;
		else
		{	
			Method[] methods = classObj.getMethods();
			for(int i=0;i<methods.length;i++)
			{
				if(methods[i].getName().equals(methodName))
				{
					this.methodCache.put(key, methods[i]);	//将找到的方法缓存起来，用来快速匹配
					return methods[i];
				}
			}
			return null;
		}	
	}
	
	/**
	 * 从URL中获取方法名.
	 *
	 * @param lookupPath the lookup path
	 * @return the method name from uri
	 */
	public String getMethodNameFromURI(String lookupPath)
	{
		String methodName = lookupPath.substring(lookupPath.lastIndexOf("/")+1);
		methodName = methodName.substring(0, methodName.lastIndexOf("."));
		return methodName;
	}
	
	/**
	 * 从URL中获取方法名
	 * @param handler
	 * @param lookupPath
	 * @return
	 */
	public String getMethodNameFromURI(Object handler, String lookupPath) {
		//取最后一个斜线与最后斜线到第一个点之间的字符串，即为方法名	
		String packageName = handler.getClass().getPackage().getName();
		//logger.debug("包名：{}", new Object[]{packageName});		
		//按规则，包名最后去掉Web即为实际包名；
		packageName = packageName.substring(0, packageName.lastIndexOf("."));
		packageName = packageName.substring(packageName.lastIndexOf(".")+1);
		//logger.debug("实际父包名：{}", new Object[]{packageName});	
		
		String className = handler.getClass().getSimpleName();	//类名
		//logger.debug("类名：{}", new Object[]{className});
		//类名去掉Controller并且首字母小写即为映射路径
		className = className.substring(0, className.lastIndexOf("Controller"));
		className = StringUtils.uncapitalize(className);
		//logger.debug("映射类名：{}", new Object[]{className});
		//从requestURI找到路径映射，从而截取出后缀字符串到.为方法名
		String methodName = StringUtils.substringBetween(lookupPath, packageName+"/"+className+"/", ".");
		//logger.debug("方法名：{}", new Object[]{methodName});
		return methodName;
	}

	/**
	 * 获取登录操作员信息.
	 *
	 * @return ILoginVo
	 */
	public ILoginVo getLoginVo()
	{
		SecurityContext context = SecurityContextHolder.getContext();
		if(context!=null)
			return context.getLoginVo();
		else 
			return null;
	}
	
	
}
