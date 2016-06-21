package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.List;

import com.aframe.json.parse.ParseJson;

/**
 * 用户未读数
 */
public class UnReadMark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12345711L;
	
	private int num;
	private String name;
	private List<UnReadMark> children;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<UnReadMark> getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = ParseJson.getEntityList(children, UnReadMark.class);
	}
}
