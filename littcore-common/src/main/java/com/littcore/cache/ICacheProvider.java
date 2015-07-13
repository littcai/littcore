package com.littcore.cache;


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
public interface ICacheProvider {
  
  /**
   * 缓存的标识名称
   * @return return cache provider name
   */
  public String getName();
  
  /**
   * Configure the cache
   *
   * @param regionName the name of the cache region
   * @param autoCreate autoCreate settings
   * @param listener listener for expired elements
   * @return return cache instance
   * @throws CacheException cache exception
   */
  public ICache buildCache(String regionName, boolean autoCreate, ICacheExpiredListener listener) throws CacheException;

  public void start();
  
  public void stop();
}
