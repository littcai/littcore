package com.litt.core.shield.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.litt.core.common.CoreConstants;
import com.litt.core.common.Utility;
import com.litt.core.shield.common.OnlineManager;
import com.litt.core.shield.vo.ILoginVo;

/**
 * 
 * <b>标题：</b> 单用户登录过滤器(需配合OnlineOperatorListener使用).
 * <pre><b>描述：</b>
 *  控制操作员登录限制(默认单SESSION)：
 *    1、同一时间只能在一个IP登录 - 【oneIP】
 *    2、同一时间只能在一个窗口登录(单SESSION) -  - 【oneSession】
 *  限制结果：仿MSN，注销之前的登录；或不允许登录
 *    1、注销前面的登录 - 【access】
 *    2、不允许当前登录 - 【deny】
 *    
 * 依赖：
 * 1、OnlineManager，递归依赖OnlineOperatorListener   
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-08-07
 * @version 1.0
 */
public class SingleLoginFilter extends OncePerRequestFilter implements Filter {
	
	private static final Log logger = LogFactory.getLog(SingleLoginFilter.class);
	
	/** 是否激活登录限制. */
	private boolean enabled = true;	
	
	/** 限制类型. */
	private String limitType;
	
	/** 限制处理类型. */
	private String limitResult;
	
	/** 限制处理后的跳转页面. */
	private String errorPage;

	public void destroy() {
		//do nothing
	}

	/**
	 * 限制处理.
	 * @param request
	 * @param response
	 * @param onlineOperatorMap
	 * @param currentOpId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dealLimit(HttpServletRequest request, HttpServletResponse response, OnlineManager onlineManager, ILoginVo cacheLoginVo, ILoginVo currentLoginVo) throws ServletException, IOException 
	{
		if("access".equalsIgnoreCase(limitResult))	//注销之前的登录
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("注销缓存操作员");
			}
			cacheLoginVo.setForceOffline(true);
			//response.sendRedirect(errorPage);
			//response.reset();
			//request.setAttribute("ERROR_MSG", "您已在其他地方登录了，当前登录已失效！");
			//request.getRequestDispatcher(errorPage).forward(request, response);	//跳转到异常页面
		}	
		else if("deny".equalsIgnoreCase(limitResult))	//不允许当前登录
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("拒绝登录");
			}	
			currentLoginVo.setForceOffline(true);
			//response.sendRedirect(errorPage);			
			response.reset();
			request.setAttribute("ERROR_MSG", "您已在其他地方登录了，禁止重复登录！");
			request.getRequestDispatcher(errorPage).forward(request, response);	//跳转到异常页面
			
		}
	}

	/**
	 * 获取Servlet容器对象
	 */
	protected void initFilterBean() throws ServletException
	{		
		super.initFilterBean();
		FilterConfig config = super.getFilterConfig();
		
		enabled = Utility.parseBoolean(config.getInitParameter("enabled"));
		if(enabled)
		{
			limitType = config.getInitParameter("limitType");
			limitResult = config.getInitParameter("limitResult");
			errorPage = config.getInitParameter("errorPage");		
			if(Utility.isEmpty(limitType))
				limitType = "oneSession";
			if(Utility.isEmpty(limitResult))
				limitResult = "access";
			if(Utility.isEmpty(errorPage))
				errorPage = "/error_single_login.jsp";		
			if(logger.isInfoEnabled())
			{
				logger.info("单用户登录限制启动...限制类型："+limitType+"，限制处理："+limitResult+"，错误页面："+errorPage);
			}
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(enabled)
		{
			HttpSession session = request.getSession();
			ILoginVo currentLoginVo = (ILoginVo)session.getAttribute(CoreConstants.SESSION_OPER);
			if(currentLoginVo!=null)
			{
				OnlineManager onlineManager = OnlineManager.getDefaultInstance();	
				Long currentOpId = currentLoginVo.getOpId();
				ILoginVo cacheLoginVo = onlineManager.getLoginVo(currentOpId);
				if(cacheLoginVo!=null)	//如果存在缓存，则需要检测
				{					
					//检测限制类型
					if("oneSession".equalsIgnoreCase(limitType))	//单SESSION
					{						
						if(!cacheLoginVo.getGuid().equals(currentLoginVo.getGuid()))	//当前操作员非缓存操作员
						{
							if(logger.isDebugEnabled())
							{
								logger.debug("当前操作员:"+currentLoginVo.getLoginId()+"唯一索引号与缓存中不匹配！");
							}						
							dealLimit(request, response, onlineManager, cacheLoginVo, currentLoginVo);								
						}	
					}
					else if("oneIP".equalsIgnoreCase(limitType))	//单IP
					{
						//比较当前登录操作员和缓存的IP
						if(!cacheLoginVo.getLoginIp().equals(currentLoginVo.getLoginIp()))	//当前操作员非缓存操作员
						{
							if(logger.isDebugEnabled())
							{
								logger.debug("当前操作员:"+currentLoginVo.getLoginId()+"IP地址与缓存中不匹配！");
							}
							dealLimit(request, response, onlineManager, cacheLoginVo, currentLoginVo);							
						}	
					}				
				}
				else	//当前操作员进行的登录操作，但是在线用户列表中已经没有他了，即已被其他客户端踢出(不可能出现)
				{
					if(logger.isDebugEnabled())
					{
						logger.debug("操作员:"+currentLoginVo.getLoginId()+"已被踢出！");
					}
					request.setAttribute("ERROR_MSG", "您已在其他地方登录了，禁止重复登录！");
					request.getRequestDispatcher("/error_single_login.jsp").forward(request, response);	//跳转到异常页面
				}
			}
		}	
		filterChain.doFilter(request, response);	
	}

}
