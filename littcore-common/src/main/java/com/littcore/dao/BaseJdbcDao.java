package com.littcore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.littcore.dao.page.IPageList;
import com.littcore.dao.page.JdbcPageList;
import com.littcore.dao.ql.CondParam;
import com.littcore.dao.ql.IQLResult;
import com.littcore.dao.ql.PageParam;
import com.littcore.dao.ql.QLCondBuilder;

/**
 * JDBC的DAO基类.
 * 
 * <pre><b>描述：</b>
 *    基于Spring数据库操作模板的实现。封装了JDBC的数据库操作，隐藏底层的复杂实现  
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-3-29
 * @version 1.0
 */
public class BaseJdbcDao extends NamedParameterJdbcDaoSupport
{
    private final static Log logger = LogFactory.getLog(BaseJdbcDao.class);
   
 
    /**
     * 执行更新操作的SQL语句.
     * 
     * @param sql 更新操作的SQL语句
     */
    public void execute(String sql)
    {
    	getJdbcTemplate().execute(sql);
    }
    
    /**
	 * 执行更新操作的SQL语句.
	 * 
	 * @param params 参数
	 * @param sql 更新操作的SQL语句
	 */
    public void execute(String sql,Object[] params)
    {
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("调用的SQL为："+sql);
    	}    	
    	getJdbcTemplate().update(sql, params);    	
    }  
    
    /**
     * 执行动态SQL语句.
     * 
     * @param dynamicSql 动态SQL
     * @param condParam 条件对象
     */
    public void execute(String dynamicSql, CondParam condParam)
    {    	   	
    	IQLResult qlResult = QLCondBuilder.generate(dynamicSql, condParam);
    	String finalSql = qlResult.generate();
    	if(logger.isDebugEnabled())
    	{
    		logger.debug("调用的SQL为："+ finalSql);
    	} 
    	getJdbcTemplate().update(finalSql, qlResult.getParams());    	
    }  
 
    /**
     * 基于动态sql的保存.
     * 
     * @param dynamicSql 动态SQL
     * @param condParam 动态条件
     */
    public void save(String dynamicSql, CondParam condParam)
    {
    	this.execute(dynamicSql, condParam);
    }
    
    /**
     * 保存并返回主键.
     *
     * @param sql the sql
     * @param params the params
     * @return the number
     */
    public Number save(final String sql, final Object[] params)
    {
      KeyHolder holder = new GeneratedKeyHolder();
      this.getJdbcTemplate().update(new PreparedStatementCreator() {  
        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {              
             PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);  
             new ArgumentPreparedStatementSetter(params).setValues(ps);
             return ps;  
        }  
      }, holder);  
    
      return holder.getKey();
    }
    
    /**
     * 基于动态sql的更新.
     * 
     * @param dynamicSql 动态SQL
     * @param condParam 动态条件
     */
    public void update(String dynamicSql, CondParam condParam)
    {
    	this.execute(dynamicSql, condParam);
    }    
    
    /**
     * JDBC查询.
     * 
     * @param listSql 查询SQL语句
     * @return List<Map>结果集
     */
    public List listAll(String listSql)
    {
        return getJdbcTemplate().queryForList(listSql);
    }
    
    /**
     * JDBC查询.
     * 
     * @param listSql 查询SQL语句
     * @param params 参数数组
     * 
     * @return List<Map>结果集
     */
    public List listAll(String listSql, Object[] params)
    {
        return getJdbcTemplate().queryForList(listSql, params);
    }    
    
	/**
	 * 动态SQL查询.
	 * 
	 * @param qlResult 动态SQL结果
	 * 
	 * @return List结果集
	 */
	public List listAll(IQLResult qlResult)
	{
		String listSql = qlResult.generate();
		return this.listAll(listSql, qlResult.getParams());
	}	
	
	/**
	 * 动态SQL查询.
	 * 
	 * @param dynamicSql 动态SQL语句
	 * @param condParam 条件参数对象
	 * 
	 * @return List结果集
	 */
	public List listAll(String dynamicSql, CondParam condParam)
	{
		IQLResult qlResult = QLCondBuilder.generate(dynamicSql, condParam);
		return this.listAll(qlResult);
	}
    
	/**
     * JDBC分页查询.
     * 
     * @param pageIndex 页码
     * @param listSql 查询SQL语句
     * @param pageSize 每页显示数
     * @return IPageList分页对象
     */
    public IPageList listPage(String listSql,int pageIndex,int pageSize)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("调用的SQL语句 - "+listSql);
		}
        final JdbcPageList page = new JdbcPageList();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);   			
		
		getJdbcTemplate().query(listSql, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				page.populate(rs);
				return null;
			}			
		});		
		return page;				
	}   
    
	/**
     * JDBC分页查询.
     * 
     * @param params 查询条件
     * @param listSql 查询SQL语句
     * @param pageIndex 页码
     * @param pageSize 每页显示数
     * @return IPageList分页对象
     */
    public IPageList listPage(String listSql,Object[] params,int pageIndex,int pageSize)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("调用的SQL语句 - "+listSql);
		}
        final JdbcPageList page = new JdbcPageList();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);   			
		
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
      
        
		getJdbcTemplate().query(listSql, params, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				page.populate(rs);
				return null;
			}			
		});		
		return page;					
	}  
    
	/**
	 * JDBC分页查询.
	 * 
	 * @param qlResult 动态SQL结果对象
	 * 
	 * @return IPageList分页对象
	 */
    public IPageList listPage(IQLResult qlResult)
	{
        return this.listPage(qlResult.generate(), qlResult.getParams(), qlResult.getPageIndex(), qlResult.getPageSize());			
	} 
    
	/**
	 * JDBC分页查询.
	 * 
	 * @param dynamicSql 动态SQL
	 * @param pageParam 分页查询参数
	 * 
	 * @return IPageList分页对象
	 */
    public IPageList listPage(String dynamicSql, PageParam pageParam)
	{
    	IQLResult qlResult = QLCondBuilder.generate(dynamicSql, pageParam);
    	return this.listPage(qlResult);		
	}     
	
	/**
     * 根据SQL语句查询记录数.
     * @param countSql 查询记录数的SQL语句
     * @return 结果集记录数
     */
    public int count(String countSql)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("调用的SQL语句 - "+countSql);
		}
        return getJdbcTemplate().queryForObject(countSql, Integer.class);
	}
    
	/**
     * 根据SQL语句查询记录数.
     * @param countSql 查询记录数的SQL语句
     * @param params 查询条件
     * @return 结果集记录数
     */
    public int count(String countSql, Object[] params)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("调用的SQL语句 - "+countSql);
		}
        return getJdbcTemplate().queryForObject(countSql,params, Integer.class);
	} 
    
	/**
	 * 根据SQL语句查询记录数.
	 * 
	 * @param qlResult 动态SQL结果对象
	 * 
	 * @return 结果集记录数
	 */
    public int count(IQLResult qlResult)
	{
        return this.count(qlResult.generate(), qlResult.getParams());
	}        
    
	/**
	 * 根据SQL语句查询记录数.
	 * 
	 * @param dynamicSql 动态SQL
	 * @param pageParam 分页查询参数
	 * 
	 * @return 结果集记录数
	 */
    public int count(String dynamicSql, PageParam pageParam)
	{
    	IQLResult qlResult = QLCondBuilder.generate(dynamicSql, pageParam);
        return this.count(qlResult);
	} 
}
