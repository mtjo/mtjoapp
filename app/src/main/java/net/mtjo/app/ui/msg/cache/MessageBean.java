package net.mtjo.app.ui.msg.cache;

import com.aframe.database.annotate.Id;
import com.aframe.database.annotate.Table;

@Table(name = "wy_lscn_msg")
public final class MessageBean {
	@Id(column = "id")
    private int id;
	 long createTime;
	 String touid;//接收用户uid，uid=0表示群发
	 String alert;
	 String params;
	 int type; //消息类型，1：webview打卡，2：移动咨询im打卡
	 String fromuid;
	 int num;//未读数
	 long msgid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	public int getNum() {
		return num;
	}
	public void setNum(int count) {
		this.num = count;
	}
	public long getMsgid() {
		return msgid;
	}
	public void setMsgid(long msgid) {
		this.msgid = msgid;
	}
}
