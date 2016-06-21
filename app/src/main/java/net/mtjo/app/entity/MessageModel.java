package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.Loger;
import com.aframe.utils.StrUtils;

public class MessageModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long createTime;
	private String touid;//接收用户uid，uid=0表示群发
	private String alert;
	private String params;
	private int type; //消息类型，1：webview打卡，2：移动咨询im打卡
	private String fromuid;
	private int count; //未读数
	private long msgid;
	private String params_qid;
	private String Params_fromuid;
	private String params_id;
	private String params_url;
	private String sCreateTime;
	
	/**
	 * 按时间排序
	 */
	public static List<MessageModel> compare(List<MessageModel> modelList){
		Collections.sort(modelList, new Comparator<MessageModel>(){
			@Override
			public int compare(MessageModel lhs, MessageModel rhs) {
				return (int) (lhs.getCreateTime() - rhs.getCreateTime());
			}
		});
		return modelList;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
		this.msgid = id;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		this.sCreateTime = StrUtils.TimeDiff(createTime*1000);
	}
	public String getTouid() {
		return touid;
	}
	public void setTouid(String touid) {
		this.touid = touid;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
		try {
			JSONObject json = new JSONObject(params);
			setParams_fromuid(json.has("fromuid")? json.getString("fromuid") : null);
			setParams_qid(json.has("qid")? json.getString("qid") : null);
			setParams_id(json.has("id")? json.getString("id") : null);
			setParams_url(json.has("url")? json.getString("url") : null);
		} catch (JSONException e) {
			Loger.debug("msg params", e);
		} catch (Exception e){
			Loger.debug("msg params", e);
		}
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFromuid() {
		return fromuid;
	}
	public void setFromuid(String fromuid) {
		this.fromuid = fromuid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getMsgid() {
		return msgid;
	}
	public void setMsgid(long msgid) {
		this.msgid = msgid;
	}
	public String getParams_qid() {
		return params_qid;
	}
	public void setParams_qid(String params_qid) {
		this.params_qid = params_qid;
	}
	public String getParams_fromuid() {
		return Params_fromuid;
	}
	public void setParams_fromuid(String params_fromuid) {
		Params_fromuid = params_fromuid;
	}
	public String getParams_id() {
		return params_id;
	}
	public void setParams_id(String params_id) {
		this.params_id = params_id;
	}
	public String getParams_url() {
		return params_url;
	}
	public void setParams_url(String params_url) {
		this.params_url = params_url;
	}
	public String getsCreateTime() {
		return sCreateTime;
	}
	public void setsCreateTime(String sCreateTime) {
		this.sCreateTime = sCreateTime;
	}
}
