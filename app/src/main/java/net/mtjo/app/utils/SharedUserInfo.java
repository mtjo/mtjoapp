package net.mtjo.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import net.mtjo.app.entity.UnReadMark;
import net.mtjo.app.entity.UserInfo;

public class SharedUserInfo {
	
	public static UserInfo userinfo = null;
	public static UnReadMark user_unread_msg;
	public static Location location; //存放用户当前的位置信息
	
	private static final String USER_MODEL = "userinfo";
	private static final String UID = "uid";
	private static final String MOBILE = "mobile";
	private static final String USERID = "userid";
	private static final String USERNAME = "username";
	private static final String IFAUDIT = "ifaudit";
	private static final String STATUS = "status";
	private static final String AUTHTOKEN = "authtoken";
	private static final String TOKEN = "token";
	private static final String ASKMSG = "askMsg";
	
	private static SharedPreferences sp;
	
	/**
	 * 问律师内容
	 * @param msg
	 * @param context
	 */
	public static void saveAskLawyerMsg(String msg, Context context){
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.putString(ASKMSG, msg);
		mEditor.commit();
	}
	public static String getAskLawyerMsg(Context context){
		sp = context.getSharedPreferences(USER_MODEL, 0);
		return sp.getString(ASKMSG, null);
		
	}
	
	public static void saveUserinfo(Context context,UserInfo user){
		userinfo = user;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.putString(UID, user.getUid());
		mEditor.putString(MOBILE, user.getMobile());
		mEditor.putString(USERID, user.getUserid());
		mEditor.putString(USERNAME, user.getUsername());
		mEditor.putString(IFAUDIT, user.getIfaudit());
		mEditor.putString(STATUS, user.getStatus());
		mEditor.putString(AUTHTOKEN, user.getAuthtoken());
		mEditor.commit();
	}
	
	public static UserInfo getUserInfo(Context context){
		if(null == context)return null;
		if(null == userinfo){
			sp = context.getSharedPreferences(USER_MODEL, 0);
			if (sp.getString(AUTHTOKEN, null) != null) {
				userinfo = new UserInfo();
				userinfo.setUid(sp.getString(UID, null));
				userinfo.setMobile(sp.getString(MOBILE, null));
				userinfo.setUserid(sp.getString(USERID, null));
				userinfo.setUsername(sp.getString(USERNAME, null));
				userinfo.setIfaudit(sp.getString(IFAUDIT, null));
				userinfo.setStatus(sp.getString(STATUS, null));
				userinfo.setAuthtoken(sp.getString(AUTHTOKEN, null));
				userinfo.setToken(sp.getString(TOKEN, null));
//				userinfo.setAuthtoken("YjczNGEyY2Y0MGI0NDcyNWQyMDllYjRmMTJiYjdlMmFiNjc0YWU5ZGUxNmYyZGIxNjQzODFiNGZjMGMyODgyM2Q5NTE4NjE2NjA1MmRkOWY0MTI0ZDU2MjBjMDEwMTUyZjE0M2UwYjQ3MTQ0ZGZiZjM3OTkyMmZlNzcwNzE5ZmQwNDQzZTc5NjQxNzg2OTVjNGY2YTAyYjQ1NzdmZTYxZTAxNTdkZjliMTYzNjBmNWQ5MWFiODEwZGE1NTU5OTZjZWU1ZmZiYzI5ODZiZWUxMTNiMGMyYmVkY2M1MDk3OGU=");
			}
			
		}
		return userinfo;
	}
	
	/**
	 * 修改手机号码
	 */
	public static void saveChange(Context context, String authtoken, String mobile){
		userinfo = null;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.putString(MOBILE, mobile);
		mEditor.putString(AUTHTOKEN, authtoken);
		mEditor.clear().commit();
	}
	
	public static void exitUserInfo(Context context){
		userinfo = null;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.remove(UID);
		mEditor.remove(MOBILE);
		mEditor.remove(USERID);
		mEditor.remove(USERNAME);
		mEditor.remove(IFAUDIT);
		mEditor.remove(STATUS);
		mEditor.remove(AUTHTOKEN);
		mEditor.commit();
	}
	
	/**
	 * 保存设备token
	 * @param context
	 * 				当前上下文
	 * @param token
	 * 				手机设备token
	 */
	public static void saveToken(Context context,String token){
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.putString(TOKEN, token);
		mEditor.commit();
	}
	
	/**
	 * 获取手机设备token
	 */
	public static String getToken(Context context){
		if(null == context)return null;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		return sp.getString(TOKEN, null);
	}
}
