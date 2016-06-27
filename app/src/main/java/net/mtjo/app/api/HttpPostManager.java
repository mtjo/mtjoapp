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
	 * 上传设备token
	 */
	public static void uploadToken(String authtoken, String token,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("deviceToken", token);
		params.put("deviceid", "2");
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "uploadToken"), params, callback, aty, msg);
	}
	
	/**
	 * 修改手机号码
	 */
	public static void modifMobile(String authtoken,String mobile, String vcode, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String>	params = new HashMap<String,String>();
		params.put("authtoken", authtoken);
		params.put("mobile", mobile);
		params.put("vcode", vcode);
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "modifMobile"), params, callback, aty, msg);
	}
	
	/**
	 * 退出登录
	 * @param authtoken
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void exit(String authtoken, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "logout"), params, callback, aty, msg);
	}
	
	/**
	 * 提交问题
	 */
	public static void commitAskQuestion(String authtoken, String content, String areacode, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("content", content);
		params.put("areacode", areacode);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.insertQuestion");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
		
	}
	/**
	 * 得到问律师记录列表
	 */
	public static void getAskLawyerList(String authtoken, int pageNo,int pageSize,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("pageNum", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.getMyQuestionList");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg); 
	}
	/**
	 * 得到问律师详细记录列表
	 */
	public static void getAskLawyerListDetail(String authtoken, String qid,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("qid", qid);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.getLawyerAnswerList");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg); 
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
	 * 预约律师
	 */
	public static void bespeakLawyer(String authtoken,String city, String type, String amount,String content,
			String mobile,String lawyerUid,StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("authtoken", authtoken);
		params.put("name", mobile);
		params.put("areacode", city);
		params.put("special", type);
		if (!StrUtils.isEmpty(type)) {
			params.put("special", type);
		}
		params.put("amount", StrUtils.strToString(amount).trim().length() > 0 ? amount : "0");
		params.put("content", content);
		params.put("lawyerUid", lawyerUid);
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL,"bespeakLawyer"), params, callback, aty, msg);
	}
	
	/**
	 * 预约律师记录
	 */
	public static void getMyBespeakList(String authtoken, int page, int pageSize,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("authtoken", authtoken);
		params.put("page", page+"");
		params.put("pageSize", pageSize+"");
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL,"getMyBespeakList"), params, callback, aty, msg);
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
	
	/**
	 * 与律师会话
	 * @param authtoken
	 * @param voice
	 * @param qid
	 * @param tuUid
	 * @param voiceType
	 * @param callback
	 * @param aty
	 * @param msg
	 */
	public static void insertVoice(String authtoken, String voice, String qid, String toUid, String voiceType,StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("voice", voice);
		params.put("qid", qid);
		params.put("toUid", toUid);
		params.put("voiceType", voiceType);
		params.put("appKey", "00002");
		params.put("v", "1.0");
		params.put("method", "lscn.app.insertVoice");
		Http.getInstancts().urlPost(Config.FINDLAW_URL,
				params,callback, aty, msg);
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
	 * 获取用户未读数
	 */
	public static void getUnreadNum(String authtoken, StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("xpath", "/lscn/dialogue/qid/");
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "getUnreadNum"), params, callback, aty, msg);
	}
	
	/**
	 * 设置用户未读数
	 */
	public static void setUnreadNum(String authtoken,String qid,String lawid, 
			StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		params.put("xpath", "/lscn/dialogue/qid/"+qid+"/fromuid/"+lawid);
		params.put("num", "0");
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "setUnreadNum"), params, callback, aty, msg);
	}
	
	/**
	 * 获取用户问律师记录，未读列表
	 */
	public static void getMyUnreadQuestionList(String authtoken,
			StringCallBack callback, Activity aty, String msg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authtoken", authtoken);
		
		Http.getInstancts().urlPost(getUrl(Config.LSCN_USER_URL, "getMyUnreadQuestionList"), params, callback, aty, msg);
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
}
