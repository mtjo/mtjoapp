package net.mtjo.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.ui.WYActivityManager;
import com.aframe.ui.widget.emoji.WXFaceUtils;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.start.MainActivity;
import net.mtjo.app.utils.SharedUserInfo;

public class SysApplication extends Application {
	private static SysApplication instance;
	private MainActivity mainActivity;
	
	public SysApplication() {
		if(null == instance)
			instance = this;
	}
	
	public synchronized static SysApplication getInstance() { 
		if (null == instance) { 
			instance = new SysApplication(); 
		} 
		return instance; 
	}

	public void addActivity(Activity activity) {
		WYActivityManager.create().addActivity(activity);
	}
	
	public void exit(Context context) {
		MobclickAgent.onKillProcess(context);
		WXFaceUtils.destory();
		WYActivityManager.create().AppExit(context);
    }
	
	public void finishAll(){
		WYActivityManager.create().finishAllActivity();
	}
	
	@Override
	public void onLowMemory() { 
		super.onLowMemory();     
		System.gc(); 
	}
	
	@Override
	public void onCreate() {
		SysAppCrashHandler handler = SysAppCrashHandler.getInstance();
		handler.init(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(handler);
		super.onCreate(); 
		initEngineManager(this);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.setDebugMode(false);
		WXFaceUtils.init(getApplicationContext());
	}
	
	public void initEngineManager(Context context) {
	}
	

	
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	/**
	 * 登录成功
	 */
	public void loginSuccess(){
		if(null != mainActivity){
			mainActivity.loginSuccess();
		}
	}
	
	/**
	 * 退出成功
	 */
	public void loginOutSuccess(){
		if(null != mainActivity){
			mainActivity.loginOutSuccess();
		}
	}
}