package com.litt.core.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

import com.litt.core.common.Utility;



/**
 * 通过HTTP采集远程文件(采用java.net包).
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 * 
 * 增加代理服务器支持，增加多协议支持
 * @since 2008-08-20 
 * @version 1.1
 * 
 */
public class SimpleHttpTool {

	private static final Log logger = LogFactory.getLog(SimpleHttpTool.class);
	
	/** 协议类型.
	 *  目前支持：http, https, ftp, file, and jar
	 */
	private String protocolType = "http";	
	
	/** 需要采集的远程文件URL地址. */
	private String remoteUrl;   
	
	/** 本地保存路径. */
	private String localFilePath;    
	
	/** 本地保存文件名. */
	private String localFileName;
	
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
	
	
	private HttpURLConnection init() throws MalformedURLException,IOException
	{
		java.net.URL urlFile = null;
		HttpURLConnection urlConnection = null;

		// 连接指定的网络资源,获取网络输入流
		if(logger.isDebugEnabled())
		{
			logger.debug("抓取远程URL："+remoteUrl);
		}
		
		urlFile = new URL(remoteUrl);					
		protocolType = urlFile.getProtocol();	//根据URL获得协议类型
		if(logger.isDebugEnabled())
		{
			logger.debug("网络协议类型："+protocolType);
		}
		if(needProxy)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("启动代理服务器："+proxyHost+":"+proxyPort);
			}
			SocketAddress addr = new InetSocketAddress(proxyHost,proxyPort);
	        Proxy proxy = null;
	        //注意： 如果 proxySet 为 false 时，依然设置了 proxyHost 和 proxyPort，代理设置仍会起作用。 
		    // 如果 proxyPort 设置有问题，代理设置不会起作用。 
	        if("socket".equalsIgnoreCase(proxyProtocolType))
	        {
	        	if(logger.isDebugEnabled())
				{
					logger.debug("代理服务器协议：SOCKET");
				}
	        	proxy = new Proxy(Proxy.Type.SOCKS,addr);
	        	System.getProperties().put("socksProxySet", "true");
	        	System.getProperties().put("socksProxyHost", proxyHost);
	        	System.getProperties().put("socksProxyPort", proxyPort);

	        }
	        else
	        {
	        	if(logger.isDebugEnabled())
				{
					logger.debug("代理服务器协议：HTTP");
				}
	        	proxy = new Proxy(Proxy.Type.HTTP,addr);
	        	System.getProperties().put( "proxySet", "true" ); 
	        	System.getProperties().put( "proxyHost", proxyHost ); 
			    System.getProperties().put( "proxyPort", proxyPort); 
	        }	    
		    
		    urlConnection = (HttpURLConnection)urlFile.openConnection(proxy);
		    if(needProxyAuth)
		    {
		    	if(logger.isDebugEnabled())
				{
					logger.debug("代理服务器需要用户认证，用户名："+proxyAuthUser+"，密码："+proxyAuthPassword);
				}
		    	String password = proxyAuthUser+":"+proxyAuthPassword;
			    String encodedPassword = "Basic " + new BASE64Encoder().encode(password.getBytes());
			    urlConnection.setRequestProperty( "Proxy-Authorization", encodedPassword);
		    }
		}
		else
			urlConnection = (HttpURLConnection)urlFile.openConnection();
		
		if(logger.isDebugEnabled())
		{
			logger.debug("尝试连接...");
		}
		urlConnection.setConnectTimeout(3000);	//30秒连接超时
		urlConnection.connect();
		if(logger.isDebugEnabled())
		{
			logger.debug("初始化连接成功...");
		}
		return urlConnection;
	}

	/**
	 * 采集远程URL文件.
	 */
	public void fetchFile() throws MalformedURLException,IOException
	{				
		HttpURLConnection urlConnection = init();
		if(Utility.isEmpty(localFileName))		//如果未定义文件名则以远程文件的文件名命名
			localFileName = Utility.getUrlFileName(remoteUrl);

		File localFile = new File(localFilePath,localFileName);
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("文件采集开始...");		
			}
			bis = new BufferedInputStream(urlConnection.getInputStream());				
			bos = new BufferedOutputStream(new FileOutputStream(localFile));		
			byte[] buf = new byte[1024];
			int bufsize = 0;
			while ((bufsize = bis.read(buf, 0, buf.length)) != -1) {
				bos.write(buf, 0, bufsize);
			}
	
			if(logger.isDebugEnabled())
			{
				logger.debug(remoteUrl + " 采集成功！文件已存储至：" + localFilePath+localFileName);		
			}
		}
		catch(IOException e)
		{
			logger.error("写文件失败",e);
		}
		finally
		{
			try {
				bos.flush();
				bis.close();
				urlConnection.disconnect();
			} catch (Exception e) {
				logger.error("关闭HTTP连接失败",e);
			}
		}
	}	


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{		
		
		SimpleHttpTool fo = new SimpleHttpTool();					 
		fo.setRemoteUrl("http://apache.mirror.phpchina.com/httpcomponents/commons-httpclient/binary/commons-httpclient-3.1.zip");
		fo.setLocalFilePath("D:\\");
		//fo.setLocalFileName("tk-filters-1.0.1.zip");
		
		fo.setNeedProxy(true);
		fo.setProxyHost("192.168.92.12");
		fo.setProxyPort(808);
		fo.fetchFile();			


	}



	/**
	 * @return the fileName
	 */
	public String getLocalFileName() {
		return localFileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setLocalFileName(String fileName) {
		this.localFileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getLocalFilePath() {
		return localFilePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setLocalFilePath(String filePath) {
		this.localFilePath = filePath;
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
	 * @return the remoteUrl
	 */
	public String getRemoteUrl() {
		return remoteUrl;
	}

	/**
	 * @param remoteUrl the remoteUrl to set
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
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

}
