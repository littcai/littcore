package com.litt.core.shield.security;


/**
 * 
 * 请求上下文管理器.
 * <pre><b>描述：</b>
 *    用ThreadLocal的方式缓存SESSION操作员信息，供业务层使用(主要用于操作日志的记录)
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-10-30
 * @version 1.0
 */
public class SecurityContextHolder 
{	
	private static ThreadLocal context = new ThreadLocal() 
	{   
        protected synchronized Object initialValue() {   
           return new SecurityContext();   //初始化一个空的安全容器
       }   
   };     
      
   public static SecurityContext getContext() {   
       return (SecurityContext) context.get();   
   }
   
   public static void setContext(SecurityContext securityContext)
   {
	   context.set(securityContext);
   }
   
   /**
    * 清理上下文
    *
    */
   public static void clear()
   {
	   context.set(null);
   }
   
}
