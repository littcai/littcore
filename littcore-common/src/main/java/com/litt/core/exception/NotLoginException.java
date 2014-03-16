package com.litt.core.exception;

/**
 * 
 * 
 * 用户未登录异常.
 * 
 * <pre><b>描述：</b>
 *    显示的抛出此异常有助于对未登录操纵进行业务处理 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-3-25
 * @version 1.0
 *
 */
public class NotLoginException extends BusiException
{

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new not login exception.
     */
    public NotLoginException() {
		super("");		
	}

	/**
	 * Instantiates a new not login exception.
	 * 
	 * @param e 异常
	 */
	public NotLoginException(Exception e) {
		super(e);		
	}

	/**
	 * Instantiates a new not login exception.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常
	 */
	public NotLoginException(String errorMsg, Exception e) {
		super(errorMsg, e);			
	}

	/**
	 * Instantiates a new not login exception.
	 * 
	 * @param errorMsg 错误提示信息
	 */
	public NotLoginException(String errorMsg) {
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
	public NotLoginException(String errorMsg, String[] params) 
	{
		super(errorMsg, params);
	}
      
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public NotLoginException(String errorMsg, String[] params, Exception e)
	{
		super(errorMsg, params, e);
	}
	
	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 * @param e 异常对象
	 * @param params 动态参数
	 */
	public NotLoginException(String errorMsg, String[] params, Throwable e)
	{
		super(errorMsg, params, e);
	}
	
}
