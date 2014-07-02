package com.litt.core.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.util.ResourceUtils;
import com.litt.core.util.StringUtils;


/**
 * ConfigManager.
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
public class ConfigManager {
  
  private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
  
  /** 配置文件名与配置对象映射. */
  public Map<String, Configuration> configMap = new ConcurrentHashMap<String, Configuration>();
  
  private CompositeConfiguration globalConfig = new CompositeConfiguration();
  
  public ConfigManager()
  {
    try
    {
      File configFile = ResourceUtils.getFile("classpath:config.xml");
      DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder(configFile);
      Configuration config = builder.getConfiguration();
      globalConfig.addConfiguration(config);
    } catch (FileNotFoundException e)
    {
      
    } 
    catch (ConfigurationException e)
    {
      logger.error("Can't initialize config.xml", e);
    } 
  }
  
  public Configuration loadItem(File file) throws ConfigurationException
  {
    String fileName = file.getName();
    String baseName = FilenameUtils.getBaseName(fileName);
    return loadItem(baseName, file, false);
  }
  
  public Configuration loadItem(File file, boolean isReload) throws ConfigurationException
  {
    String fileName = file.getName();
    String baseName = FilenameUtils.getBaseName(fileName);
    return loadItem(baseName, file, isReload);
  }
  
  public Configuration loadItem(String name, File file, boolean isReload) throws ConfigurationException
  {
    String fileName = file.getName();    
    if(!isReload && configMap.containsKey(name))
    {
      return configMap.get(name);
    }
    Configuration config = load(file);
    configMap.put(name, config);
    globalConfig.addConfiguration(config);
    return config;
    
//    DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder(file);
//    Configuration config = builder.getConfiguration();
//    configMap.put(name, config);
//    globalConfig.addConfiguration(config);
//    return config;
  }
  
  public static Configuration load(File file) throws ConfigurationException
  {  
    String fileName = file.getName();   
    if(StringUtils.endsWithIgnoreCase(fileName, ".xml"))
    {
      XMLConfiguration config = new XMLConfiguration();        
      //config.setExpressionEngine(new XPathExpressionEngine());  
      config.load(file);     
      return config;
    }
    else if(StringUtils.endsWithIgnoreCase(fileName, ".properties"))
    {
      PropertiesConfiguration config = new PropertiesConfiguration();
      config.load(file);     
      return config;
    }
    else if(StringUtils.endsWithIgnoreCase(fileName, ".ini"))
    {
      HierarchicalINIConfiguration config = new HierarchicalINIConfiguration();
      config.load(file);      
      return config;
    }
    else {
      throw new ConfigurationException("Unknown type of configuration file.");
    }
  }
  
  /**
   * Gets the config.
   *
   * @param name the name
   * @return the config
   */
  public Configuration getConfig(String name)
  {
    return configMap.get(name);
  }
  
  public Configuration getConfig()
  {
    return globalConfig;
  }
  
  public void update()
  {
    
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    Configuration config = ConfigManager.getInstance().loadItem(ResourceUtils.getFile("classpath:system-config.xml"));
    //XMLConfiguration config = new XMLConfiguration(ResourceUtils.getFile("classpath:system-config.xml"));   
    System.out.println(config.getString("baseInfo.code"));
  }
  
  private static class SingletonClassInstance { 
    private static final ConfigManager instance = new ConfigManager(); 
  } 
  
  /**
   * Gets the single instance of ConfigManager.
   *
   * @return single instance of ConfigManager
   */
  public static ConfigManager getInstance() { 
      return SingletonClassInstance.instance; 
  }

}
