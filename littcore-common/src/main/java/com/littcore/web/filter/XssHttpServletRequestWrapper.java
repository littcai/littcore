package com.littcore.web.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;

import com.littcore.exception.CheckedBusiException;
import com.littcore.util.ArrayUtils;
import com.littcore.util.StringUtils;
import com.littcore.web.util.HtmlUtils;
import com.littcore.web.util.XssUtils;


/**
 * XssHttpServletRequestWrapper.
 * 
 * <pre><b>Descr:</b>
 *    处理XSS攻击
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
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
  
  private String[] whitelists;

  /** 
   * 封装http请求 
   * @param request 
   */  
  public XssHttpServletRequestWrapper(HttpServletRequest request, String[] whitelists) {  
      super(request);        
      this.whitelists = whitelists;
  }  
  
  @Override  
  public String getParameter(String name) {  
    String value = super.getParameter(name);  
    return convertValue(name, value);
  }  
  
  @Override  
  public String[] getParameterValues(String name) {
    
    String[] parameters = super.getParameterValues(name);
    if (parameters==null||parameters.length == 0) {
      return null;     
    }
    for (int i = 0; i < parameters.length; i++) {     
      parameters[i] = convertValue(name, parameters[i]);     
    }
    return parameters;
     
  }

  /**
   * @return
   * @see javax.servlet.ServletRequestWrapper#getParameterMap()
   */
  @Override
  public Map getParameterMap()
  {
    Map<String, Object> paramMap = super.getParameterMap();
    Iterator<Entry<String, Object>> iterator = paramMap.entrySet().iterator();
    while (iterator.hasNext())
    {
      Map.Entry<String, Object> entry = iterator.next();
      if(entry.getValue()==null)
        continue;
      if(entry.getValue() instanceof String)
      {  
        String value = this.convertValue(entry.getKey(), (String)entry.getValue());
        entry.setValue(value);
      }
      else if(entry.getValue() instanceof String[])
      {  
        String[] array = (String[])entry.getValue();
        if (array!=null && array.length > 0) {
          for (int i = 0; i < array.length; i++)
          {
            String value = this.convertValue(entry.getKey(), array[i]);
            array[i] = value;
          }
          entry.setValue(array);
        }
      }
    }
    return paramMap;
  }  
  
  /**
   * @param name
   * @param value
   * @return
   */
  private String convertValue(String name, String value)
  {
    if(StringUtils.isEmpty(value))
      return value;
    
    if(ArrayUtils.contains(whitelists, name))
    {
      try
      {
        value = XssUtils.getCleanHtml(value);
      } catch (CheckedBusiException e)
      {
        value = e.getMessage();
      }      
    }
    else 
    {      
      // 若开启特殊字符替换，对特殊字符进行替换  
      value = HtmlUtils.htmlEscape(value);      
    }
    return value;
  }
  
  
}
