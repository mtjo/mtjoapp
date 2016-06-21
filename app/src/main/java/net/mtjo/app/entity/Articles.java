package net.mtjo.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.utils.StrUtils;
import net.mtjo.app.db.ArticleDBManager;

public class Articles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000002L;
	private String id;//
	private String title;	//文章标题
	private String thumb;	//文章缩略图
	private String description;	//文章描述
	private String updatetime;	//更新时间
	private String url;			//文章详情地址
	private int isRead = 0;//是否已读 0 未读 1 已读
	private String catname;
	
	public static ArrayList<Articles> jsonToList(JSONArray arr, ArticleDBManager manager) throws JSONException,Exception{
		ArrayList<Articles> list = new ArrayList<Articles>();
		if(null != arr)
			for(int i=0; i<arr.length(); i++){
				JSONObject json = arr.getJSONObject(i);
				if(null == json){
					continue;
				}
				Articles article = new Articles();
				article.setId(json.has("id")?
						StrUtils.strToString(json.getString("id")):"");
				article.setTitle(json.has("title")? 
						StrUtils.strToString(json.getString("title")):"");
				article.setThumb(json.has("thumb")? 
						StrUtils.strToString(json.getString("thumb")):"");
				article.setDescription(json.has("description")? 
						StrUtils.strToString(json.getString("description")).replaceAll(" ", "").replaceAll("　", ""):"");
				article.setUrl(json.has("url")? 
						StrUtils.strToString(json.getString("url")):"");
				article.setUpdatetime(json.has("updatetime")?
						StrUtils.strToDataFormat(json.getString("updatetime"), "yyyy-MM-dd"):"");
				article.setCatname(json.has("catname")?
						StrUtils.strToString(json.getString("catname")):"");
				list.add(article);
			}
		
		if (manager == null) {
			return list;
		}
		return manager.getArticleListType(list);
	}
	
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	
	public int getIsRead() {
		return isRead;
	};
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setCatname(String catname) {
		this.catname = catname;
	}
	
	public String getCatname() {
		return catname;
	}
}
