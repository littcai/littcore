package com.littcore.shield.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import com.littcore.common.CoreConstants;
import com.littcore.shield.security.SecurityContext;
import com.littcore.shield.security.SecurityContextHolder;
import com.littcore.shield.vo.ILoginVo;
import com.littcore.util.StringUtils;
import com.littcore.util.ValidateUtils;
import com.littcore.web.util.WebUtils;


/**
 * 
 * 操作员登录信息线程缓存过滤器.
 * <pre><b>描述：</b>
 *   用来将登录操作员的信息绑定到当前请求线程，供业务层使用。解决以前需通过参数形式传递的问题。
 *   
 *   提示：对于精细过滤，可采用路径匹配+自定义精确过滤的模式，定义preciseUrlPattern    
 *
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2009-03-31, 2009-06-26
 * @version 1.0, 1,1
 */
public class OperatorHttpSessionContextIntegrationFilter extends OncePerRequestFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorHttpSessionContextIntegrationFilter.class);
	 
	static final String FILTER_APPLIED = "_OPERATOR_SESSION_CONTEXT_INTEGRATION_APPLIED"; 	
	
	/** URL精确过滤模式. */
	private String preciseUrlPattern = "";
	
	/** 排除URL模式. */
	private String[] excludeUrlPattern = new String[0];
	
	/**
     * 失败后重定向页面.
     */
    private String failRedirect;
	 
	/** 会话中存放登录信息对象的名称. */
 	private String sessionLoginObjName = CoreConstants.SESSION_OPER;

	/* (non-Javadoc)
	 * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
	 */
	protected void initFilterBean() throws ServletException
	{		
		super.initFilterBean();
		FilterConfig config = super.getFilterConfig();
		this.excludeUrlPattern = StringUtils.split(config.getInitParameter("excludeUrlPattern"), ',');		
		this.failRedirect = config.getInitParameter("failRedirect");
		if(!ValidateUtils.isEmpty(config.getInitParameter("sessionLoginObjName")))
		{
			sessionLoginObjName = config.getInitParameter("sessionLoginObjName");
		}
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();

		ILoginVo currentLoginVo = (ILoginVo)session.getAttribute(sessionLoginObjName);   //从会话中取出登录用户信息
		
		//检查SESSION中是否有登录用户信息,没有则跳转到失败页面
		String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
		
		boolean isExcluded = StringUtils.startsWithAny(lookupPath, this.excludeUrlPattern);
		if(!isExcluded && currentLoginVo==null)
		{
			logger.debug("Url:{} needs login", new Object[]{lookupPath});				
			/*
			 * 对于Ajax的请求，用户未登录的，需要返回未登录异常，由前台捕获并做相应处理
			 * 注意：由于Ajax是异步的，对于一个界面上有多个Ajax请求的，只有第一个需要处理，其他的同样返回异常，但不需要处理
			 * 如果是ajax请求响应头会有，x-requested-with  
			 */			
			if(!WebUtils.isAjaxRequest(request))
			{
			  response.sendRedirect(request.getContextPath()+this.failRedirect); //非AJAX请求直接重定向
			  return;
			}
//			RequestDispatcher dispatcher = request.getRequestDispatcher("/system/login/index.do");
//			dispatcher.forward(request, response);
			
		}		
		if(currentLoginVo!=null)
		{
			//将登录信息存入安全管理容器并绑定到当前线程
			SecurityContext context = SecurityContextHolder.getContext();
			if(context==null)
				context = new SecurityContext();
			context.setLoginVo(currentLoginVo);				
	        SecurityContextHolder.setContext(context);   
		}   
		try
		{
			chain.doFilter(request, response);	
		}		
		finally
		{
      SecurityContextHolder.clear();  //清理线程中存放的安全上下文
		}
	}

  
  /**
   * @return the sessionLoginObjName
   */
  public String getSessionLoginObjName()
  {
    return sessionLoginObjName;
  }

}
