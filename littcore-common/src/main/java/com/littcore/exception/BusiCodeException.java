package com.littcore.exception;

import java.util.Locale;

/**
 * 业务异常编码异常类.
 * 
 * <pre><b>描述：</b>
 *    通过异常编码获得异常内容
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2014-08-21 增加defaultMessage，在无法国际化时可取默认消息
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-11-12
 * @version 1.0
 */
public class BusiCodeException extends BusiException {
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	private Object[] params;
	
	/** 默认消息，当无法国际化时取该值. */
	private String defaultMessage;
	
	/** 语言. */
	private Locale locale = Locale.getDefault();

	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 */
	public BusiCodeException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 */
	public BusiCodeException(String errorCode, Locale locale) {
		super(errorCode);
		this.errorCode = errorCode;
		this.locale = locale;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 */
	public BusiCodeException(String errorCode, Object[] params) {
		super(errorCode, params);
		this.errorCode = errorCode;
		this.params = params;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 */
	public BusiCodeException(String errorCode, Object[] params, Locale locale) {
		super(errorCode, params);
		this.errorCode = errorCode;
		this.params = params;
		this.locale = locale;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 * @param e the e
	 */
	public BusiCodeException(String errorCode, Throwable e) {
		super(errorCode, e);
		this.errorCode = errorCode;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 * @param e the e
	 */
	public BusiCodeException(String errorCode, Locale locale, Throwable e) {
		super(errorCode, e);
		this.errorCode = errorCode;
		this.locale = locale;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 * @param e the e
	 */
	public BusiCodeException(String errorCode, Object[] params, Throwable e) {
		super(errorCode, e);
		this.errorCode = errorCode;
		this.params = params;
	}
	
	/**
	 * Instantiates a new busi code exception.
	 *
	 * @param errorCode the error code
	 * @param e the e
	 */
	public BusiCodeException(String errorCode, Object[] params, Locale locale, Throwable e) {
		super(errorCode, e);
		this.errorCode = errorCode;
		this.params = params;
		this.locale = locale;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

  
  /**
   * @return the defaultMessage
   */
  public String getDefaultMessage()
  {
    return defaultMessage;
  }

  
  /**
   * @param defaultMessage the defaultMessage to set
   */
  public BusiCodeException setDefaultMessage(String defaultMessage)
  {
    this.defaultMessage = defaultMessage;
    return this;
  }
	

}
