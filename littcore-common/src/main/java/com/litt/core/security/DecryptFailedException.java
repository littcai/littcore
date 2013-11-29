package com.litt.core.security;


/**
 * 
 * <b>标题：</b> 解密失败异常.
 * <pre><b>描述：</b>
 *    统一封装解密异常
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since Sep 12, 2008
 * @version 1.0
 */
public class DecryptFailedException extends Exception 
{
	public DecryptFailedException()
	{
		super("Decrypt Failed!");
	}

	public DecryptFailedException(String errorMsg)
	{
		super(errorMsg);
	}
	
	public DecryptFailedException(Throwable error)
	{
		super("Decrypt Failed!",error);
	}	
	
	public DecryptFailedException(String errorMsg,Throwable error)
	{
		super(errorMsg,error);
	}		
	
}
