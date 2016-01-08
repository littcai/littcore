package com.littcore.web.mvc.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.littcore.common.BeanManager;
import com.littcore.common.CoreConstants;
import com.littcore.common.Utility;
import com.littcore.exception.BusiCodeException;
import com.littcore.exception.BusiException;
import com.littcore.exception.NotLoginException;
import com.littcore.pojo.ISystemInfoVo;
import com.littcore.shield.security.SecurityContext;
import com.littcore.shield.security.SecurityContextHolder;
import com.littcore.shield.vo.ILoginVo;

/**
 * Web控制器基类.
 * 
 * <pre><b>描述：</b>
 * 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * Date:2013-07-18
 * 	1、增加获取国际化内容的方法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-3-24
 * @version 1.0
 * 
 * @since 2013-07-18
 * @version 2.0
 */
public class BaseController
{
	/** 日志. */
	protected Log logger = LogFactory.getLog(this.getClass());

	/** JSON 内容类型. */
	public static final String CONTENT_TYPE_JSON = "text/javascript;charset=UTF-8";

	/** XML 内容类型. */
	public static final String CONTENT_TYPE_XML = "application/xml;charset=UTF-8";

	/** HTML 内容类型. */
	public static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

	/** 二进制文件下载. */
	public static final String CONTENT_TYPE_BINARY = "application/x-download,charset=utf-8";
	

	/**
	 * 获得项目运行根目录的实际路径.
	 * 
	 * @return String 路径
	 */
	protected String getRootPath()
	{
		String rootPath = Utility.getRootPath();
		if(Utility.isEmpty(rootPath))
			rootPath = BeanManager.getServletContext().getRealPath("/");
		return rootPath;
	}
	
	/**
	 * 获得项目上传文件根目录的实际路径.
	 * 
	 * @return String 路径
	 */
	protected String getHomePath()
	{
		return this.getSystemInfoVo().getHomePath();
	}	
	
	/**
	 * 获得HTTP访问上下文路径.
	 * 
	 * @param request the request
	 * 
	 * @return the context path
	 */
	protected String getContextPath(HttpServletRequest request)
	{
		return request.getContextPath();
	}	
	
	/**
	 * 获得HTTP访问基准路径.
	 * 
	 * @param request the request
	 * 
	 * @return the base path
	 */
	protected String getBasePath(HttpServletRequest request)
	{
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + this.getContextPath(request);
	}
	
	/**
	 * 获得国际化内容.
	 *
	 * @param key 属性key
	 * @param locale 方言
	 * @return 国际化后内容
	 */
	public String getMessage(String key, Locale locale)
	{
		return BeanManager.getMessage(key, null, locale);
	}
	
	/**
	 * 获得国际化内容.
	 *
	 * @param key 属性key
	 * @param params 动态参数
	 * @param locale 方言
	 * @return 国际化后内容
	 */
	public String getMessage(String key, Object[] params, Locale locale)
	{
		return BeanManager.getMessage(key, params, locale);
	}
	
	/**
	 * 输出JSON数据.
	 * @param response HTTP响应
	 * @param json JSON数据
	 */
	protected final void responseJSON(HttpServletResponse response, String json)
	{
		response.setContentType(CONTENT_TYPE_HTML);
		try
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("response output:" + json);
			}
			response.getWriter().write(json);
		}
		catch (IOException e)
		{
			logger.error("response JSON error!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 输出错误JSON信息.
	 * @param response HTTP响应
	 * @param json JSON数据
	 */
	protected final void responseErrorJSON(HttpServletResponse response, String json)
	{
		responseErrorJSON(response, HttpServletResponse.SC_BAD_REQUEST, json);
	}

	/**
	 * 输出错误JSON.
	 * 
	 * @param response HTTP响应
	 * @param errorId 错误代号
	 * @param json JSON数据

	 */
	protected final void responseErrorJSON(HttpServletResponse response, int errorId, String json)
	{
		response.setStatus(errorId);
		responseJSON(response, json);
	}

	/**
	 * 输出错误信息
	 * @param response HTTP响应
	 * @param errorId 错误代号
	 * @param errorMsg
	 */
	protected final void sendError(HttpServletResponse response, int errorId, String errorMsg)
	{
		response.setContentType(CONTENT_TYPE_HTML);
		try
		{
			response.sendError(errorId, errorMsg);
		}
		catch (IOException e)
		{
			logger.error("response errorMessage error!" ,e);			
			throw new RuntimeException(e);
		}
	}

	/**
	 * 输出二进制流内容
	 * @param response HTTP响应
	 * @param contentType
	 * @param data
	 */
	protected final void responseBinaryStream(HttpServletResponse response, String contentType, InputStream input)
	{
		OutputStream output = null;
		try
		{
			output = response.getOutputStream();
			response.setContentType(contentType);
			int length = FileCopyUtils.copy(input, output);
			response.setContentLength(length);
			output.flush();
		}
		catch (IOException e)
		{
			logger.error("response BinaryStream error!", e);
			throw new RuntimeException(e);
		}
		finally
		{
			if(output!=null)
				IOUtils.closeQuietly(output);
		}
	}
	
	/**
	 * 设置文件参数下载.
	 * 输出由应用代码直接获取response.getOutputStream();进行输出
	 * 
	 * @param response 响应
	 * @param fileName 文件名
	 * @param data 文件二进制字节流
	 */
	protected final void setDownload(HttpServletResponse response, String fileName)
	{
		try
		{
			fileName = URLEncoder.encode(fileName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Encode FileName Error:", e);
			throw new RuntimeException(e);
		}

		String content = "attachment; filename=" + fileName;
		response.addHeader("Content-Disposition", content);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/octet-stream");
	}	

	/**
	 * 生成文件下载.
	 * 
	 * @param response 响应
	 * @param fileName 文件名
	 * @param data 文件二进制字节流
	 */
	protected final void download(HttpServletResponse response, String fileName, byte[] data)
	{
		try
		{
			fileName = URLEncoder.encode(fileName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Encode FileName Error:", e);
			throw new RuntimeException(e);
		}

		String content = "attachment; filename=" + fileName;
		response.addHeader("Content-Disposition", content);
		response.setCharacterEncoding("utf-8");
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		responseBinaryStream(response, "application/octet-stream", stream);
	}

	/**
	 * 生成文件下载.
	 * 
	 * @param response 响应
	 * @param fileName 文件名
	 * @param file 文件
	 */
	protected final void download(HttpServletResponse response, String fileName, File file)
	{
		try
		{
			fileName = URLEncoder.encode(fileName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Encode FileName Error:", e);
			throw new RuntimeException(e);
		}
		response.reset();
		String content = "attachment; filename=" + fileName;
		response.addHeader("Content-Disposition", content);
		response.setCharacterEncoding("utf-8");
		InputStream stream = null;
		try
		{
			stream = new FileInputStream(file);
			responseBinaryStream(response, "application/octet-stream", stream);			
		}
		catch (FileNotFoundException e)
		{
			logger.error("FileNotFoundException for " + file.getAbsolutePath());
			throw new RuntimeException(e);
		}		
		finally
		{
			IOUtils.closeQuietly(stream);
		}
	}
	
	/**
	 * 生成文件下载.
	 * 
	 * @param response 响应
	 * @param file 文件
	 */
	protected final void download(HttpServletResponse response, File file)
	{
		String fileName = file.getName();
		this.download(response, fileName, file);
	}
	
	/**
	 * Download.
	 *
	 * @param file the file
	 * @return the response entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ResponseEntity<byte[]> download(File file) throws IOException
	{
		return this.download(file, file.getName());
	}
	
	/**
	 * Download.
	 * Based on SpringMVC, HttpServletResponse is useless
	 * Attention: This function depend on [org.springframework.http.converter.ByteArrayHttpMessageConverter].
	 *    and this converter must be defined before jsonConverter
	 *
	 * @param file the file
	 * @param newFileName the new file name
	 * @return the response entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ResponseEntity<byte[]> download(File file, String newFileName) throws IOException
	{
		HttpHeaders headers = new HttpHeaders();  
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  
	    headers.setContentDispositionFormData("attachment", newFileName);  
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),  
	                                      headers, HttpStatus.CREATED);  
	}
	
	/**
	 * 从Servlet容器中中获取系统配置信息.
	 * 该信息通常在系统初始化时注入Servlet容器
	 * @return [ISystemInfoVo] 系统配置信息对象
	 */
	protected ISystemInfoVo getSystemInfoVo()
	{
		return (ISystemInfoVo)BeanManager.getServletContext().getAttribute(CoreConstants.APP_SYSTEMINFO);		
	}	
	
	/**
	 * 是否有登陆用户信息.
	 * 检查SESSION中是否有SESSION_OPER对象
	 * 
	 * @return true, if checks if is login
	 */
	protected boolean isLogin()
	{
		SecurityContext context = SecurityContextHolder.getContext();
		if(context == null)
			return false;
		ILoginVo loginVo = context.getLoginVo();
		if(loginVo==null)
			return false;
		
		return true;		
	}
	
	/**
	 * 是否超级管理员.
	 * @return
	 */
	protected boolean isSuperAdmin()
	{
		if(!this.isLogin())
			return false;
		try
		{
			return this.getLoginVo().isAdministrator();
		}
		catch (NotLoginException e)
		{
			return false;
		}
	}
	
	/**
	 * 从请求的SESSION中获取登陆操作员的信息
	 * @return LoginPo登陆操作员对象
	 */
	protected ILoginVo getLoginVo() throws NotLoginException
	{
		SecurityContext context = SecurityContextHolder.getContext();
		if(context == null)
		{
			throw new NotLoginException("用户尚未登录或登录已超时！");
		}
		ILoginVo loginVo = context.getLoginVo();
		if(loginVo==null)
		{
			throw new NotLoginException("用户尚未登录或登录已超时！");
		}
		return loginVo;
	}
	
	/**
	 * 获得登录操作员id
	 * @return opId
	 */
	protected Long getLoginOpId() throws NotLoginException
	{
		ILoginVo loginVo = getLoginVo();
		if(loginVo!=null)
			return loginVo.getOpId();
		else
			return null;
	}
	
	/**
	 * 获得登录操作员登录ID
	 * @return LoginId
	 */
	protected String getLoginId() throws NotLoginException
	{
		ILoginVo loginVo = getLoginVo();
		if(loginVo!=null)
			return loginVo.getLoginId();
		else
			return null;
	}
	
	/**
	 * 获得登录操作员登录IP
	 * @return LoginId
	 */
	protected String getLoginIp() throws NotLoginException
	{
		ILoginVo loginVo = getLoginVo();
		if(loginVo!=null)
			return loginVo.getLoginIp();
		else
			return null;
	}	
	
	/**
	 * 基于Spring的异常处理.
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)  
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)  
    public ModelAndView handleException(Exception e, HttpServletRequest request) {  
        return new ModelAndView().addObject("error", "系统未知错误，请联系研发部！");  
    }  
    
	/**
	 * 基于Spring的异常处理.
	 * @param e
	 * @param request
	 * @return
	 */
    @ExceptionHandler(BusiException.class)  
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)  
    public ModelAndView handleBusiException(BusiException e, HttpServletRequest request) {  
        return new ModelAndView().addObject("error", e.getMessage());  
    }  
    
    /**
     * 基于Spring的异常处理.
     *
     * @param e the e
     * @param locale 方言
     * @return the model and view
     */
    @ExceptionHandler(BusiCodeException.class)  
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)  
    public ModelAndView handleBusiCodeException(BusiCodeException e, Locale locale) {  
        return new ModelAndView().addObject("error", BeanManager.getMessage("error."+e.getErrorCode(), e.getParams(), locale));  
    }  
    
    
}
