package net.mtjo.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aframe.core.WYTaskExecutor;
import com.aframe.json.parse.ParseJson;
import net.mtjo.app.entity.FindlawCity;
import net.mtjo.app.entity.SelectItem;

import android.app.Activity;
import android.content.Context;

/**
 * @author Moyaofen
 * 找法网城市列表，公开咨询加载
 */
public class FindLawUtils {

    public void readCityList(Activity aty,FindlawCallBact callback){
    	new LoadTask(aty, callback, 0).execute();
    }
    
    public void readTypeList(Activity aty,FindlawCallBact callback){
    	new LoadTask(aty, callback, 1).execute();
    }

    public static String readAssertsFile(String fileName, Context context) throws IOException, Exception{
        String result = "";
        InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";
        while ((line = bufReader.readLine()) != null) {
            result += line;
        }
        return result;
    }
    
    private class LoadTask extends WYTaskExecutor<Void, Void, Object>{
    	private Activity aty;
    	private FindlawCallBact callback;
    	private int task; //0为加载城市，1为加载咨询类型
    	
    	/**
    	 * @param task
    	 * 			0为加载城市，1为加载咨询类型
    	 */
    	public LoadTask(Activity aty, FindlawCallBact callback, int task){
    		this.aty = aty;
    		this.callback = callback;
    		this.task = task;
    	}

		@Override
		protected Object doInBackground(Void... params) {
			List list = null;
			try {
				if(task == 0){
					String json = readAssertsFile("arealist.json", aty);
					list = ParseJson.getEntityList(json, FindlawCity.class);
				}else{
					list = new ArrayList();
					
					String json = readAssertsFile("type.json", aty);
					if (json != null && json.startsWith("\ufeff")) {
						json = json.substring(1);
					}
					JSONArray arr = new JSONArray(json);
					for(int i = 0; i < arr.length(); i++){
						JSONObject obj = arr.getJSONObject(i);
						SelectItem item = new SelectItem();
						item.setId(obj.getString("id"));
						item.setName(obj.getString("name"));
						if(obj.getJSONArray("types").length() > 0){
							ArrayList<SelectItem> childs = new ArrayList<SelectItem>();
							JSONArray carr = obj.getJSONArray("types");
							for(int j = 0; j<carr.length();j++){
								JSONObject cobj = carr.getJSONObject(j);
								SelectItem citem = new SelectItem();
								citem.setId(cobj.getString("id"));
								citem.setName(cobj.getString("name"));
								childs.add(citem);
							}
							item.setChilds(childs);
						}
						list.add(item);
					}
					//list = ParseJson.getEntityList(json, SelectItem.class);
				}
				
			} catch (IOException e) {
				return e;
			} catch (Exception e) {
				return e;
			}
	        return list;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if(null != callback){
				if(result instanceof List){
					callback.onSuccess((List)result);
				}else if(result instanceof IOException){
					callback.onfailure((Throwable) result);
				}else if (result instanceof Exception) {
					callback.onfailure((Throwable) result);
				}else{
					callback.onfailure(null);
				}
			}
		}
    }
    
    public interface FindlawCallBact{
    	void onSuccess(List list);
    	void onfailure(Throwable e);
    }
}
