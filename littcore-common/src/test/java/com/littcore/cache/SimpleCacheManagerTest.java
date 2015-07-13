package com.littcore.cache;

import org.junit.Assert;
import org.junit.Test;

import com.littcore.cache.SimpleCacheManager;


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
 * @since 2015年5月21日
 * @version 1.0
 */
public class SimpleCacheManagerTest {
  
  @Test
  public void test_ecache()
  {
    SimpleCacheManager.getInstance().set("name", "cai");
    
    Assert.assertEquals("cai", SimpleCacheManager.getInstance().get("name"));
  }

}
