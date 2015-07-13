package com.littcore.cache.provider;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.cache.CacheException;
import com.littcore.cache.ICache;
import com.littcore.cache.ICacheExpiredListener;
import com.littcore.cache.ICacheProvider;


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
public class EhCacheProvider implements ICacheProvider {
  
  private static final Logger logger = LoggerFactory.getLogger(EhCacheProvider.class);
  
  private CacheManager manager;
  private ConcurrentHashMap<String, EhCache> cacheCache ;

  /* (non-Javadoc)
   * @see com.littcore.cache.ICacheProvider#getName()
   */
  @Override
  public String getName()
  {   
    return "ehcache";
  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICacheProvider#buildCache(java.lang.String, boolean, com.littcore.cache.ICacheExpiredListener)
   */
  @Override
  public ICache buildCache(String regionName, boolean autoCreate, ICacheExpiredListener listener) throws CacheException
  {
    EhCache ehcache = cacheCache.get(regionName);
    if (ehcache == null && autoCreate)
    {
      try
      {
        synchronized (cacheCache)
        {
          ehcache = cacheCache.get(regionName);
          if (ehcache == null)
          {
            Cache cache = manager.getCache(regionName);
            if (cache == null)
            {
              logger.warn("Could not find configuration [" + regionName + "]; using defaults.");
              manager.addCache(regionName);
              cache = manager.getCache(regionName);
              logger.debug("started EHCache region: " + regionName);
            }
            ehcache = new EhCache(cache, listener);
            cacheCache.put(regionName, ehcache);
          }
        }
      } catch (net.sf.ehcache.CacheException e)
      {
        throw new CacheException(e);
      }
    }
    return ehcache;
  }
  
  public void start() throws CacheException {
    manager = new CacheManager();
    cacheCache = new ConcurrentHashMap<String, EhCache>();
  }
  
  public void stop() {
    if (manager != null)
    {
      manager.shutdown();
      manager = null;
    }
  }

}
