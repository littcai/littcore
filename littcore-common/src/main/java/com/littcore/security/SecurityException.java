package com.littcore.security;


/**
 * 
 * <b>标题：</b> 加密失败异常.
 * <pre><b>描述：</b>
 *    统一封装加密异常
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since Sep 12, 2008
 * @version 1.0
 */
public class SecurityException extends Exception 
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public SecurityException(String errorMsg)
	{
		super(errorMsg);
	}
	
	public SecurityException(Throwable error)
	{
		super("Security error.",error);
	}	
	
	public SecurityException(String errorMsg,Throwable error)
	{
		super(errorMsg,error);
	}		
	
}
