package com.littcore.exception;

/**
 * 
 * 
 * 功能未实现异常.
 * 
 * <pre><b>描述：</b>
 *    功能未实现时抛出次异常，用来提醒开发者此处尚有功能未实现，需要完善. 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-5
 * @version 1.0
 *
 */
public class UnimplementedException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new unimplemented exception.
	 */
	public UnimplementedException()
	{
		super("function not implemented yet!");
	}
	
	/**
	 * Instantiates a new unimplemented exception.
	 * 
	 * @param errorMsg 异常详情
	 */
	public UnimplementedException(String errorMsg)
	{
		super(errorMsg);
	}	
	

}
