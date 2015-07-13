package com.littcore.dao.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.dao.dataset.IDataSet.DataSetType;
import com.littcore.dao.dataset.metadata.DataSetMetadata;

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
public class MapDataSet<K extends Serializable, V> implements IDataSet<Map<K, V>> {	

	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(MapDataSet.class);	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private DataSetMetadata metaData;
	
	/** The rs list. */
	private List<Map<K, V>> rsList = new ArrayList<Map<K, V>>();
	
	public MapDataSet(){}
	
	public MapDataSet(List<Map<K, V>> rsList){
		this.populate(rsList);		
	}
	
	public void populate(List<Map<K, V>> rsList)
	{
		if(!rsList.isEmpty())
		{
			Map<K, V> rowMap = rsList.get(0);
			if(metaData==null)	//如果元数据不存在则使用Map的字段名
			{	
				metaData = new DataSetMetadata();
				String[] columnNames = rowMap.keySet().toArray(new String[0]);
				metaData.setColumnNames(columnNames);
			}
			this.rsList = rsList;
		}
	}
	
	public IDataSet addRow(Map<K, V> rowMap)
	{
		rsList.add(rowMap);
		return this;
	}
	
	public Map<K, V> getRow(int index)
	{
		return rsList.get(index);
	}	
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String)
	 */
	public Object getValue(int row, String columnName)
	{
		Map<K, V> rowMap = rsList.get(row);
		return rowMap.get(columnName);
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String, java.lang.Class)
	 */
	public <E> E getValue(int row, String columnName, Class<E> clazz)
	{
		Map<K, V> rowMap = rsList.get(row);
		return (E)rowMap.get(columnName);
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
	 * @see com.littcore.dao.dataset.IDataSet#getRowCount()
	 */
	public int getRowCount()
	{
		return rsList.size();
	}

	/**
	 * @return the metaData
	 */
	public DataSetMetadata getMetaData() {
		return metaData;
	}

	@Override
	public Iterator<Map<K, V>> iterator() {
		 Iterator<Map<K, V>> iterator = new  Iterator<Map<K, V>>(){
			 
			 private int cursor = 0;

			@Override
			public boolean hasNext() {		
				return !rsList.isEmpty() && cursor < rsList.size();
			}

			@Override
			public Map<K, V> next() {			
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
		return DataSetType.MAP;
	}
	
	public List<Map<K, V>> toList()
	{
		return rsList;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(DataSetMetadata metaData) {
		this.metaData = metaData;
	}
}
