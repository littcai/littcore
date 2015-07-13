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
public interface ICacheExpiredListener {
  
  /**
   * 当缓存中的某个对象超时被清除的时候触发
   * @param region: Cache region name
   * @param key: cache key
   */
  public void notifyElementExpired(String region, Object key) ;

}
