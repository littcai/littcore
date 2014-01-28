package com.litt.core.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.commons.codec.binary.Base64;

import com.litt.core.security.algorithm.Algorithm;
import com.litt.core.util.ByteUtils;

/** 
 * 
 * 数字签名认证辅助类.
 * 
 * <pre><b>描述：</b>
 *    非对称加密,数字签名是利用私钥加密，公钥解密，用以保证不可否认性和完整性
 *    支持算法：
 *    1、DSA 
 *    2、SHA1withDSA
 *    3、MD2withRSA
 *    4、MD5withRSA 
 *    5、SHA1withRSA
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
public class DigitalSignatureTool
{
	/** 签名算法. */
	private String algorithm = Algorithm.DSA;
	
	/** 公钥. */
	private PublicKey pubKey;
	
	/** 私钥. */
	private PrivateKey priKey;
	
	public DigitalSignatureTool(){}

	public DigitalSignatureTool(String algorithm)
	{
		this.algorithm = algorithm;
	}	
	
	public DigitalSignatureTool(File pubKeyFile) throws FileNotFoundException, DecryptFailedException, NoSuchAlgorithmException 
	{
		this(pubKeyFile, Algorithm.DSA);
	}
	
	public DigitalSignatureTool(PublicKey pubKey) throws FileNotFoundException, DecryptFailedException, NoSuchAlgorithmException 
	{
		this(pubKey, Algorithm.DSA);
	}
	
	public DigitalSignatureTool(File pubKeyFile, String algorithm) throws FileNotFoundException, DecryptFailedException 
	{
		this.algorithm = algorithm;		
		this.pubKey = readPubKey(pubKeyFile);
		
	}	
	
	public DigitalSignatureTool(PublicKey pubKey, String algorithm) throws NoSuchAlgorithmException  // $codepro.audit.disable unnecessaryExceptions
	{
		this.pubKey = pubKey;
		this.algorithm = algorithm;		
	}	
	
	/**
	 * 获取私钥，如果当前私钥为空，则从磁盘文件读取，并将其设置为当前私钥，否则直接返回当前私钥.
	 * 
	 * @return PrivateKey私钥
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	public PrivateKey readPriKey(String priKeyFilePath) throws FileNotFoundException, DecryptFailedException
	{
		return this.readPriKey(new File(priKeyFilePath));
	}
	
	/**
	 * 获取私钥，如果当前私钥为空，则从磁盘文件读取，并将其设置为当前私钥，否则直接返回当前私钥.
	 * 
	 * @return PrivateKey私钥
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	public PrivateKey readPriKey(File pirKeyFile) throws FileNotFoundException, DecryptFailedException
	{		
		if (pirKeyFile!=null && pirKeyFile.exists()) 
		{
			priKey = (PrivateKey) this.readKey(pirKeyFile);
			return this.priKey;
		} else {
			throw new IllegalArgumentException("私钥路径不正确！");				
		}
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
		PublicKey pubKey = (PublicKey)readKey(pubKeyFile);
		return pubKey;
	}	
	
	/**
	 * 从磁盘读取密钥信息.
	 * 
	 * @param file
	 *            密钥文件
	 * 
	 * @return 返回密钥信息，为 Object 类，然后可根据具体的 PublicKey 或 PrivateKey 进行强制类型转换
	 * 
	 * @throws DecryptFailedException
	 *             the decrypt failed exception
	 */
	public static Object readKey(File file) throws FileNotFoundException, DecryptFailedException
	{
		return readKey(new FileInputStream(file));		
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
	 * 数字签名.
	 *
	 * @param source 数据源
	 * @return 签名后数据
	 * @throws SignatureException the signature exception
	 */
	public String sign(String source) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException 
	{
		//用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(this.algorithm);
		signature.initSign(this.priKey);
		signature.update(source.getBytes());

		return Base64.encodeBase64String(signature.sign());
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
			if (checkSignet.verify(Base64.decodeBase64(signed))) 
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
		DigitalSignatureTool encoder = new DigitalSignatureTool(Algorithm.MD5withRSA);
		encoder.readPriKey("c:\\priKey.key");
		String signedData = encoder.sign(source);
		System.out.println(signedData);
		DigitalSignatureTool utils = new DigitalSignatureTool(new File("C:\\pubKey.key"), Algorithm.MD5withRSA);		
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
