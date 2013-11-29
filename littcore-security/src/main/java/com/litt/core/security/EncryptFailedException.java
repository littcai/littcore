package com.litt.core.security;


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
public class EncryptFailedException extends Exception 
{
	public EncryptFailedException()
	{
		super("Encrypt Failed!");
	}

	public EncryptFailedException(String errorMsg)
	{
		super(errorMsg);
	}
	
	public EncryptFailedException(Throwable error)
	{
		super("Encrypt Failed!",error);
	}	
	
	public EncryptFailedException(String errorMsg,Throwable error)
	{
		super(errorMsg,error);
	}		
	
}
