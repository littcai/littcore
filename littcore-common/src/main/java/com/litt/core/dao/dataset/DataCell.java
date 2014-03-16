package com.litt.core.dao.dataset;

import java.math.BigDecimal;
import java.util.Date;

import com.litt.core.dao.dataset.DatasetConstants.DataType;
import com.litt.core.dao.dataset.metadata.ColumnMetadata;

/**
 * 数据单元.
 * 
 * <pre><b>Description：</b>
 *    每个实例对应某一行某一列的数据
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
public class DataCell<T> implements IDataCell {
	
	private ColumnMetadata columnMetadata;
	
	private T value;
	
	public DataCell(ColumnMetadata columnMetadata)
	{
		this.columnMetadata = columnMetadata;
	}
	
	public DataCell(ColumnMetadata columnMetadata, T value)
	{
		this.columnMetadata = columnMetadata;
		this.value = value;
	}
	 
	public String getName()
	{
		return this.columnMetadata.getName();
	}	
	
	public DataType getDataType()
	{
		return this.columnMetadata.getDataType();
	}
	
	/**
	 * Gets the string value.
	 *
	 * @return the string value
	 */
	public String getStringValue()
	{
		return String.valueOf(value);
	}
	
	public Boolean getBooleanValue()
	{
		if(DataType.BOOLEAN.equals(this.getDataType()))
		{			
			return (Boolean)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public Integer getIntValue()
	{
		if(DataType.INTEGER.equals(this.getDataType()))
		{			
			return (Integer)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public Long getLongValue()
	{
		if(DataType.LONG.equals(this.getDataType()))
		{			
			return (Long)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public Float getFloatValue()
	{
		if(DataType.FLOAT.equals(this.getDataType()))
		{			
			return (Float)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public Double getDoubleValue()
	{
		if(DataType.DOUBLE.equals(this.getDataType()))
		{			
			return (Double)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public BigDecimal getDecimalValue()
	{
		if(DataType.DECIMAL.equals(this.getDataType()))
		{			
			return (BigDecimal)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}
	
	public Date getDateValue()
	{
		if(DataType.DATE.equals(this.getDataType()))
		{			
			return (Date)value;
		}
		throw new IllegalStateException("Illegal data conversion, real data type is "+this.getDataType());
	}

	/**
	 * @return the columnMetadata
	 */
	public ColumnMetadata getColumnMetadata() {
		return columnMetadata;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

}
