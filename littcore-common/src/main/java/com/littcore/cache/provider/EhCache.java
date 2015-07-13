package com.littcore.cache.provider;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import com.littcore.cache.CacheException;
import com.littcore.cache.ICache;
import com.littcore.cache.ICacheExpiredListener;


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
public class EhCache implements ICache, CacheEventListener {
  
  private Cache cache;
  private ICacheExpiredListener listener;
  
  public EhCache(net.sf.ehcache.Cache cache, ICacheExpiredListener listener) {
    this.cache = cache;
    this.cache.getCacheEventNotificationService().registerListener(this);
    this.listener = listener;
  }


  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#get(java.lang.Object)
   */
  @Override
  public Object get(Object key) throws CacheException
  {
    try
    {
      if (key == null)
        return null;
      else
      {
        Element element = cache.get(key);
        if (element != null)
          return element.getObjectValue();
      }
      return null;
    } catch (net.sf.ehcache.CacheException e)
    {
      throw new CacheException(e);
    }
  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#put(java.lang.Object, java.lang.Object)
   */
  @Override
  public void put(Object key, Object value) throws CacheException
  {
    try {
      Element element = new Element( key, value );
      cache.put( element );
    }
    catch (IllegalArgumentException e) {
      throw new CacheException( e );
    }
    catch (IllegalStateException e) {
      throw new CacheException( e );
    }
    catch (net.sf.ehcache.CacheException e) {
      throw new CacheException( e );
    }

  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#update(java.lang.Object, java.lang.Object)
   */
  @Override
  public void update(Object key, Object value) throws CacheException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#keys()
   */
  @Override
  @SuppressWarnings("rawtypes")
  public List keys() throws CacheException
  {
    return this.cache.getKeys();
  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#evict(java.lang.Object)
   */
  @Override
  public void evict(Object key) throws CacheException
  {
    try {
      cache.remove( key );
    }
    catch (IllegalStateException e) {
      throw new CacheException( e );
    }
    catch (net.sf.ehcache.CacheException e) {
      throw new CacheException( e );
    }

  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#evict(java.util.List)
   */
  @Override
  @SuppressWarnings("rawtypes")
  public void evict(List keys) throws CacheException
  {
    for (Object key : keys)
    {
      this.evict(key);
    }

  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#clear()
   */
  @Override
  public void clear() throws CacheException
  {
    try {
      cache.removeAll();
    }
    catch (IllegalStateException e) {
      throw new CacheException( e );
    }
    catch (net.sf.ehcache.CacheException e) {
      throw new CacheException( e );
    }

  }

  /* (non-Javadoc)
   * @see com.littcore.cache.ICache#destroy()
   */
  @Override
  public void destroy() throws CacheException
  {
    try {
      cache.getCacheManager().removeCache( cache.getName() );
    }
    catch (IllegalStateException e) {
      throw new CacheException( e );
    }
    catch (net.sf.ehcache.CacheException e) {
      throw new CacheException( e );
    }

  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyElementRemoved(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
   */
  @Override
  public void notifyElementRemoved(Ehcache cache, Element element) throws net.sf.ehcache.CacheException
  {
    // TODO Auto-generated method stub
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyElementPut(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
   */
  @Override
  public void notifyElementPut(Ehcache cache, Element element) throws net.sf.ehcache.CacheException
  {
    // TODO Auto-generated method stub
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyElementUpdated(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
   */
  @Override
  public void notifyElementUpdated(Ehcache cache, Element element) throws net.sf.ehcache.CacheException
  {
    // TODO Auto-generated method stub
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyElementExpired(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
   */
  @Override
  public void notifyElementExpired(Ehcache cache, Element element)
  {
    if(listener != null){
      listener.notifyElementExpired(cache.getName(), element.getObjectKey());
    }
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyElementEvicted(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
   */
  @Override
  public void notifyElementEvicted(Ehcache cache, Element element)
  {
    // TODO Auto-generated method stub
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#notifyRemoveAll(net.sf.ehcache.Ehcache)
   */
  @Override
  public void notifyRemoveAll(Ehcache cache)
  {
    // TODO Auto-generated method stub
    
  }


  /* (non-Javadoc)
   * @see net.sf.ehcache.event.CacheEventListener#dispose()
   */
  @Override
  public void dispose()
  {
    // TODO Auto-generated method stub
    
  }
  
  public Object clone() throws CloneNotSupportedException { 
    throw new CloneNotSupportedException(); 
  }

}
