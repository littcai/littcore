package com.litt.core.dao.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.litt.core.dao.dataset.exception.DatasetException;
import com.litt.core.util.Assert;

/**
 * 数据行.
 * 
 * <pre><b>描述：</b>
 *    存放具有数据，每个实例对应一行数据
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
public class DataRow implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** 空行. */
	public static final DataRow EMPTY = new DataRow(0);
	
	private int rowIndex;
	
	/** 按列顺序存放和查询数据单元. */
	private List<DataCell> cellsByIndex = null;
	
	/** 按列名存放和查询数据单元. */
	private Map<String, DataCell> cellsByName = null;
	
	/**
	 * Instantiates a new data row.
	 */
	public DataRow(int rowIndex)
	{
		this.rowIndex = rowIndex;
		//未知列数量初始化
		this.cellsByIndex = new ArrayList<DataCell>();
		this.cellsByName = new HashMap<String, DataCell>();
	}
	
	/**
	 * Instantiates a new data row.
	 *
	 * @param initialCapacity 初始容量
	 */
	public DataRow(int rowIndex, int initialCapacity)
	{
		Assert.isTrue(initialCapacity<1, "Initial capacity must at least greater than 0"); 
		
		this.rowIndex = rowIndex;
		//未知列数量初始化
		this.cellsByIndex = new ArrayList<DataCell>(initialCapacity);
		this.cellsByName = new HashMap<String, DataCell>(initialCapacity);
	}
	
	/**
	 * Adds the cell.
	 *
	 * @param cell the cell
	 * @return the data row
	 */
	public DataRow addCell(DataCell cell)
	{		
		validate(cell);
		
		this.cellsByIndex.add(cell);
		this.cellsByName.put(cell.getName(), cell);
		return this;
	}	
	
	/**
	 * Adds the cell.
	 *
	 * @param cell the cell
	 * @param index the index
	 * @return the data row
	 */
	public DataRow addCell(DataCell cell, int index)
	{
	    if (cell.getName() != null) {
	    	this.validate(cell);	    	
	        this.cellsByName.put(cell.getName(), cell);	        
	    }
	    this.cellsByIndex.add(index, cell);
	    return this;
    }
	
	/**
	 * 校验cell是否能加入.
	 *
	 * @param cell the cell
	 */
	private void validate(DataCell cell) {
		DataCell oldCell = this.cellsByName.get(cell.getName());
        if (oldCell != null)
            throw new DatasetException("A cell with the name '" + cell.getName()
                    + "' already exists. Failed to add cell.");
	}
	
	/**
	 * Gets the cell.
	 *
	 * @param name the name
	 * @return the cell
	 */
	public DataCell getCell(String name)
	{
		return this.cellsByName.get(name);
	}
	
	/**
	 * Removes the cell.
	 *
	 * @param name the name
	 * @return the data row
	 */
	public DataRow removeCell(String name)
	{		
		DataCell removedCell = this.cellsByName.remove(name);
		if(removedCell!=null)
		{
			Iterator<DataCell> i = this.cellsByIndex.iterator();
            while (i.hasNext()) {
                if (name.equals(i.next().getName())) {
                    i.remove();
                    break;
                }
            }
		}
		return this;
	}
	
	/**
	 * Gets the cell.
	 *
	 * @param index the index
	 * @return the cell
	 */
	public DataCell getCell(int index)
	{
		return this.cellsByIndex.get(index);
	}
	
	/**
	 * Removes the cell.
	 *
	 * @param index the index
	 * @return the data row
	 */
	public DataRow removeCell(int index)
	{
		DataCell removed = null;
	    if (this.cellsByIndex.size() > index && this.cellsByIndex.get(index) != null) {
	        removed = this.cellsByIndex.remove(index);
	        if (removed.getName() != null)
	            this.cellsByName.remove(removed.getName());
	    }
		return this;
	}
	
	/**
	 * Replace cell.
	 *
	 * @param cell the cell
	 * @return the data row
	 */
	public DataRow replaceCell(DataCell cell) 
	{
		DataCell foundCell = null;
	    if (cell.getName() != null) {
	        foundCell = this.cellsByName.put(cell.getName(), cell);
	        if (foundCell != null) {
	            Iterator<DataCell> i = this.cellsByIndex.iterator();
	            while (i.hasNext()) {
	                if (cell.getName().equals(i.next().getName()))
	                    i.remove();
	            }
	        }
	    }
	    this.cellsByIndex.add(cell);
	    return this;
	}
	
	 /**
     * Replaces a cell at specified index of a line. First cell has index 0.<br>
     * Note that if the line contains another cell with the name same name as the supplied cell,
     * both that cell and the cell at the specified index will be removed. This can lead to quite
     * unexpected behavior since this also affects the index of all cells to the left of the second
     * removed cell.
     * 
     * @param cell
     *            The cell to add
     * @param index
     *            The index the cell will have in the line.
     * @return The replaced cell (at the index) or null if there were no cell within the line at
     *         that index.
     */
    public DataRow replaceCell(DataCell cell, int index) 
    {
    	DataCell removed = null;
        if (this.cellsByIndex.size() > index) {
            removed = this.cellsByIndex.set(index, cell);
            if (removed.getName() != null) {
                this.cellsByName.remove(removed.getName());
            }
        }
        if (cell.getName() != null) {
        	DataCell second = this.cellsByName.put(cell.getName(), cell);
            if (second != null && second != removed) {
                Iterator<DataCell> i = this.cellsByIndex.iterator();
                while (i.hasNext()) {
                	DataCell current = i.next();
                    if (cell.getName().equals(current.getName()) && cell != current) {
                        i.remove();
                        break;
                    }
                }
            }
        }
        return this;
    }
    
    /**
     * Checks if is cell.
     *
     * @param name the name
     * @return true, if is cell
     */
    public boolean isCell(String name)
    {
    	return this.getCell(name) != null ? true : false;
    }

    /**
     * Gets the cell count.
     *
     * @return the cell count
     */
    public int getCellCount()
    {
    	return this.cellsByIndex.size();
    }

	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

}
