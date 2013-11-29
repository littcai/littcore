
package com.litt.core.dao.ql;

import org.hibernate.transform.ResultTransformer;

/**
 * 
 * 
 * 查询构造器.
 * 
 * <pre><b>描述：</b>
 *    封装了查询条件、查询配置、结果转换等，避免方法参数传递时设置过多参数，避免重构时大量代码的更改
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-04-18
 * @version 1.0
 *
 *
 */
public class SimpleHQLBuilder {
	
	/** 查询语句. */
	private String hql;
	
	/** 查询条件. */
	private Object[] params;	

	/** 分页页码.	 */
	private int pageIndex;
	
    /**
     * 分页大小.
     */
    private int pageSize;
    
    /** 结果转换. */
    private ResultTransformer transformer;

	public SimpleHQLBuilder(String hql, Object[] params) {
		super();
		this.hql = hql;
		this.params = params;
	}	

	public SimpleHQLBuilder(String hql, Object[] params,
			ResultTransformer transformer) {
		super();
		this.hql = hql;
		this.params = params;
		this.transformer = transformer;
	}

	public SimpleHQLBuilder(String hql, Object[] params, int pageIndex, int pageSize) {
		super();
		this.hql = hql;
		this.params = params;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}	

	public SimpleHQLBuilder(String hql, Object[] params, int pageIndex,
			int pageSize, ResultTransformer transformer) {
		super();
		this.hql = hql;
		this.params = params;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.transformer = transformer;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public ResultTransformer getTransformer() {
		return transformer;
	}

}
