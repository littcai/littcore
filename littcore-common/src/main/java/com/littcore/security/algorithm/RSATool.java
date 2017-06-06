package com.littcore.security.algorithm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;

import com.littcore.security.DecryptFailedException;
import com.littcore.security.EncryptFailedException;
import com.littcore.security.ISecurity;
import com.littcore.security.ISecurityDecoder;
import com.littcore.security.ISecurityEncoder;
import com.littcore.security.SecurityFactory;
import com.littcore.util.ByteUtils;

/** 
 * 
 * RSA算法实现工具.
 * 
 * <pre><b>描述：</b>
 * 使用RSA算法对字符串进行加密解密。
 * 支持算法：RSA
 * 
 * 采用私钥加密，公钥解密的方式，公钥由客户端保存，服务端用私钥解密
 * 注：RSA 1024位加密明文最大长度117字节，解密要求密文最大长度为128字节
 * 
 * 使用RSA算法，并且加PAD的方式按照PKCS1的标准。即输入数据长度小于等于密钥的位数/8-11，例如：1024位密钥，1024/8-11=117。   
 * 不足的部分，程序会自动补齐。加密后的数据还是等于密钥的位数/8
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
public class RSATool implements ISecurity, ISecurityEncoder, ISecurityDecoder
{
	/** 算法. */
	private String algorithm = Algorithm.RSA;	
		
	/** 公钥. */
	private PublicKey pubKey;

	/** 私钥. */
	private PrivateKey priKey;

	/**
	 * 默认构造方法，使用默认密钥，DES算法.
	 * 
	 */
	public RSATool() throws NoSuchAlgorithmException
	{
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
	public RSATool(String securityKey, String algorithm) throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;
		
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());		//动态安装SUN的JCE，对整个JVM都有效

		// 生成密钥
		KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
		SecureRandom random = new SecureRandom();  
		random.setSeed(StringUtils.getBytesUtf8(securityKey));
		generator.initialize(1024, random);
		KeyPair keyPair = generator.generateKeyPair();		
		this.priKey = keyPair.getPrivate();
		this.pubKey = keyPair.getPublic();
	}
	
	
	/**
	 * 读取私钥文件.
	 *
	 * @param privateKeyFilePath the private key file path
	 */
	public PrivateKey readPriKey(String priKeyFilePath) throws SecurityException
	{
		byte[] byteArray = readByteArray(priKeyFilePath);
		
		try {
			// 构造PKCS8EncodedKeySpec对象     
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(byteArray);  
			// KEY_ALGORITHM 指定的加密算法     
			KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);   
			// 取私钥匙对象     
			priKey = keyFactory.generatePrivate(pkcs8KeySpec); 
			
			//priKey = (PrivateKey) this.readKey(priKeyFilePath);
			return this.priKey;
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("读取密钥对信息出错！",e);
		} catch (InvalidKeySpecException e) {
			throw new SecurityException("读取密钥对信息出错！",e);
		}
	}

	private byte[] readByteArray(String filePath)
	{
		try {
			FileInputStream input = new FileInputStream(new File(filePath));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			IOUtils.copyLarge(input, output);
			return output.toByteArray();
		} catch (FileNotFoundException e) {
			throw new SecurityException("读取密钥对信息出错！",e);		
		} catch (IOException e) {
			throw new SecurityException("读取密钥对信息出错！",e);		
		}
	}
	
	/**
	 * 获取公钥，如果当前公钥为空，则从磁盘文件读取，并将其设置为当前公钥，否则直接返回当前公钥.
	 *
	 * @param pubKeyFilePath the pub key file path
	 * @return PublicKey 公钥
	 * @throws SecurityException the security exception
	 */
	public PublicKey readPubKey(String pubKeyFilePath) throws SecurityException
	{
		byte[] byteArray = readByteArray(pubKeyFilePath);
		try {
			// 取得公钥     
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(byteArray);     
			KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);     
			pubKey = keyFactory.generatePublic(x509KeySpec);     
			
			//pubKey = (PublicKey) this.readKey(pubKeyFilePath);
			return this.pubKey;
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("读取密钥对信息出错！",e);	
		} catch (InvalidKeySpecException e) {
			throw new SecurityException("读取密钥对信息出错！",e);	
		}
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
	public Object readKey(String fileName) throws SecurityException
	{
		return this.readKey(new File(fileName));
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
	public Object readKey(File file) throws SecurityException
	{
		Object obj = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			obj = ois.readObject();
			ois.close();
		} catch (IOException e) {
			throw new SecurityException("读取密钥对信息出错！",e);		
		} catch (ClassNotFoundException e) {
			throw new SecurityException("读取密钥对信息出错！",e);		
		}
		return obj;
	}	
	
	public void store()
	{
		System.out.println(Base64.encodeBase64String(priKey.getEncoded()));
		System.out.println(Base64.encodeBase64String(pubKey.getEncoded()));
	}
	
	/**
	 * 将公钥和私钥都存储到文件
	 * @param privateKeyFilePath
	 * @param publicKeyFilePath
	 * @return
	 */
	public void store(String priKeyFilePath, String pubKeyFilePath) throws IOException
	{
		writeFile(priKeyFilePath, priKey);
		writeFile(pubKeyFilePath, pubKey);
	}
	
	/**
	 * @param priKeyFilePath
	 * @param encoded
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void writeFile(String filePath, Key key) throws IOException
	{
		File file = new File(filePath);
		if(!file.exists())
			file.createNewFile();
		
		byte[] array = key.getEncoded();
		
		FileOutputStream fos = new FileOutputStream(file);		
		try {
			fos.write(array);
			fos.flush();		
		} 
		finally
		{
			IOUtils.closeQuietly(fos);
		}
		
	}

//	/**
//	 * @param priKeyFilePath
//	 * @param encoded
//	 * @throws IOException
//	 * @throws FileNotFoundException
//	 */
//	private void writeFile(String filePath, Object object) throws IOException
//	{
//		File file = new File(filePath);
//		if(!file.exists())
//			file.createNewFile();
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
//		try {
//			oos.writeObject(object);
//			oos.flush();			
//		} 
//		finally
//		{
//			IOUtils.closeQuietly(oos);
//		}
//		
//	}

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
//		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(this.pubKey.getEncoded());
//		KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
//		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		
		Cipher encryptCipher = null;
		try
		{   
			encryptCipher = Cipher.getInstance(this.algorithm);				
			encryptCipher.init(Cipher.ENCRYPT_MODE, this.priKey);	//使用私钥加密
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
	 * 加密字符串.
	 * 
	 * @param strIn 需加密的字符串
	 * 
	 * @return 加密后的字符串
	 * 
	 * @throws EncryptFailedException the encrypt failed exception
	 */
	public String encrypt(String source) throws EncryptFailedException
	{
		return ByteUtils.toHexString(encrypt(StringUtils.getBytesUtf8(source)));
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
		Signature signature = Signature.getInstance(Algorithm.MD5withRSA);
		signature.initSign(this.priKey);
		signature.update(StringUtils.getBytesUtf8(source));
		return Base64.encodeBase64String(signature.sign());
	}
	
	/**
	 * 数字签名校验.
	 *
	 * @param source 数据源
	 * @param sign 签名后内容
	 * @return 是否通过校验
	 * @throws SignatureException the signature exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws InvalidKeyException the invalid key exception
	 */
	public boolean verify(String source, String sign) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException 
	{
		Signature signature = Signature.getInstance(Algorithm.MD5withRSA);
		signature.initVerify(this.pubKey);
		signature.update(StringUtils.getBytesUtf8(source));
		return signature.verify(Base64.decodeBase64(sign));
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
			decryptCipher.init(Cipher.DECRYPT_MODE, this.pubKey);	//使用公钥解密
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
		ISecurityEncoder encoder = SecurityFactory.genRSAEncoder("C:\\xwremote-pri.key");			
//		/*
//		 * 需要做数据签名的内容
//		 */
		//String source = "mobile=13818143407&content=短信认证测试&appCode=shgc&username=shgc&password=shgc2013";
		String source = "mobile=15901976098&content=测试发送短消息&appCode=xwremote&username=xwremote&password=xwremote4855";
		//对数据源进行一次加密，获得数据签名
		String sign = encoder.sign(source);
		System.out.println(sign);
		String encodedSign = URLEncoder.encode(sign, "utf-8");
		System.out.println("URL编码后的字符串："+ encodedSign);
		
		ISecurityDecoder decoder = SecurityFactory.genRSADecoder("C:\\xwremote-pub.key");
		boolean isValid = decoder.verify(source, sign);
		System.out.println(isValid);		

		
//			String test = "mobile=13818143407&content=短信认证测试&appCode=shgc&username=shgc&password=shgc2013";
//			
//			RSATool rsa = new RSATool();	//默认密钥	
//			
//			rsa.readPriKey("C:\\shgc-pri.key");
//			rsa.readPubKey("C:\\shgc-pub.key");
//			
//			System.out.println("加密前的字符：" + test);
//			System.out.println("加密后的字符：" + rsa.encrypt(test));
//			System.out.println("解密后的字符：" + rsa.decrypt(rsa.encrypt(test)));
//			
//			String sign = rsa.sign(test);
//			System.out.println(sign);
//			
//			boolean isValid = rsa.verify(test, sign);
//			System.out.println(isValid);
			
			//RSATool rsa = new RSATool("xwremote", Algorithm.RSA);	//默认密钥	
			//rsa.store("C:\\xwremote-pri.key", "C:\\xwremote-pub.key");
			
			
		
	}
}
