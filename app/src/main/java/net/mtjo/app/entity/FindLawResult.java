package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.utils.StrUtils;

public class FindLawResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000001L;
	
	private String uid;
	private String name;
	private String photo;
	private String lawroom;
	private String area;
	private int good;	//评价
	private int bespeak; //预约数
	private String special;
	private String mobile;
	private boolean isYue = false;
	private List<String> listPre;
	private String areaCode;
	
	
	public static ArrayList<FindLawResult> jsArrToObj(JSONArray arr) throws JSONException,Exception{
		ArrayList<FindLawResult> list = new ArrayList<FindLawResult>();
		if(null != arr)
			for(int i=0; i<arr.length(); i++){
				JSONObject json = arr.getJSONObject(i);
				if(null == json){
					continue;
				}
				
				FindLawResult result = new FindLawResult();
				result.setUid(json.has("uid")?StrUtils.strToString(json.getString("uid")):"");
				result.setName(json.has("name")?StrUtils.strToString(json.getString("name")):"");
				result.setPhoto(json.has("photo")?StrUtils.strToString(json.getString("photo")):"");
				result.setLawroom(json.has("lawyer_room")?StrUtils.strToString(json.getString("lawyer_room")):"");
				result.setMobile(json.has("mobile")?StrUtils.strToString(json.getString("mobile")):"");
				int type = json.has("coin_status")? json.getInt("coin_status") : 0;
				String type1 = json.has("special_status")? json.getJSONObject("special_status").getString("status"):"0";
				if (type == 1 && "1".equals(type1)) {
					result.setYue(true);
				}
				String area_name = "";
				if (json.has("area_name") && json.getString("area_name") != null) {
					String str[] = json.getString("area_name").split(" ");
					if (str.length != 0) {
						area_name = str[str.length - 1];
					}
				}
				
				result.setArea(area_name);
				result.setGood(json.has("good")?StrUtils.strToInt(json.getString("good")):0);
				result.setBespeak(json.has("bespeak")?StrUtils.strToInt(json.getString("bespeak")):0);
				String str = "";
				JSONArray good_at = json.has("good_at")? json.getJSONArray("good_at"):null;
				result.setAreaCode(good_at.getString(0));
				List<String> listVal = new ArrayList<String>();
				result.setAreaCode(good_at.getString(0));
				for (int j = 0; j < good_at.length(); j++) {
					String s = good_at.getString(j);
					
					boolean b = s.matches("[\\d]+(\\.[\\d]+)?");
					
					if (!b) {
						if (listVal.size() < 2) {
							listVal.add(good_at.getString(j));
						}
					}
				}
				
				result.setListPre(listVal);
				if(null != good_at && good_at.length() > 1){
					str = StrUtils.strToString(good_at.getString(1));
				}
				
				
				result.setSpecial(str);
				list.add(result);
			}
		return list;
	}
	
	public static ArrayList<FindLawResult> findlawArrToObj(JSONArray arr) throws JSONException,Exception{
		ArrayList<FindLawResult> list = new ArrayList<FindLawResult>();
		if(null != arr){
			for(int i=0; i<arr.length(); i++){
				JSONObject json = arr.getJSONObject(i);
				if(null == json){
					continue;
				}
				FindLawResult result = new FindLawResult();
				result.setUid(json.isNull("id") ? "": StrUtils.strToString(json.getString("id")));
				result.setName(json.isNull("username") ? "": StrUtils.strToString(json.getString("username")));
				result.setLawroom(json.isNull("lawerroom") ? "": StrUtils.strToString(json.getString("lawerroom")));
				result.setPhoto(json.isNull("photo") ? "": StrUtils.strToString(json.getString("photo")));
				String special = json.isNull("profession") ? "": StrUtils.strToString(json.getString("profession"));
				if(special.length() > 1 && special.indexOf(",") == 0){
					special = special.substring(1);
				}
				
				if (!StrUtils.isEmpty(special)) {
					List<String> listVal = result.getListPre();
					if(listVal == null) listVal = new ArrayList<String>();
					String[] str = special.split(",");
					for (int j = 0; j < str.length; j++) {
						String s = str[j];
						if (j < 3) {
							if (!StrUtils.isEmpty(s)) {
								listVal.add(s);
							}
						}
					}
					result.setListPre(listVal);
				}
				result.setSpecial(special);
				result.setMobile(json.isNull("mobile") ? "": StrUtils.strToString(json.getString("mobile")));
				String province = "",city = "",area = "";
				province = json.isNull("province") ? "": StrUtils.strToString(json.getString("province"));
				city = json.isNull("city") ? "": StrUtils.strToString(json.getString("city"));
//				if(province.length() > 0)
//					area = province + " " + city;
//				else
					area = city;
				result.setArea(area);
				
				list.add(result);
			}
		}
		return list;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getLawroom() {
		return lawroom;
	}
	public void setLawroom(String law_room) {
		this.lawroom = law_room;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
//	public int getNum() {
//		return num;
//	}
//	public void setNum(int num) {
//		this.num = num;
//	}

	public int getBespeak() {
		return bespeak;
	}

	public void setBespeak(int bespeak) {
		this.bespeak = bespeak;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isYue() {
		return isYue;
	}

	public void setYue(boolean isYue) {
		this.isYue = isYue;
	}
	
	public List<String> getListPre() {
		return listPre;
	}
	
	public void setListPre(List<String> listPre) {
		this.listPre = listPre;
	}
	
	
}
