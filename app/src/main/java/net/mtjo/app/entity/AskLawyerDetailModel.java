package net.mtjo.app.entity;

import java.io.Serializable;

import com.aframe.utils.StrUtils;

public class AskLawyerDetailModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5038253429992206825L;
	private String id;
	private String fromUid;
	private String toUid;
	private String qid;
	private String createTime;
	private String lawyerImage;
	private String lawyerName;
	private String replyNum;
	private String voice;
	
	public void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}
	
	public String getLawyerName() {
		return lawyerName;
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
	public String getToUid() {
		return toUid;
	}
	public void setToUid(String toUid) {
		this.toUid = toUid;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime =  StrUtils.TimeDiff(Long.valueOf(createTime) * 1000);
	}
	
	public String getLawyerImage() {
		return lawyerImage;
	}
	public void setLawyerImage(String lawyerImage) {
		this.lawyerImage = lawyerImage;
	}

	public String getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
}
