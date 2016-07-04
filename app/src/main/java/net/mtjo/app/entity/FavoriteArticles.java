package net.mtjo.app.entity;

import com.aframe.utils.StrUtils;

import net.mtjo.app.db.ArticleDBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mtjo on 16-7-4.
 */
public class FavoriteArticles implements Serializable {
    private String id;
    private String uid;
    private String title;
    private String url;
    private String description;
    private String table;
    private String object_id;
    private String createtime;

    public static ArrayList<FavoriteArticles> jsonToList(JSONArray arr) throws JSONException,Exception{
        ArrayList<FavoriteArticles> list = new ArrayList<FavoriteArticles>();
        if(null != arr)
            for(int i=0; i<arr.length(); i++){
                JSONObject json = arr.getJSONObject(i);
            if(null == json) {
                continue;
            }
                FavoriteArticles favoriteArticles = new FavoriteArticles();
                favoriteArticles.setId(json.has("id")? StrUtils.strToString(json.getString("id")):"");
                favoriteArticles.setTitle(json.has("title")? StrUtils.strToString(json.getString("title")):"");
                favoriteArticles.setCreatetime(json.has("createtime")? StrUtils.strToDataFormat(
                        json.getString("createtime"),"yyyy-MM-dd"):"");
                favoriteArticles.setUrl(json.has("url")? StrUtils.strToString(json.getString("url")):"");
                list.add(favoriteArticles);
            }
        return list;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUrl() {
        return url;
    }
}
