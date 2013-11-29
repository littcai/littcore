package com.litt.core.security;

/** 
 * 
 * 算法.
 * 
 * <pre><b>描述：</b>
 *    已实现算法列表 
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
public class Algorithm
{
	public static final String DEFAULT_KEY = "LITTCORE";		//默认密钥
	
	//各种加密算法
	
	/* ******************** 对称加密算法（共享密钥） *************************** */
	
	
	/**
	 * 对称分组密码.
	 * 密钥长度56位
	 * .des
	 */
	public static final String DES = "DES";	
	
	/**
	 * 对称分组密码(3倍).
	 * TripleDES/DESede
	 * 密钥长度为112或168位
	 * .3des
	 */
	public static final String TRIPLE_DES = "DESede";	
	
	/**
	 * 对称分组密码.
	 * 目前替代DES的最快和最安全的算法
	 * 密钥长度448位
	 */
	public static final String BLOWFISH = "Blowfish";	
	
	/**
	 * 对称流密码(Rivet's Code 4).
	 * 大部分SSL由它实现
	 * 密钥长度40到128位
	 * 
	 * SUN的JCE尚未实现
	 */
	public static final String RC4 = "RC4";
	
	/**
	 * 高级加密标准.
	 * 密钥长度128、192和256位，分组大小也为128、192和256位
	 */
	public static final String AES = "AES";	//AES(16)	
	
	/* ******************** 非对称加密算法（公钥、私钥） *************************** */
	
	/** 
	 * 特点：公钥和私钥都可以用于加密
	 */
	public static final String RSA = "RSA";	
	
	public static final String MD2withRSA = "MD2withRSA";
	
	public static final String MD5withRSA = "MD5withRSA";
	
	public static final String SHA1withRSA = "SHA1withRSA";	
	

	/* ******************** 消息摘要 *************************** */
	
	/**
	 * 128位
	 */
	public static final String MD5 = "MD5";

	
	/**
	 * 安全杂凑算法（SHA=SHA1）
	 * 160位
	 */
	public static final String SHA1 = "SHA1";
	
	/**
	 * MAC算法.
	 */
	public static final String HmacMD5    = "HmacMD5";
	
	/**
	 * MAC算法.
	 */
	public static final String HmacSHA1   = "HmacSHA1";
	
	/**
	 * MAC算法.
	 */
	public static final String HmacSHA256 = "HmacSHA256";
	
	/**
	 * MAC算法.
	 */
	public static final String HmacSHA384    = "HmacSHA384";	
	
	/**
	 * MAC算法.
	 */
	public static final String HmacSHA512    = "HmacSHA512";

	
	/* ******************** 数字签名 *************************** */
	
	/**
	 * 
	 * .dsa
	 */
	public static final String DSA = "DSA";	
	
	public static final String SHA1withDSA = "SHA1withDSA";
}
