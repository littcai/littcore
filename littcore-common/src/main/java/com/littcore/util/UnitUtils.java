package com.littcore.util;

import java.math.BigDecimal;

import com.littcore.converter.unit.UnitConverterManager;


/**
 * 单位换算辅助工具.
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
 * @since 2014年9月25日
 * @version 1.0
 */
public class UnitUtils {
  
  public enum UnitType{
    MM,INCH
  }
    
  /**
   * Convert.
   *
   * @param a the a
   * @param from the from
   * @param to the to
   * @return the big decimal
   */
  public static BigDecimal convert(BigDecimal a, UnitType from, UnitType to)
  {
    BigDecimal rate = UnitConverterManager.getInstance().getConverterRate(from.name().toLowerCase(), to.name().toLowerCase());
    if(rate==null)
      throw new IllegalArgumentException("Unknown converter type from:"+from+" to:"+to);
    
    return a.multiply(rate);
  }

}
