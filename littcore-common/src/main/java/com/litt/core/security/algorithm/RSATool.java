package com.litt.core.security.algorithm;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.litt.core.security.Algorithm;
import com.litt.core.security.DecryptFailedException;
import com.litt.core.security.EncryptFailedException;
import com.litt.core.security.ISecurity;
import com.litt.core.util.ByteUtils;

/** 
 * 
 * RSA算法实现工具.
 * 
 * <pre><b>描述：</b>
 * 使用RSA算法对字符串进行加密解密。
 * 支持算法：RSA
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
public class RSATool implements ISecurity
{
	/** 算法. */
	private String algorithm = Algorithm.RSA;
	
	
	/** The keyPair. */
	private KeyPair keyPair = null;

	/**
	 * 默认构造方法，使用默认密钥，DES算法.
	 * 
	 */
	public RSATool() throws NoSuchAlgorithmException
	{
		this(Algorithm.DEFAULT_KEY,Algorithm.RSA);
	}
	
	/**
	 * 指定算法构造方法.
	 * 
	 * @param algorithm 算法
	 */
	public RSATool(String algorithm) throws NoSuchAlgorithmException	
	{
		this(Algorithm.DEFAULT_KEY,algorithm);
	}

	/**
	 * 指定密钥构造方法.
	 * 
	 * @param securityKey 密钥
	 * @param algorithm 算法
	 */
	public RSATool(String securityKey,String algorithm) throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;
		
		Security.addProvider(new com.sun.crypto.provider.SunJCE());		//动态安装SUN的JCE，对整个JVM都有效

		// 生成密钥
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);	
		generator.initialize(1024);
		keyPair = generator.generateKeyPair();
		
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
			encryptCipher = Cipher.getInstance(this.algorithm);				
			encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());	//使用公钥加密
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
			return encryptCipher.doFinal(byteArray);
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
	 * @param arrB 需解密的字节数组
	 * 
	 * @return 解密后的字节数组
	 * 
	 * @throws DecryptFailedException the decrypt failed exception
	 */
	public byte[] decrypt(byte[] arrB) throws DecryptFailedException
	{
		Cipher decryptCipher = null;
		try
		{
			decryptCipher = Cipher.getInstance(this.algorithm);
			decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());	//使用私钥解密
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
			return decryptCipher.doFinal(arrB);
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
			KeyFactory mykeyFactory=KeyFactory.getInstance("RSA");
			RSATool rsa = new RSATool(Algorithm.RSA);	//默认密钥			
			System.out.println("加密前的字符：" + test);
			System.out.println("加密后的字符：" + rsa.encrypt(test));
			System.out.println("解密后的字符：" + rsa.decrypt(rsa.encrypt(test)));
		
	}
}
