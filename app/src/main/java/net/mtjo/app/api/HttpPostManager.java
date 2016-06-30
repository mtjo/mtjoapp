package net.mtjo.app.api;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.util.Log;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.http.Http;
import com.aframe.utils.StrUtils;
import net.mtjo.app.config.Config;
import net.mtjo.app.utils.AesUtil;

public class HttpPostManager {
	/**
	 * 获取加密明文
	 * @param callback 返回监听
	 * @param aty      当前上下文
	 * @param msg      提示加载信息
	 */
	public static void getFetchChallengePlaintext(StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"fetchChallengePlaintext"), params, callback, aty, msg);
	}
	
	/**
	 * 发送验证码
	 * @param callback 返回监听
	 * @param aty      当前上下文
	 * @param msg      提示加载信息
	 */
	public static void sendVcode(final String mobile,final String ticket, final StringCallBack callback,final  Activity aty, final String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("mobile", mobile);
		params.put("type", "appterms");
		
		try {
			params.put("ticket", AesUtil.encrypt(ticket));
		} catch (Exception e) {
			Loger.debug("获取短信转码", e);
		}
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"sendVcode"), params, callback, aty, msg);
			
	 }
	
	/**
	 * 登录
	 * @param mobile
	 * @param vcode
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void regist(String mobile, String vcode, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("vcode", vcode);
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL, "mobileLogin"), params, callback, aty, msg);
		
	}
	

	
	/**
	 * 退出登录
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void exit(StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		Http.getInstancts().urlPost(Config.API_LOGOUT, params, callback, aty, msg);
	}
	


	
	/**
	 * 通过手机号码跟gps获取城市编码
	 */
	public static void getAreaCode(String mobile,String location,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		if(null  != mobile)
			params.put("mobile", mobile);
		if(null  != location)
			params.put("location", location);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "findlaw.user.gps.mobile");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	
	/**
	 * 加载地区信息
	 */
	public static void getSpecialCitys(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"getSpecialCitys"),
				null,callback, aty, msg,true);
	}
	
	/**
	 * 获取服务类型
	 */
	public static void getSpecialList(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"getSpecialList"),
				null,callback, aty, msg);
	}
	/**
	 * 获取服务类型
	 */
	public static void getAmountList(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"getAmountList"),
				null,callback, aty, msg);
	}
	
	/**
	 * 搜索律师
	 */
	public static void searchLawyer(String city, String type, int page, int pageSize, String order,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("areacode", city);
		params.put("special", type);
		params.put("page", page+"");
		params.put("pageSize", pageSize+"");
		params.put("order", order);
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL,"searchLawyer"), params, callback, aty, msg);
	}
	
	/**
	 * 搜索找法律师
	 */
	public static void searchFindLawer(String city, String type, int page, int pageSize,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("areacode", city);
		if (!StrUtils.isEmpty(type)) {
			params.put("profId", type);
		}
		params.put("grade", "1");
		params.put("pageNum", page+"");
		params.put("pageSize", pageSize+"");
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "findlaw.lawyer.search.list");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	

	

	/**
	 * 法律文章列表
	 */
	public static void getArticleList(int page, int pageSize,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("page", page+"");
		params.put("pageSize", pageSize+"");

		Http.getInstancts().urlPost(Config.API_ARTICLES,
				params,callback, aty, msg);
	}
	
	/**
	 * 获取详细咨询会话列表
	 * @param authtoken
	 * @param qid
	 * @param toUid
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void getAskLawyerDetailChat(String authtoken, String qid, String toUid, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("qid", qid);
		params.put("toUid", toUid);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.getDialogue");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	
	/**
	 * 获取版本号
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void getNowView(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL, "getVersion"), null, callback, aty, msg);
	}
	

	public static void getMsgCount(String authtoken, long lastid,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("lastid", String.valueOf(lastid));
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.homepage.newmessage.num");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	
	/**
	 *获取最新消息列表 
	 */
	public static void getNewMsg(String authtoken, long lastid,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("lastid", String.valueOf(lastid));
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.homepage.fetch.alerts");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	

	

	

	
	/**
	 * 找律师排序列表
	 */
	public static void getFindlawDefalutSort(StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_PUB_URL, "getSearchSortList"), params, callback, aty, msg);
	}
	
	/**
	 * 更新问题状态
	 */
	public static void setResove(String authtoken, String qid, String status,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("qid", qid);
		params.put("status", status);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.question.set.resolve");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
	}
	
	public static String getUrl(String url,String method){
		if(null != url){
			url += "&g=Api&appkey=0002&a="+method;
		}
		return url;
	}



	/**
	 * banner
	 */
	public static void getHomeSlides(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(Config.API_HOME_SLIDES,
				null,callback, aty, msg);
	}


	/**
	 * banner
	 */
	public static void getVerifyCode(StringCallBack callback, Activity aty, String msg){
		Http.getInstancts().urlPost(Config.API_VERIFYCODE,
				null,callback, aty, msg);
	}
	/**
	 * dologin
	 */
	public static void dologin(String username,String password,String verify_code,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("verify", verify_code);

		Http.getInstancts().urlPost(Config.API_LOGIN,
				params,callback, aty, msg);
	}

}
