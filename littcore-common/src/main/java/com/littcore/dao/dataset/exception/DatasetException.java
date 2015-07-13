package com.littcore.dao.dataset.exception;

import com.littcore.exception.BusiException;

/**
 * 数据集处理异常.
 * 
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-7-4
 * @version 1.0
 */
public class DatasetException extends BusiException {

	/**
	 * @param errorMsg
	 */
	public DatasetException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * @param errorMsg
	 * @param params
	 */
	public DatasetException(String errorMsg, Object[] params) {
		super(errorMsg, params);
	}

	/**
	 * @param e
	 */
	public DatasetException(Exception e) {
		super(e);
	}

	/**
	 * @param e
	 */
	public DatasetException(Throwable e) {
		super(e);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public DatasetException(String errorMsg, Exception e) {
		super(errorMsg, e);
	}

	/**
	 * @param errorMsg
	 * @param e
	 */
	public DatasetException(String errorMsg, Throwable e) {
		super(errorMsg, e);
	}

	/**
	 * @param errorMsg
	 * @param params
	 * @param e
	 */
	public DatasetException(String errorMsg, Object[] params, Exception e) {
		super(errorMsg, params, e);
	}

	/**
	 * @param errorMsg
	 * @param params
	 * @param e
	 */
	public DatasetException(String errorMsg, Object[] params, Throwable e) {
		super(errorMsg, params, e);
	}

}
