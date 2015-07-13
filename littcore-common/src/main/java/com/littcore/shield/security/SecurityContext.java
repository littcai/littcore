package com.littcore.shield.security;

import com.littcore.shield.vo.ILoginVo;

/**
 * 
 * 
 * 用来缓存登录用户信息(参照ACEGI实现).
 * <pre><b>描述：</b>
 *    安全容器，用来存放登录用户信息，使其脱离SERVLET容器也能使用.
 *    该对象将存放于SESSION中供多个请求共享
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-10-30
 * @version 1.0
 */
public class SecurityContext 
{
	public final static String SECURITY_CONTEXT_KEY = "com.littcore.security.SecurityContext";   
	
	/** 登录对象. */
	private ILoginVo loginVo;
	
	/**
	 * @return the loginVo
	 */
	public ILoginVo getLoginVo() {
		return loginVo;
	}
	
	/**
	 * @param loginVo the loginVo to set
	 */
	public void setLoginVo(ILoginVo loginVo) {
		this.loginVo = loginVo;
	}


}
