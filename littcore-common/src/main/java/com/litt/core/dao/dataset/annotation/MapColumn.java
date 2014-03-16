package com.litt.core.dao.dataset.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Map数据库列定义.
 * 
 * <pre><b>Description：</b>
 *    通过该映射方式将Map直接转到对象
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-8-2
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)    
@Target({ElementType.FIELD, ElementType.METHOD}) 
public @interface MapColumn {
	
	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String name();

}
