package com.litt.core.dao.ql;

import com.litt.core.common.CoreConstants;

/**

 * <b>Title:</b> 分页参数对象.
 * <pre><b>Description:</b> 
 *   封装了分页参数、查询条件、排序。避免方法参数传递时设置过多参数，避免重构时大量代码的更改
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2007-12-16
 * @version 1.0
 *
 */
public class PagePoParam
{
    private int totalPage = 0; // 总的页面数 
    private int pageIndex = 1; // 当前的页码
    private int pageSize = CoreConstants.DEFAULT_PAGE_SIZE; // 一页显示的记录数，如果为0则对应全部结果
    private int totalSize = 0; //总记录数
    
    private Object conds; //查询条件(表单VO对象)  
    private String sortIndex;   //排序条件
    private String sortOrder;   //排序顺序
    
    public PagePoParam(){}
    
    /**
     * 简单查询的构造函数.
     * 
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     */
    public PagePoParam(int pageIndex,int pageSize)
    {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
 
    
    /**
     * 简单查询的构造函数.
     * 
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     * @param sortIndex 排序字段
     * @param sortOrder 排序顺序
     */
    public PagePoParam(int pageIndex,int pageSize,String sortIndex, String sortOrder)
    {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortIndex = sortIndex;
        this.sortOrder = sortOrder;
    }
    
    /**
     * 简单查询的构造函数.
     * 
     * @param conds 查询条件对象
     */
    public PagePoParam(Object conds)
    {
        this.conds = conds;      
    }  
    
    /**
     * 简单查询的构造函数.
     * 
     * @param conds 查询条件Map
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     */
    public PagePoParam(Object conds,int pageIndex,int pageSize)
    {
        this.conds = conds;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;        
    }     

    /**
     * 简单查询的构造函数.
     * 
     * @param conds 查询条件Map
     * @param pageIndex 当前的页码
     * @param pageSize 一页显示的记录数
     * @param sortIndex 排序字段
     * @param sortOrder 排序顺序
     */
    public PagePoParam(Object conds,int pageIndex,int pageSize,String sortIndex, String sortOrder)
    {
        this.conds = conds;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortIndex = sortIndex;
        this.sortOrder = sortOrder;
    }
    
   
    
    
    /**
     * 是否有排序字段
     * @return boolean
     */
    public boolean hasSort()
    {
        return !(sortIndex==null||sortIndex.trim().equals(""));
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

    /**
     * @return the sort
     */
    public String getSortIndex()
    {
        return sortIndex;
    }
    /**
     * @param sort the sort to set
     */
    public void setSortIndex(String sort)
    {
        this.sortIndex = sort;
    }

    /**
     * @return the conds
     */
    public Object getConds()
    {
        return conds;
    }

    /**
     * @param conds the conds to set
     */
    public void setConds(Object conds)
    {
        this.conds = conds;
    }

    
}
