package com.litt.core.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litt.core.cache.provider.EhCacheProvider;


/**
 * 缓存管理器.
 * 
 * <pre><b>描述：</b>
 *    单例的缓存管理器，屏蔽底层具体的缓存实现
 * 注：目前仅实现单机内存缓存和EhCache，统一应用中使用缓存的场景   
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
public class CacheFactory {
  
  private static final Logger logger = LoggerFactory.getLogger(CacheFactory.class);
  
  private static ICacheProvider l1_provider;
  private static ICacheProvider l2_provider;
  
  private static ICacheExpiredListener listener;
   
  
  /**
   * Initialize Cache Provider
   * @param listener cache listener
   */
  public static void initCacheProvider(ICacheExpiredListener listener){

    CacheFactory.listener = listener;
    try{
      l1_provider = getProviderInstance("ehcache");
      l1_provider.start();
      logger.info("Using L1 CacheProvider : " + l1_provider.getClass().getName());
      
    }catch(Exception e){
      throw new CacheException("Unabled to initialize cache providers", e);
    }
  }
  
  private final static ICacheProvider getProviderInstance(String value) throws Exception {
    if("ehcache".equalsIgnoreCase(value))
      return new EhCacheProvider();
//    if("redis".equalsIgnoreCase(value))
//      return new RedisCacheProvider();
//    if("none".equalsIgnoreCase(value))
//      return new NullCacheProvider();
    return (ICacheProvider)Class.forName(value).newInstance();
  }
  
  private final static ICache getCache(int level, String cache_name, boolean autoCreate) {
    return ((level==1)?l1_provider:l2_provider).buildCache(cache_name, autoCreate, listener);
  }
  
  /**
   * 获取缓存中的数据
   * @param level Cache Level: L1 and L2
   * @param name Cache region name
   * @param key Cache key
   * @return Cache object
   */
  public final static Object get(int level, String name, Object key){
    if(name!=null && key != null) {
            ICache cache = getCache(level, name, false);
            if (cache != null)
                return cache.get(key);
        }
    return null;
  }
  
  /**
   * 获取缓存中的数据
   * @param level Cache Level: L1 and L2
   * @param resultClass Cache object class
   * @param name Cache region name
   * @param key Cache key
   * @return Cache object
   */
  @SuppressWarnings("unchecked")
  public final static <T> T get(int level, Class<T> resultClass, String name, Object key){
    if(name!=null && key != null) {
            ICache cache = getCache(level, name, false);
            if (cache != null)
                return (T)cache.get(key);
        }
    return null;
  }
  
  /**
   * 写入缓存
   * @param level Cache Level: L1 and L2
   * @param name Cache region name
   * @param key Cache key
   * @param value Cache value
   */
  public final static void set(int level, String name, Object key, Object value){
    if(name!=null && key != null && value!=null) {
            ICache cache = getCache(level, name, true);
            if (cache != null)
                cache.put(key,value);
        }
  }
  
  /**
   * 清除缓存中的某个数据
   * @param level Cache Level: L1 and L2
   * @param name Cache region name
   * @param key Cache key
   */
  public final static void evict(int level, String name, Object key){
    //batchEvict(level, name, java.util.Arrays.asList(key));
    if(name!=null && key != null) {
            ICache cache = getCache(level, name, false);
            if (cache != null)
                cache.evict(key);
        }
  }
  
  /**
   * 批量删除缓存中的一些数据
   * @param level Cache Level： L1 and L2
   * @param name Cache region name
   * @param keys Cache keys
   */
  @SuppressWarnings("rawtypes")
  public final static void batchEvict(int level, String name, List keys) {
    if(name!=null && keys != null && keys.size() > 0) {
            ICache cache = getCache(level, name, false);
            if (cache != null)
                cache.evict(keys);
        }
  }

  /**
   * Clear the cache
   * @param level Cache level
   * @param name cache region name
   */
  public final static void clear(int level, String name) throws CacheException {
        ICache cache = getCache(level, name, false);
        if(cache != null)
          cache.clear();
  }
  
  /**
   * list cache keys
   * @param level Cache level
   * @param name cache region name
   * @return Key List
   */
  @SuppressWarnings("rawtypes")
  public final static List keys(int level, String name) throws CacheException {
    ICache cache = getCache(level, name, false);
    return (cache!=null)?cache.keys():null;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // TODO Auto-generated method stub

  }
  

  private static class SingletonClassInstance { 
    private static final CacheFactory instance = new CacheFactory(); 
  } 
  
  /**
   * Gets the single instance of ConfigManager.
   *
   * @return single instance of ConfigManager
   */
  public static CacheFactory getInstance() { 
      return SingletonClassInstance.instance; 
  }

}
