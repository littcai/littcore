package com.litt.core.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.net.HttpHeaders;
import com.litt.core.common.CoreConstants;
import com.litt.core.common.Utility;
import com.litt.core.dao.ql.PageParam;
import com.litt.core.util.ArrayUtils;
import com.litt.core.util.StringUtils;
import com.litt.core.util.ValidateUtils;

/** 
 * 
 * Web辅助函数.
 * 
 * <pre><b>描述：</b>
 *    封装实现了常用的功能 
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2013-02-21 增加将查询参数Map设置到响应中的方法.
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-9
 * @version 1.0
 *
 */
public class WebUtils
{
	/** 日志. */
	public Log logger = LogFactory.getLog(this.getClass());

	/** JSON 内容类型. */
	public static final String CONTENT_TYPE_JSON = "text/javascript;charset=UTF-8";

	/** XML 内容类型. */
	public static final String CONTENT_TYPE_XML = "application/xml;charset=UTF-8";

	/** HTML 内容类型. */
	public static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

	/** 二进制文件下载. */
	public static final String CONTENT_TYPE_BINARY = "application/x-download,charset=utf-8";
	
	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * 
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * 
	 * @param etag 内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	/**
	 * 获取参数数组.
	 * 参数值为用逗号分隔的字符串
	 *
	 * @param request the request
	 * @param name the name
	 * @return the parameter value array
	 */
	public static Integer[] getSimpleParameterValueArray(WebRequest request, String name)
	{
	  String value = request.getParameter(name);
	  if(StringUtils.isEmpty(value))
	    return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
	  return ArrayUtils.toInteger(StringUtils.split(value, ','));
	}
	
	/**
   * 获取参数数组.
   * 参数值为用逗号分隔的字符串
   *
   * @param request the request
   * @param name the name
   * @return the parameter value array
   */
  public static Integer[] getParameterValueArray(WebRequest request, String name)
  {
    String[] values = request.getParameterValues(name);
    if(values==null || values.length==0)
      return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
    return ArrayUtils.toInteger(values);
  }

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(WebRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Iterator<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(Map<String, Object> request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Iterator<Entry<String, Object>> iter = request.entrySet().iterator();
		
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String paramName = (String)entry.getKey();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				Object value = entry.getValue();
				if (value == null) {
					// Do nothing, no values found at all.
				} else {
					params.put(unprefixed, value);
				}
			}
		}
		return params;
	}
	
	/**
	 * 取得简单的参数集合.
	 * 注意：不包含相同名称参数的数组
	 *
	 * @param request the request
	 * @param prefix the prefix
	 * @return the simple parameters starting with
	 */
	public static Map<String, String> getSimpleParametersStartingWith(WebRequest request, String prefix) {
    Validate.notNull(request, "Request must not be null");
    Iterator<String> paramNames = request.getParameterNames();
    Map<String, String> params = new TreeMap<String, String>();
    if (prefix == null) {
      prefix = "";
    }
    while (paramNames != null && paramNames.hasNext()) {
      String paramName = (String) paramNames.next();
      if ("".equals(prefix) || paramName.startsWith(prefix)) {
        String unprefixed = paramName.substring(prefix.length());
        String[] values = request.getParameterValues(paramName);
        if (values == null || values.length == 0) {
          // Do nothing, no values found at all.
        } else if (values.length > 1) {
          params.put(unprefixed, values[0]);
        } else {
          params.put(unprefixed, values[0]);
        }
      }
    }
    return params;
  }
	
	/**
	 * 将查询参数设置到响应的请求中.
	 * @param parameters 参数映射
	 * @param request 请求对象
	 */
	public static void setParameters(Map<String, Object> parameters, ServletRequest request)
	{
		Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String, Object> entry = iter.next();
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 将查询参数设置到响应的请求中.
	 * @param parameters 参数映射
	 * @param request 请求对象
	 */
	public static void setParameters(Map<String, Object> parameters, WebRequest request)
	{
		Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String, Object> entry = iter.next();
			request.setAttribute(entry.getKey(), entry.getValue(), WebRequest.SCOPE_REQUEST);
		}
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
	 * 
	 * @see #getParametersStartingWith
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if (params == null || params.size() == 0) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
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
 	 * 获得客户端真实IP地址.
 	 * 
 	 * @param request the request
 	 * 
 	 * @return the remote ip
 	 */
     public static String getRemoteIp(HttpServletRequest request) 
     {
       String ip = request.getHeader("X-Forwarded-For");
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("Proxy-Client-IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("HTTP_CLIENT_IP");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("HTTP_X_FORWARDED_FOR");
       }
       if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getRemoteAddr();
       }
       if(StringUtils.contains(ip, ","))  //如果存在多个反向代理，获得的IP是一个用逗号分隔的IP集合，取第一个
       {
         ip = StringUtils.substringBefore(ip, ",");
       }
       return ip;
     }
     
     /**
 	 * 输出二进制流内容
 	 * @param response HTTP响应
 	 * @param contentType
 	 * @param data
 	 */
 	public final void responseBinaryStream(HttpServletResponse response, String contentType, InputStream input)
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
 	 * 生成文件下载.
 	 * 
 	 * @param response 响应
 	 * @param fileName 文件名
 	 * @param data 文件二进制字节流
 	 */
 	public final void download(HttpServletResponse response, String fileName, byte[] data)
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
 	public final void download(HttpServletResponse response, String fileName, File file)
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
 	public final void download(HttpServletResponse response, File file)
 	{
 		String fileName = file.getName();
 		this.download(response, fileName, file);
 	}
 	
 	/**
	  * 获得分页参数.
	  * 当有前缀时，分页参数截掉前缀即为实际数值
	  *
	  * @param request 请求对象
	  * @return PageParam
	  */
	public static PageParam getPageParam(HttpServletRequest request)
	{
		return getPageParam(request, null);
	}
	
	/**
	  * 获得分页参数.
	  * 当有前缀时，分页参数截掉前缀即为实际数值
	  *
	  * @param request 请求对象
	  * @return PageParam
	  */
	public static PageParam getPageParam(WebRequest request)
	{
		return getPageParam(request, null);
	}
 	
 	/**
	  * 获得分页参数.
	  * 当有前缀时，分页参数截掉前缀即为实际数值
	  *
	  * @param request 请求对象
	  * @param prefix 参数前缀
	  * @return PageParam
	  */
	public static PageParam getPageParam(HttpServletRequest request, String prefix)
 	{		
		 int pageIndex = 1;
		 int pageSize = CoreConstants.DEFAULT_PAGE_SIZE;
		 String sortField = null;
		 String sortOrder = null;
		 
		 if(ValidateUtils.isEmpty(prefix))
		 {
			 pageIndex = Utility.parseInt(request.getParameter("pageIndex"), 1);
			 pageSize = Utility.parseInt(request.getParameter("pageSize"), CoreConstants.DEFAULT_PAGE_SIZE);
			 sortField = request.getParameter("sortField");
			 sortOrder = request.getParameter("sortOrder");
		 }
		 else 
		 {
			 Map<String, Object> paramMap = WebUtils.getParametersStartingWith(request, prefix);
			 
			 pageIndex = Utility.parseInt((String)paramMap.get("pageIndex"), 1);
			 pageSize = Utility.parseInt((String)paramMap.get("pageSize"), CoreConstants.DEFAULT_PAGE_SIZE);
			 sortField = (String)paramMap.get("sortField");
			 sortOrder = (String)paramMap.get("sortOrder");			
		 }
		 
		 PageParam param = new PageParam(pageIndex, pageSize, sortField, sortOrder);
		 return param;
 	}
	
	/**
	  * 获得分页参数.
	  * 当有前缀时，分页参数截掉前缀即为实际数值
	  *
	  * @param request 请求对象
	  * @param prefix 参数前缀
	  * @return PageParam
	  */
	public static PageParam getPageParam(WebRequest request, String prefix)
	{		
		 int pageIndex = 1;
		 int pageSize = CoreConstants.DEFAULT_PAGE_SIZE;
		 String sortField = null;
		 String sortOrder = null;
		 
		 if(ValidateUtils.isEmpty(prefix))
		 {
			 pageIndex = Utility.parseInt(request.getParameter("pageIndex"), 1);
			 pageSize = Utility.parseInt(request.getParameter("pageSize"), CoreConstants.DEFAULT_PAGE_SIZE);
			 sortField = request.getParameter("sortField");
			 sortOrder = request.getParameter("sortOrder");
		 }
		 else 
		 {
			 Map<String, Object> paramMap = WebUtils.getParametersStartingWith(request, prefix);
			 
			 pageIndex = Utility.parseInt((String)paramMap.get("pageIndex"), 1);
			 pageSize = Utility.parseInt((String)paramMap.get("pageSize"), CoreConstants.DEFAULT_PAGE_SIZE);
			 sortField = (String)paramMap.get("sortField");
			 sortOrder = (String)paramMap.get("sortOrder");			
		 }
		 
		 PageParam param = new PageParam(pageIndex, pageSize, sortField, sortOrder);
		 return param;
	}
 	
	/**
	 * Gets the locale.
	 * 如果没有Locale，则取浏览器的Locale
	 *
	 * @param request the request
	 * @return the locale
	 */
	public static Locale getLocale(HttpServletRequest request) 
	{
		Locale locale = request.getLocale();
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver != null) {
			locale = localeResolver.resolveLocale(request);
		}	
		return locale==null?Locale.getDefault():locale;
	}	
	
}
