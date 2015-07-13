package com.littcore.io.fileupload;

import org.apache.commons.io.FilenameUtils;

import com.littcore.io.util.FileUtils;
import com.littcore.uid.UUID;

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
	public static final String ILLEGAL_MIME_TYPE = "ILLEGAL_MIME_TYPE";	//非法的文件类型
	
	public static final String OUT_OF_SIZE_LIMIT = "OUT_OF_SIZE_LIMIT";		//文件大小超限
	
	public static final String STORE_FAILED = "STORE_FAILED";		//保存文件失败
	
	private String uid;
	
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
	
	/** 错误编号. */	
	private String errorCode;
	
	/** 错误详情. */
	private String errorMessage;	

	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return errorCode==null;
	}
	
	public String getSimpleFileName()
	{
		return FilenameUtils.getBaseName(fileName);
	}
	
	/**
	 * 获得用于显示的文件大小.
	 *
	 * @return the display file size
	 */
	public String getDisplayFileSize()
	{
		return FileUtils.humanReadableByteCount(fileSize);
	}

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

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
