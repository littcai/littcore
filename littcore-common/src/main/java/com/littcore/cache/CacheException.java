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
public class CacheException extends RuntimeException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  public CacheException(String s) {
    super(s);
  }

  public CacheException(String s, Throwable e) {
    super(s, e);
  }

  public CacheException(Throwable e) {
    super(e);
  }
}
