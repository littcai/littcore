package com.litt.core.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 
 * 权限注解.
 * 
 * <pre><b>描述：</b>
 *    利用注解完成模块权限的描述.
 * 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-12-09
 * @version 1.0
 *
 * @deprecated 注：该功能已整合到Func注解
 */
@Retention(RetentionPolicy.RUNTIME)    
@Target(ElementType.METHOD) 
public @interface Permission
{	
	String funcCode();
	
	String moduleCode();
	
	/**
	 * 是否启用权限校验.
	 * 
	 * @return true, if is enable permission
	 */
	boolean enablePermission() default true;
}
