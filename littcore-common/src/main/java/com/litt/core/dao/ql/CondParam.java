package com.litt.core.dao.ql;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.litt.core.util.DateUtils;

/** 
 * 
 * Map式参数传递对象.
 * 
 * <pre><b>描述：</b>
 * 2013-07-23 1、重构对排序的支持，支持多重排序   
 *    
 * 注意事项：   
 *    1、不支持重复的键值（若重复，新值将替换旧值）
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2009-12-23,2013-07-23
 * @version 1.0,1.1
 *
 */
public class CondParam
{	
	/**
	 * 参数映射.
	 * KEY:参数名
	 * VALUE:参数值 
	 */
	private Map<String, Object> condMap = new HashMap<String, Object>();
	
	/** 
	 * 排序映射.
	 * KEY:排序字段名
	 * VALUE:排序方式 
	 */
	private Map<String, String> sortMap = new HashMap<String, String>();		
	
	public CondParam(){}
	
	public CondParam(Map<String, Object> condMap){
		this.condMap = condMap;		
	}
	
	public CondParam(Map<String, Object> condMap, Map<String, String> sortMap){
		this.condMap = condMap;		
		this.sortMap = sortMap;
	}
	
    /**
     * 增加查询条件.
     * 
     * @param key 键值
     * @param value 对象值
     */
    public void addCond(String key, Object value)
    {
    	this.condMap.put(key, value);
    }
    
    /**
     * 增加查询条件.
     * 
     * @param key 键值
     * @param value 对象值
     * @param flag 标志，true时插入条件，false时不插
     */
    public void addCond(String key, Object value, boolean flag)
    {
    	if(flag)
    		this.condMap.put(key, value);
    }    
    
    /**
     * 添加参数集合.
     * 
     * @param condMap 参数集合
     */
    public void addAllCond(Map<String, Object> condMap)
    {
    	this.condMap.putAll(condMap);
    }
    
    /**
     * 删除条件.
     * 
     * @param key 键
     */
    public void removeCond(String key)
    {
    	condMap.remove(key);
    }
    
    /**
     * 删除所有条件.
     * 
     * @param key 键
     */
    public void removeAllCond()
    {
    	condMap.clear();
    } 
    
    /**
     * 是否拥有参数.
     * 
     * @param condName 参数名
     * 
     * @return 存在返回true；否则返回false
     */
    public boolean hasCond(String condName)
    {
    	return condMap.containsKey(condName);
    }
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Object 查询条件值(不存在返回NULL)
     */
    public Object getCond(String condName)
    {
        return condMap.get(condName);       
    }	
    
    /**
	 * 增加排序条件.
	 *
	 * @param sortField 排序字段名
	 * @param sortOrder 排序方式
	 */
    public void addSort(String sortField, String sortOrder)
    {
    	this.sortMap.put(sortField, sortOrder);
    }
    
    /**
     * 获得排序字段名(只有一个字段排序时使用).
     *
     * @return the sort field
     */
    public String getSortField()
    {
    	if(sortMap.isEmpty())
    		return null;
    	else {
    		return sortMap.entrySet().iterator().next().getKey();
		}
    }
    
    /**
     * 获得排序字段顺寻(只有一个字段排序时使用).
     *
     * @return the sort field
     */
    public String getSortOrder()
    {
    	if(sortMap.isEmpty())
    		return null;
    	else {
    		return sortMap.entrySet().iterator().next().getValue();
		}
    }
    
    /**
     * 获得排序条件.
     * 
     * @param sortField 排序字段名
     * 
     * @return String 排序方式(不存在返回NULL)
     */
    public String getSort(String sortField)
    {
        return sortMap.get(sortField);       
    }	
    
    /**
     * 是否有排序字段
     * @return boolean
     */
    public boolean hasSort(String sortField)
    {
    	return sortMap.containsKey(sortField);
    }
    
    /**
     * 返回所有排序字段名.
     * 
     * @return 所有排序字段名
     */
    public String[] getSortFields()
    {
    	return this.sortMap.keySet().toArray(new String[this.sortMap.size()]);    	
    }   
    
    /**
     * 返回所有排序.
     * 
     * @return 所有排序
     */
    public Map<String, String> getSorts()
    {
    	return this.sortMap;    	
    }
    
    /**
     * 是否有排序字段
     * @return boolean
     */
    public boolean hasSort()
    {
        return !sortMap.isEmpty();
    }  
    
    /**
     * 返回所有条件.
     * 
     * @return 所有条件
     */
    public Map<String, Object> getConds()
    {
    	return this.condMap;
    }    
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return String 查询条件值
     */
    public String getStringCond(String condName)
    {
        if(condMap!=null)
            return (String)condMap.get(condName);
        else
            return null;
    }

    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Integer 查询条件值
     */
    public Integer getIntegerCond(String condName)
    {
    	return (Integer)condMap.get(condName);    	
    } 
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Long 查询条件值
     */
    public Long getLongCond(String condName)
    {
    	return (Long)condMap.get(condName);    	
    } 
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Double 查询条件值
     */
    public Double getDoubleCond(String condName)
    {
    	return (Double)condMap.get(condName);    	
    }     
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Float 查询条件值
     */
    public Float getFloatCond(String condName)
    {
    	return (Float)condMap.get(condName);
    }  
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return BigDecimal 查询条件值
     */
    public BigDecimal getBigDecimalCond(String condName)
    {
    	return (BigDecimal)condMap.get(condName);
    }    
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Date 查询条件值
     */
    public Date getDateCond(String condName)
    {
    	return (Date)condMap.get(condName);
    }   
    
    /**
     * 获得查询条件值.
     * 
     * @param condName 查询条件名称
     * 
     * @return Timestamp 查询条件值
     */
    public Timestamp getTimestampCond(String condName)
    {
    	Date date = this.getDateCond(condName);
    	if(date!=null)
    		return DateUtils.convertDate2Timestamp(date);
    	else
    		return null;
    }  
    
}
