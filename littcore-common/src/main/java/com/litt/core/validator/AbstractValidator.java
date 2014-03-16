package com.litt.core.validator;

import java.util.List;

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
public class AbstractValidator implements IValidator {
	
	private String fieldName;
	
	private List<IValidatorRule> validatorRules;
	
	public AbstractValidator addRule(IValidatorRule rule)
	{
		validatorRules.add(rule);
		return this;
	}
	
	public boolean validate(Object value)
	{
		return false;
	}

}
