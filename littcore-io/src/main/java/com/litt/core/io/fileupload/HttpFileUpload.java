package com.litt.core.io.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.litt.core.common.Utility;
import com.litt.core.io.util.FileUtils;

/**
 * 
 * 
 * 处理HTTP方式文件上传.
 * 
 * <pre><b>描述：</b>
 *     上传的文件主要有2类：
 *     1类为文件型附件
 *     		* 保存于Web容器外
 *          * 信息保存于数据库中
 *          * 下载时需调用Download控制器
 *          * 通常文件体积比较大，可配合AJAX上传进度条
 *          * 程序更新时无需备份该目录
 *     1类为页面型附件
 *     		* 保存于Web容器内的特定目录
 *          * 可在页面上通过HTTP地址直接访问，如图片、FLASH等。
 *          * 通常文件体积比较小
 *          * 程序更新时需备份该目录
 *     依赖库：【commons-fileupload】、【commons-io】
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2010-11-28 v1.1 
 *    		1、原处理parseRequest时就完成了文件的保存操作，这在uploadPath需要从requst中获取参数相违背，因此特增加upload方法，分离文件保存的操作
 *    		2、去除自动生成年月文件夹
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-4-17
 * @version 1.0,1.1
 *
 */
public class HttpFileUpload extends ServletFileUpload
{ 
	/** 日志工具. */
    private static final Log logger = LogFactory.getLog(HttpFileUpload.class);  	

    /** The Constant K. */
    private static final long K = 1024;
    
    /** The Constant M. */
    private static final long M = 1024 * 1024;

    /**
     * 单个文件大小限制，0为无限制，配置文件中存放的为String类型.
     * 默认：10M
     */
    private long fileLimitSize = 10 * M;    
    
    /**
     * 文件上传类型限制.
     * 默认：image/jpeg,image/gif,image/pjpeg,image/png,image/bmp,application/vnd.ms-excel,application/octet-stream
     */
    private String allowFileTypes ="image/jpeg,image/gif,image/pjpeg,image/png,image/bmp,application/vnd.ms-excel,application/msword,application/octet-stream";

    /** 是否允许处理非文件域，默认为否. */
    private boolean allowField = false;
    
    /** 是否自动重命名，默认为否. */
    private boolean isAutoRename = false;
    
    /**  最小报告长度，默认为10K. */
    private int reportLimitSize = 10240; 
    
    /** 上传根路径（绝对路径）. */
    private String homePath;
    
    /** 上传根路径（相对路径路径）. */
    private String uploadPath;
    
    /**
     * 临时的文件FileItem.
     */
    private List<FileItem> tempFileItem = new ArrayList<FileItem>();
    
    private Map parameterMap = new HashMap();			//表单字段映射
    private List<UploadFile> succeedFiles = new ArrayList<UploadFile>();		//UploadFile
    private List failedFileNames = new ArrayList();		//上传失败文件
    private List invalidFileNames = new ArrayList();	//非法的上传文件	
    
    /**
     * 构造函数初始化.
     *
     */
    public HttpFileUpload(String homePath, String uploadPath)
    {
        super(); 
        this.homePath = homePath;
        this.uploadPath = uploadPath;
    }

    /**
     * 
     * @param fileItemFactory
     */
    public HttpFileUpload(String homePath, String uploadPath, FileItemFactory fileItemFactory)
    {
        super(fileItemFactory);   
        this.homePath = homePath;
        this.uploadPath = uploadPath;
    }
    
    /**
     * 获得表单字段的值
     * @param fieldName 字段名称
     * @return 字段值
     */
    public String getParameter(String fieldName)
    {
    	return (String)this.parameterMap.get(fieldName);    	
    }

    /**
     * 解析配置大小
     * @param size 文件上传大小
     * @return
     */
    public static long getByteSize(String size)
    {
        String unit = size.substring(size.length() - 1).toUpperCase();
        String num;
        if (unit.equals("K"))
        {
            num = size.substring(0, size.length() - 1);
            return Long.parseLong(num) * HttpFileUpload.K;
        }
        else if (unit.equals("M"))
        {
            num = size.substring(0, size.length() - 1);
            return Long.parseLong(num) * HttpFileUpload.M;
        }
        else
        {
            return Long.parseLong(size);
        }
    }

    /**
     * @return [String] 允许上传的文件类型
     */
    public String getAllowFileTypes()
    {
        return allowFileTypes;
    }

    /**
     * 设置允许上传的文件类型，默认是""，无限制
     * 
     * @param allowFileTypes
     */
    public void setAllowFileTypes(String allowFileTypes)
    {
        this.allowFileTypes = allowFileTypes;
    }

    public void addAllowFileTypes(String allowFileTypes)
    {
        this.allowFileTypes += ("," + allowFileTypes);
    }

    /**
     * 设置是否允许上载除file以外的field的内容
     * 
     * @param allow
     */
    public void setAllowField(boolean allow)
    {
        allowField = allow;
    }

    /**
     * @return [boolean] 是否允许上载除file以外的field
     */
    public boolean isAllowField()
    {
        return allowField;
    }

    /**
     * 设置最小报告长度，这个参数影响报告频度，默认是10K
     * 
     * @param reportLimitSize
     */
    public void setReportLimitSize(int reportLimitSize)
    {
        this.reportLimitSize = reportLimitSize;
    }

    public int getReportLimitSize()
    {
        return reportLimitSize;
    }

    /**
     * 上传后清理工作
     */
    public void dispose()
    {
      //TODO  需要增加清理函数
    }

    /**
     * 完成上传操作 根据allowField值决定是否接收除file以外的其它field，并检查上传的文件是否为允许的文件
     * 
     * @param request
     * @return
     * @throws FileUploadException
     * @throws IOException
     */
    public List parseRequest(HttpServletRequest request)
            throws FileUploadException
    {    	
        ServletRequestContext ctx = new ServletRequestContext(request);
        if (ctx == null)
        {
            throw new NullPointerException(
                "HttpFileUpload(parseRequestEx): ctx parameter");
        }   
        int requestSize = ctx.getContentLength();        
        long sizeMax = getSizeMax();
        if (sizeMax >= 0 && requestSize > sizeMax)
        {
            throw new SizeLimitExceededException("由于文件总大小 [ " + requestSize
                    + " ] 超出限定 [ " + sizeMax + " ]，上传被终止！", requestSize,
                sizeMax);
        }                 	
           
        List /* FileItem */ items = super.parseRequest(request); 
        
        FileItem item = null;           
        for(int i=0;i<items.size();i++)	//递归处理上传元素
        {
        	item = (FileItem)items.get(i);
        	if (item.isFormField()) // 非文件域
            {
        		String name = item.getFieldName();	
        	    String value = getEncodedString(item);
        	    parameterMap.put(name, value);
            }
            else	 // 文件域                       
            {
            	tempFileItem.add(item);            	
            }
        }
        //
        return items;  
    }
    
    /**
     * 完成上传操作 根据allowField值决定是否接收除file以外的其它field，并检查上传的文件是否为允许的文件
     * 
     * @param request
     * @return
     * @throws FileUploadException
     * @throws IOException
     */
    public void upload() throws FileUploadException
    {    	
    	FileItem item = null;
        for(int i=0;i<this.tempFileItem.size();i++)	//递归处理上传元素
        {
        	item = this.tempFileItem.get(i);
        	String fieldName = item.getFieldName();
            String fileName = item.getName();	//这里为全路径名
            String fileSimpleName = Utility.getSimpleFileName(fileName);	//纯文件名                      
            String fileSuffix = Utility.getFileNameSuffix(fileName);	//后缀名
            String fileContentType = item.getContentType();
            boolean isInMemory = item.isInMemory();
            if(Utility.isEmpty(fileName) || item.getSize()<=0)
            	continue;
           
            long sizeInBytes = item.getSize();                  
           
            if (!isAllowFileType(fileContentType)) // 不属于允许上传的文件类型
            {
            	invalidFileNames.add(fileSimpleName);  
            	throw new InvalidFileUploadException(
                        "非法的文件上传类型 [ " + fileContentType + " ]",
                        invalidFileNames);
            }                   
            if (sizeInBytes > this.fileLimitSize)
            {
            	invalidFileNames.add(fileSimpleName);
                throw new InvalidFileUploadException("[ "
                        + fileSimpleName + " ]超过单个文件大小限制，文件大小[ "
                        + sizeInBytes + " ]，限制为[ "
                        + this.fileLimitSize + " ] ",
                        invalidFileNames);
            }   
            //写文件   
            String actualFileName;
            if(this.isAutoRename)
            	actualFileName = FileUtils.currentToFileName() + "." + fileSuffix;	//实际保存文件名
            else
            	actualFileName = fileSimpleName;
            if(logger.isDebugEnabled())
            {
            	logger.debug("文件保存路径："+uploadPath+"，文件名："+actualFileName);
            }
            String finalPath = homePath + File.separator + uploadPath;
            FileUtils.createDirectory(new File(finalPath));
            File uploadedFile  = new File(finalPath, actualFileName);  
            
            try
			{
				item.write(uploadedFile);
				UploadFile uploadFile = new UploadFile();
				uploadFile.setFieldName(fieldName);
				uploadFile.setSrcFileName(fileSimpleName);	//原文件名
				uploadFile.setFileSuffix(fileSuffix);		//文件后缀名
				uploadFile.setFileName(actualFileName);		//现文件名					
				uploadFile.setFilePath(finalPath);			//绝对路径
				uploadFile.setMimeType(fileContentType);
				uploadFile.setFileSize(sizeInBytes);
				succeedFiles.add(uploadFile);
			}
			catch (Exception e)
			{
				logger.error("文件写入失败！", e);
				failedFileNames.add(fileSimpleName);
			}
			finally
			{
				item.delete();	//清理临时文件
			}
        }
    }

	/**
	 * @param item
	 * @return
	 */
	private String getEncodedString(FileItem item)
	{
		String value = null;
		try
		{
			value = item.getString(this.getHeaderEncoding());
		}
		catch (UnsupportedEncodingException e)
		{
			
		}
		return value;
	}

    /**
     * 文件类型是否允许上传
     * @param fileType 文件类型
     * @return
     */
    private boolean isAllowFileType(String fileType)
    {
    	if(allowFileTypes == null || Utility.isEmpty(allowFileTypes))
    		return true;
    	else if (allowFileTypes.length() > 0 && !Utility.isEmpty(fileType))
            return allowFileTypes.indexOf(fileType.toLowerCase()) != -1;
        else
            return false;
    }

    /**
     * 
     * 
     * 无效的文件上传异常.
     * 
     * <pre><b>描述：</b>
     *    无效的文件上传异常 
     * </pre>
     * 
     * <pre><b>修改记录：</b>
     *    
     * </pre>
     * 
     * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
     * @since 2009-4-17
     * @version 1.0
     *
     */
    public class InvalidFileUploadException extends FileUploadException
    {
        private static final long serialVersionUID = 5458085280561303071L;
        
        /** 无效的文件列表. */
        private List invalidFileList;

        public List getInvalidFileList()
        {
            return invalidFileList;
        }

        public InvalidFileUploadException(List list)
        {
            invalidFileList = list;
        }

        public InvalidFileUploadException(String message, List list)
        {
            super(message);
            invalidFileList = list;
        }
    }
    

    public long getFileLimitSize()
    {
        return fileLimitSize;
    }

    public void setFileLimitSize(long fileLimitSize)
    {
        this.fileLimitSize = fileLimitSize;
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
	 * @return the failedFileNames
	 */
	public List getFailedFileNames()
	{
		return failedFileNames;
	}

	/**
	 * @param failedFileNames the failedFileNames to set
	 */
	public void setFailedFileNames(List failedFileNames)
	{
		this.failedFileNames = failedFileNames;
	}

	/**
	 * @return the fieldNameMap
	 */
	public Map getParameterMap()
	{
		return parameterMap;
	}


	/**
	 * @return the invalidFileNames
	 */
	public List getInvalidFileNames()
	{
		return invalidFileNames;
	}

	/**
	 * @return the succeedFiles
	 */
	public List<UploadFile> getSucceedFiles()
	{
		return succeedFiles;
	}

	/**
	 * @return the isAutoRename
	 */
	public boolean isAutoRename()
	{
		return isAutoRename;
	}

	/**
	 * @param isAutoRename the isAutoRename to set
	 */
	public void setAutoRename(boolean isAutoRename)
	{
		this.isAutoRename = isAutoRename;
	}

	/**
	 * @return the homePath.
	 */
	public String getHomePath()
	{
		return homePath;
	}

}
