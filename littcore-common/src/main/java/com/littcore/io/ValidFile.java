package com.littcore.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/** 
 * 
 * 有效文件.
 * 
 * <pre><b>描述：</b>
 *    继承了java.io.File，增加了文件是否存在的校验，确保文件真实存在，不用再在代码中判断了。
 *    1、若文件不存在则自动创建 
 *    2、若文件所在文件夹不存在，则自动创建
 *    3、可设置若文件存在时是自动替换还是追加
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-12-3
 * @version 1.0
 *
 */
public class ValidFile extends File
{	

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -7474977077695517467L;

	/**
	 * Instantiates a new valid file.
	 * 
	 * @param filePath 文件路径
	 */
	public ValidFile(String filePath) throws IOException
	{
		super(filePath);
		init();
	}
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param filePath 文件路径
	 * @param isCovered 是否覆盖已存在文件
	 */
	public ValidFile(String filePath, boolean isCovered) throws IOException
	{
		super(filePath);
		init(isCovered);
	}	
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param parent 父文件路径
	 * @param child 子文件路径
	 */
	public ValidFile(String parent, String child) throws IOException
	{
		super(parent, child);
		init();
	}	
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param parent 父文件路径
	 * @param child 子文件路径
	 * @param isCovered 是否覆盖已存在文件
	 */
	public ValidFile(String parent, String child, boolean isCovered) throws IOException
	{
		super(parent, child);
		init(isCovered);
	}	
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param parent 父文件对象
	 * @param child 子文件路径
	 */
	public ValidFile(File parent, String child) throws IOException 
	{
		super(parent, child);
		init();
	}
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param parent 父文件对象
	 * @param child 子文件路径
	 * @param isCovered 是否覆盖已存在文件
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ValidFile(File parent, String child, boolean isCovered) throws IOException 
	{
		super(parent, child);
		init(isCovered);
	}	
	
	/**
	 * Instantiates a new valid file.
	 * 
	 * @param uri the uri
	 */
	public ValidFile(URI uri) throws IOException
	{
		super(uri);
		init();
	}
	
	/**
	 * 初始化.
	 * 若文件不存在则自动创建。
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void init() throws IOException
	{
		this.init(false);
	}
	
	/**
	 * 初始化.
	 * 若文件不存在则自动创建。
	 * 
	 * @param isCovered 是否覆盖已存在文件
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void init(boolean isCovered) throws IOException
	{
		if(!this.exists())
		{			
			if(this.isDirectory())
			{
				this.mkdirs();
			}
			else
			{
				File parentFile = this.getParentFile();
				if(!parentFile.exists())
				{
					if(!parentFile.mkdirs())
						throw new IOException("Directory auto create failed!");
				}
				boolean isSuccess = this.createNewFile();
				if(!isSuccess)
					throw new IOException("File auto create failed!");
			}
		}			
		else
		{
			if(isCovered)	//覆盖原文件
			{
				if(this.isFile())
				{
					boolean isSuccess = this.delete();
					if(!isSuccess)
						throw new IOException("File auto delete failed!");
					isSuccess = this.createNewFile();
					if(!isSuccess)
						throw new IOException("File auto create failed!");
				}
			}
		}
	}

}
