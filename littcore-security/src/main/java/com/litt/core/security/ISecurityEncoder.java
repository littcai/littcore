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
public interface ISecurityEncoder {
	
	/**
	 * Encrypt.
	 *
	 * @param source the source
	 * @return the string
	 * @throws EncryptFailedException the encrypt failed exception
	 */
	public String encrypt(String source) throws EncryptFailedException;
	
	
	/**
	 * 数字签名.
	 *
	 * @param source 数据源
	 * @return 签名后数据
	 * @throws SignatureException the signature exception
	 */
	public String sign(String source) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException;
	

}
