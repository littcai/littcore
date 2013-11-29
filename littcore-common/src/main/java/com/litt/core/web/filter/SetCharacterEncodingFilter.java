package com.litt.core.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * 网页编码过滤器(非Spring环境下使用).
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-09-01
 * @version 1.0
 *
 */
public class SetCharacterEncodingFilter implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig = null;
   
  
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding"); 
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    					throws IOException, ServletException {
        if(request.getCharacterEncoding()==null){
            String encoding = selectEncoding(request);
            if(encoding!=null)
                request.setCharacterEncoding(encoding);
        }
        chain.doFilter(request,response);

    }

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
 
    }
    
    protected String selectEncoding(ServletRequest request) {
        return this.encoding;
    }
}
