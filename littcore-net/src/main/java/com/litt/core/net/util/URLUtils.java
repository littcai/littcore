package com.litt.core.net.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.Utility;


/**
 * <b>标题:</b> URL功能类.
 * <pre><b>描述:</b> 
 *   1.对URL中文进行转码,2.对URL中文进行反转码，需要JDK5以上支持
 * </pre>
 * 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-09-07
 * @version 1.0
 */
public class URLUtils{

	private static final Log logger = LogFactory.getLog(URLUtils.class);	
	
    /**
     * 对URL中文进行转码 一般get方式查询参数用到
     * @param str 中文字符串
     * @param coder 编码方式，如 UTF-8\GBK
     * @return
     */
    public static String urlEncoder(String url, String coder){
    	url = Utility.trimNull(url);
        if(coder==null || coder.equals("")){
            coder = "UTF-8";
        }
        try {
        	url = java.net.URLEncoder.encode(url, coder);
        } catch (UnsupportedEncodingException e) {
        	logger.error("URL编码出错，编码类型 - "+coder,e);
        }
        return url;
    }
    
    
    /**
     * 对URL中文进行反转码，一般get方式查询参数用到
     * @param str 中文字符串
     * @param coder 编码方式，如 UTF-8\GBK
     * @return
     */
    public static String urlDecoder(String url, String coder){
        url = Utility.trimNull(url);
        if(coder==null || coder.equals("")){
            coder = "UTF-8";
        }
        try {
        	url = java.net.URLDecoder.decode(url, coder);
        } catch (UnsupportedEncodingException e) 
        {        	
            logger.error("URL解码出错，编码类型 - "+coder,e);
        }
        return url;
    }
    
    
    

    /**
	 * 将参数直接附加到URL上(类似于GET方法).
	 * 
	 * @param requestUrl
	 *            请求的URL地址
	 * @param parameters
	 *            参数MAP
	 * 
	 * @return 目标URL
	 */
    public static String getUrl(String requestUrl,Map parameters) 
    {
        StringBuffer originalURL = new StringBuffer(requestUrl);
        if (parameters != null && parameters.size() > 0) 
        {
        	if(originalURL.indexOf("?")>0)
        		originalURL.append("&");
        	else
        		originalURL.append("?");
            for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String values = (String) parameters.get(key);               
                originalURL.append(key).append("=").append(values);                
            }
        }
        return originalURL.toString();
    } 
    
    /**
     * 根据提供的域名地址返回网页html内容
     * @param remoteUrl
     * @return
     */
    public static String getWebContent(String remoteUrl){
    	StringBuffer sb = new StringBuffer();
        try{  
            java.net.URL url = new java.net.URL(remoteUrl);
            BufferedReader in =
                new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while((line = in.readLine()) != null){
                sb.append(line);
            }
            in.close();           
        }catch(Exception e) {           
        	logger.error("获取远程网址内容失败 - "+remoteUrl,e);            
        }  
        return sb.toString();         
    }
    /**
     * 根据提供的域名地址返回网页html内容。指定编码类型
     * @param remoteUrl 
     * @param encoding 编码类型
     * @return
     */
    public static String getWebContent(String remoteUrl, String encoding){
    	StringBuffer sb = new StringBuffer();
        try{  
            java.net.URL url = new java.net.URL(remoteUrl);
            BufferedReader in =
                new BufferedReader(new InputStreamReader(url.openStream(),encoding));
            String line;
            while((line = in.readLine()) != null){
                sb.append(line);
            }
            in.close();            
        }catch(Exception e) { // Report any errors that arise 
        	logger.error("获取远程网址内容失败 - "+remoteUrl,e); 
        }  
        return sb.toString();         
    }
    
    public static void post(String action ,String formData) throws Exception
    {    	
        // Send data
        URL url = new URL(action);
        SocketAddress addr = new InetSocketAddress("210.51.14.197",80);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,addr);        
        URLConnection conn = url.openConnection(proxy);
        
       // 注意： 如果 proxySet 为 false 时，依然设置了 proxyHost 和 proxyPort，代理设置仍会起作用。 
       // 如果 proxyPort 设置有问题，代理设置不会起作用。 
       System.getProperties().put( "proxySet", "true" ); 
       System.getProperties().put( "proxyHost", "210.51.14.197" ); 
       System.getProperties().put( "proxyPort", "80"); 
      
        conn.setDoOutput(true);     
        conn.setUseCaches(false);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(formData);
        wr.flush();
    
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        wr.close();
        rd.close();
        
        
        
//        URL                 url;
//        URLConnection   urlConn;
//        DataOutputStream    printout;
//        DataInputStream     input;
//        // URL of CGI-Bin script.
//        url = new URL (action);
//        // URL connection channel.
//        urlConn = url.openConnection();
//        // Let the run-time system (RTS) know that we want input.
//        urlConn.setDoInput (true);
//        // Let the RTS know that we want to do output.
//        urlConn.setDoOutput (true);
//        // No caching, we want the real thing.
//        urlConn.setUseCaches (false);
//        // Specify the content type.
//        urlConn.setRequestProperty
//        ("Content-Type", "application/x-www-form-urlencoded");
//        // Send POST output.
//        printout = new DataOutputStream (urlConn.getOutputStream ());        
//        printout.writeBytes (formData);
//        printout.flush ();
//        printout.close ();
//        // Get response data.
//        input = new DataInputStream(urlConn.getInputStream ());
//        String str;
//        while (null != ((str = input.readLine())))
//        {
//        	System.out.println (str);        
//        }
//        input.close ();

    }
    
    public static void main(String[] args) throws Exception
    {
    	//Construct data
//        String data = URLEncoder.encode("xuhao", "GBK") + "=18";  
//        post("http://www.harbinyouth.com.cn/UploadFiles/tp/save.asp",data);
    	
    	String url = "http://localhost/login.do";
    	Map paramMap = new HashMap();
    	paramMap.put("method", "123");
    	paramMap.put("abc", "abc");
    	System.out.println(getUrl(url,paramMap));
    
    }
}
