package com.litt.core.security.algorithm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import com.litt.core.security.Algorithm;
import com.litt.core.security.EncryptFailedException;
import com.litt.core.util.ByteUtils;
import com.litt.core.util.ValidateUtils;

/**
 * 
 * 数字签名公钥解码辅助类.
 * <pre><b>描述：</b>
 * 验证端通过自己的公钥文件对数字签名解码，并验证签名的有效性
 * 支持算法：
 *    1、DSA 
 *    2、SHA1withDSA
 *    3、MD2withRSA
 *    4、MD5withRSA 
 *    5、SHA1withRSA
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-12-12,2009-03-10
 * @version 1.0,2.0
 */
public class DSATool 
{	
	/** 签名算法. */
	private String algorithm = Algorithm.DSA;

	/** 公钥. */
	private PublicKey pubKey;

	/** 私钥. */
	private PrivateKey priKey;

	public DSATool() throws NoSuchAlgorithmException 
	{
		this(Algorithm.DSA);
	}
	
	/**
	 * 
	 * @param algorithm 签名算法
	 */
	public DSATool(String algorithm) throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;
		this.generateKeys(Algorithm.DEFAULT_KEY);
	}
	
	/**
	 * The Constructor.
	 * 
	 * @param algorithm 签名算法
	 * @param securityKey 密钥
	 * 
	 * @throws NoSuchAlgorithmException 未找到算法异常
	 */
	public DSATool(String algorithm, String securityKey) throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;
		if(ValidateUtils.isEmpty(securityKey))
			securityKey = Algorithm.DEFAULT_KEY;
		this.generateKeys(securityKey);
	}	
	
	/**
	 * 生成非对称密钥对.
	 * 
	 * @param securityKey 密钥
	 * 
	 * @throws NoSuchAlgorithmException 未找到算法异常
	 */
	private void generateKeys(String securityKey) throws NoSuchAlgorithmException
	{		
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm);
		
		//参数:keysize 算法位长.其范围必须在 512 到 1024 之间，且必须为 64 的倍数
		//参数:random 一个随机位的来源
		keygen.initialize(512, new SecureRandom(securityKey.getBytes())); //初始化密钥生成器   

		KeyPair keyPair = keygen.generateKeyPair();//生成新密钥对   
		pubKey = keyPair.getPublic();
		priKey = keyPair.getPrivate();		
	}
	
	/**
	 * 将公钥私钥保存到文件.
	 * 
	 * @param priFileName 私钥文件
	 * @param pubFileName 公钥文件
	 */
	public void saveKeys(String priFileName,String pubFileName) throws IOException
	{				
		FileOutputStream fos = new FileOutputStream(pubFileName);
			
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(pubKey);
		oos.close();
		fos = new FileOutputStream(priFileName);
		oos = new ObjectOutputStream(fos);
		oos.writeObject(priKey);
		oos.close();		
	}
	
	/**
	 * 利用当前私钥对信息进行加密签名.
	 * 
	 * @param source            
	 *            需要签名的字符串
	 * 
	 * @return 签名信息（byte类型）
	 * 
	 * @throws EncryptFailedException
	 *             the encrypt failed exception
	 */
	public byte[] encrypt(byte[] source) throws EncryptFailedException
	{		
		if (priKey == null) 
		{
			throw new EncryptFailedException("提取密钥信息错误，无法完成签名！");			
		}
		try {
			Signature sign = Signature.getInstance(algorithm);	//没有默认算法名，必须指定名称
			sign.initSign(priKey);
			sign.update(source);
			return sign.sign();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptFailedException(e);
		} catch (InvalidKeyException e) {
			throw new EncryptFailedException("签名错误，无效的密钥！",e);
		} catch (SignatureException e) 
		{
			throw new EncryptFailedException(e);
		}		
	}
	
	/**
	 * 利用当前私钥对信息进行加密签名.
	 * 
	 * @param source            
	 *            需要签名的字符串
	 * 
	 * @return 签名信息（byte类型）
	 * 
	 * @throws EncryptFailedException
	 *             the encrypt failed exception
	 */
	public String encrypt(String source) throws EncryptFailedException
	{
		return ByteUtils.toHexString(this.encrypt(source.getBytes()));
	}


	public static void main(String[] args) throws Exception
	{	
		//进行签名校验			
		DSATool utils = new DSATool();
		utils.saveKeys("D:\\pirKey.key", "D:\\pubKey.key");
		String signed = utils.encrypt("eqFMjeXq77xS");
		System.out.println(signed);
		
		
	}

}
