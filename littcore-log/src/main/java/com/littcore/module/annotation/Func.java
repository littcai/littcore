package com.littcore.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.littcore.log.FuncType;

/** 
 * 
 * 模块注解.
 * 
 * <pre><b>描述：</b>
 *    利用注解完成模块相关功能的描述
 *    用途：通过注解中的模块号和功能号，实现日志记录、权限校验
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-1-8
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)    
@Target(ElementType.METHOD) 
public @interface Func
{
	/**   
     * 功能类型.
     * @return 业务类型   
     */  
	int funcType() default FuncType.ALL;  
	
	String funcCode();
	
	String moduleCode();	
	
	String content() default "";
	
	/**
	 * 是否启用日志记录.
	 * 
	 * @return true, if is enable log
	 */
	boolean enableLog() default true;
	
	/**
	 * 是否启用权限校验.
	 * 
	 * @return true, if is enable permission
	 */
	boolean enablePermission() default true;
	
	/**
	 * 是否记录失败日志(默认否).
	 * 
	 * @return true, if is log fail
	 */
	boolean enableLogFail() default false;
}
