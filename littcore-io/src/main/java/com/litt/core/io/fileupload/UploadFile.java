package com.litt.core.io.fileupload;

/** 
 * 
 * 上传文件信息.
 * 
 * <pre><b>描述：</b>
 *    描述上传文件的一些信息，该信息可用于数据库归档等操作
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2011-07-03 增加fieldName用来区分上传文件的表单项
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-21
 * @version 1.0
 *
 */
public class UploadFile
{	
	private String fieldName;
	
	/** 原文件名. */
	private String srcFileName;
	
	/** 原文件后缀名. */
	private String fileSuffix;
	
	/** 文件类型. */
	private String mimeType;
	
	/** 目标文件名. */
	private String fileName;
	
	/** 上传文件路径（相对路径）. */
	private String filePath;
	
	/**
	 * 文件大小.
	 */
	private long fileSize;

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * @return the srcFileName
	 */
	public String getSrcFileName()
	{
		return srcFileName;
	}

	/**
	 * @param srcFileName the srcFileName to set
	 */
	public void setSrcFileName(String srcFileName)
	{
		this.srcFileName = srcFileName;
	}	

	/**
	 * @return the fileType
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setMimeType(String fileType)
	{
		this.mimeType = fileType;
	}

	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix()
	{
		return fileSuffix;
	}

	/**
	 * @param fileSuffix the fileSuffix to set
	 */
	public void setFileSuffix(String fileSuffix)
	{
		this.fileSuffix = fileSuffix;
	}

	/**
	 * @return the fileSize.
	 */
	public long getFileSize()
	{
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set.
	 */
	public void setFileSize(long fileSize)
	{
		this.fileSize = fileSize;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
