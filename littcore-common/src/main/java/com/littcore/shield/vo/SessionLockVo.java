package com.littcore.shield.vo;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.littcore.exception.BusiException;

/**
 * 会话锁定信息对象.
 * 
 * <pre><b>描述：</b>
 * 	 使用相关策略对会话出现恶意操作是锁定会话。
 * 1、检查SESSION中的锁定信息，没有则会自动创建
 * 2、检查是否需要锁定
 * 3、在业务异常时更新锁定信息，在正常时移除锁定
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-21
 * @version 1.0
 */
public class SessionLockVo implements Serializable
{	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4947331452607082684L;

	/** The Constant SESSION_LOCK. */
	public static final String SESSION_LOCK = SessionLockVo.class.getName()+".SESSION_LOCK";
	
	/** 开始锁定时间. */
	private long beginLockTime;
	
	/** 上次重试时间（毫秒）. */
	private long lastRetryTime;
	
	/** 累计重试次数. */
	private int retryTimes;
	
	/** 重试次数限制. */
	private int retryTimesLimit = 3;
	
	/** 帐户锁定时间（毫秒）. */
	private long lockTimes = 30 * 1000;	//默认30秒
	
	private HttpSession session;
	
	public SessionLockVo(HttpSession session)
	{
		this.session = session;
	}
	
	/**
	 * 移除登录锁定信息
	 * @param session
	 */
	public void removeLock()
	{
		session.removeAttribute(SessionLockVo.SESSION_LOCK);	//成功登录后移除会话中的登录锁定信息		
	}
	
	/**
	 * 获得锁定信息
	 * @param session
	 * @return
	 */
	public static SessionLockVo getLock(HttpSession session)
	{
		SessionLockVo sessionLockVo = (SessionLockVo)session.getAttribute(SessionLockVo.SESSION_LOCK);	//登录会话锁定
		//当发生业务异常时，在会话中记录尝试次数，当次数超过3次时锁定该会话30秒
		if(sessionLockVo==null)	
		{
			sessionLockVo = new SessionLockVo(session);			
			session.setAttribute(SessionLockVo.SESSION_LOCK,sessionLockVo);
		}
		return sessionLockVo;
		
	}

	/**
	 * 更新锁定信息
	 * @param session	
	 */
	public void updateLock()
	{
		//当发生业务异常时，在会话中记录尝试次数，当次数超过3次时锁定该会话30秒		
		this.setLastRetryTime(System.currentTimeMillis());
		this.setRetryTimes(this.getRetryTimes()+1);
		session.setAttribute(SessionLockVo.SESSION_LOCK,this);		
	}

	/**
	 * 检查登录锁定信息
	 * @param session	
	 * @throws BusiException
	 */
	public void checkLock() throws BusiException
	{
		//在指定的时间段内，输入不正确的密码达到了指定的次数，帐户锁定策略将禁用用户帐户		
		
			long beginLockTime = this.getBeginLockTime();	//第开始锁定时间
			long lastRetryTime = this.getLastRetryTime();
			long currentTime = System.currentTimeMillis();			
			
			if((currentTime-lastRetryTime)<3000)	//2次登录重试间隔必须大于等于3秒
			{
				throw new BusiException("2次登录重试间隔必须大于3秒！");
			}
			
			int retryTimes = this.getRetryTimes();
			if(retryTimes>=this.getRetryTimesLimit())	//重试次数超过限制
			{
				if(beginLockTime==0)	//如果之前还没锁定
				{
					this.setBeginLockTime(System.currentTimeMillis());	//设置开始锁定时间
					throw new BusiException("超过重试次数限制，本次会话将锁定30秒，30秒后自动解锁！");
				}
				else
				{
					if((currentTime-beginLockTime)>this.getLockTimes())	//超过锁定时间则清除锁定信息，允许再次尝试
					{
						session.removeAttribute(SessionLockVo.SESSION_LOCK);	//成功登录后移除会话中的登录锁定信息				
					}
					else
					{
						throw new BusiException("超过重试次数限制，本次会话将锁定30秒，30秒后自动解锁！");
					}
				}				
			}				
	}

	/**
	 * @return the lastRetryTime
	 */
	public long getLastRetryTime()
	{
		return lastRetryTime;
	}

	/**
	 * @param lastRetryTime the lastRetryTime to set
	 */
	public void setLastRetryTime(long lastRetryTime)
	{
		this.lastRetryTime = lastRetryTime;
	}

	/**
	 * @return the lockTimes
	 */
	public long getLockTimes()
	{
		return lockTimes;
	}

	/**
	 * @param lockTimes the lockTimes to set
	 */
	public void setLockTimes(long lockTimes)
	{
		this.lockTimes = lockTimes;
	}

	/**
	 * @return the retryTimes
	 */
	public int getRetryTimes()
	{
		return retryTimes;
	}

	/**
	 * @param retryTimes the retryTimes to set
	 */
	public void setRetryTimes(int retryTimes)
	{
		this.retryTimes = retryTimes;
	}

	/**
	 * @return the retryTimesLimit
	 */
	public int getRetryTimesLimit()
	{
		return retryTimesLimit;
	}

	/**
	 * @param retryTimesLimit the retryTimesLimit to set
	 */
	public void setRetryTimesLimit(int retryTimesLimit)
	{
		this.retryTimesLimit = retryTimesLimit;
	}

	/**
	 * @return the beginTime
	 */
	public long getBeginLockTime()
	{
		return beginLockTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginLockTime(long beginTime)
	{
		this.beginLockTime = beginTime;
	}
}
