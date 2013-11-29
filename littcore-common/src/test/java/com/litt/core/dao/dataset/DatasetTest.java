package com.litt.core.dao.dataset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.litt.core.dao.dataset.DatasetConstants.DataType;
import com.litt.core.dao.dataset.metadata.ColumnMetadata;
import com.litt.core.dao.dataset.metadata.DataSetMetadata;

/**
 * .
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
public class DatasetTest extends TestCase {
	
	public void testMapDataSet()
	{
		List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("ID", i);
			rowMap.put("NAME", i+"name");
			rowMap.put("DATE", new Date());
			rsList.add(rowMap);
		}
		
		MapDataSet<String, Object> dataSet = new MapDataSet<String, Object>();
		dataSet.populate(rsList);
		
		for(int i=0;i<dataSet.getRowCount();i++)
		{
			assertEquals(i, dataSet.getIntegerValue(i, "ID").intValue());			
			assertEquals(i+"name", dataSet.getStringValue(i, "NAME"));
			assertEquals(Date.class, dataSet.getValue(i, "DATE", Date.class).getClass());			
		}
		
		Iterator<Map<String, Object>> iterator = dataSet.iterator();
		int j=0;
		while (iterator.hasNext()) {
			Map<java.lang.String, java.lang.Object> map = (Map<java.lang.String, java.lang.Object>) iterator.next();
			assertEquals(j, map.get("ID"));			
			assertEquals(j+"name", map.get("NAME"));
			j++;
		}
		
		DataSetMetadata metaData = dataSet.getMetaData();
		String[] columnNames = metaData.getColumnNames();
		assertEquals(3, columnNames.length);
		
	}
	
	public void testObjectDataSet()
	{
		List<User> rsList = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setId(i);
			user.setName(i+"name");
			user.setDate(new Date());
			rsList.add(user);
		}
		
		ObjectDataSet<User> dataSet = new ObjectDataSet<User>(User.class);
		dataSet.populate(rsList);
		
		int i=0;
		while (dataSet.next()) {
			assertEquals(i, dataSet.getIntegerValue("id").intValue());			
			assertEquals(i+"name", dataSet.getStringValue("name"));
			assertEquals(Date.class, dataSet.getValue("date", Date.class).getClass());
			i++;
		}
		
		DataSetMetadata metaData = dataSet.getMetaData();
		String[] columnNames = metaData.getColumnNames();
		assertEquals(3, columnNames.length);
		
	}
	
	public void testObjectDataSetAnnotation() throws Exception
	{
		System.out.println(User.class.getDeclaredConstructors().length);     
		User test = User.class.newInstance();
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("id", i);
			rowMap.put("name", i+"name");
			rowMap.put("date_value", new Date());
			dataList.add(rowMap);
		}
		
		ObjectDataSet<User> dataSet = new ObjectDataSet<User>(User.class);
		dataSet.populateMap(dataList);
		
		int i=0;
		while (dataSet.next()) {
			User user = dataSet.getRow();
			
			assertEquals(i, user.getId());			
			assertEquals(i+"name", user.getName());
			assertNotNull(user.getDate());
			i++;
		}
		
		DataSetMetadata metaData = dataSet.getMetaData();
		String[] columnNames = metaData.getColumnNames();
		assertEquals(3, columnNames.length);
		
	}

	public void testCellDataSet()
	{
		ColumnMetadata[] columnMetadatas = new ColumnMetadata[4];
		ColumnMetadata columnId = new ColumnMetadata("id", "ID", DataType.INTEGER, 0);
		ColumnMetadata columnName = new ColumnMetadata("name", "Name", DataType.STRING, 1);
		ColumnMetadata columnBirthday = new ColumnMetadata("birthday", "Birthday", DataType.DATE, 2);
		ColumnMetadata columnIsMale = new ColumnMetadata("isMale", "IsMale", DataType.BOOLEAN, 3);
		
		columnMetadatas[0] = columnId;
		columnMetadatas[1] = columnName;
		columnMetadatas[2] = columnBirthday;
		columnMetadatas[3] = columnIsMale;
		
		List<DataRow> rsList = new ArrayList<DataRow>();
		for (int i = 0; i < 4; i++) {
			DataRow dataRow = new DataRow(i);
			dataRow.addCell(new DataCell<Integer>(columnMetadatas[0], i));
			dataRow.addCell(new DataCell<String>(columnMetadatas[1], i+"name"));
			dataRow.addCell(new DataCell<Date>(columnMetadatas[2], new Date()));
			dataRow.addCell(new DataCell<Boolean>(columnMetadatas[3], false));
			rsList.add(dataRow);
		}		
		
		DataSetMetadata metadata = new DataSetMetadata(columnMetadatas);
		CellDataSet dataSet = new CellDataSet(metadata, rsList);
		
		DataSetMetadata metaData = dataSet.getMetaData();
		String[] columnNames = metaData.getColumnNames();
		assertEquals(4, columnNames.length);		
		
		Iterator<DataRow> iter =dataSet.iterator();
		int i=0;
		while (iter.hasNext()) {
			DataRow dataRow = iter.next();
			
			assertEquals(i, dataRow.getCell("id").getIntValue().intValue());			
			assertEquals(i+"name", dataRow.getCell("name").getStringValue());
			assertEquals(Date.class, dataRow.getCell("birthday").getValue().getClass());
			i++;
		}
	}
	
	public void testCellDataSetWithMap()
	{
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("id", i);
			rowMap.put("name", i+"name");
			rowMap.put("birthday", new Date());
			rowMap.put("isMale", false);
			dataList.add(rowMap);
		}		
		
		ColumnMetadata[] columnMetadatas = new ColumnMetadata[4];
		ColumnMetadata columnId = new ColumnMetadata("id", "ID", DataType.INTEGER, 0);
		ColumnMetadata columnName = new ColumnMetadata("name", "Name", DataType.STRING, 1);
		ColumnMetadata columnBirthday = new ColumnMetadata("birthday", "Birthday", DataType.DATE, 2);
		ColumnMetadata columnIsMale = new ColumnMetadata("isMale", "IsMale", DataType.BOOLEAN, 3);
		
		columnMetadatas[0] = columnId;
		columnMetadatas[1] = columnName;
		columnMetadatas[2] = columnBirthday;
		columnMetadatas[3] = columnIsMale;
		
		DataSetMetadata metadata = new DataSetMetadata(columnMetadatas);		
		
		CellDataSet dataSet = new CellDataSet(metadata);
		dataSet.populateMap(dataList);
		
		DataSetMetadata metaData = dataSet.getMetaData();
		String[] columnNames = metaData.getColumnNames();
		assertEquals(4, columnNames.length);		
		
		Iterator<DataRow> iter =dataSet.iterator();
		int i=0;
		while (iter.hasNext()) {
			DataRow dataRow = iter.next();
			
			assertEquals(i, dataRow.getCell("id").getIntValue().intValue());			
			assertEquals(i+"name", dataRow.getCell("name").getStringValue());
			assertEquals(Date.class, dataRow.getCell("birthday").getValue().getClass());
			i++;
		}
	}
	
}
