package com.littcore.cache;

import java.util.List;


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
public class SimpleCacheManager implements ICacheExpiredListener {

  public static final String REGION_NAME = "static";
  
  public SimpleCacheManager()
  {
    CacheFactory.initCacheProvider(this);
  }
  
  /* (non-Javadoc)
   * @see com.littcore.cache.ICacheExpiredListener#notifyElementExpired(java.lang.String, java.lang.Object)
   */
  @Override
  public void notifyElementExpired(String region, Object key)
  {
    // TODO Auto-generated method stub

  }
  
  public Object get(Object key){
    return CacheFactory.get(1, REGION_NAME, key);    
  }
  
  public void set(Object key, Object value){
    if(key != null){
      if(value == null)
        evict(key);
      else{
        //分几种情况
        //Object obj1 = CacheManager.get(LEVEL_1, region, key);
        //Object obj2 = CacheManager.get(LEVEL_2, region, key);
        //1. L1 和 L2 都没有
        //2. L1 有 L2 没有（这种情况不存在，除非是写 L2 的时候失败
        //3. L1 没有，L2 有
        //4. L1 和 L2 都有
        CacheFactory.set(1, REGION_NAME, key, value);
      }
    }
    //log.info("write data to cache region="+region+",key="+key+",value="+value);
  }
  
  /**
   * 删除缓存
   * @param region:  Cache Region name
   * @param key: Cache key
   */
  public void evict(Object key) {
    CacheFactory.evict(1, REGION_NAME, key); //删除一级缓存
  }

  /**
   * 批量删除缓存
   * @param region: Cache region name
   * @param keys: Cache key
   */
  @SuppressWarnings({ "rawtypes" })
  public void batchEvict(List keys) {
    CacheFactory.batchEvict(1, REGION_NAME, keys);
  }

  /**
   * Clear the cache
   * @param region: Cache region name
   */
  public void clear() throws CacheException {
    CacheFactory.clear(1, REGION_NAME);
  }
  
  /**
   * Get cache region keys
   * @param region: Cache region name
   * @return key list
   */
  @SuppressWarnings("rawtypes")
  public List keys() throws CacheException {
    return CacheFactory.keys(1, REGION_NAME);
  }
  
  private static class SingletonClassInstance { 
    private static final SimpleCacheManager instance = new SimpleCacheManager(); 
  } 
  
  /**
   * Gets the single instance of ConfigManager.
   *
   * @return single instance of ConfigManager
   */
  public static SimpleCacheManager getInstance() { 
      return SingletonClassInstance.instance; 
  }

}
