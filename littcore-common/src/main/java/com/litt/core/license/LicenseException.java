package com.litt.core.license;

/**
 * 
 * <b>标题：</b> 证书校验异常.
 * <pre><b>描述：</b>
 *    当系统未通过证书授权则抛出此异常
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-12-12
 * @version 1.0
 */
public class LicenseException extends Exception 
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * 抛出默认错误信息的异常.
	 */
	public LicenseException()
	{
		super("license not valid!");
	}	
	
	/**
	 * 抛出指定错误信息的异常.
	 * 
	 * @param errorMsg 错误信息
	 */
	public LicenseException(String errorMsg)
	{
		super(errorMsg);
	}
	
	/**
	 * 抛出指定异常的异常.
	 *  
	 * @param e 异常
	 */
	public LicenseException(Exception e)
	{
		super(e);
	}	
	
	/**
	 * 抛出指定异常的异常.
	 * 
	 * @param errorMsg 错误信息
	 * @param e 异常
	 */
	public LicenseException(String errorMsg, Exception e)
	{
		super(errorMsg, e);
	}	
}
