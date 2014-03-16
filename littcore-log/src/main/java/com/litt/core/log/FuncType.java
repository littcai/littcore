package com.litt.core.log;

/** 
 * 
 * 功能点操作类型.
 * 
 * <pre><b>描述：</b>
 *    功能点对数据的操作类型
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-1-8
 * @version 1.0
 *
 */
public class FuncType
{	
	/** 业务类型：保存. */
	public static final int SAVE = 1;				
	
	/** 业务类型：更新. */
	public static final int UPDATE = 2;
	
	/** 业务类型：删除. */
	public static final int DELETE = 3;	
	
	/** 业务类型：查询. */
	public static final int QUERY = 4;
	
	/** 业务类型：综合(包含其他业务). */
	public static final int ALL = 5;
	
}
