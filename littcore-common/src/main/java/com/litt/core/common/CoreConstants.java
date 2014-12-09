package com.litt.core.common;

/**
 * 
 * 
 * 全局常量.
 * 
 * <pre><b>描述：</b>
 *    全局常量 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-9-4
 * @version 1.0
 *
 */ 
public abstract class CoreConstants
{	
	//静态变量
  /** 是否开启DEBUG模式. */
  public static boolean IS_DEBUG = false;     
  
  
	/** 默认一页显示行数. */
	public static final int DEFAULT_PAGE_SIZE = 15;         
    
    /** 默认显示第几页. */
    public static final int DEFAULT_PAGE_INDEX = 1;   
    
    /** 默认登录失败重试次数. */
    public static final int DEFAULT_LOGIN_RETRY_TIMES = 8;
    
    /** SESSION操作员. */
    public static final String SESSION_OPER = "SESSION_OPER";	
    
    /** SESSION中存放的验证码图片KEY. */
    public static final String SESSION_CAPTCHA = "SESSION_CAPTCHA";	    
    
    /** 
     * COOKIE操作员系统ID. 
     * 
     * 在cookie中存在该ID号即认为该操作员可以自动登录
     * 
     */
    public static final String COOKIE_OPER_TOKEN = "COOKIE_OPER_TOKEN";	
    
    /**
     * Cookie中保存的用户语言
     */
    public static final String COOKIE_LOCALE = "COOKIE_LOCALE";
        
    /** 应用程序级别的系统配置信息缓存. */
    public static final String APP_SYSTEMINFO = "APP_SYSTEMINFO";	
    
    /** 应用程序级别的证书校验. */
    public static final String APP_LICENSE = "APP_LICENSE";	    
    
    /** 默认英文字体. */
    public static final String DEFAULT_FONT_FAMILY_EN = "Verdana";	
    
    /** 默认中文字体. */
    public static final String DEFAULT_FONT_FAMILY_CN = "宋体";	

    /** 默认查询条件字段前缀. */
    public static final String DEFAULT_SEARCH_PREFIX = "s_";		
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";  
    public static final String DEFAULT_TIME_FORMAT = " HH:mm:ss";  
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";  
    
    //动态，需运行时设置
    
    /** 项目根目录的绝对路径，需要在项目启动时设置，使用监听器方式注入. */
    public static String ROOT_PATH;		
}
