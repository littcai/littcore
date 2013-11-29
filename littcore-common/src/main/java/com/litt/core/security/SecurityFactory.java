package com.litt.core.security;

import java.security.NoSuchAlgorithmException;

import com.litt.core.security.algorithm.DESTool;
import com.litt.core.security.algorithm.PBETool;

/** 
 * 
 * 安全工厂.
 * 
 * <pre><b>描述：</b>
 *    单例处理密码加密解密 
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
public final class SecurityFactory
{
	public static ISecurity genDES() throws NoSuchAlgorithmException
	{
		DESTool des = new DESTool(Algorithm.BLOWFISH);
		return des;
	}
	
	public static ISecurity genDES(String securityKey, String algorithm) throws NoSuchAlgorithmException
	{
		DESTool des = new DESTool(securityKey, algorithm);
		return des;
	}
	
	/**
	 * 生成PBE口令加密的DES算法工具
	 * @param password 密码
	 * @return ISecurity
	 * @throws NoSuchAlgorithmException
	 */
	public static ISecurity genPBE(String password)
	{
		PBETool des = new PBETool(password);
		return des;
	}

}
