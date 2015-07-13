package com.littcore.web.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.springframework.web.context.request.WebRequest;

import com.littcore.common.CoreConstants;
import com.littcore.common.Utility;
import com.littcore.dao.ql.CondParam;
import com.littcore.dao.ql.PageParam;
import com.littcore.util.StringUtils;
import com.littcore.web.util.WebUtils;

/**
 * 动态查询过滤器.
 * 
 * <pre><b>描述：</b>
 *    根据页面输入动态产生查询条件
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2012-12-21
 * @version 1.0
 */
public class QueryFilter {
	
	public static final String DEFAULT_QUERY_PREFIX = "q_";
	
	public static final String DEFAULT_SORT_PREFIX = "s_";
	
	/** 查询条件前缀. */
	private String queryPrefix = DEFAULT_QUERY_PREFIX;
	
	/** 排序条件前缀. */
	private String sortPrefix = DEFAULT_SORT_PREFIX;
	
	/** 查询条件. */
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private List<Sort> sorts = new ArrayList<QueryFilter.Sort>();
	
	/** 当前的页码. */
	private int pageIndex = 1;
	
	/** 一页显示的记录数，如果为0则对应全部结果. */
	private int pageSize = CoreConstants.DEFAULT_PAGE_SIZE; 
	
	/** 地域. */
	private Locale locale;
	
	/**
	 * 从request中获取查询条件，构造查询过滤器.
	 *
	 * @param request HttpServletRequest
	 */
	public QueryFilter(ServletRequest request)
	{
		Map<String, Object> paramMap = WebUtils.getParametersStartingWith(request, queryPrefix);
		Map<String, Object> sortMap = WebUtils.getParametersStartingWith(request, sortPrefix);
		this.extractQueryParam(paramMap);
		this.extractSort(sortMap);		
	}	
	
	/**
	 * 从request中获取查询条件，构造查询过滤器.
	 *
	 * @param request WebRequest
	 */
	public QueryFilter(WebRequest request)
	{
		Map<String, Object> paramMap = WebUtils.getParametersStartingWith(request, queryPrefix);
		Map<String, Object> sortMap = WebUtils.getParametersStartingWith(request, sortPrefix);
		this.extractQueryParam(paramMap);
		this.extractSort(sortMap);
	}	
	
	/**
	 * 从map中获取查询条件，构造查询过滤器.
	 * @param request Map型查询条件
	 */
	public QueryFilter(Map<String, Object> request){
		Map<String, Object> paramMap = WebUtils.getParametersStartingWith(request, queryPrefix);
		Map<String, Object> sortMap = WebUtils.getParametersStartingWith(request, sortPrefix);		
		this.extractQueryParam(paramMap);
		this.extractSort(sortMap);	
	}
	
	/**
	 * 转换为CondParam.
	 *
	 * @return CondParam
	 */
	public CondParam toCondParam()
	{
		CondParam condParam = new CondParam(this.paramMap);
		return condParam;
	}
	
	/**
	 * 转换为CondParam.
	 *
	 * @return CondParam
	 */
	public PageParam toPageParam()
	{
		PageParam pageParam = new PageParam(this.paramMap);
		return pageParam;
	}

	/**
	 * 提取查询条件.
	 * @param request 原始查询参数
	 * @return QueryFilter
	 */
	private QueryFilter extractQueryParam(Map<String, Object> request) 
	{
		//解析参数
		for (Entry<String, Object> entry : request.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value==null || StringUtils.isBlank(value.toString())) {
				continue;
			}
			
			String filedName = key;
			String dataType = "String";
			
			if(StringUtils.contains(filedName, "_"))
			{
				// 拆分operator与filedAttribute
				String[] names = StringUtils.split(key, "_");
//				if (names.length < 2) {
//					throw new IllegalArgumentException(key + " is not a valid search filter name");
//				}
				filedName = names[0];
				if (names.length > 1) {
					dataType = names[names.length-1];
				}
				//根据数据类型转换数据值
				if("int".equals(dataType))
					value =  Utility.parseInt(value.toString());
				else if("date".equals(dataType))
					value =  Utility.parseDate(value.toString());
				else if("datetime".equals(dataType))
					value =  Utility.parseDateTime(value.toString());
				else if("boolean".equals(dataType))
					value =  Utility.parseBoolean(value.toString());				
			}
			// 构造QueryParam
			this.paramMap.put(filedName, value);
		}		
		return this;
	}
	
	/**
	 * 提取排序条件.
	 * @param request 原始查询参数
	 * @return QueryFilter
	 */
	private QueryFilter extractSort(Map<String, Object> request) 
	{
		//解析参数
		for (Entry<String, Object> entry : request.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value==null || StringUtils.isBlank((String) value)) {
				continue;
			}
			
			this.sorts.add(new Sort(key, value.toString()));
		}		
		return this;
	}
	
	private class Sort{
		private String field;
		private String order;
		
		public Sort(String field, String order) {
			super();
			this.field = field;
			this.order = order;
		}
		/**
		 * @return the field
		 */
		public String getField() {
			return field;
		}
		/**
		 * @param field the field to set
		 */
		public void setField(String field) {
			this.field = field;
		}
		/**
		 * @return the order
		 */
		public String getOrder() {
			return order;
		}
		/**
		 * @param order the order to set
		 */
		public void setOrder(String order) {
			this.order = order;
		}
		
	}

	/**
	 * @return the queryPrefix
	 */
	public String getQueryPrefix() {
		return queryPrefix;
	}

	/**
	 * @param prefix the queryPrefix to set
	 */
	public void setQueryPrefix(String queryPrefix) {
		this.queryPrefix = queryPrefix;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the filters
	 */
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the sortPrefix
	 */
	public String getSortPrefix() {
		return sortPrefix;
	}

	/**
	 * @param sortPrefix the sortPrefix to set
	 */
	public void setSortPrefix(String sortPrefix) {
		this.sortPrefix = sortPrefix;
	}

}
