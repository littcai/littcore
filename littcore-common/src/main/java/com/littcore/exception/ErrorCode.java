package com.littcore.exception;

import java.io.Serializable;
import java.util.Locale;

/**
 * 
 * 
 * 异常代码.
 * 
 * <pre><b>描述：</b>
 *    封装异常代码和异常参数，用于实现异常的国际化
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-07-17
 * @version 1.0 
 *
 */
public class ErrorCode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private Object[] params;	
	
	private Locale locale;

	/**
	 * Instantiates a new error code.
	 *
	 * @param code the code
	 */
	public ErrorCode(String code, Locale locale) {
		this.code = code;
		this.locale = locale;
	}

	/**
	 * Instantiates a new error code.
	 *
	 * @param code the code
	 * @param params the params
	 */
	public ErrorCode(String code, Object[] params, Locale locale) {
		this.code = code;
		this.params = params;
		this.locale = locale;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
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

}
