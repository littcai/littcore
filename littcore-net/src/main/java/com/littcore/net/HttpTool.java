package com.littcore.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.littcore.exception.UnimplementedException;



/**
 * 通过HTTP采集远程文件.
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 * 
 * 增加代理服务器支持，增加多协议支持
 * @since 2008-08-20 
 * @version 1.1
 * 
 * 增加响应数据编码设置，查询参数编码设置
 * @since 2009-11-14 
 * @version 1.2
 * 
 */
public class HttpTool {

	private static final Log logger = LogFactory.getLog(HttpTool.class);
	
	/** 协议类型.
	 *  目前支持：http, https, ftp, file, and jar
	 */
	private String protocolType = "http";		
	
	/** 是否需要启动代理服务器. */
	private boolean needProxy = false;
	
	/** 代理服务器协议类型.
	 *  默认：HTTP
	 *  支持：SOCKET
	 */
	private String proxyProtocolType = "http";	
	
	/** 代理服务器地址. */
	private String proxyHost;
	
	/** 代理服务器端口. */
	private int proxyPort;

	/** 代理服务器需要用户身份验证. */
	private boolean needProxyAuth = false;
	
	/** 认证用户名称. */
	private String proxyAuthUser;
	
	/** 认证密码. */
	private String proxyAuthPassword;	
	
	
	/** 需要用户身份验证. */
	private boolean needAuth = false;
	
	/** 
	 * 身份认证类型(默认：Basic). 
	 * 支持类型：Basic,Digest, NTLM 
	 */
	private String authType = "Basic";
	
	/** 认证用户名称. */
	private String authUser;
	
	/** 认证密码. */
	private String authPassword;	
	
	/**
	 * 是否对查询参数编码.
	 */
	private boolean isEncodeQueryParam = true;
	
	/**
	 * 响应数据的字符编码（默认UTF-8）.
	 */
	private Charset responseCharsetName = Charset.forName("UTF-8");
	
	HttpClient client = null;
	
	public void init() throws IOException
	{
		
//		URL urlFile = new URL(remoteUrl);					
//		protocolType = urlFile.getProtocol();	//根据URL获得协议类型
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("网络协议类型："+protocolType);
//		}
		client = new HttpClient();
		//HttpVersion ver = (HttpVersion)httpclient.getParams().getParameter("http.protocol.version");
		//httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");

		
		if(needProxy)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("启动代理服务器："+proxyHost+":"+proxyPort);
			}
			
	        if("socket".equalsIgnoreCase(proxyProtocolType))
	        {
	        	if(logger.isDebugEnabled())
				{
					logger.debug("代理服务器协议：SOCKET");
				}
	        	//TODO 暂时未实现
	        	throw new UnimplementedException();
	        }
	        else
	        {
	        	if(logger.isDebugEnabled())
				{
					logger.debug("代理服务器协议：HTTP");
				}
//	    		Properties prop = System.getProperties();
//	    		//设置http访问要使用的代理服务器的地址
//	    		prop.setProperty("http.proxyHost", "172.14.2.100");
//	    		//设置http访问要使用的代理服务器的端口
//	    		prop.setProperty("http.proxyPort", "808");
//	    		设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
//	    		prop.setProperty("http.nonProxyHosts", "localhost|192.9.*");
	        	client.getHostConfiguration().setProxy(proxyHost, proxyPort);
	        }
	        //代理服务器需要身份验证
	        if(needProxyAuth)
	        {
	        	Credentials credentials = new UsernamePasswordCredentials(proxyAuthUser,proxyAuthPassword);
	    		client.getState().setProxyCredentials(new AuthScope(proxyHost, proxyPort), credentials);
	        }
		}
		
		//服务器需要用户身份验证
	    if(needAuth)
	    {
	    	if(logger.isDebugEnabled())
			{
				logger.debug("用户身份认证，验证类型："+authType+"，用户名："+authUser+"，密码："+authPassword);
			}	
	    	if("Basic".equals(authType))
	    	{
	    		client.getParams().setAuthenticationPreemptive(true);	//设置强制发送验证信息，从而令客户端不必处理响应码401		    	
	    		Credentials credentials = new UsernamePasswordCredentials(authUser,authPassword);
	    		client.getState().setCredentials(new AuthScope("localhost", 443), credentials);
	    	}
	    	else if("NTLM".equals(authType))
	    	{
	    		//String localHostName = InetAddress.getLocalHost().getHostName();
	    		//Credentials credentials = new org.apache.commons.httpclient.NTCredentials(authUser,authPassword,localHostName);
	    		//TODO 暂时未实现
	        	throw new UnimplementedException();
	    	}
	    }
		
		if(logger.isDebugEnabled())
		{
			logger.debug("尝试连接...");
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("初始化连接成功...");
		}
		//return urlConnection;
	}		
	
	private GetMethod get(String url,Map paramMap) throws IOException
	{
		GetMethod getMethod = new GetMethod(url); 
		if(paramMap!=null)
		{	
			if(logger.isDebugEnabled())
			{
				logger.debug("参数不为空！");
			}
			int paramCount = paramMap.size();
			NameValuePair[] params = new NameValuePair[paramCount];
			Object[] keys = paramMap.keySet().toArray();
			for(int i=0;i<paramCount;i++)
			{
				String key = keys[i].toString();
				String paramValue = paramMap.get(key).toString();
				if(isEncodeQueryParam)
					paramValue = URIUtil.encodeQuery(paramValue);
				params[i] = new NameValuePair(URIUtil.encodeQuery(key), paramValue);//使用URIUtil对参数编码以符合URL规范
				if(logger.isDebugEnabled())
				{
					logger.debug("参数："+key+"，值："+paramMap.get(key));
				}
			}		
			getMethod.setQueryString(params);	
		}	
		client.executeMethod(getMethod);
		return getMethod;
	}
	
	/**
	 * 采集远程URL文件.
	 */
	public void fetchToFile(File localFile,String remoteUrl) throws IOException
	{
		this.fetchToFile(localFile,remoteUrl,null);
	}

	/**
	 * 采集远程URL文件.
	 */
	public void fetchToFile(File localFile,String remoteUrl,Map paramMap) throws IOException
	{		
		GetMethod getMethod = this.get(remoteUrl, paramMap);
		
		//连接指定的网络资源,获取网络输入流
		if(logger.isDebugEnabled())
		{
			logger.debug("抓取远程URL："+remoteUrl);
		}		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("文件采集开始...");		
			}
			bis = new BufferedInputStream(getMethod.getResponseBodyAsStream());				
			bos = new BufferedOutputStream(new FileOutputStream(localFile));		
			byte[] buf = new byte[1024];
			int bufsize = 0;
			while ((bufsize = bis.read(buf, 0, buf.length)) != -1) {
				bos.write(buf, 0, bufsize);
			}
	
			if(logger.isDebugEnabled())
			{
				logger.debug(remoteUrl + " 采集成功！文件已存储至：" + localFile.getName());		
			}
		}
		catch(IOException e)
		{
			logger.error("写文件失败",e);
		}
		finally
		{
			try {
				bos.close();
				bis.close();
				getMethod.releaseConnection();
			} catch (Exception e) {
				logger.error("关闭HTTP连接失败",e);
			}
		}
	}
	
	/**
	 * 采集远程URL内容.
	 */
	public String fetchToString(String remoteUrl) throws IOException
	{
		return this.fetchToString(remoteUrl, null);
	}
	
	/**
	 * 采集远程URL内容.
	 */
	public String fetchToString(String remoteUrl,Map paramMap) throws IOException
	{	
		GetMethod getMethod = this.get(remoteUrl, paramMap);
		
		//连接指定的网络资源,获取网络输入流
		if(logger.isDebugEnabled())
		{
			logger.debug("抓取远程URL："+remoteUrl);
		}	
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		InputStreamReader isr = null;		
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("文件采集开始...");		
			}
			isr = new InputStreamReader(getMethod.getResponseBodyAsStream(), responseCharsetName);			
				
			char[] buf = new char[1024];
			int bufsize = 0;
			while ((bufsize = isr.read(buf, 0, buf.length)) != -1) {
				pw.write(buf, 0, bufsize);	
			}	
		}
		catch(IOException e)
		{
			logger.error("写字符串失败",e);
		}
		finally
		{
			try {
				pw.close();
				sw.close();
				isr.close();
				getMethod.releaseConnection();
			} catch (Exception e) {
				logger.error("关闭HTTP连接失败",e);
			}
		}
		return sw.toString();
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		HttpTool tool = new HttpTool();
		
		tool.setEncodeQueryParam(false);
		//tool.setNeedProxy(true);
		//tool.setProxyHost("172.14.2.100");
		//tool.setProxyPort(808);
		tool.init();		
		Map paramMap = new HashMap();
		paramMap.put("theCityName", "上海");	
		System.out.println(tool.fetchToString("http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName",paramMap));
	}

	/**
	 * @return the authPassword
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * @param authPassword the authPassword to set
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	/**
	 * @return the authUser
	 */
	public String getAuthUser() {
		return authUser;
	}

	/**
	 * @param authUser the authUser to set
	 */
	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	/**
	 * @return the needAuth
	 */
	public boolean isNeedAuth() {
		return needAuth;
	}

	/**
	 * @param needAuth the needAuth to set
	 */
	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	/**
	 * @return the needProxy
	 */
	public boolean isNeedProxy() {
		return needProxy;
	}

	/**
	 * @param needProxy the needProxy to set
	 */
	public void setNeedProxy(boolean needProxy) {
		this.needProxy = needProxy;
	}

	/**
	 * @return the protocolType
	 */
	public String getProtocolType() {
		return protocolType;
	}

	/**
	 * @param protocolType the protocolType to set
	 */
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * @return the proxyProtocolType
	 */
	public String getProxyProtocolType() {
		return proxyProtocolType;
	}

	/**
	 * @param proxyProtocolType the proxyProtocolType to set
	 */
	public void setProxyProtocolType(String proxyProtocolType) {
		this.proxyProtocolType = proxyProtocolType;
	}


	/**
	 * @return the needProxyAuth
	 */
	public boolean isNeedProxyAuth() {
		return needProxyAuth;
	}

	/**
	 * @param needProxyAuth the needProxyAuth to set
	 */
	public void setNeedProxyAuth(boolean needProxyAuth) {
		this.needProxyAuth = needProxyAuth;
	}

	/**
	 * @return the proxyAuthPassword
	 */
	public String getProxyAuthPassword() {
		return proxyAuthPassword;
	}

	/**
	 * @param proxyAuthPassword the proxyAuthPassword to set
	 */
	public void setProxyAuthPassword(String proxyAuthPassword) {
		this.proxyAuthPassword = proxyAuthPassword;
	}

	/**
	 * @return the proxyAuthUser
	 */
	public String getProxyAuthUser() {
		return proxyAuthUser;
	}

	/**
	 * @param proxyAuthUser the proxyAuthUser to set
	 */
	public void setProxyAuthUser(String proxyAuthUser) {
		this.proxyAuthUser = proxyAuthUser;
	}

	/**
	 * @return the isEncodeQueryParam
	 */
	public boolean isEncodeQueryParam()
	{
		return isEncodeQueryParam;
	}

	/**
	 * @param isEncodeQueryParam the isEncodeQueryParam to set
	 */
	public void setEncodeQueryParam(boolean isEncodeQueryParam)
	{
		this.isEncodeQueryParam = isEncodeQueryParam;
	}

	/**
	 * @return the responseCharsetName
	 */
	public Charset getResponseCharsetName()
	{
		return responseCharsetName;
	}

	/**
	 * @param responseCharsetName the responseCharsetName to set
	 */
	public void setResponseCharsetName(Charset responseCharsetName)
	{
		this.responseCharsetName = responseCharsetName;
	}


}
