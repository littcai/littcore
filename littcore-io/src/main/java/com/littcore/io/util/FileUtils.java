package com.littcore.io.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.littcore.common.Utility;
import com.littcore.format.FormatDateTime;
import com.littcore.util.ValidateUtils;

/**
 *  
 * <b>标题：</b>文件操作辅助类.
 * <pre><b>描述：</b>
 *    本地文件处理，常用操作：创建、删除、列表，文件编码转换
 * </pre>
 * <pre><b>变更日志：</b>
 * 	2014-04-28：移除byteCountToDisplaySize，直接用commons-io里的
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-07-25
 * @version 1.0
 *
 */
public class FileUtils extends org.apache.commons.io.FileUtils 
{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FileUtils.class);
	
	/**
     * 返回当前日期时间字符串+3位随机数，格式'20030809161245321001'作为文件名的唯一标识(用途：文件唯一标识).
     * 
     * @return String 日期时间+3位随机数字符串
     */
    public static String currentToFileName()
    {
        String fileName = FormatDateTime.format(new Date(), "yyyyMMddHHmmssSSS");        
        //取随机产生的认证码(4位数字)
        String choose = "0123456789";
        Random random = new Random();
        char[] sRand = {'0','0','0','0'};
        char temp;
        for (int i = 0; i < 4; i++) {
            
            temp=choose.charAt(random.nextInt(10));            
            sRand[i]= temp;           
        }
        fileName += String.valueOf(sRand);
        return fileName;
    }		
	

    /**
     * 创建一个文件，采用内容自身的编码方式
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent 文本文件内容
     */
    public static void writeStringToFile(File file, String fileContent) throws IOException
    {      
        if(logger.isDebugEnabled())
        {
        	logger.debug("创建文件 - "+file.getAbsolutePath());
        }	
        writeStringToFile(file,fileContent, "UTF-8");
    } 	
	
    /**
     * 有编码方式的文件创建
     * @param file 文件对象
     * @param fileContent 文本文件内容
     * @param encoding 编码方式 例如 GBK 或者 UTF-8
     */
    public static void writeStringToFile(File file, String fileContent, String encoding) throws IOException
    {           
        if(logger.isDebugEnabled())
        {
        	logger.debug("创建文件 - "+file.getAbsolutePath());
        }	    	
        if (!file.exists()) 
        {
          	file.createNewFile();
        }        
        String encodedContent;
        if(Utility.isEmpty(encoding))
        	encodedContent = new String(fileContent.getBytes());
        else
        	encodedContent = new String(fileContent.getBytes(),encoding);

        FileOutputStream fos = new FileOutputStream(file);        
        PrintWriter pw = new PrintWriter(fos);   
        pw.println(encodedContent);        
        //PrintWriter pw = new PrintWriter(file,encoding); //JDK5以上可直接用该语句替换上面
        pw.close();
    } 
    
    /**
     * 读取文件内容
     * @param file 文件对象
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader readFile(File file) throws IOException
    {
    	return readFile(file,null);
    }
    
    /**
	 * 读取文件内容.
	 * 
	 * @param file
	 *            文件对象
	 * @param encoding
	 *            文件编码(默认为操作系统编码)
	 * 
	 * @return BufferedReader对象
	 * 
	 * @throws IOException
	 */
    public static BufferedReader readFile(File file,String encoding) throws IOException
    {
    	InputStreamReader is = null;
    	if(ValidateUtils.isEmpty(encoding))    
    		is = new InputStreamReader(openInputStream(file));
    	else
    		is = new InputStreamReader(openInputStream(file),encoding);
    	BufferedReader br = new BufferedReader(is);
    	return br;
    }    
    
    /**
     * 删除文件.
     * 
     * @param file 文件对象
     * @throws IOException 文件不存在时抛异常，文件删除失败时抛异常
     */
    public static void deleteFile(File file) throws IOException
    {
    	if(logger.isDebugEnabled())
        {
        	logger.debug("删除文件 - "+file.getAbsolutePath());
        }  
    	if (!file.exists())	//如果文件不存在，抛异常
    	{
            throw new FileNotFoundException("File does not exist: " + file);
        }
    	boolean successFlag = deleteQuietly(file);	//静默删除
    	if (!successFlag) //如果静默删除失败，抛异常
    	{  
             throw new IOException("Unable to delete file: " + file);
    	}     
    } 
    
    /**
     * 静默删除文件或文件夹(递归删除其子文件)
     * @param file 文件对象
     * @return boolean 是否删除成功
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
              deleteDirectory(file);
            }
        } catch (Exception e) {
        }

        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }    
    
    /**
     * 删除文件目录及其所有子目录(支持递归).
     * @param directory 文件目录
     * 
     * @throws IOException 目录不存在时抛FileNotFoundException，目录删除失败时抛IOException
     */
    public static void deleteDirectory(File directory) throws IOException
    {
    	if(logger.isDebugEnabled())
        {
        	logger.debug("删除文件目录 - "+directory);
        }    	
    	if (!directory.exists()) {
    		throw new FileNotFoundException("Directory does not exist: " + directory.getAbsolutePath());
        }
        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        for (int i = 0; i < files.length; i++) 
        {
            File file = files[i];
            if(file.isFile())
            {
            	deleteFile(file);
            }
            else if(file.isDirectory())
            {
            	deleteDirectory(file);	//递归清理文件夹
            }            
        }        
        if (!directory.delete()) 	//文件都删除之后就可以删除这个目录了
        {        	
            throw new IOException("Unable to delete directory " + directory.getAbsolutePath() + ".");
        }    	
    } 
    
    /**
     * 创建一个目录，如存在则什么都不做
     * @param directory 文件目录
     */
    public static void createDirectory(File directory)
    {           
        if(logger.isDebugEnabled())
        {
        	logger.debug("创建文件目录 - "+directory);
        }	
        
        if(!directory.exists())
        	directory.mkdirs();
       
    } 

    /**
     * 清理文件目录(不删除文件目录本身)(支持递归).
     * @param directory 文件目录
     * @throws IOException 目录不存在时抛FileNotFoundException，传入参数不是目录时抛IllegalArgumentException，有安全性无法列目录文件时抛IOException，有文件删除失败时抛异常
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) 
            throw new FileNotFoundException(directory.getAbsolutePath() + " does not exist");

        if (!directory.isDirectory()) 
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");  

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        for (int i = 0; i < files.length; i++) 
        {
            File file = files[i];
            if(file.isFile())
            {
            	deleteFile(file);
            }
            else
            {
            	cleanDirectory(file);	//递归清理文件夹
            }            
        }
    }
    
    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file  the file to open for input, must not be <code>null</code>
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be read
     * @since Commons IO 1.3
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file.getAbsolutePath() + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file.getAbsolutePath() + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file.getAbsolutePath() + "' does not exist");
        }
        return new FileInputStream(file);
    }   
    
    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     *
     * @param file  the file to open for output, must not be <code>null</code>
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     * @since Commons IO 1.3
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file.getAbsolutePath() + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file.getAbsolutePath() + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file.getAbsolutePath() + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }
    
    /**
     * Implements the same behaviour as the "touch" utility on Unix. It creates
     * a new file with size 0 or, if the file exists already, it is opened and
     * closed without modifying it, but updating the file date and time.
     * <p>
     * NOTE: As from v1.3, this method throws an IOException if the last
     * modified date of the file cannot be set. Also, as from v1.3 this method
     * creates parent directories if they do not exist.
     *
     * @param file  the File to touch
     * @throws IOException If an I/O problem occurs
     */
    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            OutputStream out = openOutputStream(file);
            IOUtils.closeQuietly(out);
        }
        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }
    
    /**
     * Human readable byte count.
     *
     * @param bytes the bytes
     * @return the string
     */
    public static String humanReadableByteCount(long bytes)
    {
    	return humanReadableByteCount(bytes, false);
    }
    
    
    /**
     * Human readable byte count.
     *
     * @param bytes the bytes
     * @param si the si
     * @return the string
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;        
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "KMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "i": "");
        DecimalFormat df = new DecimalFormat("#.##");       
        return new StringBuilder()
          .append(df.format(bytes / Math.pow(unit, exp)))
          .append(" ")
          .append(pre)
          .append("B").toString();
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		//FileUtils.cleanDirectory(new File("D:\\temp"));
		//FileUtils.deleteDirectory(new File("D:\\temp"));
	  System.out.println(FileUtils.humanReadableByteCount(1000));
	  System.out.println(FileUtils.humanReadableByteCount(1024));

	}
	
}
