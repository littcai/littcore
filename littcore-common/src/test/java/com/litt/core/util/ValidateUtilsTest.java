package com.litt.core.util;

import junit.framework.TestCase;


/**
 * ValidateUtilsTest.
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
 * @since 2014年10月9日
 * @version 1.0
 */
public class ValidateUtilsTest extends TestCase {
  
  public void test_isDecimal()
  {
    super.assertTrue(ValidateUtils.isDecimal("100"));
    super.assertTrue(ValidateUtils.isDecimal("100.01"));
    super.assertTrue(ValidateUtils.isDecimal("-100"));
    super.assertTrue(ValidateUtils.isDecimal("0.01"));
    super.assertFalse(ValidateUtils.isDecimal(null));
    super.assertFalse(ValidateUtils.isDecimal(""));
    super.assertFalse(ValidateUtils.isDecimal(" "));
    super.assertFalse(ValidateUtils.isDecimal("100a"));
    super.assertFalse(ValidateUtils.isDecimal("100.0a"));
  }

}
