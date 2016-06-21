package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.List;

import com.aframe.json.parse.ParseJson;

public class FindlawCity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 13455622L;
	
	private String id;
	private String province;
	private String city;
	private String grade;
	private List<FindlawCity> citys;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public List<FindlawCity> getCitys() {
		return citys;
	}
	public void setCitys(String citys) {
		this.citys = ParseJson.getEntityList(citys, FindlawCity.class);
	}
}
