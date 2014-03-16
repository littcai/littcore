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
public class Validators {
	
	private List<IValidator> validators;
	
	public Validators addValidator(IValidator validator)
	{
		validators.add(validator);
		return this;
	}
	
	public void validate()
	{
		
	}

}
