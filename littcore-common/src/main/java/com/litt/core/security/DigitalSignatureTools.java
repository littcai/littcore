package com.litt.core.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import com.litt.core.util.ByteUtils;

/** 
 * 
 * 数字签名认证辅助类.
 * 
 * <pre><b>描述：</b>
 *    进行数字签名的认证 
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
public class DigitalSignatureTools
{
	/** 签名算法. */
	private String algorithm = Algorithm.DSA;
	
	/** 公钥. */
	private PublicKey pubKey;

	
	public DigitalSignatureTools(File pubKeyFile) throws FileNotFoundException, DecryptFailedException, NoSuchAlgorithmException 
	{
		this(pubKeyFile, Algorithm.DSA);
	}
	
	public DigitalSignatureTools(PublicKey pubKey) throws FileNotFoundException, DecryptFailedException, NoSuchAlgorithmException 
	{
		this(pubKey, Algorithm.DSA);
	}
	
	public DigitalSignatureTools(File pubKeyFile, String algorithm) throws FileNotFoundException, DecryptFailedException 
	{
		this.pubKey = DigitalSignatureTools.readPubKey(pubKeyFile);
		this.algorithm = algorithm;		
	}	
	
	public DigitalSignatureTools(PublicKey pubKey, String algorithm) throws NoSuchAlgorithmException  // $codepro.audit.disable unnecessaryExceptions
	{
		this.pubKey = pubKey;
		this.algorithm = algorithm;		
	}	
	
	/**
	 * 获取公钥，如果当前公钥为空，则从磁盘文件读取，并将其设置为当前公钥，否则直接返回当前公钥.
	 * 
	 * @return PublicKey 公钥
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	public static final PublicKey readPubKey(File pubKeyFile) throws FileNotFoundException,DecryptFailedException
	{
		PublicKey pubKey = (PublicKey)readKey(new FileInputStream(pubKeyFile));
		return pubKey;
	}
	
	/**
	 * 获取公钥，如果当前公钥为空，则从磁盘文件读取，并将其设置为当前公钥，否则直接返回当前公钥.
	 * 
	 * @return PublicKey 公钥
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	public static final PublicKey readPubKey(InputStream is) throws DecryptFailedException
	{	
		PublicKey pubKey = (PublicKey) readKey(is);
		return pubKey;
	}
	
	/**
	 * 从磁盘读取密钥信息.
	 * 
	 * @param fileName
	 *            密钥存放路径
	 * 
	 * @return 返回密钥信息，为 Object 类，然后可根据具体的 PublicKey 或 PrivateKey 进行强制类型转换
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	private static final Object readKey(InputStream is) throws DecryptFailedException
	{
		Object obj = null;
		try {
			
			ObjectInputStream ois = new ObjectInputStream(is);
			obj = ois.readObject();
			ois.close();
		} catch (IOException e) {
			throw new DecryptFailedException("读取密钥对信息出错！",e);		
		} catch (ClassNotFoundException e) {
			throw new DecryptFailedException("读取密钥对信息出错！",e);		
		}
		return obj;
	}
	
	

	/**
	 * 利用当前公钥对信息进行解密.
	 * 
	 * @param source            
	 *            需要解密的字符串
	 * 
	 * @return 签名信息（byte类型）
	 * 
	 * @throws EncryptFailedException
	 *             the encrypt failed exception
	 */
	public boolean validateSign(String source,String signed) throws DecryptFailedException
	{		
		if (pubKey == null) 
		{
			throw new DecryptFailedException("提取密钥信息错误，无法完成签名！");			
		}
		try 
		{
			Signature checkSignet = Signature.getInstance(algorithm);
			checkSignet.initVerify(pubKey);
			checkSignet.update(source.getBytes());			
			if (checkSignet.verify(ByteUtils.hexStringToByteArray(signed))) 
			{				
				return true;
			} 
			else 
			{
				throw new DecryptFailedException("签名验证失败！");				
			}
		} catch (NoSuchAlgorithmException e) 
		{
			throw new DecryptFailedException(e);
		} catch (InvalidKeyException e) 
		{
			throw new DecryptFailedException("签名验证失败，无效的密钥！",e);
			
		} catch (SignatureException e) {
			throw new DecryptFailedException("签名验证失败！",e);
		}	
	}	
	
	public static void main(String[] args) throws Exception
	{
		String source = "this is a test";	
		DigitalSignatureTools utils = new DigitalSignatureTools(new File("D:\\pubKey.key"));		
		String signedData = "302C0214565F42D13F19FF01751064760DFB6DF67E6D53B302142D3F804EC32FCA11EAA473CC52F19F0AFF51FD77";
		System.out.println(utils.validateSign(source, signedData));
	}

	/**
	 * @return the pubKey
	 */
	public PublicKey getPubKey()
	{
		return pubKey;
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm()
	{
		return algorithm;
	}
}
