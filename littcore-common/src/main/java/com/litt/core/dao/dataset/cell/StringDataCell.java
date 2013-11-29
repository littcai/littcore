package com.litt.core.dao.dataset.cell;

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
public class StringDataCell extends BaseDataCell {
		
	private String value;
	
	public StringDataCell(ColumnMetadata columnMetadata, String value)
	{
		super(columnMetadata);
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
