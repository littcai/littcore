package com.littcore.web.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.littcore.exception.NotLoginException;
import com.littcore.shield.security.SecurityContext;
import com.littcore.shield.security.SecurityContextHolder;
import com.littcore.shield.vo.ILoginVo;
import com.littcore.util.StringUtils;
import com.littcore.web.util.WebUtils;

/** 
 * 
 * SpringMVC拦截器基础类.
 * 
 * <pre><b>描述：</b>
 *   2015-10-30 增加对Ajax请求未登录用户的特殊处理，抛出异常到客户端，有客户端处理
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
	
	/** 排除URL模式. */
  private String[] excludeUrlPattern = new String[0];
	
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
		int index = className.lastIndexOf("Controller");
		if(index<0)
		  index = className.lastIndexOf("Rest");
		className = className.substring(0, index);
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

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    boolean isExcluded = isExcluded(request);
    /*
     * 对于Ajax的请求，用户未登录的，需要返回未登录异常，由前台捕获并做相应处理
     * 注意：由于Ajax是异步的，对于一个界面上有多个Ajax请求的，只有第一个需要处理，其他的同样返回异常，但不需要处理
     * 如果是ajax请求响应头会有，x-requested-with  
     */
    if(!isExcluded && WebUtils.isAjaxRequest(request))
    { 
      if(this.getLoginVo()==null)
      {
        throw new NotLoginException();
      }
    }
    return super.preHandle(request, response, handler);
  }

  /**
   * @param request
   * @return
   */
  protected boolean isExcluded(HttpServletRequest request)
  {
    String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
    
    boolean isExcluded = StringUtils.startsWithAny(lookupPath, this.excludeUrlPattern);
    return isExcluded;
  }

  
  /**
   * @return the excludeUrlPattern
   */
  public String[] getExcludeUrlPattern()
  {
    return excludeUrlPattern;
  }

  
  /**
   * @param excludeUrlPattern the excludeUrlPattern to set
   */
  public void setExcludeUrlPattern(String[] excludeUrlPattern)
  {
    this.excludeUrlPattern = excludeUrlPattern;
  }
	
	
}
