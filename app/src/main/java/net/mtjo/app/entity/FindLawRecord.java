package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.utils.StrUtils;

public class FindLawRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12222222L;
	
	private String id;
	private String lawyerId;
	private String content;
	private String amount;
	private String lawyerName;
	private String specialName;
	private String areaName;
	private String photo;
	private String good;
	private String lawyerRoom;
	
	public static ArrayList<FindLawRecord> jsonArrtoList(JSONArray arr) throws JSONException, Exception{
		ArrayList<FindLawRecord> records = new ArrayList<FindLawRecord>();
		if(null != arr){
			for(int i=0; i<arr.length(); i++){
				JSONObject json = arr.getJSONObject(i);
				if(null == json){
					continue;
				}
				FindLawRecord record = new FindLawRecord();
				record.setId(json.has("id")?StrUtils.strToString(json.getString("id")):"");
				record.setLawyerId(json.has("lawyerId")?StrUtils.strToString(json.getString("lawyerId")):"");
				record.setContent(json.has("content")?StrUtils.strToString(json.getString("content")):"");
				record.setAmount(json.has("amount")?
						(StrUtils.strToDounle(json.getString("amount"))>0? 
								StrUtils.strToString(json.getString("amount"))+"万元":"无"):"无");
				record.setLawyerName(json.has("lawyerName")?StrUtils.strToString(json.getString("lawyerName")):"");
				record.setSpecialName(json.has("specialName")?StrUtils.strToString(json.getString("specialName")):"");
				record.setAreaName(json.has("areaName")?StrUtils.strToString(json.getString("areaName")):"");
				record.setPhoto(json.has("lawyerImage")?StrUtils.strToString(json.getString("lawyerImage")):"");
				record.setGood(json.has("lawyerGood")?StrUtils.strToString(json.getString("lawyerGood")):"");
				if (record.getGood() == null || "".equals(record.getGood())) {
					record.setGood("100%");
				}
				record.setLawyerRoom(json.has("lawyerRoom")?StrUtils.strToString(json.getString("lawyerRoom")):"");
				records.add(record);
			}
		}
		return records;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLawyerId() {
		return lawyerId;
	}
	public void setLawyerId(String lawyerId) {
		this.lawyerId = lawyerId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLawyerName() {
		return lawyerName;
	}
	public void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getLawyerRoom() {
		return lawyerRoom;
	}

	public void setLawyerRoom(String lawyerRoom) {
		this.lawyerRoom = lawyerRoom;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
