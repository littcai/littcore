
package com.littcore.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.littcore.common.Utility;
import com.littcore.dao.page.HibernatePageList;
import com.littcore.dao.page.IPageList;
import com.littcore.dao.ql.CondParam;
import com.littcore.dao.ql.IQLResult;
import com.littcore.dao.ql.PageParam;
import com.littcore.dao.ql.QLCondBuilder;
import com.littcore.dao.ql.QLResult;
import com.littcore.dao.ql.SimpleHQLBuilder;
import com.littcore.util.ArrayUtils;
import com.littcore.util.StringUtils;


/**
 * Hibernate的DAO基类实现.
 * 
 * <pre><b>描述：</b>
 *    基于Spring数据库操作模板的实现。封装了Hibernate的数据库操作，隐藏底层的复杂实现  
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 * 	2013-02-19 增加通过Map方式读取和查询	
 *  2013-04-18 增加SimpleHQLBuilder，简化多表查询后的数组转换
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2007-3-29
 * @version 1.0
 */
public class BaseHibernateDao extends HibernateDaoSupport
{
    private final static Log logger = LogFactory.getLog(BaseHibernateDao.class);
    
    private SessionFactory mySessionFactory;
	
	public SessionFactory getMySessionFactory() {
		return mySessionFactory;
	}
	
	@Autowired
	public void setMySessionFactory(SessionFactory mySessionFactory) {
		this.mySessionFactory = mySessionFactory;
	}
	
	@PostConstruct   
	protected void setSuperSessionFactory(){//BaseDao被初始化里执行这个方法

		super.setSessionFactory(mySessionFactory);
	}
	
    /**
	 * 获得Hibernate命名查询的语句.
	 * 
	 * @param name 查询名称
	 * 
	 * @return 命名查询的语句（不存在时返回NULL）
	 */
	public String getNamedQueryString(String name)
	{
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(name);
		if(query!=null)
			return query.getQueryString();
		else
			return null;
	}
	
	/**
	 * 直接执行更新操作的HQL语句.
	 * 
	 * @param hql 需要执行的HQL语句
	 * 
	 * @return int 更新的结果数量
	 */
	public int execute(String hql)
	{	
		final String temp = hql;
		Integer updateCount = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				int updateCount = query.executeUpdate();
				return Integer.valueOf(updateCount);
			}
		});
		return updateCount.intValue();
	}
	
	/**
	 * 直接执行更新操作的HQL语句.
	 * 
	 * @param hql 需要执行的HQL语句
	 * @param params 参数数组
	 * 
	 * @return 更新的结果数量
	 */
	public int execute(String hql,Object[] objs)
	{		
		final String temp = hql;	
		final Object[] params = objs;
		Integer updateCount = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);				
				for(int i=0;i<params.length;i++)
		        {
		        	query.setParameter(i, params[i]);	        	
		        }
				int updateCount = query.executeUpdate();
				return new Integer(updateCount);
			}
		});
		return updateCount.intValue();
	}	
	
	/**
	 * 批量执行更新操作的HQL语句，同1个事务中.
	 * 
	 * @param hql 更新的HQL数组
	 * 
	 * @return 	更新的结果数量
	 */
	public int execute(String[] hql)
	{
		final String[] temp = hql;
		
		Integer updateCount = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int updateCount = 0;
				for(int i=0;i<temp.length;i++)
		        {	
					Query query = session.createQuery(temp[i]);					
					updateCount += query.executeUpdate();
		        }				
				return new Integer(updateCount);
			}
		});
		return updateCount.intValue();
	}
	
	/**
     * 直接执行查询唯一结果HQL语句.
     * @param hql 查询的HQL语句
     * @return Object 对象
     */
	public Object uniqueResult(String hql)
	{
		final String temp = hql;
		return getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				query.setMaxResults(1);				
				return query.uniqueResult();
			}
		});
	}
	
	/**
	 * 直接执行查询唯一结果HQL语句.
	 * 
	 * @param hql
	 *            查询的HQL语句
	 * @param params
	 *            查询条件
	 * 
	 * @return Object 对象
	 */
	public Object uniqueResult(String hql,Object[] params)
	{
		final String temp = hql;
		final Object[] fParams = params;
		return getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				for(int i=0;i<fParams.length;i++)
	              {    
//	            	  if(fParams[i] instanceof java.util.Date)	//对日期型的特别处理
//	            		  query.setParameter(i, fParams[i], StandardBasicTypes.TIMESTAMP);	//Hibernate.DATE 是net.sf.hibernate.type.DateType 它返回的插入到sql中的值是'yyyy-mm-dd'
//	                  else			//Hibernate.TIMESTAMPnet.sf.hibernate.type.TimestampType它返回的插入到sql中的值 "yyyy-MM-dd HH:mm:ss";
	                	  query.setParameter(i, fParams[i]);
	              }
				query.setMaxResults(1);				
				return query.uniqueResult();
			}
		});
	}	
	
	/**
	 * 直接执行查询唯一结果HQL语句.
	 * 
	 * @param hql
	 *            查询的HQL语句
	 * @param params
	 *            查询条件
	 * 
	 * @return Object 对象
	 */
	public <T> T uniqueResult(String hql,Object[] params, Class className)
	{
		final String temp = hql;
		final Object[] fParams = params;
		return getHibernateTemplate().execute(new HibernateCallback<T>() {

			public T doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				for(int i=0;i<fParams.length;i++)
	              {    
//	            	  if(fParams[i] instanceof java.util.Date)	//对日期型的特别处理
//	            		  query.setParameter(i, fParams[i], StandardBasicTypes.TIMESTAMP);	//Hibernate.DATE 是net.sf.hibernate.type.DateType 它返回的插入到sql中的值是'yyyy-mm-dd'
//	                  else			//Hibernate.TIMESTAMPnet.sf.hibernate.type.TimestampType它返回的插入到sql中的值 "yyyy-MM-dd HH:mm:ss";
	                	  query.setParameter(i, fParams[i]);
	              }
				query.setMaxResults(1);				
				return (T)query.uniqueResult();
			}
		});
	}
	

	/**
     * 根据主键查找对象.
     * @param className 类
     * @param id 主键值
     * @return Object 对象
     */
	public <T> T load(Class<T> className,Serializable id)
	{
		return getHibernateTemplate().get(className, id); 
	}
	
	/**
     * 根据某个字段查找对象，如果有多个对象的话取第1个对象.
     * @param idenityName 字段名称
     * @param className 类名
     * @param id 字段值(Serializable)
     * @return Object 对象
     */
	public <T> T load(Class<T> className,String idenityName,Serializable id)
	{
		StringBuffer hql = new StringBuffer("from ");
		hql.append(className.getName());
		hql.append(" where ");
		hql.append(idenityName);
		hql.append('=');
		hql.append(id);
		
		final String temp = hql.toString();
		return getHibernateTemplate().execute(new HibernateCallback<T>() {

			public T doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				query.setMaxResults(1);				
				return (T)query.uniqueResult();
			}
		});
	}		
	
	/**
     * 根据某个字段查找对象，如果有多个对象的话取第1个对象.
     * @param idenityName 字段名称
     * @param className 类名
     * @param id 字段值(String)
     * @return Object 对象
     */
	public <T> T load(Class<T> className,String idenityName,String id)
	{
		StringBuffer hql = new StringBuffer("from ");
		hql.append(className.getName());
		hql.append(" where ");
		hql.append(idenityName);
		hql.append("='");
		hql.append(id);
		hql.append('\'');
		
		final String temp = hql.toString();
		return getHibernateTemplate().execute(new HibernateCallback<T>() {

			public T doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				query.setMaxResults(1);				
				return (T)query.uniqueResult();
			}
		});
	}
	
	/**
	 * 根据某个字段查找对象，如果有多个对象的话取第1个对象.
	 *
	 * @param <T> the generic type
	 * @param className 类名
	 * @param propertyNameValues 属性映射
	 * @return Object 对象
	 */
	public <T> T load(Class<T> className, Map<String, Object> propertyNameValues)
	{
		DetachedCriteria criteria =  DetachedCriteria.forClass(className);
		criteria.add(Restrictions.allEq(propertyNameValues));
		List<T> rsList = getHibernateTemplate().findByCriteria(criteria, 0, 1);
		if(!rsList.isEmpty())
			return rsList.get(0);
		else
			return null;
	}
	
	/**
     * 根据对象保存.
     * @param obj 对象
     * @return Long 插入对象的主键值
     */
	public Serializable save(Object obj)
	{
		return getHibernateTemplate().save(obj);
	}	
	
	// 使用HSQL语句直接增加、更新、删除实体
    public int bulkUpdate(String queryString) {
        return getHibernateTemplate().bulkUpdate(queryString);
    }

    // 使用带参数的HSQL语句增加、更新、删除实体
    public int bulkUpdate(String queryString, Object[] values) {
        return getHibernateTemplate().bulkUpdate(queryString, values);
    }
	
	/**
     * 对象数组的保存（批量保存）.
     * 大数据量保存时请使用此方法，避免内存泄露。
     * 
     * @param objs hibernate对象数组
     */
	public void saveBatch(Object[] objs)
	{
		if(objs == null || objs.length==0)
			return; 
		final Object[] temp = objs;
		
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				for(int i=0;i<temp.length;i++)
		        {	
					session.save(temp[i]);
					if(i%20==0) //以每20个数据作为一个处理单元 
					{
						session.flush(); //保持与数据库数据的同步 
						session.clear(); //清除内部缓存的全部数据，及时释放出占用的内存 
					}
		        }				
				return null;
			}
		});
	}
	
	/**
     * 对象数组的保存（批量保存）.
     * 大数据量保存时请使用此方法，避免内存泄露。
     * 
     * @param objs hibernate对象List
     */
	public void saveBatch(List objs)
	{
		if(objs == null || objs.isEmpty())
			return; 
		final List temp = objs;
		
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				for(int i=0;i<temp.size();i++)
		        {	
					session.save(temp.get(i));
					if(i%20==0) //以每20个数据作为一个处理单元 
					{
						session.flush(); //保持与数据库数据的同步 
						session.clear(); //清除内部缓存的全部数据，及时释放出占用的内存 
					}
		        }				
				return null;
			}
		});
	}		
		
	
	/**
     * 根据对象更新
     * @param obj hibernate对象
     */
	public void update(Object obj)
	{
		getHibernateTemplate().update(obj);
	}
	
	/**
     * 对象数组的更新（批量更新）.
     * 大数据量保存时请使用此方法，避免内存泄露。
     * 
     * @param objs hibernate对象数组
     */
	public void updateBatch(Object[] objs)
	{
		if(objs == null || objs.length == 0)
			return; 
		final Object[] temp = objs;
		
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				for(int i=0;i<temp.length;i++)
		        {	
					session.update(temp[i]);
					if(i%20==0) //以每20个数据作为一个处理单元 
					{
						session.flush(); //保持与数据库数据的同步 
						session.clear(); //清除内部缓存的全部数据，及时释放出占用的内存 
					}
		        }				
				return null;
			}
		});
	}	
	
	/**
     * 对象数组的更新（批量更新）.
     * 大数据量保存时请使用此方法，避免内存泄露。
     * 
     * @param objs hibernate对象List
     */
	public void updateBatch(List objs)
	{
		if(objs == null || objs.isEmpty())
			return; 
		final List temp = objs;
		
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				for(int i=0;i<temp.size();i++)
		        {	
					session.update(temp.get(i));
					if(i%20==0) //以每20个数据作为一个处理单元 
					{
						session.flush(); //保持与数据库数据的同步 
						session.clear(); //清除内部缓存的全部数据，及时释放出占用的内存 
					}
		        }				
				return null;
			}
		});
	}		
	
	/**
	 * 根据HQL更新.
	 * 
	 * @param hql 更新的HQL语句
	 * @param objs 参数值数组
	 */
	public void update(String hql,Object[] objs)
	{
		final String temp = hql.toString();
		final Object[] params = objs;
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(temp);
				for(int i=0;i<params.length;i++)
		        {
		        	query.setParameter(i, params[i]);	        	
		        }
		        query.executeUpdate();   			
				return null;
			}
		});
	}	
	
	public void saveOrUpdate(Object entity)
    {
        getHibernateTemplate().saveOrUpdate(entity);
    }
	
	public void saveOrUpdateBatch(Collection entities)
    {
		if(entities == null || entities.isEmpty())
			return; 
        getHibernateTemplate().saveOrUpdateAll(entities);
    }
	
    /**
     * 根据对象删除
     * @param obj hibernate对象
     */
	public void delete(Object obj)
    {
        getHibernateTemplate().delete(obj);
    } 
	
	public void deleteBatch(Collection objs)
    {
		if(objs == null || objs.isEmpty())
			return; 
        getHibernateTemplate().deleteAll(objs);
    } 
	
    /**
     * 根据对象删除.
     * 
     * @param className the class name
     * @param id the id
     */
	public void delete(Class className, Serializable id)
    {
        getHibernateTemplate().delete(this.load(className, id));
    } 	
    
    /**
     * 根据主键名删除.
     * 
     * @param idenityName 主键名称
     * @param className 类名
     * @param id 主键值
     */
	public void delete(Class className, String idenityName,Serializable id)
    {
        this.delete(className.getName(), idenityName, id);
    }    
	
	/**
	 * 根据主键名删除.
	 * 
	 * @param idenityName 关键字段名称
	 * @param className 类名
	 * @param id 主键值（Serializable）
	 */
	private void delete(String className, String idenityName, Serializable id)
	{
		StringBuffer hql = new StringBuffer("delete from ");
		hql.append(className);
		hql.append(" where ");
		hql.append(idenityName);
		hql.append('=');
		hql.append(id);
        getHibernateTemplate().bulkUpdate(hql.toString());
//		final String temp = hql.toString();
//		getHibernateTemplate().execute(new HibernateCallback() {
//
//			public Object doInHibernate(Session session)
//					throws HibernateException, SQLException {
//				Query query = session.createQuery(temp);
//				query.executeUpdate();
//				return null;
//			}
//		});
	}
	
	/**
     * 通过ID删除多个对象.
     * @param ids 一个或多个ID,用IN的方式一次性处理
     * @param idenityName 关键字段名称
     * @param className 类名
     */
	public void delete(Class className, String idenityName, Object[] ids)
	{			
		String[] conds = new String[ids.length];
		conds = ArrayUtils.fillWith(conds, "?");
		
		StringBuffer hql = new StringBuffer("delete from ");
		hql.append(className.getSimpleName());
		hql.append(" where ");
		hql.append(idenityName);
		hql.append(" in(");
		hql.append(StringUtils.join(conds, ","));
		hql.append(')');
		
		getHibernateTemplate().bulkUpdate(hql.toString(), ids);
	}
	
	/**
     * Hibernate查询.
     * @param listHql 查询的HQL语句
     * @return List结果集
     */
	public List listAll(String listHql)
	{
		return getHibernateTemplate().find(listHql);
	}
	
	/**
	 * Hibernate查询.
	 * 
	 * @param params 参数数组
	 * @param listHql 查询的HQL语句
	 * 
	 * @return List结果集
	 */
	public List listAll(String listHql,Object[] params)
	{
		return getHibernateTemplate().find(listHql,params);
	}	
	
	/**
	 * 动态HQL查询.
	 * 
	 * @param qlResult 动态QL结果
	 * 
	 * @return List结果集
	 */
	public List listAll(IQLResult qlResult)
	{
		String listHql = qlResult.generate();
		return this.listAll(listHql, qlResult.getParams());
	}	
	
	/**
	 * 动态HQL查询.
	 * 
	 * @param dynamicHql 动态HQL语句
	 * @param condParam 条件参数对象
	 * 
	 * @return List结果集
	 */
	public List listAll(String dynamicHql, CondParam condParam)
	{
		QLResult qlResult = QLCondBuilder.generate(dynamicHql, condParam);
		return this.listAll(qlResult);
	}	
	
	/**
     * Hibernate查询.
     * @param className 类名
     * @return List结果集
     */
	public <T> List<T> listAll(Class<T> className)
	{
		return getHibernateTemplate().loadAll(className);		
	}	
	
	/**
	 * 根据某个字段查找对象.
	 *
	 * @param <T> the generic type
	 * @param className 类名
	 * @param propertyNameValues 属性映射
	 * @return List 对象
	 */
	public <T> List<T> listAll(Class<T> className, Map<String, Object> propertyNameValues)
	{
		DetachedCriteria criteria =  DetachedCriteria.forClass(className);
		criteria.add(Restrictions.allEq(propertyNameValues));
		return getHibernateTemplate().findByCriteria(criteria);		
	}
    
	/**
     * Hibernate分页查询.
     * @param pageIndex 当前页码
     * @param listHql 查询的HQL语句
     * @param pageSize 每页显示数
     * @return List结果集
     */
	public List listPage(String listHql,int pageIndex,int pageSize)
	{		
		if(pageIndex<=0 || pageSize<=0)	//如果分页参数设置不正确，则全查
			return getHibernateTemplate().find(listHql);
		
		int startRow = pageSize * pageIndex - pageSize;
        final String fHql = listHql;
        final int fStartRow = startRow;
        final int fPageSize = pageSize;

        return  getHibernateTemplate().executeFind( new  HibernateCallback(){
            public  Object doInHibernate(Session session)  throws  SQLException,
                   HibernateException {
              Query q  =  session.createQuery(fHql);
              
              q.setFirstResult(fStartRow);
              q.setMaxResults(fPageSize);
               return  q.list();
              }
       });      
	}
    
    /**
     * Hibernate分页查询.
     * 
     * @param pageIndex 当前页码
     * @param params 查询参数数组
     * @param pageSize 每页显示数
     * @param listHql 查询的HQL语句
     * 
     * @return List结果集
     */
    public List listPage(String listHql,Object[] params,int pageIndex,int pageSize)
    {       
    	if(pageIndex<=0 || pageSize<=0)	//如果分页参数设置不正确，则全查
			return getHibernateTemplate().find(listHql, params);
    	
        int startRow = pageSize * pageIndex - pageSize;
        final String fHql = listHql;
        final Object[] fParams = params;
        final int fStartRow = startRow;
        final int fPageSize = pageSize;
        
        return getHibernateTemplate().executeFind( new  HibernateCallback(){
            public  Object doInHibernate(Session session)  throws  SQLException,
                   HibernateException {
              Query q  =  session.createQuery(fHql);
              for(int i=0;i<fParams.length;i++)
              {    
//            	  if(fParams[i] instanceof java.util.Date)	//对日期型的特别处理
//                	  q.setParameter(i, fParams[i], StandardBasicTypes.TIMESTAMP);	//Hibernate.DATE 是net.sf.hibernate.type.DateType 它返回的插入到sql中的值是'yyyy-mm-dd'
//                  else			//Hibernate.TIMESTAMPnet.sf.hibernate.type.TimestampType它返回的插入到sql中的值 "yyyy-MM-dd HH:mm:ss";
                	  q.setParameter(i, fParams[i]);
              }
              q.setFirstResult(fStartRow);
              q.setMaxResults(fPageSize);              
               return  q.list();
              }
       });
    }    
	
    /**
     * 查询结果集记录数.
     * @param countHql 查询记录数的HQL
     * @return 结果集记录数
     */
	public int count(String countHql)
	{
		int count = 0;
        List list = getHibernateTemplate().find(countHql);
        if(list!=null&&list.size()>0)
        	count = Utility.parseInt(list.get(0).toString());
        return count;
	}
    
    /**
     * 查询结果集记录数.
     * 
     * @param params 查询参数数组
     * @param countHql 查询记录数的HQL
     * 
     * @return 结果集记录数
     */
    public int count(String countHql, Object[] params)
    {
        int count = 0;
        List list = getHibernateTemplate().find(countHql,params);
        if(list!=null&&list.size()>0)
            count = Utility.parseInt(list.get(0).toString());
        return count;
    }    
    
    /**
     * Hibernate分页查询.
     * 
     * @param pageIndex 当前页码
     * @param pageSize 每页显示数
     * @param listHql 查询的HQL语句
     * @param countHql 查询记录数的HQL
     * @return IPageList分页对象
     */
    public IPageList listPage(String listHql,String countHql,int pageIndex,int pageSize)
    {
        int totalSize = count(countHql);
        
        IPageList page = new HibernatePageList();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotalSize(totalSize);   //该方法将计算pageIndex是否超出最大页数
        page.setRsList(listPage(listHql,page.getPageIndex(),page.getPageSize()));
        return page;
    }
    
    /**
     * Hibernate分页查询.
     * 
     * @param pageIndex 当前页码
     * @param params 查询参数数组
     * @param listHql 查询的HQL语句
     * @param pageSize 每页显示数
     * @param countHql 查询记录数的HQL
     * 
     * @return IPageList分页对象
     */
    public IPageList listPage(String listHql,String countHql,Object[] params,int pageIndex,int pageSize)
    {
      int totalSize = count(countHql, params);
      return this.listPage(listHql, totalSize, params, pageIndex, pageSize, true);
    } 
    
    public IPageList listPage(String listHql,String countHql,Object[] params,int pageIndex,int pageSize, boolean enableEdgeCheck)
    {
      int totalSize = count(countHql, params);
      return this.listPage(listHql, totalSize, params, pageIndex, pageSize, enableEdgeCheck);
    }
    
    public IPageList listPage(String listHql, int totalSize,Object[] params,int pageIndex,int pageSize)
    {
      return this.listPage(listHql, totalSize, params, pageIndex, pageSize, true);
    }
    
    /**
     * Hibernate分页查询.
     * 
     * @param pageIndex 当前页码
     * @param params 查询参数数组
     * @param listHql 查询的HQL语句
     * @param pageSize 每页显示数
     * @param totalSize 总结果集大小
     * 
     * @return IPageList分页对象
     */
    public IPageList listPage(String listHql, int totalSize,Object[] params,int pageIndex,int pageSize, boolean enableEdgeCheck)
    {
        IPageList page = new HibernatePageList();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotalSize(totalSize);   //该方法将计算同时页数
        /*
         * 检查记录数的分页结果是否越限，若不启动边界检查，则无需执行实际查询
         */
        //如果输入的页面超过最大页面则跳转到最后一页
        int totalPage = page.getTotalPage();
        if(!enableEdgeCheck && pageIndex > totalPage)
        {
          page.setRsList(ListUtils.EMPTY_LIST);
        }
        else
        {
          if(pageIndex > totalPage)
            page.setPageIndex(totalPage);
          page.setRsList(listPage(listHql,params,page.getPageIndex(),pageSize));
        }
        return page;
    }    
    
    /**
     * Hibernate分页查询.
     * 
     * @param dynamicHql 动态查询语句
     * @param dynamicCountHql 动态统计语句
     * @param pageParam 分页参数对象
     * 
     * @return IPageList分页对象
     */
    public IPageList listPage(String dynamicHql, String dynamicCountHql, PageParam pageParam)
    {
    	IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);
    	IQLResult countResult = QLCondBuilder.generate(dynamicCountHql, pageParam);
    	return this.listPage(listResult.generate(), countResult.generate(), listResult.getParams(), pageParam.getPageIndex(), pageParam.getPageSize());
    }
    
    /**
     * Hibernate分页查询.
     * 
     * @param dynamicHql 动态查询语句
     * @param dynamicCountHql 动态统计语句
     * @param pageParam 分页参数对象
     * 
     * @return IPageList分页对象
     */
    public IPageList listPage(String dynamicHql, PageParam pageParam)
    {
    	IQLResult listResult = QLCondBuilder.generate(dynamicHql, pageParam);
    	//通过分析原始语句及子查询嵌套的方式，直接生成统计语句
    	String dynamicCountHql = listResult.generateCount();
    	return this.listPage(listResult.generate(), dynamicCountHql, listResult.getParams(), pageParam.getPageIndex(), pageParam.getPageSize(), pageParam.isEnableEdgeCheck());
    }  
    
    /**
     * hibernate 的count 查询
     * @param dynamicHql
     * @param pageParam
     * @return
     */
    public int count(String dynamicHql, CondParam condParam)
    {
    	IQLResult listResult = QLCondBuilder.generate(dynamicHql, condParam);
    	//通过分析原始语句及子查询嵌套的方式，直接生成统计语句
    	String dynamicCountHql = listResult.generateCount();
    	return this.count(dynamicCountHql,listResult.getParams());
      } 
    
    /**
     * 属性查询.
     * 注：基本数据类型由于有默认值，会生成默认的查询条件，有可能影响最终条件
     * 
     * @param className 实体类
     * @param condParam 动态查询条件
     * 
     * @return T集合
     */
    public List<Map<String, Object>> listBy(final SimpleHQLBuilder builder)
    {    	
    	return  getHibernateTemplate().executeFind(new  HibernateCallback(){
                public  Object doInHibernate(Session session)  throws  SQLException,
                       HibernateException {
                  Query q  =  session.createQuery(builder.getHql());
                  Object[] params = builder.getParams();
                  if(builder.getParams()!=null)
                  {
                	  for(int i=0;i<params.length;i++)
                	  {
//                		  if(params[i] instanceof java.util.Date)	//对日期型的特别处理
//                        	  q.setParameter(i, params[i], StandardBasicTypes.TIMESTAMP);	//Hibernate.DATE 是net.sf.hibernate.type.DateType 它返回的插入到sql中的值是'yyyy-mm-dd'
//                          else			//Hibernate.TIMESTAMPnet.sf.hibernate.type.TimestampType它返回的插入到sql中的值 "yyyy-MM-dd HH:mm:ss";
                        	  q.setParameter(i, params[i]);
                	  }
                	  
                  }
                  
                  if(builder.getPageIndex()>0 || builder.getPageSize()>0)	//如果分页参数设置不正确，则全查
                  {
                	  int startRow = builder.getPageSize() * builder.getPageIndex() - builder.getPageSize();
                	  
                	  q.setFirstResult(startRow);
                      q.setMaxResults(builder.getPageSize());                	  
              	  } 
                  if(builder.getTransformer()!=null)
                  {
                	  q.setResultTransformer(builder.getTransformer());
                  }
                  return  q.list();
                }   
           });      
    			
    }
    
    /**
     * 属性查询.
     * 注：基本数据类型由于有默认值，会生成默认的查询条件，有可能影响最终条件
     * 
     * @param className 实体类
     * @param condParam 动态查询条件
     * 
     * @return T集合
     */
    public <T> List<T> listBy(Class<T> className, CondParam condParam)
    {	    	
		T t = null;
		try
		{
			t = className.newInstance();
			BeanUtils.populate(t, condParam.getConds());
		}
		catch (InstantiationException e)
		{
			throw new DataAccessResourceFailureException("实体对象实例化失败！", e);
		}
		catch (IllegalAccessException e)
		{
			throw new DataAccessResourceFailureException("实体对象访问失败！", e);
		}
		catch (InvocationTargetException e)
		{
			throw new DataAccessResourceFailureException("实体对象调用失败！", e);
		}    	
		return getHibernateTemplate().findByExample(t);		
    }
    
   
	
}
