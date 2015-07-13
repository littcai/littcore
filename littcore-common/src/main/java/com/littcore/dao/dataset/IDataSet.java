package com.littcore.dao.dataset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.littcore.dao.dataset.metadata.DataSetMetadata;

public interface IDataSet<T> extends Serializable, Iterable<T>{
	
	public static enum DataSetType{MAP, OBJCT, DATACELL}
	
	public DataSetType getType();		

	/**
	 * Gets the value.
	 *
	 * @param row the row
	 * @param columnName the column name
	 * @return the value
	 */
	public Object getValue(int row, String columnName);

	/**
	 * Gets the value.
	 *
	 * @param row the row
	 * @param columnName the column name
	 * @return the value
	 */
	public <T> T getValue(int row, String columnName, Class<T> clazz);

	public Integer getIntegerValue(int row, String columnName);

	public Long getLongValue(int row, String columnName);

	public Float getFloatValue(int row, String columnName);

	public Double getDoubleValue(int row, String columnName);

	public Boolean getBooleanValue(int row, String columnName);

	public Date getDateValue(int row, String columnName);

	public String getStringValue(int row, String columnName);
	
	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	public int getRowCount();
	
	public T getRow(int index);
	
	/**
	 * @return the metaData
	 */
	public DataSetMetadata getMetaData();
	
	public List<T> toList();

}