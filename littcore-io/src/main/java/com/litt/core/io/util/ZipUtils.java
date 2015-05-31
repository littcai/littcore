package com.litt.core.io.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.io.zip.ZipEntry;
import com.litt.core.io.zip.ZipFile;
import com.litt.core.io.zip.ZipOutputStream;

/**
 * ZIP压缩解压缩工具.
 * 
 * <pre><b>描述：</b>
 * 利用java.util.zip压缩解压缩
 * RAR文件的算法是加密的，故只能解压缩ZIP方式压缩的文件
 * 注：当压缩文件夹时产生了一个空文件，是由于ZipEntry对目录判断时是根据路径名以斜杠"/"结尾的，与windows下的File.seperator不相同
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 2013-01-16 修正文件夹压缩时多出了一个空文件
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2008-12-4, 2013-01-16
 * @version 1.0, 1.1
 */
public class ZipUtils
{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(ZipUtils.class);
	
	/** The Constant BUFFERED_SIZE. */
	public static final int BUFFERED_SIZE = 1024;
	
	/**
	 * 压缩文件或目录.
	 * 
	 * @param srcFileOrPath 需要压缩的文件或路径
	 * @param targetFileNamePath 目标文件绝对路径
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void zip(String srcFileOrPath, String targetFileNamePath) throws IOException
	{
		zip(new File(srcFileOrPath), new File(targetFileNamePath));
	}
	
	/**
	 * 压缩文件或目录.
	 *
	 * @param srcFile the src file
	 * @param targetFileNamePath the target file name path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void zip(File srcFile, String targetFileNamePath) throws IOException
  {
    zip(srcFile, new File(targetFileNamePath));
  }
	
	public static void zip(File srcFileOrPath, File targetFileNamePath) throws IOException
	{
		//使用输出流检查
        CheckedOutputStream cs = new CheckedOutputStream(new FileOutputStream(targetFileNamePath), new CRC32());
        //声明输出zip流
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(cs));
        out.setEncoding("GBK");
		zip(out, srcFileOrPath, "");	
		out.close();
	}
	
	
	/**
	 * 递归方式压缩.
	 * 
	 * @param out ZIP输出流
	 * @param srcFile 需要被压缩的文件
	 * @param basePath 基础路径
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void zip(ZipOutputStream out, File srcFile, String basePath) throws IOException
	{
		if(srcFile.isDirectory()) 
		{
			File[] files = srcFile.listFiles();
			
			if(files.length==0)
			{
				out.putNextEntry(new ZipEntry(basePath + srcFile.getName()+"/"));	//创建子文件夹
				out.closeEntry();
			}	
			else
			{
				basePath += srcFile.getName() + "/";  
				for (int i = 0; i < files.length; i++) 
				{
					zip(out, files[i], basePath);
				}
			}
			
		}
		else 
		{			
			out.putNextEntry(new ZipEntry(basePath + srcFile.getName()));
			FileInputStream in = new FileInputStream(srcFile);
			int len;	
			byte[] buf = new byte[BUFFERED_SIZE];
			while ( (len = in.read(buf)) >0) 
			{
				out.write(buf,0,len);
			}
			out.closeEntry();
			in.close();
		}
	}
	
	/**
	 * 批量压缩多个文件
	 * @param files 文件数组
	 * @param targetFileNamePath 目标文件
	 * @throws IOException
	 */
	public static void batchZip(File[] files,String targetFileNamePath) throws IOException
	{
		//使用输出流检查
        CheckedOutputStream cs = new CheckedOutputStream(new FileOutputStream(targetFileNamePath), new CRC32());
        //声明输出zip流
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(cs));	
        File srcFile = null;
        for(int i=0;i<files.length;i++)
        {
        	srcFile = files[i];
        	out.putNextEntry(new ZipEntry(srcFile.getName()));
			FileInputStream in = new FileInputStream(srcFile);
			int len;	
			byte[] buf = new byte[BUFFERED_SIZE];
			while ( (len = in.read(buf)) >0) 
			{
				out.write(buf,0,len);
			}
			out.closeEntry();
			in.close();
        }
		out.close();
	}
	
	public static void unzip(File srcZipFile, File targetFilePath, boolean isDelSrcFile) throws IOException
	{
		if(!targetFilePath.exists())	//如果目标文件夹不存在，则创建
			targetFilePath.mkdirs();
		ZipFile zipFile = new ZipFile(srcZipFile); 
		
		Enumeration fileList = zipFile.getEntries();
		ZipEntry zipEntry = null;
	    while(fileList.hasMoreElements()) 
	    {
	            zipEntry = (ZipEntry) fileList.nextElement();	            
	            if(zipEntry.isDirectory()) 	//多级目录的支持
	            {	            	
	            	File subFile = new File(targetFilePath, zipEntry.getName());
	            	if(!subFile.exists())	//如果子文件夹不存在，则创建
	            		subFile.mkdir();
	            	continue;
	            }
	            else	//保存解压缩的文件
	            { 	            	
	            	File targetFile = new File(targetFilePath, zipEntry.getName());
	            	if(logger.isDebugEnabled())
	            	{
	            		logger.debug("正在解压缩文件："+targetFile.getName());
	            	}
	            	
	            	if(!targetFile.exists())
	            	{
	            		File parentPath = new File(targetFile.getParent());
	            		if(!parentPath.exists())	//需要判断文件是否在子文件夹里，是的话要解析文件名并创建目录
	            			parentPath.mkdirs();
	            		//targetFile.createNewFile();
	            	}

	                InputStream in = zipFile.getInputStream(zipEntry);	                
	                FileOutputStream out = new FileOutputStream(targetFile);  
	                int len;
	                byte[] buf = new byte[BUFFERED_SIZE];
	                while ((len = in.read(buf)) !=-1) 
	                {
	                    out.write(buf, 0, len);
	                }		                   
	                out.close();
	                in.close();
	            }
	    }
	    zipFile.close();
	    if(isDelSrcFile)
	    {	    	
	    	boolean isDeleted = srcZipFile.delete();
	    	if(isDeleted)
	    	{
	    		if(logger.isDebugEnabled())
	    		{
	        		logger.debug("原文件被删除了");
	        	}
	    	}        	
	    }
	}
	
	
	/**
	 * 解压缩ZIP文件.
	 * 解压缩时并不会将目录作为一个ZipEntry，所以需要根据文件路径判断是否要创建文件夹
	 * 
	 * @param srcZipFile 原ZIP文件
	 * @param targetFilePath 目标文件夹路径
	 * @param isDelSrcFile 是否解压缩成功后删除原压缩文件
	 * 
	 * @throws ZipException the zip exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void unzip(String srcZipFile, String targetFilePath, boolean isDelSrcFile) throws IOException
	{	
		unzip(new File(srcZipFile), new File(targetFilePath), isDelSrcFile);
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception
	{
		
		ZipUtils.zip("D:\\licenses\\trannms\\wenzhou\\", "D:\\zip.zip");
		//ZipUtils.zip("D:\\zip.zip", "D:\\zip1.zip");
		//ZipUtils.unzip("D:\\zip.zip", "D:\\unzip", false);
		//File[] files = new File[]{new File("D:\\signedKey.dat"),new File("D:\\temp\\dump.txt")};
		//ZipUtils.batchZip(files, "D:\\zip.zip");
		
		//File[] files = new File[]{new File("D:\\licenses\\trannms\\wenzhou\\license.xml"),new File("D:\\licenses\\trannms\\wenzhou\\license.key")};
		//ZipUtils.batchZip(files, "D:\\zip.zip");
	}
}
