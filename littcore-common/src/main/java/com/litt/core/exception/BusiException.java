package com.litt.core.exception;

import java.text.MessageFormat;

import com.litt.core.common.BeanManager;

/**
 * 
 * 
 * 业务异常类.
 * 
 * <pre><b>描述：</b>
 *    将后台无法处理的异常及一些业务异常统一封装成此异常，用于表现层的异常信息显示
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2010-12-03 v1.1
 *    	1、重定义该类异常为运行时异常，默认无需处理
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2007-12-14
 * @version 1.0
 * 
 * 增加动态参数自动组装到异常信息
 * @since 2009-11-18
 * @version 1.1
 *
 */
public class BusiException extends RuntimeException {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数实例化.
	 */
	public BusiException() {
		super();
	}

	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 */
	public BusiException(String errorMsg) {
		super(errorMsg);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * 支持动态参数自动组装到异常消息中，示例："我是{0}，今年{1}岁"输出为"我是蔡源，今年19岁"
	 * 
	 * @param errorMsg 错误提示信息
	 * @param params 动态参数
	 */
	public BusiException(String errorMsg, Object[] params) 
	{		
		super(MessageFormat.format(errorMsg, params));
	}	

	/**
	 * 构造函数实例化.
	 * 
	 * @param e 异常对象
	 */
	public BusiException(Exception e) {
		super(e);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param e 异常对象
	 */
	public BusiException(Throwable e) {
		super(e);
	}	

	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 */
	public BusiException(String errorMsg, Exception e) {
		super(errorMsg, e);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 */
	public BusiException(String errorMsg, Throwable e) {
		super(errorMsg, e);
	}	
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public BusiException(String errorMsg, Object[] params, Exception e) {
		super(MessageFormat.format(errorMsg, params), e);
	}	
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public BusiException(String errorMsg, Object[] params, Throwable e) {
		super(MessageFormat.format(errorMsg, params), e);
	}
	
}
