package com.litt.core.security.algorithm;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.litt.core.security.Algorithm;
import com.litt.core.security.DecryptFailedException;
import com.litt.core.security.EncryptFailedException;
import com.litt.core.security.ISecurity;
import com.litt.core.util.ByteUtils;

/**
 * DES算法实现工具.
 * 
 * <pre><b>描述</b>
 * 使用DES算法对字符串进行加密解密。
 * 支持算法：DES,TRIPLE_DES,BLOWFISH,AES
 * 
 * DES          key size must be equal to 56
 * DESede(TripleDES) key size must be equal to 112 or 168
 * AES          key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish     key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2          key size must be between 40 and 1024 bits
 * RC4(ARCFOUR) key size must be between 40 and 1024 bits
 * 
 * 
 * the SunJCE provider uses ECB as the default mode, and PKCS5Padding as the default padding scheme for DES, DES-EDE and Blowfish ciphers. This means that in the case of the SunJCE provider, 
 *	    Cipher c1 = Cipher.getInstance("DES/ECB/PKCS5Padding");
 *	and 
 *	    Cipher c1 = Cipher.getInstance("DES");
 *	are equivalent statements.
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-16,2009-04-13
 * @version 1.0,1,1
 */
public class DESTool implements ISecurity
{
	/** 算法. */
	private String algorithm = Algorithm.DES;
	
	
	/** The key. */
	private Key key = null;

	/**
	 * 默认构造方法，使用默认密钥，DES算法.
	 * 
	 */
	public DESTool() throws NoSuchAlgorithmException
	{
		this(Algorithm.DEFAULT_KEY,Algorithm.DES);
	}
	
	/**
	 * 指定算法构造方法.
	 * 
	 * @param algorithm 算法
	 */
	public DESTool(String algorithm) throws NoSuchAlgorithmException	
	{
		this(Algorithm.DEFAULT_KEY,algorithm);
	}

	/**
	 * 指定密钥构造方法.
	 * 
	 * @param securityKey 密钥
	 * @param algorithm 算法
	 */
	public DESTool(String securityKey, String algorithm) throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;
		
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());		//动态安装SUN的JCE，对整个JVM都有效
	
		//从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位.		
		// 创建一个空的8位字节数组（默认值为0）
//		byte[] arrTarget = new byte[8];
//		byte[] arrSrc = securityKey.getBytes();
//
//		// 将原始字节数组转换为8位
//		for (int i = 0; i < arrSrc.length && i < arrTarget.length; i++)
//		{
//			arrTarget[i] = arrSrc[i];
//		}
		// 采用标准API生成KEY
		// KeyGenerator generator = KeyGenerator.getInstance("DES");
		// generator.init(new SecureRandom());
		// key = generator.generateKey();
		// 采用javax.crypto的API
//		 DESKeySpec dks = new DESKeySpec(strKey.getBytes());
//		 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//		 SecretKey securekey = keyFactory.generateSecret(dks);

		// 生成密钥
		KeyGenerator generator = KeyGenerator.getInstance(algorithm);
		if(Algorithm.DES.equals(this.algorithm))		
			generator.init(new SecureRandom(securityKey.getBytes()));
		else if(Algorithm.TRIPLE_DES.equals(this.algorithm))		
			generator.init(168, new SecureRandom(securityKey.getBytes()));
		else if(Algorithm.BLOWFISH.equals(this.algorithm))	
			generator.init(128, new SecureRandom(securityKey.getBytes()));
		else if(Algorithm.AES.equals(this.algorithm))	
			generator.init(128, new SecureRandom(securityKey.getBytes()));
			
		key = generator.generateKey();	
		
	}

	/**
	 * 加密字节数组.
	 * 
	 * @param byteArray 需加密的字节数组
	 * 
	 * @return 加密后的字节数组
	 * 
	 * @throws EncryptFailedException the encrypt failed exception
	 */
	public byte[] encrypt(byte[] byteArray) throws EncryptFailedException
	{
		Cipher encryptCipher = null;
		try
		{   
			if(Algorithm.DES.equals(this.algorithm))				//DES/CBC/NoPadding
				encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");	//第一个参数加密算法，第二个参数加密算法使用模块，第三个参数填充符
			else if(Algorithm.TRIPLE_DES.equals(this.algorithm))				
				encryptCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");		//DESede/ECB/NoPadding
			else
				encryptCipher = Cipher.getInstance(this.algorithm);
				
			encryptCipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());	//使用私钥初始化
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptFailedException(e);
		}
		try
		{
			byte[] ret = encryptCipher.doFinal(byteArray);
			return Base64.encodeBase64(ret);		//由于使用了PKCS5Padding，不满8字节需要补0，这里通过BASE64直接编码
			//return ret;
		}
		catch (IllegalStateException e)
		{
			throw new EncryptFailedException();
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptFailedException();
		}
		catch (BadPaddingException e)
		{
			throw new EncryptFailedException();
		}
	}

	/**
	 * 加密字符串.
	 * 
	 * @param strIn 需加密的字符串
	 * 
	 * @return 加密后的字符串
	 * 
	 * @throws EncryptFailedException the encrypt failed exception
	 */
	public String encrypt(String strIn) throws EncryptFailedException
	{
		return ByteUtils.toHexString(encrypt(strIn.getBytes()));
	}

	/**
	 * 解密字节数组.
	 * 
	 * @param binArray 需解密的字节数组
	 * 
	 * @return 解密后的字节数组
	 * 
	 * @throws DecryptFailedException the decrypt failed exception
	 */
	public byte[] decrypt(byte[] binArray) throws DecryptFailedException
	{
		Cipher decryptCipher = null;
		try
		{
			if(Algorithm.DES.equals(this.algorithm))				
				decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");	//第一个参数加密算法，第二个参数加密算法使用模块，第三个参数填充符
			else if(Algorithm.TRIPLE_DES.equals(this.algorithm))				
				decryptCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			else
				decryptCipher = Cipher.getInstance(this.algorithm);
			decryptCipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());	//使用私钥解密
		}
		catch (InvalidKeyException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new DecryptFailedException(e);
		}

		try
		{
			binArray = Base64.decodeBase64(binArray);	//需要解码
			return decryptCipher.doFinal(binArray);
		}
		catch (IllegalStateException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (IllegalBlockSizeException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (BadPaddingException e)
		{
			throw new DecryptFailedException(e);
		}
	}

	/**
	 * 解密字符串.
	 * 
	 * @param strIn 需解密的字符串
	 * 
	 * @return 解密后的字符串
	 * 
	 * @throws DecryptFailedException the decrypt failed exception
	 */
	public String decrypt(String strIn) throws DecryptFailedException
	{
		return new String(decrypt(ByteUtils.hexStringToByteArray(strIn)));
	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) throws Exception
	{		
			String test = "this is a test";
			// DESPlus des = new DESPlus();//默认密钥
			//DESTool des = new DESTool(Algorithm.ALGORITHM_TRIPLE_DES);
			DESTool des = new DESTool(Algorithm.BLOWFISH);
			//DESTool des = new DESTool(Algorithm.ALGORITHM_AES);
			System.out.println("加密前的字符：" + test);
			String encrypt = des.encrypt(test);
			System.out.println("加密后的字符：" + encrypt);
			DESTool des2 = new DESTool(Algorithm.BLOWFISH);
			System.out.println("解密后的字符：" + des2.decrypt(encrypt));
		
	}
}
