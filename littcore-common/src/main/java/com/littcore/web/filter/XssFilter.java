package com.littcore.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.littcore.common.Utility;


/**
 * XssFilter.
 * 
 * <pre><b>Descr:</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog:</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Caiyuan</a>
 * @since 2014年12月9日
 * @version 1.0
 */
public class XssFilter implements Filter {
  
  /** The enable xss log. */
  private boolean enableXssLog;

  /**
   * @param filterConfig
   * @throws ServletException
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException
  {
    this.enableXssLog = Utility.parseBoolean(filterConfig.getInitParameter("enableXssLog"));
  }

  /**
   * @param request
   * @param response
   * @param chain
   * @throws IOException
   * @throws ServletException
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    //判断是否使用HTTP  
    checkRequestResponse(request, response);  
    // 转型  
    HttpServletRequest httpRequest = (HttpServletRequest) request;  
    HttpServletResponse httpResponse = (HttpServletResponse) response;  
    // http信息封装类  
    XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest);

    // 对request信息进行封装并进行校验工作，若校验失败（含非法字符），根据配置信息进行日志记录和请求中断处理
    if(enableXssLog)
    {
      // 记录攻击访问日志  
      // 可使用数据库、日志、文件等方式  
    }   
    chain.doFilter(xssRequest, response);  
  }

  /**
   * 
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy()
  {
   

  }
  
  /** 
   * 判断Request ,Response 类型 
   * @param request 
   *            ServletRequest 
   * @param response 
   *            ServletResponse 
   * @throws ServletException  
   */  
  private void checkRequestResponse(ServletRequest request,  
          ServletResponse response) throws ServletException {  
      if (!(request instanceof HttpServletRequest)) {  
          throw new ServletException("Can only process HttpServletRequest");  

      }  
      if (!(response instanceof HttpServletResponse)) {  
          throw new ServletException("Can only process HttpServletResponse");  
      }  
  }  

}
