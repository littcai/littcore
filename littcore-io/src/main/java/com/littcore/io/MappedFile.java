package com.littcore.io;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * <b>标题：</b> 大文件处理辅助类.
 * <pre><b>描述：</b>
 *    对超大文件，采用NIO进行文件读写操作，提高性能
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-08-05
 * @version 1.0
 */
public class MappedFile 
{
	
	public static void MappedFile(String  fileName) throws Exception 
	{ 
		long   length   =   new   File(fileName).length(); 
		MappedByteBuffer   in   =   new   FileInputStream(fileName).getChannel().map(FileChannel.MapMode.READ_ONLY,0,length); 
		int   i   =   0; 
		while(i   <   length) 
		{ 
			//System.out.print((char)in.get(i++)); 
		} 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)    throws   Exception 
	{
		MappedFile.MappedFile("D:\\test.txt");

	}

}
