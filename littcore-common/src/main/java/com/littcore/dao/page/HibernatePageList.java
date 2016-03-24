package com.littcore.dao.page;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.littcore.common.CoreConstants;
import com.littcore.dao.IResultsetTransformer;

/**
 * <b>标题：</b>Hibernate分页对象.
 * <pre><b>描述</b>
 *    基于Hibernate的分页对象，分页参数由统计HQL查询获得，查询HQL语句直接分页
 * </pre> 
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-4-1
 * @version 1.0
 */
public class HibernatePageList implements IPageList {

    private final static Log logger = LogFactory.getLog(HibernatePageList.class);
    
    private int totalPage = 0; // 总的页面数 
    private int pageIndex = 1; // 当前的页码
    private int pageSize = CoreConstants.DEFAULT_PAGE_SIZE; // 一页显示的记录数，如果为0则对应全部结果
    private int totalSize = 0; //总记录数
    
    private List rsList = null; //缓存的页面集结果集    
    
    private IResultsetTransformer resultsetTransformer;
 
    /**
     * 根据总的结果集数量计算页面数.
     *
     * @return 页数
     */
    private int countPages()
    {
        // 如果一页显示数小于等于0
        if (pageSize <= 0)
        {   
            totalPage = 1;
            pageSize = this.totalSize;
            return totalPage;
        }
        //计算总的页面数
        this.totalPage = this.totalSize / this.pageSize;
        if ((this.totalSize % this.pageSize) != 0)
            this.totalPage++; 
        
        return totalPage;
    }    
    
    /* (non-Javadoc)
     * @see com.littcore.dao.page.IPageList#isHasNext()
     */
    public boolean isHasNext()
    {
      return pageIndex < totalPage;
    }
    
    /**
     * @return 返回 pageIndex。
     */
    public int getPageIndex() {
        if(pageIndex<=0)
            pageIndex = 1;
        return pageIndex;
    }

    /**
     * @param pageIndex 设置 pageIndex。
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return 返回 pageSize。
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize 设置 pageSize。
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return 返回 rsList。
     */
    public List getRsList() {
        return rsList;
    }

    /**
     * @param rsList 设置 rsList。
     */
    public void setRsList(List rsList) {
    	if(resultsetTransformer==null)
    		this.rsList = rsList;
    	else 
    	{
    	  this.rsList = resultsetTransformer.transform(rsList);		
    	  //重新计算分页
        this.totalSize = rsList.size();
        this.countPages();
        //如果输入的页面超过最大页面则跳转到最后一页
        if(pageIndex>totalPage)
        {
            pageIndex = totalPage;
        }
    	}
    }

    /**
     * @return 返回 totalPage。
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage 设置 totalPage。
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return 返回 totalSize。
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     * @param totalSize 设置 totalSize。
     */
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        countPages(); //根据总结果集大小计算总的页面数        
    }

	/**
	 * @return the resultsetTransformer
	 */
	public IResultsetTransformer getResultsetTransformer() {
		return resultsetTransformer;
	}

	/**
	 * @param resultsetTransformer the resultsetTransformer to set
	 */
	public IPageList setResultsetTransformer(IResultsetTransformer resultsetTransformer) {
		this.resultsetTransformer = resultsetTransformer;
		this.rsList = resultsetTransformer.transform(rsList);
		//不用重新计算分页，否则由于之前已经分过页，这里的size就不对了
		//this.totalSize = rsList.size();
		//this.countPages();
		return this;
	}
}
