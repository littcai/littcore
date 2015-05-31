package com.litt.core.io.util;

import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;


/**
 * .
 * 
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2015年5月31日
 * @version 1.0
 */
public class FileUtilsTest {
  
  @Test
  public void test_humanReadableByteCount()
  {
    DecimalFormat df = new DecimalFormat("#.##");
    System.out.println(df.format(1000D));
    Assert.assertEquals("1000 B", FileUtils.humanReadableByteCount(1000, false));
    Assert.assertEquals("1 KB", FileUtils.humanReadableByteCount(1024));
    Assert.assertEquals("1.1 KB", FileUtils.humanReadableByteCount(1124));
    Assert.assertEquals("1.1 MB", FileUtils.humanReadableByteCount(1150976));
    Assert.assertEquals("100 MB", FileUtils.humanReadableByteCount(104857600));
  }

}
