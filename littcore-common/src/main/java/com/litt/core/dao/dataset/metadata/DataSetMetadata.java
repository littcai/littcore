package com.litt.core.dao.dataset.metadata;

import java.io.Serializable;

import com.litt.core.util.Assert;

/**
 * 结果集元数据.
 * 
 * <pre><b>Description：</b>
 *    可获取结果集对象的字段名，字段类型
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
public class DataSetMetadata implements Serializable {
	
	private ColumnMetadata[] columnMetadatas;
	
	private String[] columnNames;
	
	public DataSetMetadata()
	{
		columnMetadatas = new ColumnMetadata[0];
		columnNames = new String[0];
	}
	
	/**
	 * 通过列名初始化.
	 * 注：缺少必要的数据类型
	 *
	 * @param columnNames the column names
	 */
	public DataSetMetadata(String[] columnNames)
	{
		ColumnMetadata[] columnMetadatas = new ColumnMetadata[columnNames.length];
		for(int i=0;i<columnNames.length;i++)
		{
			columnMetadatas[i] = new ColumnMetadata(columnNames[i]);
			columnMetadatas[i].setIndex(i);
		}
		this.columnMetadatas = columnMetadatas;
		this.columnNames = columnNames;
	}
	
	public DataSetMetadata(ColumnMetadata[] columnMetadatas)
	{
		String[] columnNames = new String[columnMetadatas.length];
		for(int i=0;i<columnMetadatas.length;i++)
		{
			columnNames[i] = columnMetadatas[i].getName();
		}
		this.columnMetadatas = columnMetadatas;
		this.columnNames = columnNames;		
	}
	
	/**
	 * Gets the column count.
	 *
	 * @return the column count
	 */
	public int getColumnCount()
	{
		return columnMetadatas.length;
	}
	
	public ColumnMetadata getColumnMetadata(int i)
	{
		Assert.isTrue(i>=columnMetadatas.length, "Invalid column index:"+i);
		
		return columnMetadatas[i];
	}
	
	public String getColumnName(int i)
	{		
		return columnNames[i];
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

}
