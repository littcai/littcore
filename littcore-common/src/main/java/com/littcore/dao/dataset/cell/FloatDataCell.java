package com.littcore.dao.dataset.cell;

import com.littcore.dao.dataset.metadata.ColumnMetadata;

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
public class FloatDataCell extends BaseDataCell {
		
	private Float value;
	
	public FloatDataCell(ColumnMetadata columnMetadata, Float value)
	{
		super(columnMetadata);
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
	}

}
