package net.mtjo.app.entity;

import java.io.Serializable;

/**
 * 用户信息对象
 * @author zxp 
 * 
 *
 */
public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7805630856961024968L;
	private String uid;
	private String mobile;
	private String userid;
	private String username;
	private String ifaudit;
	private String status;
	private String authtoken;
	private String token;
	
//	//未登录所发咨询内容
//	private String content;
//	
//	public String getContent() {
//		return content;
//	}
	
//	public void setContent(String content) {
//		this.content = content;
//	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIfaudit() {
		return ifaudit;
	}
	public void setIfaudit(String ifaudit) {
		this.ifaudit = ifaudit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuthtoken() {
		return authtoken;
	}
	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	

}
