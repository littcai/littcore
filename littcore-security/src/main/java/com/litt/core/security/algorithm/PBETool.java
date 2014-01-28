package com.litt.core.security.algorithm;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.litt.core.security.DecryptFailedException;
import com.litt.core.security.EncryptFailedException;
import com.litt.core.security.ISecurity;
import com.litt.core.security.SecurityFactory;
import com.litt.core.util.ByteUtils;

/** 
 * 
 * 带PBE密码加密的DES辅助工具.
 * 
 * <pre><b>描述：</b>
 *    通过PBE对DES进行口令加密，以方便解密 
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
public class PBETool implements ISecurity
{
	private String password = Algorithm.DEFAULT_KEY;
	
	public PBETool()
	{
		
	}
	
	public PBETool(String password)
	{
		this.password = password;
	}
	
	
	/**
	 * 加密.
	 * @param strIn 需要加密的字符串
	 * @return String
	 */
	public String encrypt(String strIn) throws EncryptFailedException
	{
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		try
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES"); //PBEWithSHAAndDEA-CBC
			SecretKey key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
				
			byte[] salt = new byte[8];
			Random random = new Random();
			random.nextBytes(salt);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);

			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			
			byte[] encryptedData = cipher.doFinal(strIn.getBytes());
			byte[] cdata = new byte[salt.length + encryptedData.length];
			System.arraycopy(salt, 0, cdata, 0, salt.length);
			System.arraycopy(encryptedData, 0, cdata, salt.length, encryptedData.length);
			return ByteUtils.toHexString(cdata);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (InvalidKeySpecException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (InvalidAlgorithmParameterException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (IllegalStateException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (BadPaddingException e)
		{
			throw new EncryptFailedException(e);
		}
	}

	/**
	 * 解密.
	 * 
	 * @param 需要解密的字符串
	 * 
	 * @return 明文
	 * 
	 * @throws DecryptFailedException the decrypt failed exception
	 */
	public String decrypt(String str) throws DecryptFailedException
	{
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		try
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

			//从加密串中取随机盐 
			byte[] data = ByteUtils.hexStringToByteArray(str);
			byte[] salt = new byte[8];
			byte[] cdata = new byte[data.length - salt.length];
			System.arraycopy(data, 0, salt, 0, salt.length);
			System.arraycopy(data, salt.length, cdata, 0, cdata.length);

			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			byte[] decryptedData = cipher.doFinal(cdata);
			return new String(decryptedData);
		}
		catch (InvalidKeyException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (InvalidKeySpecException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new DecryptFailedException(e);
		}
		catch (InvalidAlgorithmParameterException e)
		{
			throw new DecryptFailedException(e);
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
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) throws Exception
	{
		//从安全工厂获得解密处理实例类(基于接口)
		ISecurity security = SecurityFactory.genPBE("password");	
		String encrypt = security.encrypt("this is a test");
		System.out.println("加密后的字符串："+encrypt);
		
		String decrypt = security.decrypt(encrypt);
		System.out.println("解密后的字符串："+decrypt);
	}
	
}
