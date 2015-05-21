package com.litt.core.cache;

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
public interface ICache {
  
  /**
   * Get an item from the cache, nontransactionally
   * @param key cache key
   * @return the cached object or null
   */
  public Object get(Object key) throws CacheException;
  
  /**
   * Add an item to the cache, nontransactionally, with
   * failfast semantics
   * @param key cache key
   * @param value cache value
   */
  public void put(Object key, Object value) throws CacheException;
  
  /**
   * Add an item to the cache
   * @param key cache key
   * @param value cache value
   */
  public void update(Object key, Object value) throws CacheException;

  @SuppressWarnings("rawtypes")
  public List keys() throws CacheException ;
  
  /**
   * Remove an item from the cache
   */
  public void evict(Object key) throws CacheException;
  
  /**
   * Batch remove cache objects
   * @param keys the cache keys to be evicted
   */
  @SuppressWarnings("rawtypes")
  public void evict(List keys) throws CacheException;
  
  /**
   * Clear the cache
   */
  public void clear() throws CacheException;
  
  /**
   * Clean up
   */
  public void destroy() throws CacheException;

}
