package com.littcore.web.util;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.common.Utility;
import com.littcore.exception.CheckedBusiException;
import com.littcore.security.DecryptFailedException;
import com.littcore.security.EncryptFailedException;
import com.littcore.security.ISecurity;
import com.littcore.security.MessageDigestTool;
import com.littcore.security.algorithm.Algorithm;
import com.littcore.security.algorithm.DESTool;
import com.littcore.util.StringUtils;

/** 
 * 
 * Cookie辅助工具类.
 * 
 * <pre><b>描述：</b>
 *    简化cookie的创建、移除和查找 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-12-05
 * @version 1.0
 *
 */
public final class CookieUtils
{
    /** 日志工具. */
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	/**
	 * Default maximum age of cookies: maximum integer value, i.e. forever.
	 */
	public static final int DEFAULT_COOKIE_MAX_AGE = Integer.MAX_VALUE;
	
	
	/**
	 * 禁止外部创建对象实例.
	 */
	private CookieUtils(){}
	
	/**
	 * 添加cookie.
	 * 
	 * @param response http response
	 * @param cookie the cookie
	 */
	public static void addCookie(HttpServletResponse response, Cookie cookie) 
	{
	  //cookie.setSecure(true);
		response.addCookie(cookie);
	}
	
	/**
	 * 添加cookie(无时限).
	 * 
	 * @param response the response
	 * @param cookieName cookie名称
	 * @param cookieValue cookie值
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) 
	{
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(DEFAULT_COOKIE_MAX_AGE);
		cookie.setPath("/");	//这句很重要，以保证项目所有路径都能访问到该cookie
		//cookie.setSecure(true);
		response.addCookie(cookie);
		if (logger.isDebugEnabled()) {
			logger.debug("Added cookie with name [" + cookieName + "] and value [" + cookieValue + "]");
		}
	}
	
	/**
	 * 删除cookie.
	 * 
	 * @param response the response
	 * @param cookieName cookie名称
	 */
	public static void removeCookie(HttpServletResponse response,String cookieName) {
		Cookie cookie = new Cookie(cookieName,"");
		cookie.setMaxAge(0);
		cookie.setPath("/");	//这句很重要，以保证项目所有路径都能访问到该cookie
		//cookie.setSecure(true);
		response.addCookie(cookie);
		if (logger.isDebugEnabled()) {
			logger.debug("Removed cookie with name [" + cookieName + "]");
		}
	}
	
	/**
	 * 获得cookie.
	 * 
	 * @param cookieName cookie名称
	 * @param request http请求对象
	 * 
	 * @return the cookie
	 */
	public static Cookie getCookie(HttpServletRequest request,String cookieName) 
	{
		Cookie[] cookies = request.getCookies();
		if(cookies==null)
			return null;
		for(int i=0;i<cookies.length;i++)
		{
			Cookie cookie = cookies[i];
			if(cookie.getName().equals(cookieName))
				return cookie;
		}
		return null;
	}
	
	/**
	 * 获得cookie值.
	 * 
	 * @param cookieName cookie名称
	 * @param request http请求对象
	 * 
	 * @return the cookie's value
	 */
	public static String getCookieValue(HttpServletRequest request,String cookieName) 
	{
		Cookie[] cookies = request.getCookies();
		if(cookies==null)
			return null;
		for(int i=0;i<cookies.length;i++)
		{
			Cookie cookie = cookies[i];
			if(cookie.getName().equals(cookieName))
				return cookie.getValue();
		}
		return null;
	}	
	
	/**
	 * 加密cookie.
	 * cookie值构成：原始内容+客户端IP+生成时间戳+过期时间+数字摘要
	 * 
	 * 采用DES算法进行加密，SHA1算法进行数字摘要
	 * 
	 *
	 * @param securityKey 密钥
	 * @param value 原始cookie值
	 * @param clientIp 客户端IP
	 * @param expiry 有效期
	 * @return 加密后字符串
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws EncryptFailedException 加密失败
	 */
	public static String encryptCookie(String securityKey, String value, String clientIp, int expiry) throws NoSuchAlgorithmException, EncryptFailedException
	{
		ISecurity tool = new DESTool(securityKey, Algorithm.DES);
		StringBuilder sb = new StringBuilder(value);
		sb.append(":").append(clientIp);						//追加客户端IP
		sb.append(":").append(System.currentTimeMillis());		//追加时间戳
		sb.append(":").append(expiry);							//追加过期时间
		
		String targetValue = sb.toString();			//最终cookie值
		//对目标值进行数字摘要
		targetValue = MessageDigestTool.encryptSHA(targetValue);
		//将摘要追加到cookie值
		sb.append(":").append(targetValue);
		//用DES算法加密
		
		String finalValue = tool.encrypt(sb.toString());	//已在内部使用了BASE64编码
		//用BASE64编码
		//String finalValue = Base64.encodeBase64String(targetValue.getBytes());
		if(logger.isDebugEnabled())
		{
			logger.debug("Final cookie value:{}, length:{}", new Object[]{finalValue, finalValue.length()});
		}
		return finalValue;
	}
	
	/**
	 * 解密cookie.
	 *
	 * @param securityKey 密钥
	 * @param cookieValue 加密的cookie值
	 * @param clientIp 客户端IP
	 * @return 解密后cookie值
	 * @throws NoSuchAlgorithmException 算法不存在
	 * @throws DecryptFailedException 解密失败
	 * @throws EncryptFailedException 加密失败
	 * @throws CheckedBusiException 其他校验失败
	 */
	public static String decryptCookie(String securityKey, String cookieValue, String clientIp) throws NoSuchAlgorithmException, DecryptFailedException, EncryptFailedException, CheckedBusiException
	{
		ISecurity tool = new DESTool(securityKey, Algorithm.DES);
		cookieValue = tool.decrypt(cookieValue);
		//获取数字摘要并检验		
		String[] valueArray = StringUtils.split(cookieValue, ':');	
		
		if(valueArray.length<5)	//内容中可能也会带分隔符，所以通过分隔符切分长度不定，需要从后面反推
			throw new DecryptFailedException("Fake cookie");
		String sourceValue = StringUtils.substringBeforeLast(cookieValue, ":");	//最后一位为数字摘要
		
		//进行SHA数字摘要，并核对
		String messageDigest = MessageDigestTool.encryptSHA(sourceValue);
		if(!messageDigest.equals(valueArray[valueArray.length-1]))
			throw new DecryptFailedException("Fake cookie");
		//通过时间戳和过期时间检查cookie是否过期
		String timestampStr = valueArray[valueArray.length-3];
		long timestamp = Utility.parseLong(timestampStr);
		String expiryStr = valueArray[valueArray.length-2];
		int expiry = Utility.parseInt(expiryStr);
		String ip = valueArray[valueArray.length-4];
		if((System.currentTimeMillis() - timestamp - expiry*1000) > 0)
		{
			throw new CheckedBusiException("Cookie is expired");
		}	
		//可选，检查客户端IP的一致性
		if(!ip.equals(clientIp))
		{
			throw new CheckedBusiException("Cookie from another ip");
		}
		String value = StringUtils.substring(sourceValue, 0, sourceValue.length() - timestampStr.length() - expiryStr.length() - ip.length() - 3);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Source cookie value:{}", new Object[]{value});
		}
		return value;
	}
	
	/**
	 * 解密cookie.
	 *
	 * @param securityKey 密钥
	 * @param cookie cookie对象
	 * @param clientIp 客户端IP
	 * @return 解密后cookie值
	 * @throws NoSuchAlgorithmException 算法不存在
	 * @throws DecryptFailedException 解密失败
	 * @throws EncryptFailedException 加密失败
	 * @throws CheckedBusiException 其他校验失败
	 */
	public static String decryptCookie(String securityKey, Cookie cookie, String clientIp) throws NoSuchAlgorithmException, DecryptFailedException, EncryptFailedException, CheckedBusiException
	{
		return decryptCookie(securityKey, cookie.getValue(), clientIp);
	}
	
}
