/* Licence:
 *   Use this however/wherever you like, just don't blame me if it breaks anything.
 *
 * Credit:
 *   If you're nice, you'll leave this bit:
 *
 *   Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
 *   email : plosson@users.sourceforge.net
 */
package com.littcore.io.fileupload;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * <b>标题：</b> 文件上传进度监听器.
 * <pre><b>描述：</b>
 *    1、文件上传时，创建该监听器，并将上传进度对象存入SESSION中。
 *    2、上传过程中，利用AJAX异步调用功能，不断读取SESSION中的上传进度信息，并更新页面上传进度条
 *    3、TODO 上传完成后，移除SESSION中的信息
 *    
 *    
 * </pre>   
 * <pre><b>备注：</b>   
 *    1、SESSION中存放对像的名称为： "uploadProgressInfo"+guid
 *    2、需【commons-fileupload-1.2.1.jar】以上版本包支持
 * </pre>  
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-28
 * @version 1.0
 */
public class UploadListener implements ProgressListener {
	
	private static final Log logger = LogFactory.getLog(UploadListener.class);


	/** 上传进度对象. */
	private UploadInfo uploadInfo = null;

	/**
	 * 创建文件上传进度监听器
	 * @param request 请求对象
	 * @param guid 多组件同时上传时的唯一索引标识
	 */
	public UploadListener(HttpServletRequest request) 
	{
    	if(logger.isDebugEnabled()){
    		logger.debug("启动监听....");
    	} 
        uploadInfo = new UploadInfo();           
        request.getSession().setAttribute(UploadInfo.SESSION_UPLOAD_INFO, uploadInfo);        
	}
	
	/**
	 * 更新上传进度.
	 * 
	 * @param pBytesRead
	 *            当前上传文件已读取字节数.
	 * @param pContentLength
	 *            当前上传文件需要读取的字节数.
	 * @param pItems
	 *            当前读取文件对象索引.(0 = no item so far, 1 = first item is being read,
	 *            ...)
	 */
	public void update(long pBytesRead, long pContentLength,int pItems) 
	{
		if(logger.isDebugEnabled()){
    		logger.debug("更新上传进度....");
    	}
        if(pContentLength == -1) //没有需要读取的内容了
        {
        	uploadInfo.setStatus(UploadInfo.STATUS_DONE);
        	long elapsedTime = System.currentTimeMillis()-uploadInfo.getStartTime();
        	uploadInfo.setElapsedTime(elapsedTime);
        	if(logger.isDebugEnabled()){
	       		logger.debug("上传完成，总计消耗时间："+elapsedTime);
	        }
        }
        else 	//继续读取
        {
        	if(logger.isDebugEnabled()){
	       		logger.debug(pBytesRead+"/"+pContentLength);
	        }
        	uploadInfo.setFileIndex(pItems);
        	uploadInfo.setTotalSize(pContentLength);
        	uploadInfo.setBytesRead(pBytesRead);
        	uploadInfo.setStatus(UploadInfo.STATUS_PROGRESSING);
        }		
	}


}
