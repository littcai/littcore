package com.litt.core.dao.dataset;

import java.io.Serializable;
import java.util.Date;

import com.litt.core.dao.dataset.annotation.MapColumn;

public class User implements Serializable {
	private int id;
	
	private String name;
	
	@MapColumn(name="date_value")
	private Date date;
	
	public User(){}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
