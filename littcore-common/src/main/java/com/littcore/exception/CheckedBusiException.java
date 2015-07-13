package com.littcore.exception;

import java.text.MessageFormat;

/**
 * 
 * 
 * 受查业务异常类.
 * 
 * <pre><b>描述：</b>
 *    将后台无法处理的异常及一些业务异常统一封装成此异常，该异常需捕获并处理，用于异常产生时做特定处理
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2012-02-21
 * @version 1.0
 *
 */
public class CheckedBusiException extends Exception {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造函数实例化.
	 */
	public CheckedBusiException() {
		super();
	}

	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 */
	public CheckedBusiException(String errorMsg) {
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
	public CheckedBusiException(String errorMsg, Object[] params) 
	{		
		super(MessageFormat.format(errorMsg, params));
	}	

	/**
	 * 构造函数实例化.
	 * 
	 * @param e 异常对象
	 */
	public CheckedBusiException(Exception e) {
		super(e);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param e 异常对象
	 */
	public CheckedBusiException(Throwable e) {
		super(e);
	}	

	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 */
	public CheckedBusiException(String errorMsg, Exception e) {
		super(errorMsg, e);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 */
	public CheckedBusiException(String errorMsg, Throwable e) {
		super(errorMsg, e);
	}	
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public CheckedBusiException(String errorMsg, Object[] params, Exception e) {
		super(MessageFormat.format(errorMsg, params), e);
	}	
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public CheckedBusiException(String errorMsg, Object[] params, Throwable e) {
		super(MessageFormat.format(errorMsg, params), e);
	}	
}
