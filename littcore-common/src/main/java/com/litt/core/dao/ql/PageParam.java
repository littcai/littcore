package com.litt.core.dao.ql;

import java.util.Map;

import com.litt.core.common.CoreConstants;

/**
 * 
 * 
 * 分页参数对象.
 * 
 * <pre><b>描述：</b>
 *    封装了分页参数、查询条件、排序。避免方法参数传递时设置过多参数，避免重构时大量代码的更改
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2013-07-23 1、排序条件合入CondParam处理
 *    2009-12-23 1、增加CondParam用于统一管理查询条件，PageParam继承于该类
 *    			 2、屏蔽了底层存储实现，避免今后结构更改导致上层代码的更改
 *    2008-11-19 1、增加直接获取类型参数的方法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2007-4-11
 * @version 1.0
 * 
 * @since 2008-11-19
 * @version 1.1
 *
 */
public class PageParam extends CondParam
{
    private int totalPage = 0; // 总的页面数 
    private int pageIndex = 1; // 当前的页码
    private int pageSize = CoreConstants.DEFAULT_PAGE_SIZE; // 一页显示的记录数，如果为0则对应全部结果
    private int totalSize = 0; //总记录数  
    
    /**
     * 默认构造函数（查询全部）.
     */
    public PageParam()
    {
    	pageIndex = 0;
    	pageSize = 0;
    }
    
    
    /**
     * 简单查询的构造函数.
     * 
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     */
    public PageParam(int pageIndex,int pageSize)
    {
    	if(pageIndex>0)
    		this.pageIndex = pageIndex;
    	if(pageSize>0)
    		this.pageSize = pageSize;
    }
 
    
    /**
     * 简单查询的构造函数.
     * 
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     * @param sortField 排序字段
     * @param sortOrder 排序顺序
     */
    public PageParam(int pageIndex, int pageSize, String sortField, String sortOrder)
    {
        this(pageIndex, pageSize);        
        super.addSort(sortField, sortOrder);
    }

    /**
     * 简单查询的构造函数.
     * 
     * @param condMap 查询条件Map
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     * @param sortField 排序字段
     * @param sortOrder 排序顺序
     */
    public PageParam(Map<String, Object> condMap, int pageIndex, int pageSize, String sortField, String sortOrder)
    {    	
    	this(pageIndex, pageSize, sortField, sortOrder);
    	super.addAllCond(condMap);
    }
    
    /**
     * 简单查询的构造函数.
     * 
     * @param condMap 查询条件Map
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     */
    public PageParam(Map<String, Object> condMap,int pageIndex,int pageSize)
    {
    	this(pageIndex, pageSize);
    	super.addAllCond(condMap);     
    }    
    
    /**
     * Instantiates a new page param.
     *
     * @param condMap the cond map
     */
    public PageParam(Map<String, Object> condMap){
		super(condMap);
	}
	
	/**
	 * Instantiates a new page param.
	 *
	 * @param condMap the cond map
	 * @param sortMap the sort map
	 */
	public PageParam(Map<String, Object> condMap, Map<String, String> sortMap){
		super(condMap, sortMap);
	}
    
    /**
     * @return the pageIndex
     */
    public int getPageIndex()
    {
        return pageIndex;
    }
    /**
     * @param pageIndex the pageIndex to set
     */
    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }
    /**
     * @return the pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }
    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
    /**
     * @return the totalPage
     */
    public int getTotalPage()
    {
        return totalPage;
    }
    /**
     * @param totalPage the totalPage to set
     */
    public void setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
    }
    /**
     * @return the totalSize
     */
    public int getTotalSize()
    {
        return totalSize;
    }
    /**
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }

}
