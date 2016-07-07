package net.mtjo.app.api;


import java.util.HashMap;

import android.app.Activity;

import com.aframe.http.StringCallBack;
import com.aframe.http.Http;
import com.aframe.utils.StrUtils;


import net.mtjo.app.config.Config;

public class HttpPostManager {


    /**
     * 退出登录
     *
     * @param callback
     * @param aty
     * @param msg
     */
    public static void exit(StringCallBack callback, Activity aty, String msg) {
        HashMap<String, String> params = new HashMap<String, String>();
        Http.getInstancts().urlPost(getUrl(Config.API_URL, "user","logout"), params, callback, aty, msg);
    }


    /**
     * 文章列表
     */
    public static void getArticleList(int page, int pageSize,
                                      StringCallBack callback, Activity aty, String msg) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");

        Http.getInstancts().urlPost(getUrl(Config.API_URL, "index","article_list"),
                params, callback, aty, msg);
    }

    /**
     * 搜索
     */
    public static void getSearchResult(String keyword, int page, int pageSize,
                                       StringCallBack callback, Activity aty, String msg) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keyword", keyword);
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");

        Http.getInstancts().urlPost(getUrl(Config.API_URL, "index","search"),
                params, callback, aty, msg);
    }




    /**
     * banner
     */
    public static void getHomeSlides(StringCallBack callback, Activity aty, String msg) {
        Http.getInstancts().urlPost(getUrl(Config.API_URL, "index","home_slides"),
                null, callback, aty, msg);
    }


    /**
     * getcode
     */
    public static void getVerifyCode(StringCallBack callback, Activity aty, String msg) {
        Http.getInstancts().urlPost(getUrl(Config.API_URL, "index","getcode"),
                null, callback, aty, msg);
    }

    /**
     * dologin
     */
    public static void dologin(String username, String password, String verify_code, StringCallBack callback, Activity aty, String msg) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("verify", verify_code);

        Http.getInstancts().urlPost(getUrl(Config.API_URL, "user","dologin"),
                params, callback, aty, msg);
    }

    /**
     * dologin
     */
    public static void regist(String email_phone, String password, String verify_code, StringCallBack callback, Activity aty, String msg) {
        HashMap<String, String> params = new HashMap<String, String>();
        if (StrUtils.isMobileNO(email_phone)) {
            params.put("mobile", email_phone);
        } else {
            params.put("email", email_phone);
        }
        params.put("password", password);
        params.put("repassword", password);
        params.put("verify", verify_code);

        Http.getInstancts().urlPost(getUrl(Config.API_URL, "user","doregister"),
                params, callback, aty, msg);
    }


    /**
     * dologin
     */
    public static void getFavorite(String uid, StringCallBack callback, Activity aty, String msg) {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("uid",uid);
        Http.getInstancts().urlPost(getUrl(Config.API_URL, "user","favorite"),
                params, callback, aty, msg);
    }



    public static String getUrl(String url, String Controller, String method) {
        if (null != url) {
            url +=Controller+ "&a=" + method;
        }
        return url;
    }
}
