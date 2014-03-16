package com.litt.core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.litt.core.dao.ql.CondParam;


/** 
 * 
 * 通用HibernateDAO.
 * 
 * <pre><b>描述：</b>
 *    基于泛型的通用HibernateDAO实现
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    2012-06-20 1、由于save方法无法覆盖BaseHibernateDao，改为完全重写GenericHibernateDao
 *    					2、增加若干方法
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2010-8-9
 * @version 1.0
 *
 */
public class GenericHibernateDao<T extends Serializable,PK extends Serializable> extends BaseHibernateDao
{	
	/**
	 * 实体对象Class类型.
	 */
	private Class<T> entityClass;
	
	public GenericHibernateDao() 
	{
	   entityClass =(Class<T>) ((ParameterizedType) getClass()
	                                .getGenericSuperclass()).getActualTypeArguments()[0];
	}	
	
	/**
	 * 保存.
	 * 
	 * @param entity 实体对象
	 * 
	 * @return 主键
	 */	
	public  PK save(T entity) 
	{
		return (PK)getHibernateTemplate().save(entity);
	}

	
	/**
	 * 读取.
	 * @param id 主键
	 * @return  实体对象
	 */	
	public T load(PK id)	
	{
	   return getHibernateTemplate().get(entityClass, id);
	}
	
	/**
	 * 根据主键删除.
	 * 
	 * @param id 主键
	 */
	public void deleteById(PK id)
	{
		Object entity = load(id);
		if(entity == null) {
			throw new ObjectRetrievalFailureException(entityClass, id);
		}
		getHibernateTemplate().delete(entity);
	}
	
	/**
	 * 根据某个可序列化字段删除(常用于基于外键删除).
	 *
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void deleteBy(String fieldName, Serializable fieldValue)
	{
		Object entity = super.load(entityClass, fieldName, fieldValue);
		if(entity == null) {
			throw new ObjectRetrievalFailureException(entityClass, fieldValue);
		}
		getHibernateTemplate().delete(entity);
	}
	
	/**
	 * 根据某个字符串字段删除.
	 *
	 * @param fieldName 字段名
	 * @param fieldValue 字段值
	 */
	public void deleteBy(String fieldName, String fieldValue)
	{
		Object entity = super.load(entityClass, fieldName, fieldValue);
		if(entity == null) {
			throw new ObjectRetrievalFailureException(entityClass, fieldValue);
		}
		getHibernateTemplate().delete(entity);
	}
	
	
	/**
	 * Merge.
	 *
	 * @param entity the entity
	 */
	public void merge(T entity){
		getHibernateTemplate().merge(entity);
    }
    
    /**
     * Evict the cache from current session.
     *
     * @param entity the entity
     */
    public void evict(T entity){
    	getHibernateTemplate().evict(entity);
        
    }
    
    /**
     * Reload data from db and refresh the cache.
     *
     * @param entity the entity
     */
    public void refresh(T entity){
    	getHibernateTemplate().refresh(entity);
        
    }
	
	/**
	 * 查询全部.
	 * @return 实体集合
	 */
	public List<T> listAll()
	{
		return getHibernateTemplate().loadAll(entityClass);
	}
	
	/**
	 * 按动态条件查询.
	 * 
	 * @param condParam 动态条件
	 * 
	 * @return List<T>
	 */
	public List<T> listBy(CondParam condParam)
    {
        return this.listBy(entityClass, condParam);
    }
	
	// -------------------------------- Criteria ------------------------------

    // 创建与会话无关的检索标准
    public DetachedCriteria createDetachedCriteria() {
        return DetachedCriteria.forClass(this.entityClass);
    }

    // 创建与会话绑定的检索标准
    public Criteria createCriteria() {
        return this.createDetachedCriteria().getExecutableCriteria(
                this.getSession());
    }

    // 检索满足标准的数据
    public List listByCriteria(DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    // 检索满足标准的数据，返回指定范围的记录
    public List listByCriteria(DetachedCriteria criteria, int firstResult,
            int maxResults) {
        return getHibernateTemplate().findByCriteria(criteria, firstResult,
                maxResults);
    }
	
	 // 使用指定的实体及属性检索（满足除主键外属性＝实体值）数据
    public List<T> listEqualByEntity(T entity, String[] propertyNames) {
        Criteria criteria = this.createCriteria();
        Example exam = Example.create(entity);
        exam.excludeZeroes();
        String[] defPropertys = getSessionFactory().getClassMetadata(entity.getClass()).getPropertyNames();
        for (String defProperty : defPropertys) {
            int ii = 0;
            for (ii = 0; ii < propertyNames.length; ++ii) {
                if (defProperty.equals(propertyNames[ii])) {
                    criteria.addOrder(Order.asc(defProperty));
                    break;
                }
            }
            if (ii == propertyNames.length) {
                exam.excludeProperty(defProperty);
            }
        }
        criteria.add(exam);
        return (List<T>) criteria.list();
    }

    // 使用指定的实体及属性检索（满足属性 like 串实体值）数据
    public List<T> listLikeByEntity(T entity, String[] propertyNames) {
        Criteria criteria = this.createCriteria();
        for (String property : propertyNames) {
            try {
                Object value = PropertyUtils.getProperty(entity, property);
                if (value instanceof String) {
                    criteria.add(Restrictions.like(property, (String) value,
                            MatchMode.ANYWHERE));
                    criteria.addOrder(Order.asc(property));
                } else {
                    criteria.add(Restrictions.eq(property, value));
                    criteria.addOrder(Order.asc(property));
                }
            } catch (Exception ex) {
                // 忽略无效的检索参考数据。
            }
        }
        return (List<T>) criteria.list();
    }
	
	/**
	 * 分页查询.
	 * 可用于TOP查询
	 * 
	 * @return 实体集合
	 */
	public List<T> listPage(int pageIndex, int pageSize)
	{		
		return super.listPage("from "+entityClass.getName(), pageIndex, pageSize);
	}

	/**
	 * @return the entityClass.
	 */
	public Class<T> getEntityClass()
	{
		return entityClass;
	}	
}
