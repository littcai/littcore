package com.littcore.web.mvc.support;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import com.littcore.web.mvc.editor.DateEditor;
import com.littcore.web.mvc.editor.StringEscapeEditor;


/**
 * 自定义Web参数自动绑定.
 * 
 * <pre><b>描述：</b>
 *    实现扩展的Web参数自动绑定，如日期，XSS处理等
 * 这是全局绑定，spring初始化时即加载   
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2015年12月1日
 * @version 1.0
 */
public class CustomWebBindingInitializer implements WebBindingInitializer {

  /* (non-Javadoc)
   * @see org.springframework.web.bind.support.WebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.WebRequest)
   */
  @Override
  public void initBinder(WebDataBinder binder, WebRequest request)
  {
    //防止XSS攻击
    binder.registerCustomEditor(String.class, new StringEscapeEditor(true, true));
    
    //对于需要转换为Date类型的属性，使用DateEditor进行处理  
    binder.registerCustomEditor(Date.class, new DateEditor());

  }

}
