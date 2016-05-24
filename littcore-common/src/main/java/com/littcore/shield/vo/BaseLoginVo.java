package com.littcore.shield.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.mvel2.util.ThisLiteral;

import com.littcore.exception.BusiException;
import com.littcore.uid.RandomGUID;
import com.littcore.util.ArrayUtils;

/**
 * <b>Title:</b>登录操作员基本信息基类对象.
 * 
 * <b>Description:</b>
 * 
 * <pre>
 * 保存登录操作员的最基本信息，实际项目使用中继承于该基类
 * 
 * 2010-12-24 1.1
 * 	1、增加roleIds缓存，在某些需要根据角色查询的地方可以使用该属性
 * 
 * 2016-05-19 1.2
 *  1、增加一个Map用于存储动态的附加属性
 * 	
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">caiyuan</a>
 * @since 2008-08-08
 * @version 1.0
 */
public class BaseLoginVo implements ILoginVo, Serializable 
{		
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -7872789351234669285L;

	/** 操作员唯一数据库索引号. */
	private Long opId; 
	
	/** 登陆ID. */
	private String loginId; 
	
	/** 操作员名称. */
	private String opName;
	
	/** 当前登录IP. */
	private String loginIp;	
	
	/** 登录时间. */
	private Date loginDatetime = new Date();	
	
	/** 用户语言. */
	private String locale = Locale.SIMPLIFIED_CHINESE.toString();
	
	/** 时区. */
	private int timezone;
	
	/** 主题. */
	private String theme;
	
	/** 全局唯一索引号(用来区分同一用户不同设备登录). */
	private String guid = new RandomGUID().toString();
	
	/** 自动登录令牌. */
	private String autoLoginToken;
	
	/** 用户拥有权限队列. */
	private String[] permissionCodes = new String[0];	
	
	/**
	 * 用户所属角色列表.
	 */
	private long[] roleIds = new long[0];	
	
	/**
	 * 用户所属组织列表.
	 */
	private long[] groupIds = new long[0];		
	
	/** 统一会话ID. */
	private String token;
	
	/** 用户状态.
	 * 状态(1001)
	 *       0：未审核
	 *       1：正常
	 *       2：注销
	 *       3：删除
	 *       4：锁定.
	 */
	private int status;
	
	/** 工作状态. */
	private int workStatus;
	
	/** 是否强制下线. */
	private boolean isForceOffline;
	
	/** 动态属性. */
	private Map<String, Object> props = new HashMap<String, Object>();
	
	/**
	 * 构造函数.
	 *
	 * @param opId 登录操作员ID
	 * @param loginId 登录名
	 * @param opName 操作员名称
	 * @param loginIp 登录IP
	 */
	public BaseLoginVo(Long opId, String loginId, String opName, String loginIp)
	{
		this.opId = opId;
		this.loginId = loginId;
		this.opName = opName;
		this.loginIp = loginIp;
	}
	
	/**
	 * 初始化角色队列.
	 * 登录时需调用该方法初始化操作员权限
	 * 
	 * @param roleIds 角色列表
	 */
	public void initRole(long[] roleIds)
	{
		this.roleIds = roleIds;
	}	
	
	/**
	 * 初始化组织队列.
	 * 登录时需调用该方法初始化操作员权限
	 * 
	 * @param groupIds 组织列表
	 */
	public void initGroup(long[] groupIds)
	{
		this.groupIds = groupIds;
	}	
	
	/**
	 * 初始化权限队列.
	 * 登录时需调用该方法初始化操作员权限
	 * 
	 * @param permissionCodes 权限队列
	 */
	public void initPermission(String[] permissionCodes)
	{
		this.permissionCodes = permissionCodes;
	}
	
	/**
	 * 初始化权限队列.
	 * 登录时需调用该方法初始化操作员权限
	 * 
	 * @param permissionCodes 权限列表
	 */
	public void initPermission(List<String> permissionCodeList)
	{
		int size = permissionCodeList.size();
		this.permissionCodes = new String[size];
		for(int i=0;i<size;i++)
		{
			permissionCodes[i] = permissionCodeList.get(i);
		}
	}
	
	/**
	 * Adds the role id.
	 *
	 * @param roleId the role id
	 */
	public void addRoleId(long roleId)
	{
		this.roleIds = ArrayUtils.add(roleIds, roleId);
	}
	
	/**
	 * Removes the role id.
	 *
	 * @param roleId the role id
	 */
	public void removeRoleId(long roleId)
	{
		roleIds = ArrayUtils.removeElement(roleIds, roleId);
	}
	
	/**
	 * Removes the role id.
	 *
	 * @param roleIds the role ids
	 */
	public void removeRoleIds(long[] roleIds)
	{
		for (long roleId : roleIds) {
			this.removeRoleId(roleId);
		}
	}
	
	/**
	 * Adds the group id.
	 *
	 * @param groupId the group id
	 */
	public void addGroupId(long groupId)
	{
		this.groupIds = ArrayUtils.add(groupIds, groupId);
	}
	
	/**
	 * Removes the group id.
	 *
	 * @param groupId the group id
	 */
	public void removeGroupId(long groupId)
	{
		groupIds = ArrayUtils.removeElement(groupIds, groupId);
	}
	
	/**
	 * Removes the group ids.
	 *
	 * @param groupIds the group ids
	 */
	public void removeGroupIds(long[] groupIds)
	{
		for (long groupId : groupIds) {
			this.removeGroupId(groupId);
		}
	}
	
	/**
	 * 添加权限.
	 * 
	 * @param permissionCodes 权限编号(NOT NULL)
	 */
    public void addPermissions(String[] permissionCodes)
    {
    	this.permissionCodes = (String[])ArrayUtils.addAll(this.permissionCodes, permissionCodes);
    }
	
	/**
	 * 添加权限.
	 * 
	 * @param permissionCode 权限编号(NOT NULL)
	 */
    public void addPermission(String permissionCode)
    {
    	if(this.isAdministrator())	//默认系统超级管理员
			return;
    	
    	if(!this.withPermission(permissionCode))	//权限不存在才添加
    	{	    
    		boolean isFree = false;	//是否有空闲
    		int length = permissionCodes.length;
	    	for(int i=0;i<length;i++)
			{
				if(permissionCodes[i]==null)
				{
					isFree = true;
					permissionCodes[i] = permissionCode;
					break;
				}
			}
	    	if(!isFree)	//默认数据已满，需扩容
	    	{
	    		String[] newPermissionCodes = new String[length + 10];	//扩容10个空位
	    		System.arraycopy(permissionCodes, 0, newPermissionCodes, 0, length);
	    		newPermissionCodes[length] = permissionCode;
	    		permissionCodes = newPermissionCodes;
	    	}
	    	
    	}	
    }

    /**
     * 删除权限
     * @param permissionCode 权限编号(NOT NULL)
     */
    public void removePermission(String permissionCode)
    {
    	if(this.isAdministrator())	//默认系统超级管理员
			return;
    	
    	for(int i=0;i<permissionCodes.length;i++)
		{
	    	if(permissionCode.equals(permissionCodes[i]))
			{
				permissionCodes[i] = null;
				break;
			}
		}    		
    }
    
    public void removePermissions(String[] permissions)
	{   	
		for (String permissionCode : permissions) {
			permissionCodes = (String[])ArrayUtils.removeElement(permissionCodes, permissionCode);
		}
	}
    
	/**
	 * 检查是否拥有指定权限（常用在没有权限则跳转到异常页面的情况）.
	 * 
	 * @param permissionCode 权限编号(NOT NULL)
	 * 
	 * @return 拥有返回true，否则抛出异常
	 */
	public boolean hasPermission(String permissionCode) throws BusiException
	{
		if(this.isAdministrator())	//默认系统超级管理员
			return true;
		for(int i=0;i<permissionCodes.length;i++)
		{
			if(permissionCode.equals(permissionCodes[i]))
				return true;
		}
		throw new BusiException("Permission denied.");	//最终没有找到则抛出异常
	}
	
	/**
	 * 检查是否拥有指定权限（常用在判断页面某个功能点是否有权限显示）.
	 * 
	 * @param permissionCode 权限编号(NOT NULL)
	 * 
	 * @return  拥有返回true，否则返回false
	 */
	public boolean withPermission(String permissionCode)
	{
		if(this.isAdministrator())	//默认系统超级管理员
			return true;
		for(int i=0;i<permissionCodes.length;i++)
		{
			if(permissionCode.equals(permissionCodes[i]))
				return true;
		}		
		return false;
	}	
	
	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public Locale toLocale()
	{
		return LocaleUtils.toLocale(this.getLocale());
	}
	
	/**
	 * 是否默认系统管理员.
	 * 
	 * @return 是返回true；否返回false
	 */
	public boolean isAdministrator()
	{
		return opId != null && opId.longValue() == -1L;
	}
	
	/**
	 * Adds the prop.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void addProp(String key, Object value)
	{
	  this.props.put(key, value);
	}
	
	/**
	 * Gets the prop.
	 *
	 * @param key the key
	 * @return the prop
	 */
	public Object getProp(String key)
	{
	  return this.props.get(key);
	}
	
	/**
	 * Gets the login id.
	 * 
	 * @return the login id
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * Gets the op id.
	 * 
	 * @return the op id
	 */
	public Long getOpId() {
		return opId;
	}

	/**
	 * @return the loginDatetime
	 */
	public Date getLoginDatetime() {
		return loginDatetime;
	}


	/**
	 * @param loginDatetime the loginDatetime to set
	 */
	public void setLoginDatetime(Date loginDatetime) {
		this.loginDatetime = loginDatetime;
	}


	/**
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * @return the opName
	 */
	public String getOpName() {
		return opName;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}
	
	/**
	 * @return the roleIds.
	 */
	public long[] getRoleIds()
	{
		return roleIds;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the isForceOffline
	 */
	public boolean isForceOffline() {
		return isForceOffline;
	}

	/**
	 * @param isForceOffline the isForceOffline to set
	 */
	public void setForceOffline(boolean isForceOffline) {
		this.isForceOffline = isForceOffline;
	}

	/**
	 * @return the autoLoginToken
	 */
	public String getAutoLoginToken() {
		return autoLoginToken;
	}

	/**
	 * @param autoLoginToken the autoLoginToken to set
	 */
	public void setAutoLoginToken(String autoLoginToken) {
		this.autoLoginToken = autoLoginToken;
	}

	/**
	 * @return the groupIds
	 */
	public long[] getGroupIds() {
		return groupIds;
	}

	/**
	 * @return the permissionCodes
	 */
	public String[] getPermissionCodes() {
		return permissionCodes;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @param opName the opName to set
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * @param loginIp the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * @return the timezone
	 */
	public int getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(int timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

  
  /**
   * @param token the token to set
   */
  public void setToken(String token)
  {
    this.token = token;
  }

  
  /**
   * @return the props
   */
  public Map<String, Object> getProps()
  {
    return props;
  }

  
  /**
   * @param props the props to set
   */
  public void setProps(Map<String, Object> props)
  {
    this.props = props;
  }

  
  /**
   * @return the workStatus
   */
  public int getWorkStatus()
  {
    return workStatus;
  }

  
  /**
   * @param workStatus the workStatus to set
   */
  public void setWorkStatus(int workStatus)
  {
    this.workStatus = workStatus;
  }
}
