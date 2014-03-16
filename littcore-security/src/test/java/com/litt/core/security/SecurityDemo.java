package com.litt.core.security;

/**
 * 演示程序.
 * 
 * <pre><b>描述：</b>
 *    演示采用RSA非对称加密算法加密内容
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2014-1-14
 * @version 1.0
 */
public class SecurityDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		//读取本地私钥文件，创建编码实例对象
		ISecurityEncoder encoder = SecurityFactory.genRSAEncoder("C:\\priKey.key");			
		/*
		 * 需要做数据签名的内容
		 */
		String source = "mobile=13812345678&content=这里是短信内容&code=shgc&username=admin&password=abcdef";	
		//对数据源进行一次加密，获得数据签名
		String sign = encoder.sign(source);
		System.out.println(sign);		
		System.out.println(sign.length());
		//将签名内容作为附加参数加到请求参数中
		String url = "http://xxx.xxx.xxx.xxx/rest/sms/sendSms.json?mobile=13812345678&content=这里是短信内容&code=shgc&sign="+sign;
		
	}

}
