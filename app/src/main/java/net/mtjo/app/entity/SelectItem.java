package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.utils.StrUtils;

public class SelectItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000000L;
	
	private String id;
	private String name;
	private ArrayList<SelectItem> childs;
	private SelectItem parent; //只用于选择结果返回
	
	/**
	 * 获取城市列表
	 * @throws JSONException,Exception 
	 */
	public static ArrayList<SelectItem> cityJSONToList(JSONObject obj) throws JSONException,Exception{
		ArrayList<SelectItem> citys = new ArrayList<SelectItem>();
		if(null != obj){
			@SuppressWarnings("unchecked")
			Iterator<String> it = obj.keys();
			while(it.hasNext()){
				String key = it.next().toString();
				JSONObject pObj = obj.getJSONObject(key);
				if(null == pObj) continue;
				SelectItem province = new SelectItem();
				if(pObj.has("province")){
					JSONObject proObj = pObj.getJSONObject("province");
					if(null == proObj)continue;
					province.setId(proObj.has("id")? StrUtils.strToString(proObj.getString("id")):"");
					province.setName(proObj.has("name")? StrUtils.strToString(proObj.getString("name")):"");
				}
				citys.add(province);
				
				if(pObj.has("citys")){
					JSONObject cObj = pObj.getJSONObject("citys");
					if(null == cObj)continue;
					ArrayList<SelectItem> chil = new ArrayList<SelectItem>();
					@SuppressWarnings("unchecked")
					Iterator<String> cit = cObj.keys();
					while(cit.hasNext()){
						String ckey = cit.next().toString();
						JSONArray arr = cObj.getJSONArray(ckey);
						if(null == arr)continue;
						if(arr.length() < 3)continue;
						SelectItem city = new SelectItem();
						city.setId(StrUtils.strToString(arr.getString(0)));
						city.setName(StrUtils.strToString(arr.getString(2)));
						chil.add(city);
					}
					
					if(chil.size() > 0)
						province.setChilds(chil);
				}
			}
		}
		return citys;
	}
	
	/**
	 * 获取服务类型列表
	 * @throws JSONException,Exception 
	 */
	public static ArrayList<SelectItem> typeJSONToList(JSONArray json) throws JSONException,Exception{
		ArrayList<SelectItem> skills = new ArrayList<SelectItem>();
		for (int i = 0; i < json.length(); i++) {
			SelectItem skill = new SelectItem();
			JSONObject obj = json.getJSONObject(i);
			if(null == obj){
				continue;
			}
			skill.setId(obj.has("id")?
					StrUtils.strToString(obj.getString("id")):"");
			skill.setName(obj.has("name")? 
					StrUtils.strToString(obj.getString("name")):"");
			obj.getJSONArray("special_sub");
			skills.add(skill);
			
			if(obj.has("special_sub")){
				JSONArray cArr = obj.getJSONArray("special_sub");
				if(null == cArr || cArr.length() == 0) continue;
				ArrayList<SelectItem> childs = new ArrayList<SelectItem>();
				for(int j=0; j< cArr.length(); j++){
					JSONArray subArr = cArr.getJSONArray(j);
					if(null == subArr || subArr.length()<2)continue;
					SelectItem child = new SelectItem();
					child.setId(StrUtils.strToString(subArr.getString(0)));
					child.setName(StrUtils.strToString(subArr.getString(1)));
					childs.add(child);
				}
				if(childs.size() > 0)
					skill.setChilds(childs);
			}
		}
		
		
		
		
		
//		if(null != json){
//			@SuppressWarnings("unchecked")
//			Iterator<String> it = json.keys();
//			while(it.hasNext()){
//				String key = it.next().toString();
//				if(null == key)continue;
//				JSONObject pObj = json.getJSONObject(key);
//				if(null == pObj) continue;
//				SelectItem skill = new SelectItem();
//				skill.setId(pObj.has("id")? StrUtils.strToString(pObj.getString("id")): "");
//				skill.setName(pObj.has("name")? StrUtils.strToString(pObj.getString("name")): "");
//				skills.add(skill);
//				
//				if(pObj.has("special_sub")){
//					JSONArray cArr = pObj.getJSONArray("special_sub");
//					if(null == cArr || cArr.length() == 0) continue;
//					ArrayList<SelectItem> childs = new ArrayList<SelectItem>();
//					for(int i=0; i< cArr.length(); i++){
//						JSONArray subArr = cArr.getJSONArray(i);
//						if(null == subArr || subArr.length()<2)continue;
//						SelectItem child = new SelectItem();
//						child.setId(StrUtils.strToString(subArr.getString(0)));
//						child.setName(StrUtils.strToString(subArr.getString(1)));
//						childs.add(child);
//					}
//					if(childs.size() > 0)
//						skill.setChilds(childs);
//				}
//			}
//		}
		return skills;
	}
	
	/**
	 * 获取涉及金额列表
	 * @throws JSONException,Exception 
	 */
	public static ArrayList<SelectItem> amountJSONToList(JSONArray arr) throws JSONException,Exception{
		ArrayList<SelectItem> amounts = new ArrayList<SelectItem>();
		if(null != arr){
			for(int i = 0; i < arr.length(); i++){
				JSONObject json = arr.getJSONObject(i);
				if(null == json){
					continue;
				}
				SelectItem item = new SelectItem();
				item.setId(json.has("id")?StrUtils.strToString(json.getString("id")):"");
				item.setName(json.has("name")?StrUtils.strToString(json.getString("name")):"");
				amounts.add(item);
			}
		}
		return amounts;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<SelectItem> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<SelectItem> childs) {
		this.childs = childs;
	}

	public SelectItem getParent() {
		return parent;
	}

	public void setParent(SelectItem parent) {
		this.parent = parent;
	}
}
