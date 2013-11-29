package com.litt.core.io.fileupload.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.Utility;
import com.litt.core.io.fileupload.HttpFileUpload;
import com.litt.core.io.fileupload.UploadListener;



/**
 * 文件上传处理器.
 * 
 * <pre><b>描述：</b>
 * 使用apache.commons.fileupload包。采用AJAX方式获取上传进度显示
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-17
 * @version 1.0
 */
public class UploadServlet extends HttpServlet
{
	
	/** 日志工具. */
    private static final Log logger = LogFactory.getLog(UploadServlet.class); 
    
    /** 上传文件根路径（绝对路径）. */
    private String homePath;
    
    /** 上传文件根路径（相对路径）. */
    private String uploadPath;
    
    /** 是否启动上传进度监听器. */
    private boolean isEnableProgressListener = true;
    

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException
	{		
		super.init();
		homePath = super.getInitParameter("homePath");		
		if(Utility.isEmpty(homePath))
			throw new ServletException("上传根路径不能为空！");
		uploadPath = super.getInitParameter("uploadPath");		
		if(Utility.isEmpty(uploadPath))
			throw new ServletException("上传相对路径不能为空！");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{		
		super.doPost(request, response);
		//处理文件上传		
		boolean isMultipart = HttpFileUpload.isMultipartContent(request);	//是否为文件上传
		if(isMultipart)
		{
			String guid = request.getParameter("guid");	//获取请求的唯一ID
			
			if(logger.isDebugEnabled())
			{
				logger.debug("处理文件上传...");
			}
			String characterEncoding = request.getCharacterEncoding(); 	//编码
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(4096);	//内存使用4K
			File repository = new File(this.homePath + File.separator + uploadPath+File.separator+ "TEMP");
			if(!repository.exists())
				repository.mkdirs();
			factory.setRepository(repository);	//上传临时路径，超出缓存的部分将存储到此处

			HttpFileUpload fileUpload = new HttpFileUpload(this.homePath, this.uploadPath, factory);
			fileUpload.addAllowFileTypes("text/plain,application/x-zip-compressed"); //添加允许上传类型
			fileUpload.setAllowField(true); //允许表单字段
			fileUpload.setHeaderEncoding(characterEncoding); //设置头编码
			fileUpload.setSizeMax(100 * 1024 * 1024); //设置最大上传尺寸100M
			fileUpload.setFileSizeMax(10 * 1024 * 1024);	//设置单个文件上传尺寸10M
			
			
			fileUpload.setProgressListener(new UploadListener(request));	//设置进度监听器，用于AJAX进度提示
			try
			{
				fileUpload.parseRequest(request);
			}
			catch (Exception e)
			{
				logger.error("文件上传失败！", e);
			}
			finally
			{
				fileUpload.dispose();
			}
		}	
		
	}

	/**
	 * @return the isEnableProgressListener
	 */
	public boolean isEnableProgressListener()
	{
		return isEnableProgressListener;
	}

	/**
	 * @param isEnableProgressListener the isEnableProgressListener to set
	 */
	public void setEnableProgressListener(boolean isEnableProgressListener)
	{
		this.isEnableProgressListener = isEnableProgressListener;
	}

	/**
	 * @return the uploadPath
	 */
	public String getUploadPath()
	{
		return uploadPath;
	}

	/**
	 * @param uploadPath the uploadPath to set
	 */
	public void setUploadPath(String uploadPath)
	{
		this.uploadPath = uploadPath;
	}

	/**
	 * @return the homePath.
	 */
	public String getHomePath()
	{
		return homePath;
	}


}
