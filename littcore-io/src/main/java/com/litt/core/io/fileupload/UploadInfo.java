package com.litt.core.io.fileupload;

/**
 * 
 * <b>标题：</b> 上传进度对象.
 * <pre><b>描述：</b>
 *    存储上传过程中相关信息
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-08-19
 * @version 1.0
 */
public class UploadInfo 
{
	
	/** 开始状态. */
	public static final byte STATUS_START = 0;
	/** 上传中状态. */
	public static final byte STATUS_PROGRESSING = 1;		
	/** 完成状态. */
	public static final byte STATUS_DONE = 2;		
	
	/** SESSION中存放的上传进度对象KEY. */
	public static final String SESSION_UPLOAD_INFO = "SESSION_UPLOAD_INFO";
	
    
	/** 总上传字节数. */
	private long totalSize = 0;
	
	/** 剩余需要读取字节数. */
	private long bytesRead = 0;
	
	/** 监听起始时间(毫秒). */
	private long startTime = System.currentTimeMillis();
	
	/** 已花费时间(毫秒). */
	private long elapsedTime = 0;	
	
	/** 当前状态. */
	private byte status = STATUS_START;

	
	/** 上传文件索引号(多文件上传时使用). */
	private int fileIndex = 0;
	
	/**
	 * 是否正在处理
	 * @return
	 */
	public boolean isInProgress() 
	{
		return STATUS_PROGRESSING==status || STATUS_START==status;
	}

	/**
	 * @return the bytesRead
	 */
	public long getBytesRead() {
		return bytesRead;
	}

	/**
	 * @param bytesRead the bytesRead to set
	 */
	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	/**
	 * @return the elapsedTime
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the fileIndex
	 */
	public int getFileIndex() {
		return fileIndex;
	}

	/**
	 * @param fileIndex the fileIndex to set
	 */
	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	/**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


}
