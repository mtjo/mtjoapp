package net.mtjo.app.entity;

import java.io.Serializable;

public class AskLawyerDetailChat implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1398883231583903557L;
	private String id;
	private String fromUid;
	private String idtoUid;
	private String qid;
	private String voiceType;
	private String voice;
	private String createTime;
	private String readTime;
	private String status;
	private String audited;
	private String url;
	private String username;
	private boolean isIssending;
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFromUid() {
		return fromUid;
	}
	public void setFromUid(String fromUid) {
		this.fromUid = fromUid;
	}
	public String getIdtoUid() {
		return idtoUid;
	}
	public void setIdtoUid(String idtoUid) {
		this.idtoUid = idtoUid;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getVoiceType() {
		return voiceType;
	}
	public void setVoiceType(String voiceType) {
		this.voiceType = voiceType;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReadTime() {
		return readTime;
	}
	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAudited() {
		return audited;
	}
	public void setAudited(String audited) {
		this.audited = audited;
	}
	public boolean isIssending() {
		return isIssending;
	}
	public void setIssending(boolean isIssending) {
		this.isIssending = isIssending;
	}

}
