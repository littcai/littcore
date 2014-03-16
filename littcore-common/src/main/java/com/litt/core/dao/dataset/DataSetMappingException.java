package com.litt.core.dao.dataset;

import com.litt.core.exception.BusiException;

/**
 * .
 * 
 * <pre><b>Description：</b>
 *    
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-8-7
 * @version 1.0
 */
public class DataSetMappingException extends BusiException {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数实例化.
	 * 
	 * @param errorMsg 错误提示信息
	 */
	public DataSetMappingException(String errorMsg) {
		super(errorMsg);
	}
	
	public DataSetMappingException(Throwable e)
    {
        super(e);
    }
	
	public DataSetMappingException(String msg, Throwable e)
    {
        super(msg, e);
    }
	
}
