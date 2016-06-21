package net.mtjo.app.entity;

import java.io.Serializable;

import com.aframe.utils.StrUtils;

public class AskLawyerModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9071544790127342637L;
	private String id;
	private String uid;
	private String sort1;
	private String sort2;
	private String title;
	private String content;
	private String createTime;
	private String sCreateTime;
	private String sidInfo;
	private String statusWord;
	
	public void setSidInfo(String sidInfo) {
		this.sidInfo = sidInfo;
	}
	
	public String getSidInfo() {
		return sidInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
		this.sCreateTime = StrUtils.TimeDiff(Long.valueOf(createTime) * 1000);
	}

	public String getsCreateTime() {
		return sCreateTime;
	}

	public void setsCreateTime(String sCreateTime) {
		this.sCreateTime = sCreateTime;
	}

	public String getSort1() {
		return sort1;
	}

	public void setSort1(String sort1) {
		this.sort1 = sort1;
		if(null != sort2){
			this.sidInfo = new StringBuffer(sort1).append("-").append(this.sort2).toString();
		}
	}

	public String getSort2() {
		return sort2;
	}

	public void setSort2(String sort2) {
		this.sort2 = sort2;
		if(null != sort1){
			this.sidInfo = new StringBuffer(sort1).append("-").append(this.sort2).toString();
		}
	}

	public String getStatusWord() {
		return statusWord;
	}

	public void setStatusWord(String statusWord) {
		this.statusWord = statusWord;
	}

}
