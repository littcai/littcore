package com.litt.core.dao.page;

import java.util.List;

import com.litt.core.dao.IResultsetTransformer;

/**
 * <b>标题：</b>分页对象通用接口.
 * <pre><b>描述</b>
 *    JdbcPageList、HibernatePageList的通用接口
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-4-1
 * @version 1.0
 */
public interface IPageList {

    /**
     * @return 返回 pageIndex。
     */
    public int getPageIndex();

    /**
     * @param pageIndex 设置 pageIndex。
     */
    public void setPageIndex(int pageIndex);

    /**
     * @return 返回 pageSize。
     */
    public int getPageSize();

    /**
     * @param pageSize 设置 pageSize。
     */
    public void setPageSize(int pageSize);

    /**
     * @return 返回 rsList。
     */
    public List getRsList();

    /**
     * @param rsList 设置 rsList。
     */
    public void setRsList(List rsList);

    /**
     * @return 返回 totalPage。
     */
    public int getTotalPage();

    /**
     * @param totalPage 设置 totalPage。
     */
    public void setTotalPage(int totalPage);

    /**
     * @return 返回 totalSize。
     */
    public int getTotalSize();

    /**
     * @param totalSize 设置 totalSize。
     */
    public void setTotalSize(int totalSize);
    
    public void setResultsetTransformer(IResultsetTransformer resultsetTransformer); 
    
    public IResultsetTransformer getResultsetTransformer();
}