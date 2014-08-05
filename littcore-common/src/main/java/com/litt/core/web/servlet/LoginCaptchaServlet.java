package com.litt.core.web.servlet;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.litt.core.common.CoreConstants;
import com.litt.core.common.Utility;
import com.litt.core.security.Captcha;

/**
 * 用户登录认证码验证.
 * 
 * <pre><b>描述：</b> 
 * 用户登录时为避免恶意攻击的认证码验证，使用该认证码登录后，用户必需在页面上输入正确的认证码才能登录
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-22
 * @version 1.0
 */
public class LoginCaptchaServlet extends HttpServlet 
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;	
	
	private String sessionCaptchaName = CoreConstants.SESSION_CAPTCHA;
	

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * 
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * 
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		HttpSession session = request.getSession();		
		response.setContentType("image/gif");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		//获取初始化参数
		int authCodeLength = Utility.parseInt(this.getInitParameter("length"),4);
		String charset = Utility.trimNull(this.getInitParameter("charset"),Captcha.CHARSET_LETTER_NUM);
		String charArray = Utility.trimNull(this.getInitParameter("charArray"));
		//生成随机认证码
		Captcha captcha = new Captcha();
		captcha.setLength(authCodeLength);
		captcha.setCharset(charset);
		if(Captcha.CHARSET_USER_DEFINE.equals(charset))
			captcha.setCharArray(charArray.toCharArray());
		captcha.generate(captcha.getRandom());
		//将认证码存入SESSION
		session.setAttribute(sessionCaptchaName, captcha.getCaptchaCode());
		//图片处理
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(captcha.getCaptchaImage(), "jpg", out);
		
	}
	
	/**
	 * 从请求中获取认证码，并校验.
	 * 认证码需以request的参数形式传递，名称：captcha
	 * 
	 * @param request 请求对象
	 * 
	 * @return 校验通过返回true，不通过返回false
	 */
	public static boolean validateCaptcha(HttpServletRequest request)
	{
		return validateCaptcha(request,CoreConstants.SESSION_CAPTCHA);
	}
	
	/**
	 * 从请求中获取认证码，并校验.
	 * 认证码需以request的参数形式传递，名称：captcha
	 * 
	 * @param request 请求对象
	 * @param sessionCaptchaName SESSION中认证码的名称
	 * 
	 * @return 校验通过返回true，不通过返回false
	 */
	public static boolean validateCaptcha(HttpServletRequest request,String sessionCaptchaName)
	{
		String captcha = Utility.trimNull(request.getParameter("captcha"));		
			
		HttpSession session = request.getSession();		
		String sessionCaptcha = getSessionCaptcha(session,sessionCaptchaName);
		//取出后清理SESSION
		session.removeAttribute(sessionCaptchaName);		
		if(Utility.isEmpty(sessionCaptcha)||Utility.isEmpty(captcha))
			return false;
		return captcha.equalsIgnoreCase(sessionCaptcha);
	}	
	
	/**
	 * 从请求中获取认证码，并校验.
	 * 认证码需以request的参数形式传递，名称：captcha
	 * 
	 * @param session 会话
	 * @param captcha 请求中的认证码
	 * 
	 * @return 校验通过返回true，不通过返回false
	 */
	public static boolean validateCaptcha(HttpSession session,String captcha)
	{
		return validateCaptcha(session,captcha,CoreConstants.SESSION_CAPTCHA);
	}
	
	/**
	 * 从请求中获取认证码，并校验.
	 * 认证码需以request的参数形式传递，名称：captcha
	 * 
	 * @param sessionCaptchaName SESSION中认证码的名称
	 * @param session 会话
	 * @param captcha 请求中的认证码
	 * 
	 * @return 校验通过返回true，不通过返回false
	 */
	public static boolean validateCaptcha(HttpSession session,String captcha,String sessionCaptchaName)
	{
		String sessionCaptcha = getSessionCaptcha(session,sessionCaptchaName);
		if(Utility.isEmpty(sessionCaptcha)||Utility.isEmpty(captcha))
			return false;
		return captcha.equalsIgnoreCase(sessionCaptcha);
	}	
	
	/**
	 * 从SESSION中获得认证码（获得后SESSION中的值将被清除）.
	 * @param session 会话
	 * @param sessionCaptchaName 认证码名称
	 * @return 认证码
	 */
	public static String getSessionCaptcha(HttpSession session,String sessionCaptchaName)
	{
		Object sessionCaptcha = session.getAttribute(sessionCaptchaName);
		//取出后清理SESSION
		session.removeAttribute(sessionCaptchaName);
		return Utility.trimNull(sessionCaptcha);
	}

	/**
	 * 从请求中获取认证码，并校验.
	 * 认证码需以request的参数形式传递，名称：captcha
	 * 
	 * @param request 请求对象
	 * @param sessionCaptchaName SESSION中认证码的名称
	 * 
	 * @return 校验通过返回true，不通过返回false
	 */
	public static boolean validateCaptcha(String captcha,String sessionCaptcha)
	{		
		if(Utility.isEmpty(sessionCaptcha)||Utility.isEmpty(captcha))
			return false;
		return captcha.equalsIgnoreCase(sessionCaptcha.toString());	//不区分大小写
	}

	/**
	 * @return the sessionCaptchaName
	 */
	public String getSessionCaptchaName()
	{
		return sessionCaptchaName;
	}

	/**
	 * @param sessionCaptchaName the sessionCaptchaName to set
	 */
	public void setSessionCaptchaName(String sessionCaptchaName)
	{
		this.sessionCaptchaName = sessionCaptchaName;
	}

}
