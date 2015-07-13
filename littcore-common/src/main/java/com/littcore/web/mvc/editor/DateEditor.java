package com.littcore.web.mvc.editor;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import com.littcore.common.CoreConstants;
import com.littcore.common.Utility;
import com.littcore.format.FormatDateTime;
import com.littcore.util.StringUtils;


/**
 * DateEditor.
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
public class DateEditor extends PropertyEditorSupport {
  
  /**
   * @return
   * @see java.beans.PropertyEditorSupport#getAsText()
   */
  @Override
  public String getAsText()
  {
    Date value = (Date)super.getValue();
    return ((value != null) ? FormatDateTime.formatDateTime(value) : ""); //TODO 这里由于只有值，转字符串只能按完整的转
  }

  /**
   * @param text
   * @throws IllegalArgumentException
   * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
   */
  @Override
  public void setAsText(String text) throws IllegalArgumentException
  {
    if(StringUtils.isEmpty(text))
      super.setValue(null);
    else {
      if(text.length()==10)
      {
        super.setValue(Utility.parseDate(text, CoreConstants.DEFAULT_DATE_FORMAT));
      }
      else if (text.length()==8) {
        super.setValue(Utility.parseDate(text, CoreConstants.DEFAULT_TIME_FORMAT));
      }
      else if (text.length()==19) {
        super.setValue(Utility.parseDate(text, CoreConstants.DEFAULT_DATETIME_FORMAT));
      }
      else {
        throw new IllegalArgumentException("Unknown date format: "+text);
      }
    }
  }
  
}
