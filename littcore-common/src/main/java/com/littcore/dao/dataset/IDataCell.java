package com.littcore.dao.dataset;

import com.littcore.dao.dataset.DatasetConstants.DataType;
import com.littcore.dao.dataset.metadata.ColumnMetadata;

public interface IDataCell {

	/**
	 * @return the columnMetadata
	 */
	public ColumnMetadata getColumnMetadata();

	public String getName();

	public DataType getDataType();
	
	public Object getValue();

}