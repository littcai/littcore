package com.littcore.dao.dataset;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.MirrorList;
import net.vidageek.mirror.set.dsl.FieldSetter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.dao.dataset.annotation.MapColumn;
import com.littcore.dao.dataset.metadata.DataSetMetadata;
import com.littcore.util.ArrayUtils;
import com.littcore.util.StringUtils;

/**
 * Map型结果集.
 * 
 * <pre><b>Description：</b>
 * 
 *    注意：HashMap的MetaData中columnNames是没有顺序的
 * </pre>
 * 
 * <pre><b>Changelog：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">Bob.cai</a>
 * @since 2012-8-2
 * @version 1.0
 */
public class ObjectDataSet<T extends Serializable> implements IDataSet<T> {	

	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(ObjectDataSet.class);	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private DataSetMetadata metadata;
	
	/** The rs list. */
	private List<T> rsList = new ArrayList<T>();
	
	/** The _index. */
	private int _index = -1;
	
	private Class<T> clazz;
	
	public ObjectDataSet(Class<T> clazz){		
		this.clazz = clazz;
		this.initMetadata();
	}
	
	public ObjectDataSet(Class<T> clazz, List<T> dataList){		
		this.clazz = clazz;
		this.initMetadata();
		this.populate(dataList);
	}

	/**
	 * 
	 */
	private void initMetadata() {		
		
		MirrorList<Method> getList = new Mirror().on(clazz).reflectAll().getters();
		
		String[] columnNames = new String[getList.size()];		
		int i=0;
		for (Method method : getList) {
			if(java.lang.Object.class.equals(method.getDeclaringClass()))	//过滤java.lang.Object的方法
					continue;
			String fieldName = StringUtils.uncapitalize(method.getName().substring(3));				
			
			columnNames[i] = fieldName;
			i++;
		}
		
		columnNames = (String[])ArrayUtils.removeElement(columnNames, null);
		
		this.metadata = new DataSetMetadata(columnNames);		
	}
	
	public ObjectDataSet(List<T> rsList){		
		this.populate(rsList);		
	}
	
	public void populate(List<T> rsList)
	{
		this.rsList.clear();
		if(!rsList.isEmpty())
		{			
			this.rsList = rsList;
		}
	}
	
	/**
	 * 导入Map.
	 * 基于Annotation方式的对象转换
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param dataList the data list
	 * @return the object data set
	 */
	public <K, V> ObjectDataSet<T> populateMap(List<Map<K, V>> dataList)  
	{	
		this.rsList.clear();
		if(!dataList.isEmpty())
		{						
			for (int i = 0; i < dataList.size(); i++) {
				Map<K, V> rowMap = dataList.get(i);				
				T instance = DataSetUtils.mapping(clazz, rowMap);
				rsList.add(instance)	;					
			}
		}
		return this;
	}
	
	/**
	 * 导入Map并根据映射接口转换为对象T.
	 * 
	 * @param dataList
	 * @param rowMapper
	 */
	public <K, V> ObjectDataSet<T> populateMap(List<Map<K, V>> dataList, RowMapper<T, K, V> rowMapper)
	{		
		this.rsList.clear();
		if(!dataList.isEmpty())
		{					
			for (int i = 0; i < dataList.size(); i++) {
				Map<K, V> rowMap = dataList.get(i);				
				T obj = rowMapper.mapRow(rowMap, i);				
				this.rsList.add(obj);					
			}
		}
		return this;
	}	
	
	public IDataSet<T> addRow(T row)
	{
		rsList.add(row);
		return this;
	}
	
	public T getRow()
	{
		return rsList.get(_index);
	}
	
	public T getRow(int index)
	{
		return rsList.get(index);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#next()
	 */
	public boolean next()
    {
        _index++;
        return hasNext();
    }	
	
	public boolean hasNext()
	{
		return !rsList.isEmpty() && _index < rsList.size();
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#first()
	 */
	public void first()
    {
        _index = -1;
    }
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(java.lang.String)
	 */
	public Object getValue(String columnName)
	{
		return getValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(java.lang.String, java.lang.Class)
	 */
	public <E> E getValue(String columnName, Class<E> clazz)
	{
		return getValue(this.getRowIndex(), columnName, clazz);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getIntegerValue(java.lang.String)
	 */
	public Integer getIntegerValue(String columnName)
	{		
		return getIntegerValue(getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getLongValue(java.lang.String)
	 */
	public Long getLongValue(String columnName)
	{		
		return getLongValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getFloatValue(java.lang.String)
	 */
	public Float getFloatValue(String columnName)
	{		
		return getFloatValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getDoubleValue(java.lang.String)
	 */
	public Double getDoubleValue(String columnName)
	{		
		return getDoubleValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getBooleanValue(java.lang.String)
	 */
	public Boolean getBooleanValue(String columnName)
	{		
		return getBooleanValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getDateValue(java.lang.String)
	 */
	public Date getDateValue(String columnName)
	{		
		return getDateValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getStringValue(java.lang.String)
	 */
	public String getStringValue(String columnName)
	{		
		return getStringValue(this.getRowIndex(), columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String)
	 */
	public Object getValue(int row, String columnName)
	{
		T rowObject = rsList.get(row);		
		return new Mirror().on(rowObject).get().field(columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String, java.lang.Class)
	 */
	public <E> E getValue(int row, String columnName, Class<E> clazz)
	{
		T rowObject = rsList.get(row);		
		return (E)new Mirror().on(rowObject).get().field(columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getIntegerValue(int, java.lang.String)
	 */
	public Integer getIntegerValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastInteger(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getLongValue(int, java.lang.String)
	 */
	public Long getLongValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastLong(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getFloatValue(int, java.lang.String)
	 */
	public Float getFloatValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastFloat(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getDoubleValue(int, java.lang.String)
	 */
	public Double getDoubleValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastDouble(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getBooleanValue(int, java.lang.String)
	 */
	public Boolean getBooleanValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastBoolean(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getDateValue(int, java.lang.String)
	 */
	public Date getDateValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastDate(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getStringValue(int, java.lang.String)
	 */
	public String getStringValue(int row, String columnName)
	{		
		Object value = this.getValue(row, columnName);
		return DataSetUtils.typeCastString(value);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getRowIndex()
	 */
	public int getRowIndex()
	{
		return _index;
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getRowCount()
	 */
	public int getRowCount()
	{
		return rsList.size();
	}
	
	public List<T> toList()
	{
		return rsList;
	}

	/**
	 * @return the metaData
	 */
	public DataSetMetadata getMetaData() {
		return metadata;
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> iterator = new  Iterator<T>(){
			 
			 private int cursor = 0;

			@Override
			public boolean hasNext() {		
				return !rsList.isEmpty() && cursor < rsList.size();
			}

			@Override
			public T next() {			
			     return rsList.get(cursor++);
			}

			@Override
			public void remove() {
				rsList.remove(cursor);
			}			 
		 };
		 return iterator;
	}

	@Override
	public DataSetType getType() {
		return DataSetType.OBJCT;
	}
	

}
