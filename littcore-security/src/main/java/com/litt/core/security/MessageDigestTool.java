package com.litt.core.security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.litt.core.security.algorithm.Algorithm;

/**
 * 
 * 消息摘要辅助类.
 * 
 * <pre><b>描述：</b>
 *    MD5加密、SHA加密
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2009-04-13
 * @version 1.0
 */
public class MessageDigestTool 
{
	
	/**
	 * MD5输入可以是任何长度的消息，输出是固定的128位的一个字符串
	 * 处理步骤：
	 *	步骤1：增加填充位。填充消息使得其长度与448模512同余。也就是说填充后的消息长度比512的某个整数倍少64位。
	 *	步骤2：填充长度。用64位表示填充前消息的长度，并将其附在步骤1所得结果之后。填充前消息长度如果大于2的64次方，则只使用其低64位，即它所包含的是填充前消息的长度对2的64次方取模的结果。
	 *	步骤3：初始化MD缓冲区。hash函数的中间结果和最终结果保存于128位的缓冲区中，缓冲区用4个32位寄存器（A，B，C，D）表示，并将这些寄存器初始化为下列32位的整数（16进制）：
	 *	A＝67452301
	 *	B＝EFCDAB89
	 *	C＝98BADCFE
	 *	D＝10325476
	 *	以上初始化值以低端格式存储，也就是说，字的最低有效位存储在低地址字节位置。
	 *	步骤4：以 512 位的分组（16个字）位单位处理消息。具体是4轮运算结构相同，但各轮使用不同的基本逻辑函数。
	 *	（具体步骤略）
	 *	步骤5：输出。所有的 L 个 512 位的分组处理完后，第 L 个分组的输出即是128位的消息
	 *
	 * 不可逆MD5加密
	 * @param source 原数据
	 * @return String 加密后数据
	 * @throws EncryptFailedException 加密失败异常
	 */
	public static String encryptMD5(String source) throws EncryptFailedException
	{		
		return encrypt(source,Algorithm.MD5);
	}
	
	/**
	 * 不可逆SHA加密
	 * @param source 原数据
	 * @return String 加密后数据
	 * @throws EncryptFailedException 加密失败异常
	 */
	public static String encryptSHA(String source) throws EncryptFailedException
	{		
		return encrypt(source,Algorithm.SHA1);
	}
	
	/**
	 * HMAC加密.
	 * 
	 * @param source 原数据
	 * @param algorithm HMAC算法
	 * 
	 * @return 加密后数据
	 * 
	 * @throws EncryptFailedException 加密失败异常
	 */  
    public static byte[] encryptHMAC(String source) throws EncryptFailedException
    { 
    	return encryptHMAC(source, Algorithm.HmacMD5);
    }
	
	/**
	 * HMAC加密.
	 * 
	 * @param source 原数据
	 * @param algorithm HMAC算法
	 * 
	 * @return 加密后数据
	 * 
	 * @throws EncryptFailedException 加密失败异常
	 */  
    public static byte[] encryptHMAC(String source, String algorithm) throws EncryptFailedException
    {   
        try
		{
			SecretKey secretKey = new SecretKeySpec(source.getBytes(), algorithm);   
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());   
			mac.init(secretKey);     
			return mac.doFinal(source.getBytes());
		}
        catch (NoSuchAlgorithmException e) 
        {
			throw new EncryptFailedException(e);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptFailedException(e);
		}
		catch (IllegalStateException e)
		{
			throw new EncryptFailedException(e);
		}   
    }   
	
	/**
	 * MD5和SHA的公用加密算法.
	 * 
	 * @param source
	 *            原数据
	 * @param encryptType
	 *            加密算法
	 * 
	 * @return String 加密后数据
	 * 
	 * @throws EncryptFailedException
	 *             加密失败异常
	 */
	public static String encrypt(String source,String algorithm) throws EncryptFailedException
	{
		 try {
			MessageDigest md = MessageDigest.getInstance(algorithm);		
			byte[] digest =  md.digest(source.getBytes());
			StringBuffer result = new StringBuffer();			
	        for(int i=0;i<digest.length;i++) 
	        {
	               char[] digit = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };
	               char[] ob = new char[2];
	               ob[0] = digit[(digest[i] >>> 4) & 0X0F];
	               ob[1] = digit[digest[i] & 0X0F];
	               result.append(new String(ob));
	        }
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptFailedException(e);
		}
	}
	

	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println(MessageDigestTool.encryptMD5("a=1&b=3&c=2&app_id=8934e7d15453e97507ef794cf7b0519d").toUpperCase());
		System.out.println(MessageDigestTool.encryptSHA("000000"));
		System.out.println(MessageDigestTool.encryptSHA("000000"));
	}

}
