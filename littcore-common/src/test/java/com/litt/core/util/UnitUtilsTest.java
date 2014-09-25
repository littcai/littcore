package com.litt.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import junit.framework.TestCase;

import com.litt.core.util.UnitUtils.UnitType;


/**
 * UnitUtilsTest.
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
public class UnitUtilsTest extends TestCase {
  
  public void test_convert()
  {
    BigDecimal a = new BigDecimal(2);
    BigDecimal b = UnitUtils.convert(a, UnitType.MM, UnitType.INCH);
    assertEquals(a.multiply(new BigDecimal(0.0394)).setScale(4, RoundingMode.HALF_UP), b);
    
  }

}
