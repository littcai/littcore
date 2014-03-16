package com.litt.core.net.telnet;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetOption;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import com.litt.core.common.Utility;

/**
 * 
 * Telnet客户端.
 * 
 * <pre><b>描述：</b>
 *    
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2011-4-28
 * @version 1.0
 */
public class TelnetClientTool
{
    private static final Log logger = LogFactory.getLog(TelnetClientTool.class);
    
    /** 默认读超时. */
    public static final long DEFAULT_READ_TIMEOUT = 1000L;
    
    /** 
     * 常用可见的结尾提示符. 
     * 当指定提示符未发现时，通过匹配默认提示符以保证读结束
     * 若一直没发现，则等待读超时，认为无法处理
     */
    public static final String[] DEFAULT_PROMPT = new String[]{"$", "#", ">", ":"};
    
    private TelnetClient telnet;
    
    private InputStream in;
    
    private PrintStream out;
    
    /** 
     * 读超时. 
     * 超过该时间没读取到内容则认为读取失败
     */
    private long readTimeout = DEFAULT_READ_TIMEOUT;
    
    /** 
     * 提示符. 
     * 用于从响应中判断命令是否已执行完毕
     */
    private String prompt = "#";
    
    /** 响应内容. */
    private String retContent;

    /**
     * Instantiates a new telnet.
     *
     * @param server 服务器地址
     * @param port 端口
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public TelnetClientTool(String server, int port)
        throws IOException
    {
        this.connect(server, port);       
        in = telnet.getInputStream();
        out = new PrintStream(telnet.getOutputStream());
        if(logger.isDebugEnabled())
            logger.debug("命令下发开始..");
    }

    /**
     * 是否已连接.
     *
     * @return true, if is connected
     */
    public boolean isConnected()
    {
        return telnet.isConnected();
    }

    /**
     * 写到输出流.
     *
     * @param value 输出内容
     */
    private void write(String value)
    {
        out.println(value);
        out.flush();
    }

    /**
     * 连接服务器.
     *
     * @param server 服务器地址
     * @param port 端口
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void connect(String server, int port)
        throws IOException
    {
        if(telnet == null)
        {
        	telnet = new TelnetClient();
        	this.setOption();
        	//telnet.setDefaultTimeout(5000);
        }
        if(!telnet.isConnected())
            telnet.connect(server, port);
    }

    /**
     * 用户登录.
     *
     * @param user 用户名
     * @param userPrompt 用户提示符
     * @param password 密码
     * @param passwordPrompt 密码提示符
     * @return true, if successful
     */
    public boolean login(String user, String userPrompt, String password, String passwordPrompt)
    {
        if(logger.isDebugEnabled())
            logger.debug((new StringBuffer("尝试登陆，登陆用户：")).append(Utility.trimNull(user)).toString());        
        try
        {
        	StringBuffer sb = new StringBuffer();
            sb.append(readUnit(userPrompt));   
            write(user);            
            sb.append(readUnit(passwordPrompt));
            write(password);
            sb.append(readUnit(prompt));
            retContent = sb.toString();
	        return true;	        
        }
        catch(Exception e)
        {
            logger.error("登陆失败！", e);
            return false;
        }        
    }
    
    private boolean setOption()  
    {  
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);  
        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);  
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);  
          
        try  
        {  
        	telnet.addOptionHandler(ttopt);  
        	telnet.addOptionHandler(echoopt);  
        	telnet.addOptionHandler(gaopt);  
        }  
        catch (InvalidTelnetOptionException e)  
        {  
            logger.warn("Exception: ", e);  
            return false;  
        } catch (IOException e) {
        	logger.warn("Exception: ", e);  
            return false;  
		}  
        return true;  
    }  
    
    private void removeOption() throws Exception  
    {  
    	telnet.deleteOptionHandler(TelnetOption.TERMINAL_TYPE);  
    	telnet.deleteOptionHandler(TelnetOption.ECHO);  
    	telnet.deleteOptionHandler(TelnetOption.SUPPRESS_GO_AHEAD);  
    }  

    /**
     * 断开连接.
     */
    public void disconnect()
    {
        try
        {
            if(telnet.isConnected())
                telnet.disconnect();
            if(logger.isDebugEnabled())
                logger.debug("连接已断开");
        }
        catch(Exception e)
        {
            logger.error("断开连接失败!", e);
        }
    }
    
    /**
     * 读取响应.
     * 通过字符串匹配获取完整响应.
     * 注意：根据字符串匹配的方式通常是不可靠的，任何异常情况都可能导致死循环
     *
     * @param pattern 模式
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String readUnit(String pattern) throws IOException
    {      
		char lastChar = pattern.charAt(pattern.length() - 1);
		StringBuffer sb = new StringBuffer();
		char ch = (char) in.read();
		while (true) {
			sb.append(ch);			
			if (ch == lastChar) {
				String ret = sb.toString();
				System.out.println(ret);
				if (ret.endsWith(pattern)) {					
					return ret;
				}
				else {
					for (String prompt : DEFAULT_PROMPT) {
						if (ret.endsWith(prompt)) {
							return ret;
						}
					}
				}
			}
			ch = (char) in.read();
		}       
    }

    
    /**
     * 发送命令.
     *
     * @param cmd 命令
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String sendCmd(String cmd)
        throws IOException
    {
    	logger.debug("发送命令："+cmd);
        write(cmd);
        retContent = this.readUnit(this.prompt);
        return retContent;
    }

    public static void main(String args[])
        throws Exception
    {
    	TelnetClientTool telnet = new TelnetClientTool("172.16.10.132", 23);
    	//telnet.setPrompt("$");
    	boolean isLogined = telnet.login("transoft", "login: ", "Transoft1", "Password: ");
    	if(isLogined)
    	{
    		String ret = telnet.sendCmd("ls");
    		System.out.println(ret);
    	}
    	
//    	TelnetClientTool telnet = new TelnetClientTool("192.168.6.32", 23);
//    	boolean isLogined = telnet.login("admin", "User Name:", "12345678", "Password:");
//    	System.out.println(telnet.getRetContent());
//    	if(isLogined)
//    	{
//    		telnet.sendCmd("show cpu utilization");
//    		System.out.println(telnet.getRetContent());
//    	}
    }

	/**
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * @param prompt the prompt to set
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	 * @return the readTimeout
	 */
	public long getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * @return the retContent
	 */
	public String getRetContent() {
		return retContent;
	}

    

}