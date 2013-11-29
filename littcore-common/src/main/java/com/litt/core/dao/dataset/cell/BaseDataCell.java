package com.litt.core.dao.dataset.cell;

import com.litt.core.dao.dataset.DatasetConstants.DataType;
import com.litt.core.dao.dataset.IDataCell;
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
public abstract class BaseDataCell implements IDataCell {
	
	private ColumnMetadata columnMetadata;

	public BaseDataCell(ColumnMetadata columnMetadata)
	{
		this.columnMetadata = columnMetadata;
	}
	
	/* (non-Javadoc)
	 * @see com.litt.core.dao.dataset.cell.IDataCell#getColumnMetadata()
	 */
	public ColumnMetadata getColumnMetadata() {
		return columnMetadata;
	}

	/* (non-Javadoc)
	 * @see com.litt.core.dao.dataset.cell.IDataCell#getName()
	 */
	public String getName()
	{
		return this.columnMetadata.getName();
	}	
	
	/* (non-Javadoc)
	 * @see com.litt.core.dao.dataset.cell.IDataCell#getDataType()
	 */
	public DataType getDataType()
	{
		return this.columnMetadata.getDataType();
	}

}
