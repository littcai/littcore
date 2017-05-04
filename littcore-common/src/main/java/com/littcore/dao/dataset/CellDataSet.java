package com.littcore.dao.dataset;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.littcore.dao.dataset.metadata.ColumnMetadata;
import com.littcore.dao.dataset.metadata.DataSetMetadata;

/**
 * 自定义结构结果集.
 * 
 * <pre><b>Description：</b> 
 *    
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
public class CellDataSet implements IDataSet<DataRow> {	

	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(CellDataSet.class);	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The meta data. */
	private DataSetMetadata metadata;
	
	/** The rs list. */
	private List<DataRow> rsList = new ArrayList<DataRow>();	
	
	public CellDataSet(DataSetMetadata metadata)
	{
		this.metadata = metadata;
	}
	
	public CellDataSet(DataSetMetadata metadata, List<DataRow> rsList){
		this(metadata);
		this.rsList = rsList;
	}
	
	/**
	 * 导入DataCell.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param dataList the data list
	 * @return the object data set
	 */
	public <K, V> CellDataSet populateMap(List<Map<K, V>> dataList)  
	{	
		this.rsList.clear();
		if(!dataList.isEmpty())
		{
			for (int i = 0; i < dataList.size(); i++) {
				Map<K, V> rowMap = dataList.get(i);				
				
				DataRow dataRow = new DataRow(i);
				for(int n=0;n<metadata.getColumnCount();n++)
				{
					ColumnMetadata columnMetadata = metadata.getColumnMetadata(n);
					dataRow.addCell(new DataCell(columnMetadata, rowMap.get(columnMetadata.getName())));
				}
				rsList.add(dataRow);					
			}
		}
		return this;
	}
		
	public IDataSet<DataRow> addRow(DataRow dataRow)
	{
		rsList.add(dataRow);
		return this;
	}
	
	public DataRow getRow(int index)
	{
		return rsList.get(index);
	}	
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String)
	 */
	public Object getValue(int row, String columnName)
	{
		DataRow dataRow = rsList.get(row);
		return dataRow.getCell(columnName).getValue();
	}
	
	/* (non-Javadoc)
	 * @see com.littcore.dao.dataset.IDataSet#getValue(int, java.lang.String, java.lang.Class)
	 */
	public <E> E getValue(int row, String columnName, Class<E> clazz)
	{
		return (E)this.getValue(row, columnName);
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
		return metadata;
	}

	@Override
	public Iterator<DataRow> iterator() {
		 Iterator<DataRow> iterator = new  Iterator<DataRow>(){
			 
			private int cursor = 0;

			@Override
			public boolean hasNext() {		
				return !rsList.isEmpty() && cursor < rsList.size();
			}

			@Override
			public DataRow next() {			
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
		return DataSetType.DATACELL;
	}
	
	public List<DataRow> toList()
	{
		return rsList;
	}
}
