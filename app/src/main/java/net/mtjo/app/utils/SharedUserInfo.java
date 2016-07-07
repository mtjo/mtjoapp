package net.mtjo.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.aframe.database.annotate.Id;

import net.mtjo.app.entity.UnReadMark;
import net.mtjo.app.entity.UserInfo;


public class SharedUserInfo {
	
	public static UserInfo userinfo = null;
	public static UnReadMark user_unread_msg;
	public static Location location; //存放用户当前的位置信息
	
	private static final String USER_MODEL = "userinfo";
	private static final String ID = "id";
	private static final String USER_NICENAME = "user_nicename";
	private static final String USER_PASS = "user_pass";
	private static final String USER_LOGIN = "user_login";
	private static final String USER_EMAIL ="user_email";
	private static final String USER_URL ="user_url";
	private static final String AVATAR = "avatar";
	private static final String SEX = "sex";
	private static final String BIRTHDAY = "birthday";
	private static final String SIGNATURE = "signature";
	private static final String LAST_LOGIN_IP = "last_login_ip";
	private static final String LAST_LOGIN_TIME = "last_login_time";
	private static final String CREATE_TIME = "create_time";
	private static final String USER_ACTIVATION_KEY ="user_activation_key";
	private static final String USER_STATUS ="user_status";
	private static final String COIN ="coin";
	private static final String MOBILE = "mobile";
	private static final String SCORE ="score";
	private static final String USER_TYPE = "user_type";

	private static final String TOKEN = "token";
	
	private static SharedPreferences sp;
	

	
	public static void saveUserinfo(Context context,UserInfo user){
		userinfo = user;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.putString(ID, user.getId());
		mEditor.putString(MOBILE, user.getMobile());
		mEditor.putString(USER_LOGIN, user.getUser_login());
		mEditor.putString(USER_NICENAME, user.getUser_nicename());
		mEditor.putString(SEX, user.getSex());
		mEditor.putString(USER_STATUS, user.getUser_status());
		mEditor.commit();
	}
	
	public static UserInfo getUserInfo(Context context){
		if(null == context)return null;
		if(null == userinfo){
			sp = context.getSharedPreferences(USER_MODEL, 0);
			if (sp.getString(ID, null) != null) {
				userinfo = new UserInfo();
				userinfo.setId(sp.getString(ID, null));
				userinfo.setMobile(sp.getString(MOBILE, null));
				userinfo.setUser_login(sp.getString(USER_LOGIN, null));
				userinfo.setUser_nicename(sp.getString(USER_NICENAME, null));
				userinfo.setSex(sp.getString(SEX, null));
				userinfo.setAvatar(sp.getString(AVATAR,null));
				userinfo.setUser_status(sp.getString(USER_STATUS, null));
			}
			
		}
		return userinfo;
	}
	

	
	public static void exitUserInfo(Context context){
		userinfo = null;
		sp = context.getSharedPreferences(USER_MODEL, 0);
		SharedPreferences.Editor mEditor = sp.edit();
		mEditor.remove(ID);
		mEditor.remove(MOBILE);
		mEditor.remove(AVATAR);
		mEditor.remove(USER_LOGIN);
		mEditor.remove(USER_NICENAME);
		mEditor.remove(SEX);
		mEditor.remove(USER_STATUS);
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
