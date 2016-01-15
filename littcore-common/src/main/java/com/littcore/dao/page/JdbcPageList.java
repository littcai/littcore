package com.littcore.dao.page;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.littcore.common.CoreConstants;
import com.littcore.dao.IResultsetTransformer;
/**
 * <b>标题：</b>Jdbc分页对象.
 * <pre><b>描述</b>
 *    基于Jdbc的分页对象，根据分页参数控制游标只取分页大小的结果
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-4-1
 * @version 1.0
 */
public class JdbcPageList implements IPageList {

    private final static Log logger = LogFactory.getLog(JdbcPageList.class);
    
    private int totalPage = 0; // 总的页面数 
    private int pageIndex = 1; // 当前的页码
    private int pageSize = CoreConstants.DEFAULT_PAGE_SIZE; // 一页显示的记录数，如果为0则对应全部结果
    private int totalSize = 0; //总记录数
    
    private List rsList = null; //缓存的页面集结果集    
    
    private IResultsetTransformer resultsetTransformer;
 
    /**
     * 缓存结果.
     * @param rs 结果集可以是JDBC的List<Map>型由于是一次获取所有结果所有直接取得总记录数
     */
    private void populate(List rs)
    {        
        totalSize = rs.size();
        countPages(); //根据总结果集大小计算总的页面数 
        rsList = new ArrayList();
        int iRow = 0;
        int rowStart = (pageIndex - 1) * pageSize; // 开始行数
        int rowEnd = rowStart + pageSize; // 结束行数
        for (int i = 0; i < rs.size(); i++)  //根据分页参数重新生成一个页面大小的List结果集
        {
            iRow++;
            if ((iRow > rowStart && iRow <= rowEnd) || pageSize <= 0) // 取当前页面大小结果||pageSize小于等于0获取全部结果
            {
                rsList.add(rs.get(i));
            }
        }
        if(resultsetTransformer!=null)    	
    		this.rsList = resultsetTransformer.transform(rsList);	
    } 
    
    /**
     * 缓存结果(大结果集时使用).
     * @param rs ResultSet
     */
    public void populate(ResultSet rs) throws SQLException
    {        
    	rsList = new ArrayList();
    	
    	ResultSetMetaData rsmd = rs.getMetaData();
    	int columnCount = rsmd.getColumnCount();
		
		int iRow = 0;
        int rowStart = (pageIndex - 1) * pageSize; // 开始行数
        int rowEnd = rowStart + pageSize; // 结束行数
        while(rs.next())  //根据分页参数重新生成一个页面大小的List结果集
        {
            iRow++;
            if ((iRow > rowStart && iRow <= rowEnd) || pageSize <= 0) // 取当前页面大小结果||pageSize小于等于0获取全部结果
            {
            	Map rowMap = new HashMap();        		
            	for(int i=1;i<=columnCount;i++)	//保存数据到Map
        		{        			
        			rowMap.put(rsmd.getColumnName(i).toUpperCase(), rs.getObject(i));
        		}
        		rsList.add(rowMap);            	
            }
        }    	
        if(resultsetTransformer!=null)    	
    		this.rsList = resultsetTransformer.transform(rsList);	
    	totalSize = iRow;
        countPages(); //根据总结果集大小计算总的页面数  
    }     
    
    /**
     * 根据总的结果集数量计算页面数.
     */
    private void countPages()
    {
        // 如果一页显示数小于等于0
        if (pageSize <= 0)
        {   
            totalPage = 1;
            pageSize = this.totalSize;
            return;
        }
        //计算总的页面数
        this.totalPage = this.totalSize / this.pageSize;
        if ((this.totalSize % this.pageSize) != 0)
            this.totalPage++; 
        //如果输入的页面超过最大页面则跳转到最后一页
        if(pageIndex>totalPage)
        {
            pageIndex = totalPage;
        } 
        
    }    
    
    /**
     * @return 返回 pageIndex。
     */
    public int getPageIndex() {
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
    		this.rsList = resultsetTransformer.transform(rsList);	
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
    }

	/* (non-Javadoc)
	 * @see com.littcore.dao.page.IPageList#setResultsetTransformer(com.littcore.dao.IResultsetTransformer)
	 */
	@Override
	public IPageList setResultsetTransformer(IResultsetTransformer resultsetTransformer) {
		this.resultsetTransformer = resultsetTransformer;
		this.rsList = this.resultsetTransformer.transform(this.rsList);		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.littcore.dao.page.IPageList#getResultsetTransformer()
	 */
	@Override
	public IResultsetTransformer getResultsetTransformer() {
		return resultsetTransformer;
	}
}
