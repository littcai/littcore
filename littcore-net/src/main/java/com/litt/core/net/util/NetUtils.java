package com.litt.core.net.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <b>标题:</b> 网络测试工具包.
 * <pre><b>描述:</b> 
 *   网络测试工具包，需要JDK5以上支持
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since Nov 7, 2007
 * @version 1.0
 *
 */
public class NetUtils
{
	private transient static final Log logger = LogFactory.getLog(NetUtils.class);
	
    /**
     * 模拟PING操作，测试网络连通性。连接成功返回true，否则返回false
     * @param ip
     * @return
     */
    public static boolean ping(String ip)
    {
        boolean ret = false;
        try {
            InetAddress address = InetAddress.getByName(ip);            
            ret = address.isReachable(5000);
            if(logger.isDebugEnabled())
            {
                logger.debug("PING网元设备，IP地址："+ ip +"，结果："+ret);
            }
        } catch (UnknownHostException e) {
            ret = false;
        } catch (IOException e) {
            ret = false;
        }
        return ret;
    }
    
    /**
     * 模拟TELNET操作，连接成功返回true，否则返回false
     * @return
     */
    public static boolean telnet(String ip,int port)
    {
        boolean ret = false;
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(ip,port);
            server.connect(address, 5000);
            ret = true;
        } catch (UnknownHostException e) {
            ret = false;
        } catch (IOException e) {
            ret = false;
        }finally{
            if(server!=null)
                try{
                    server.close();
                } catch (IOException e) {
                }
        }
        return ret;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
//        System.out.println(NetUtil.ping("127.0.0.1"));
//        System.out.println(NetUtil.telnet("127.0.0.1",23));
    	
    	File f = new File("d:\\b\\a.txt");
    	System.out.println(f.getName());
    	System.out.println(f.getCanonicalPath());
    	System.out.println(f.getCanonicalFile().getName());
    
    }

}
