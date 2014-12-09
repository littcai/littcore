package com.litt.core.converter.beanutils;

import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateTimeConverter;

import com.litt.core.common.CoreConstants;
import com.litt.core.common.Utility;


/**
 * DateConverter.
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
public class DateConverter extends DateTimeConverter {

  /**
   * @param type
   * @param value
   * @return
   * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
   */
  @Override
  public Object convert(Class type, Object value)
  {
    if(value instanceof String)
    {
      String text = (String)value;  
      if(text== null ||text.trim().length()==0){  
          return null;  
      }  
      else {
        if(text.length()==10)
        {
          return Utility.parseDate(text, CoreConstants.DEFAULT_DATE_FORMAT);
        }
        else if (text.length()==8) {
          return Utility.parseDate(text, CoreConstants.DEFAULT_TIME_FORMAT);
        }
        else if (text.length()==19) {
          return Utility.parseDate(text, CoreConstants.DEFAULT_DATETIME_FORMAT);
        }
        else {
          throw new IllegalArgumentException("Unknown date format: "+text);
        }
      }
    }
    else {
      return super.convert(type, value);
    }
  }

  /**
   * @return
   * @see org.apache.commons.beanutils.converters.AbstractConverter#getDefaultType()
   */
  @Override
  protected Class getDefaultType()
  {
    return Date.class;
  }

}
