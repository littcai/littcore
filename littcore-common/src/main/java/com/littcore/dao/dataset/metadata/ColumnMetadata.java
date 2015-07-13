package com.littcore.dao.dataset.metadata;

import java.util.Locale;

import com.littcore.dao.dataset.DatasetConstants.DataType;

/**
 * 数据列元数据.
 * 
 * <pre><b>描述：</b>
 *    描述一列数据的基本信息，如列的索引号，列名，数据类型等.
 * </pre>
 * 
 * <pre><b>修改记录：</b>
 *    
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">蔡源</a>
 * @since 2013-7-4
 * @version 1.0
 */
public class ColumnMetadata {
	
	/** 
	 * 列的索引位置.
	 * 在需要有序遍历列的时候有用
	 */
	private int index = -1;
	
	/** 数据类型(默认String型). */
	private DataType dataType = DataType.STRING;
	
	/** 列名. */
	private String name;
	
	/** 显示名称. */
	private String displayName;
	
	/** 数据格式化模式. */
	private String format;
	
	/** 本地化. */
	private Locale locale;
	
	public ColumnMetadata(String name)
	{
		this.name = name;
		this.displayName = name;
	}
	
	public ColumnMetadata(String name, String displayName)
	{
		this.name = name;
		this.displayName = displayName;
	}
	
	public ColumnMetadata(String name, String displayName, DataType dataType)
	{
		this.name = name;
		this.displayName = displayName;
		this.dataType = dataType;
	}
	
	public ColumnMetadata(String name, String displayName, DataType dataType, int index)
	{
		this.name = name;
		this.displayName = displayName;
		this.dataType = dataType;
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	

}
