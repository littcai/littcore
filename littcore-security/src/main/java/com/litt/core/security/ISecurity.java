package com.litt.core.security;

/** 
 * 
 * 安全认证接口.
 * 
 * <pre><b>描述：</b>
 *    安全认证接口 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-13
 * @version 1.0
 *
 */
public interface ISecurity
{

	/**
	 * 解密字节数组
	 * 
	 * @param str 需解密的字符串
	 * @return 解密后的字符串
	 * @throws DecryptFailedException 解密失败异常
	 */
	public String decrypt(String str) throws DecryptFailedException;
	
	/**
	 * 加密字节数组
	 * 
	 * @param str 需加密的字符串
	 * @return 加密后的字符串
	 * @throws EncryptFailedException 加密失败异常
	 */
	public String encrypt(String str) throws EncryptFailedException;	

}
