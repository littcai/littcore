package com.littcore.validator;

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
 * @since 2012-9-21
 * @version 1.0
 */
public class AbstractValidatorRule {
	
	/** 
	 * 规则编号.
	 * 具有唯一性，可实现国际化
	 */
	private String code;
	
	
	public boolean isValid(Object value)
	{
		return true;
	}
	
	/**
	 * validate value by domain rule.
	 */
	public void validate(Object value) throws DataValidationException
	{
		
	}

}
