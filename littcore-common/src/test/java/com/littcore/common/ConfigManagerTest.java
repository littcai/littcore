package com.littcore.common;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;

import com.littcore.common.ConfigManager;
import com.littcore.util.ResourceUtils;


/**
 * ConfigManagerTest.
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
 * @since 2014年7月2日
 * @version 1.0
 */
public class ConfigManagerTest {
  
  @Test
  public void test_init()
  {
    Configuration config = ConfigManager.getInstance().getConfig();
    Assert.assertEquals("/usr/local/userData2", config.getString("home.path"));
    
    Assert.assertEquals("SYS-SAAS-0001", config.getString("baseInfo.code"));
    
  }

}
