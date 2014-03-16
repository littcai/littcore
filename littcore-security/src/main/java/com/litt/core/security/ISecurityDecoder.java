package com.litt.core.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * 编码器.
 * 
 * <pre><b>描述：</b>
 * 	根据特定算法将数据源加密   
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2014-1-14
 * @version 1.0
 */
public interface ISecurityDecoder {
	
	/**
	 * 解密字节数组
	 * 
	 * @param str 需解密的字符串
	 * @return 解密后的字符串
	 * @throws DecryptFailedException 解密失败异常
	 */
	public String decrypt(String str) throws DecryptFailedException;
	
	
	/**
	 * 数字签名校验.
	 *
	 * @param source 数据源
	 * @param sign 签名后内容
	 * @return 是否通过校验
	 * @throws SignatureException the signature exception
	 */
	public boolean verify(String source, String sign) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException; 

}
