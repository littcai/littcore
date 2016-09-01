package com.littcore.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.littcore.common.Utility;
import com.littcore.util.ArrayUtils;
import com.littcore.util.StringUtils;


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
public class XssFilter extends OncePerRequestFilter {
  
  /** The enable xss log. */
  private boolean enableXssLog;
  
  /** 
   * 白名单(用逗号分隔).
   * 参数名称匹配白名单的采用anitsamy进行XSS清洗，返回值仍为HTML
   */
  private String[] whitelists = ArrayUtils.EMPTY_STRING_ARRAY;

  /* (non-Javadoc)
   * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
   */
  @Override
  protected void initFilterBean() throws ServletException
  {    
    super.initFilterBean();
    FilterConfig filterConfig = super.getFilterConfig();
    this.enableXssLog = Utility.parseBoolean(filterConfig.getInitParameter("enableXssLog"));
    this.whitelists = StringUtils.split(filterConfig.getInitParameter("whitelists"), ',');
    this.whitelists = whitelists==null?ArrayUtils.EMPTY_STRING_ARRAY:whitelists;
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
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
  {
    // 转型  
    HttpServletRequest httpRequest = (HttpServletRequest) request;  
    HttpServletResponse httpResponse = (HttpServletResponse) response;  
    // http信息封装类  
    XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest, this.whitelists);
    //Cookie增加HttpOnly防护
    Cookie[] cookies = request.getCookies();  
    
    if (cookies != null) {  
       for (Cookie cookie : cookies)
       {          
         if("JSESSIONID".equalsIgnoreCase(cookie.getName()))
         {
           String value = cookie.getValue();  
           StringBuilder builder = new StringBuilder();  
           builder.append("JSESSIONID=" + value + "; ");  
           builder.append("Secure; ");  
           builder.append("HttpOnly; ");  
           response.setHeader("Set-Cookie", builder.toString()); 
         } 
       }             
    }        
    // 对request信息进行封装并进行校验工作，若校验失败（含非法字符），根据配置信息进行日志记录和请求中断处理
    if(enableXssLog)
    {
      // 记录攻击访问日志  
      // 可使用数据库、日志、文件等方式  
    }   
    chain.doFilter(xssRequest, response);  
  }
    
}
