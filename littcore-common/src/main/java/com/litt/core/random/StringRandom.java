package com.litt.core.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <b>标题：</b> 随机数辅助类.
 * 
 * <pre><b>描述：</b>
 * 生成随机数的辅助类
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-22
 * @version 1.0
 */
public class StringRandom 
{    
	   
   	/** 日志工具. */
    private static final Log logger = LogFactory.getLog(StringRandom.class);
	
    /**
	 * 由 SUN 提供者所提供的伪随机数生成 (PRNG) 算法名. 该实现符合 IEEE P1363 标准（附录 G.7：源位的扩展），并用 SHA1
	 * 作为 PRNG 的基础。 它计算一个真随机种子值上的 SHA1 散列，该真随机种子值与一个 64 位的计数器串接在一起，每运算一次值就增加 1。
	 * 从 160 位的 SHA1 输出中，只用 64 位.
	 */
    public static final String SHA1PRNG = "SHA1PRNG";	
    
    /** 随机数长度(默认4位). */
    private int  length = 4;  
    
    /** 
     * 随机数生成模式(默认：CHARSET_LETTER_NUM).
     * 可选值：
     *  CHARSET_ALL、CHARSET_NUM、CHARSET_LETTER、CHARSET_LETTER_NUM、CHARSET_USER_DEFINE
     */
    private String charset = StringRandom.CHARSET_LETTER_NUM;
    
    /** 支持全部字符. */
    public static final String CHARSET_ALL = "CHARSET_ALL";
    
    /** 只支持数字0-9. */
    public static final String CHARSET_NUM = "CHARSET_NUM";
    
    /** 只支持字母a-z(大小写). */
    public static final String CHARSET_LETTER = "CHARSET_LETTER";
    
    /** 支持字母和数字混合(默认). */
    public static final String CHARSET_LETTER_NUM = "CHARSET_LETTER_NUM";
    
    /** 用户指定的字符集. */
    public static final String CHARSET_USER_DEFINE = "CHARSET_USER_DEFINE";   
    
    /** 用户指定的字符数组，随机数将限制在该范围. */
    private char[]  charArray = null;
    
    /** 是否安全加密随机数. */
    private boolean  enableSecurity  = false;

    /**
	 * 生成随机对象. 支持加密的强随机数生成器(SecureRandom)
	 */
    private Random initRandomObject()
    {
    	Random random = null;
        // check to see if the object is a SecureRandom object
        if (enableSecurity)
        {
            try
            {                
            	random = SecureRandom.getInstance(StringRandom.SHA1PRNG);
            } catch (NoSuchAlgorithmException e)
            {                
            	logger.error(e);
                random = new Random();	//加密失败则使用默认随机数生成器
            }
        } 
        else
            random = new Random();
        return random;
    }

    /**
	 * 生成随机数.
	 */
    private String generateRandom()
    {
    	Random random = initRandomObject();
    	String randomStr = "";
    	char[] sTemp = new char[this.length];	//临时存放生成的随机数字符    	
    	if(this.charset.equals(StringRandom.CHARSET_ALL))
		{  
    		for(int i = 0; i < this.length; i++) 
			{ 
    			sTemp[i] = (char)(34 + ((int)(random.nextFloat() * 93)));
			}	    		
		}      
    	else
    	{	
	    	if(this.charset.equals(StringRandom.CHARSET_LETTER))	//只能是字符
	    	{
	    		this.charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();	    		
	    	}	
	    	else if(this.charset.equals(StringRandom.CHARSET_NUM))	//只能是字符
	    	{
	    		this.charArray = "0123456789".toCharArray();
	    		
	    	}
	    	else if(this.charset.equals(StringRandom.CHARSET_LETTER_NUM))	//只能是字符
	    	{
	    		this.charArray = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	    		
	    	}
	    	else if(this.charset.equals(StringRandom.CHARSET_USER_DEFINE))	//用户指定的字符数组
	    	{	    		
	    		if(charArray==null||charArray.length==0)
	    			throw new java.lang.IllegalArgumentException("随机数范围不能为空！");
	    	}	
	    	else
	        	throw new java.lang.IllegalArgumentException("不受支持的生成模式！");
	    	//从随机数范围中获取随机值	    	
			int charArrayLen = charArray.length;
			for(int i = 0; i < this.length; i++) 
			{ 
				sTemp[i] = charArray[random.nextInt(charArrayLen)];		
			}			
    	}
    	randomStr = String.valueOf(sTemp);
    	
    	return randomStr;
    }

    /**
	 * 获得随机数字符串.
	 * 
	 * @return 随机数字符串
	 */
    public String getRandom()
    {              
    	return generateRandom(); // generate the first random string   
    }
    
    /**
	 * 示例.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) throws Exception
    {
    	StringRandom random = new StringRandom();
    	random.setCharset(StringRandom.CHARSET_USER_DEFINE);    	
    	random.setCharArray(new char[]{'a','1','b','d','$'});
    	random.setEnableSecurity(true);
    	random.setLength(6);
    	System.out.println(random.getRandom());
    	
    }

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @return the charArray
	 */
	public char[] getCharArray() {
		return charArray;
	}

	/**
	 * @param charArray the charArray to set
	 */
	public void setCharArray(char[] charArray) {
		this.charArray = charArray;
	}

	/**
	 * @return the enableSecurity
	 */
	public boolean isEnableSecurity() {
		return enableSecurity;
	}

	/**
	 * @param enableSecurity the enableSecurity to set
	 */
	public void setEnableSecurity(boolean enableSecurity) {
		this.enableSecurity = enableSecurity;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	

}
