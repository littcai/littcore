package com.litt.core.shield.common;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.litt.core.shield.vo.ILoginVo;

/** 
 * 
 * 在线操作员管理.
 * 
 * <pre><b>描述：</b>
 *    存储并管理在线操作员信息.需单例运行
 * 依赖：
 * 1、OnlineOperatorListener：用于监听并存储在线用户信息，对象实例在该监听器创建时创建
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-12-21
 * @version 1.0
 *
 */
public class OnlineManager
{		
	/**
	 * 在线操作员存储映射.
	 * KEY: GUID会话唯一ID
	 * VALUE: ILoginVo
	 */
	Map<String, ILoginVo> onlineOperatorMap = new ConcurrentHashMap<String, ILoginVo>();
	
	/**
	 * The Class SingletonClassInstance.
	 */
	private static class SingletonClassInstance { 
	    
    	/** The Constant instance. */
    	private static final OnlineManager instance = new OnlineManager(); 
	} 

	/**
	 * Gets the single instance of ClientSessionManager.
	 *
	 * @return single instance of ClientSessionManager
	 */
	public static OnlineManager getDefaultInstance() { 
	    return SingletonClassInstance.instance; 
	}
	
	/**
	 * 添加登录对象.
	 * @param loginVo 登录操作员对象
	 */
	public void addLoginVo(ILoginVo loginVo)
	{
		onlineOperatorMap.put(loginVo.getGuid(), loginVo);
	}
	
	/**
	 * 移除登录对象.
	 * @param loginVo 登录操作员对象
	 */
	public void removeLoginVo(ILoginVo loginVo)
	{
		onlineOperatorMap.remove(loginVo.getGuid());
	}
	
	/**
	 * 是否存在该会话.
	 *
	 * @param guid the guid
	 * @return true, if is exist
	 */
	public boolean isExist(String guid)
	{
		return onlineOperatorMap.containsKey(guid);
	}
	
	/**
	 * 获取登录操作员对象.
	 * @param guid 会话唯一ID
	 * @return ILoginVo对象(不存在则返回NULL)
	 */
	public ILoginVo getLoginVo(String guid)
	{
		return onlineOperatorMap.get(guid);
	}
	
	/**
	 * 获取登录操作员对象.
	 * 
	 * @param loginId 登录名
	 * 
	 * @return ILoginVo对象(不存在则返回NULL)
	 */
	public ILoginVo getLoginVoByLoginId(String loginId)
	{
		Iterator<ILoginVo> iter = this.getAll();
		while (iter.hasNext())
		{
			ILoginVo element = iter.next();
			if(element.getLoginId().equalsIgnoreCase(loginId))
			{
				return element;
			}
		}
		return null;
	}	
	
	/**
	 * 获取登录操作员对象.
	 * 
	 * @param opId 操作员ID
	 * 
	 * @return ILoginVo对象(不存在则返回NULL)
	 */
	public ILoginVo getLoginVo(long opId)
	{
		Iterator<ILoginVo> iter = this.getAll();
		while (iter.hasNext())
		{
			ILoginVo element = iter.next();
			if(element.getOpId().longValue()==opId)
			{
				return element;
			}
		}
		return null;
	}	
	
	/**
	 * 强制下线.
	 * @param guid 会话唯一ID
	 */
	public void forceOffline(String guid)
	{
		ILoginVo loginVo = getLoginVo(guid);
		if(loginVo!=null)
			loginVo.setForceOffline(true);
	}
	
	/**
	 * 获得所有在线用户列表.
	 *
	 * @return the all
	 */
	public Iterator<ILoginVo> getAll()
	{
		return onlineOperatorMap.values().iterator();		
	}
	
	/**
	 * 销毁.
	 *
	 */
	public void destroy()
	{
		onlineOperatorMap.clear();
	}
	
}
