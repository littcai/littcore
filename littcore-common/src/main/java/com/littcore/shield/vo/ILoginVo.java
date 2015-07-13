package com.littcore.shield.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import com.littcore.exception.BusiException;

/**
 * 
 * 
 * 登录操作员对象接口类.
 * 
 * <pre><b>描述：</b>
 *    描述了登录对象具有的基本方法 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2012-11-06 实现序列化接口
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-10-22
 * @version 1.0
 *
 */
public interface ILoginVo extends Serializable
{		
	public static final int STATUS_NORMAL = 1;			//正常
	public static final int STATUS_CANCELLED = 2;			//注销
	public static final int STATUS_DELETED = 3;			//删除
	public static final int STATUS_LOCKED = 4;			//锁定
	public static final int STATUS_UNCHANGEABLE = 9;		//不可更改
	
	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public Locale toLocale();	
	
	/**
	 * 是否默认系统管理员.
	 * 
	 * @return 是返回true；否返回false
	 */
	public boolean isAdministrator();	
	
	/**
	 * 检查是否拥有指定权限（常用在没有权限则跳转到异常页面的情况）.
	 * 
	 * @param permissionCode 权限编号(NOT NULL)
	 * 
	 * @return 拥有返回true，否则抛出异常
	 */
	public boolean hasPermission(String permissionCode) throws BusiException;
	

	/**
	 * 检查是否拥有指定权限（常用在判断页面某个功能点是否有权限显示）.
	 * 
	 * @param permissionCode 权限编号(NOT NULL)
	 * 
	 * @return  拥有返回true，否则返回false
	 */
	public boolean withPermission(String permissionCode);
	

	/**
	 * Gets the login id.
	 * 
	 * @return the login id
	 */
	public String getLoginId();

	/**
	 * Gets the op id.
	 * 
	 * @return the op id
	 */
	public Long getOpId();

	/**
	 * @return the loginDatetime
	 */
	public Date getLoginDatetime();

	/**
	 * @param loginDatetime the loginDatetime to set
	 */
	public void setLoginDatetime(Date loginDatetime);

	/**
	 * @return the loginIp
	 */
	public String getLoginIp();

	/**
	 * @return the opName
	 */
	public String getOpName();

	/**
	 * @return the guid
	 */
	public String getGuid();
	
	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public String getLocale();	
	
	/**
	 * Gets Timezone.
	 * @return the timezone
	 */
	public int getTimezone() ;
	
	/**
	 * @return the theme
	 */
	public String getTheme();

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus();
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status);
	
	/**
	 * @return the autoLoginToken
	 */
	public String getAutoLoginToken();
	
	/**
	 * @return the isForceOffline
	 */
	public boolean isForceOffline();
	
	/**
	 * @param isForceOffline the isForceOffline to set
	 */
	public void setForceOffline(boolean isForceOffline);
	
	/**
	 * Gets the permission codes.
	 *
	 * @return the permission codes
	 */
	public String[] getPermissionCodes();
	
	/**
	 * @return the groupIds
	 */
	public long[] getGroupIds();
}