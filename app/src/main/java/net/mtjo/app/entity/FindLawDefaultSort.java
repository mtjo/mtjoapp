package net.mtjo.app.entity;

import java.io.Serializable;

public class FindLawDefaultSort implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String name;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
