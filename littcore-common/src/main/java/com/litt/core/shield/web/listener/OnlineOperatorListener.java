package com.litt.core.shield.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.CoreConstants;
import com.litt.core.shield.common.OnlineManager;
import com.litt.core.shield.vo.ILoginVo;

/**
 * 
 * <b>标题：</b> 在线用户监听器.
 * <pre><b>描述：</b>
 *    监听在线用户的SESSION状态，属性变动，维护在线用户列表.
 *    PS:用户访问首页的时候其实已经创建了一个SESSION，所以对用户登录行为只能在属性变动时检测
 * 依赖：
 * 1、OnlineManager：用户存储   
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    将原先存储于ServletContext中改为存储到OnlineManager管理器实例中，并采用单例模式进行管理
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-08-06
 * @version 1.0
 */
public class OnlineOperatorListener implements ServletContextListener,
		HttpSessionListener, HttpSessionAttributeListener {
	
	private static final Log logger = LogFactory.getLog(OnlineOperatorListener.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent event) {
		//ClientSessionManager.getInstance().addSession(event.getSession());
	}

	/** 
	 * Session失效时将该登录操作员信息从在线用户列表中移除
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		//ClientSessionManager.getInstance().removeSession(session.getId());
		//只有session中存在操作员信息时才会删除，因此业务层面不能remove操作员信息，否则该逻辑将失效，OnlineManager将出现垃圾数据
		ILoginVo currentLoginVo = (ILoginVo)session.getAttribute(CoreConstants.SESSION_OPER);
		if(currentLoginVo!=null)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Operator:"+currentLoginVo.getLoginId()+" SESSION is invalid, loginIp:"+currentLoginVo.getLoginIp()+", GUID:"+currentLoginVo.getGuid());
			}
			OnlineManager onlineManager = OnlineManager.getDefaultInstance();
			if(onlineManager.isExist(currentLoginVo.getGuid()))	//存在该操作员
			{
				onlineManager.removeLoginVo(currentLoginVo);				
			}
		}
	}

	/** 
	 * 操作员登录
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent event) 
	{
		if(CoreConstants.SESSION_OPER.equals(event.getName()))	//添加的属性为操作员对象
		{
			HttpSession session = event.getSession();	//存在由于Ajax异步调用导致同一客户端同时自动登录的情况，需要增加标识处理
			ILoginVo loginVo = (ILoginVo)event.getValue();
			if(logger.isDebugEnabled())
			{
				logger.debug("Operator:"+loginVo.getLoginId()+" login, loginIp:"+loginVo.getLoginIp()+"，SessionId："+session.getId()+", GUID:"+loginVo.getGuid());
			}			
			if(session.getAttribute("x-requested-with")!=null && "XMLHttpRequest".equals(session.getAttribute("x-requested-with")))
			{
				logger.debug("Request from Ajax, disable management");
				return;
			}
			OnlineManager onlineManager = OnlineManager.getDefaultInstance();
			onlineManager.addLoginVo(loginVo);
		}		
	}

	/**
	 * 操作员注销
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent event) 
	{
		if(CoreConstants.SESSION_OPER.equals(event.getName()))	//添加的属性为操作员对象
		{
			ILoginVo currentLoginVo = (ILoginVo)event.getValue();			
			if(currentLoginVo!=null)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Operator:"+currentLoginVo.getLoginId()+" logout safely, loginIp:"+currentLoginVo.getLoginIp()+", GUID:"+currentLoginVo.getGuid());
				}			
				OnlineManager onlineManager = OnlineManager.getDefaultInstance();
				if(onlineManager.isExist(currentLoginVo.getGuid()))	//存在该操作员
				{
					onlineManager.removeLoginVo(currentLoginVo);				
				}
			}
		}	
	}

	/**
	 * 操作员重新登录
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent event) 
	{
		if(CoreConstants.SESSION_OPER.equals(event.getName()))	//添加的属性为操作员对象
		{
			ILoginVo loginVo = (ILoginVo)event.getValue();			
			OnlineManager onlineManager = OnlineManager.getDefaultInstance();
			if(onlineManager.isExist(loginVo.getGuid()))	//存在该操作员
			{
				onlineManager.removeLoginVo(loginVo);	//移除旧的操作员
			}
			onlineManager.addLoginVo(loginVo);
		}	
	}

	/**
	 * 容器清理.
	 * 
	 * @param event the event
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) 
	{		
		OnlineManager onlineManager = OnlineManager.getDefaultInstance();
		onlineManager.destroy();
		//ClientSessionManager.getInstance().destroy();		
	}
	
	/** 
	 * Servlet容器启动时的事件
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		OnlineManager instance = OnlineManager.getDefaultInstance();	//初始化默认实例
	}

}
