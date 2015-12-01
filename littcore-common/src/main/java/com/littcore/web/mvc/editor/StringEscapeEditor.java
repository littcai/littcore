package com.littcore.web.mvc.editor;

import java.beans.PropertyEditorSupport;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.web.util.JavaScriptUtils;

import com.littcore.util.StringUtils;
import com.littcore.web.util.HtmlUtils;

/**
 * StringEscapeEditor.
 * 
 * <pre>
 * <b>Descr:</b>
 * 
 * </pre>
 * 
 * <pre>
 * <b>Changelog:</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Caiyuan</a>
 * @since 2014年12月9日
 * @version 1.0
 */
public class StringEscapeEditor extends PropertyEditorSupport {

  private boolean escapeHTML;// 编码HTML

  private boolean escapeJavaScript;// 编码javascript
  
//  private Policy policy;    
//  
//  private AntiSamy as;

  public StringEscapeEditor()
  {
    super();
//    policy = Policy.getInstance("D:\\antisamy\\antisamy-slashdot-1.4.4.xml");
//    as = new AntiSamy();
  }

  public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript)
  {
    this();
    this.escapeHTML = escapeHTML;
    this.escapeJavaScript = escapeJavaScript;
    
  }

  @Override
  public String getAsText()
  {
    Object value = getValue();
    return value != null ? value.toString() : "";
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException
  {
    if (text == null)
    {
      setValue(null);
    } 
    else if (StringUtils.isEmpty(text)) {
      setValue(text);
    }
    else
    {
      String value = text;
      if (escapeHTML)
      {
        value = HtmlUtils.htmlEscape(value);
      }
      if (escapeJavaScript)
      {
        value = JavaScriptUtils.javaScriptEscape(value);
      }
      setValue(value);
    }
  }

}
