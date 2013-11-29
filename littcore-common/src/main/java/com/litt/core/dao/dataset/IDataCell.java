package com.litt.core.dao.dataset;

import com.litt.core.dao.dataset.DatasetConstants.DataType;
import com.litt.core.dao.dataset.metadata.ColumnMetadata;

public interface IDataCell {

	/**
	 * @return the columnMetadata
	 */
	public ColumnMetadata getColumnMetadata();

	public String getName();

	public DataType getDataType();
	
	public Object getValue();

}