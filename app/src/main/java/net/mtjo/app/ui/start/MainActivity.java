package net.mtjo.app.ui.start;

import java.lang.ref.WeakReference;
import java.util.List;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.application.SysApplication;
import net.mtjo.app.entity.MessageModel;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.msg.MessageListActivity;
import net.mtjo.app.ui.msg.cache.MessageCache;
import net.mtjo.app.ui.my.RegistActivity;
import net.mtjo.app.utils.SharedUserInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	private MainActivity mContext;
	
	private static FragmentManager fMgr;
	private ArticleFragment article_fragment;
	private HomeFragment home_fragment;
	private FindFragment find_fragment;
	private MyFragment my_fragment;
	
	private RadioButton home_rb, find_rb, article_rb, my_rb;
	private TextView msg_count_tv;
	private Long lastClickTime = 0l;
	
	private boolean ishomeAdd, isfindAdd, isarticleAdd, ismyAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initVeiw();
	}
	
	private FragmentManager getFMgr(){
		if(null == fMgr)
			fMgr  = getSupportFragmentManager();
		return fMgr;
	}
	
	protected void initVeiw() {
		mContext = this;
		
		//获取FragmentManager实例
		fMgr = getSupportFragmentManager();
		home_rb = (RadioButton) findViewById(R.id.rbHome);
		find_rb = (RadioButton) findViewById(R.id.rbFind);
		article_rb = (RadioButton) findViewById(R.id.rbArticle);
		my_rb = (RadioButton) findViewById(R.id.rbMy);
		msg_count_tv = (TextView) findViewById(R.id.msg_count_tv);
		
		
		initFragment();
		
		SysApplication.getInstance().setMainActivity(this);
		XGPushConfig.enableDebug(this, true);
		
		registerPush();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/*****************页面切换控制***************/
	
	private Fragment mContent;
	
	private void initFragment() {
		if(null != find_rb)find_rb.setChecked(false);
		if(null != home_rb)home_rb.setChecked(true);
		if(null != article_rb)article_rb.setChecked(false);
		if(null != my_rb)my_rb.setChecked(false);
		
		FragmentTransaction ft = getFMgr().beginTransaction();
		if(null == home_fragment && !ishomeAdd)
			home_fragment = new HomeFragment();
		ft.add(R.id.fragmentRoot, home_fragment, "HomeFragment");
		ft.addToBackStack("HomeFragment");
		ft.commit();	
		mContent = home_fragment;
		ishomeAdd = true;
	}
	
	public void goHome(View v){
		lastClickTime = 0l;
		if(null != find_rb)find_rb.setChecked(false);
		if(null != home_rb)home_rb.setChecked(true);
		if(null != article_rb)article_rb.setChecked(false);
		if(null != my_rb)my_rb.setChecked(false);
		
		if(getFMgr().findFragmentByTag("HomeFragment")!=null && getFMgr().findFragmentByTag("HomeFragment").isVisible()) {
			return;
		}
		
		FragmentTransaction ft = getFMgr().beginTransaction();
		if((home_fragment == null || !home_fragment.isAdded())&& !ishomeAdd){
			if(null == home_fragment)
				home_fragment = new HomeFragment();
			ft.add(R.id.fragmentRoot, home_fragment, "HomeFragment");
			ft.addToBackStack("HomeFragment");
			ishomeAdd = true;
		}
		ft.hide(mContent).show(home_fragment).commit();
		mContent = home_fragment;
	}
	
	public void goFind(View v){
		lastClickTime = 0l;
		if(null != find_rb)find_rb.setChecked(true);
		if(null != home_rb)home_rb.setChecked(false);
		if(null != article_rb)article_rb.setChecked(false);
		if(null != my_rb)my_rb.setChecked(false);
		
		if(getFMgr().findFragmentByTag("FindFragment")!=null && getFMgr().findFragmentByTag("FindFragment").isVisible()) {
			return;
		}
		
		FragmentTransaction ft = getFMgr().beginTransaction();
		if((find_fragment == null || !find_fragment.isAdded())&& !isfindAdd){
			if(null == find_fragment)
				find_fragment = new FindFragment();
			ft.add(R.id.fragmentRoot, find_fragment, "FindFragment");
			ft.addToBackStack("FindFragment");
			isfindAdd = true;
		}
		ft.hide(mContent).show(find_fragment).commit();
		mContent = find_fragment;
	}
	
	public void goArticle(View v){
		lastClickTime = 0l;
		if(null != find_rb)find_rb.setChecked(false);
		if(null != home_rb)home_rb.setChecked(false);
		if(null != article_rb)article_rb.setChecked(true);
		if(null != my_rb)my_rb.setChecked(false);
		
		if(getFMgr().findFragmentByTag("ArticleFragment")!=null && getFMgr().findFragmentByTag("ArticleFragment").isVisible()) {
			return;
		}
		
		FragmentTransaction ft = getFMgr().beginTransaction();
		if((article_fragment == null || !article_fragment.isAdded())&& !isarticleAdd){
			if(null == article_fragment)
				article_fragment = new ArticleFragment();
			ft.add(R.id.fragmentRoot, article_fragment, "ArticleFragment");
			ft.addToBackStack("ArticleFragment");
			isarticleAdd = true;
		}
		ft.hide(mContent).show(article_fragment).commit();
		mContent = article_fragment;
	}
	
	public void goMy(View v){
		lastClickTime = 0l;
		if(null != find_rb)find_rb.setChecked(false);
		if(null != home_rb)home_rb.setChecked(false);
		if(null != article_rb)article_rb.setChecked(false);
		if(null != my_rb)my_rb.setChecked(true);
		
		if(getFMgr().findFragmentByTag("MyFragment")!=null && getFMgr().findFragmentByTag("MyFragment").isVisible()) {
			return;
		}
		
		FragmentTransaction ft = getFMgr().beginTransaction();
		if((my_fragment == null || !my_fragment.isAdded())&& !ismyAdd){
			if(null == my_fragment)
				my_fragment = new MyFragment();
			ft.add(R.id.fragmentRoot, my_fragment, "MyFragment");
			ft.addToBackStack("MyFragment");
			ismyAdd = true;
		}
		ft.hide(mContent).show(my_fragment).commit();
		mContent = my_fragment;
	}
	
	@Override
	public void onBackPressed() {
		if(getFMgr().findFragmentByTag("FindFragment")!=null && getFMgr().findFragmentByTag("FindFragment").isVisible() && null != find_fragment){
			if(!find_fragment.onBackPressed())
				showExit();
		} else {
			showExit();
		}
	}
	
	private void showExit(){
		if (System.currentTimeMillis() - lastClickTime > 2000) {
			lastClickTime = System.currentTimeMillis();
			ViewInject.showToast(mContext, "再按一次退出系统");
		} else {
			SysApplication.getInstance().finishAll();
		}
	}
	
	/**
	 *消息列表
	 */
	public void goMsg(View v){
		if(null == SharedUserInfo.getUserInfo(mContext)){
			//RegistActivity.open(mContext, 1);
		} else {
			MessageListActivity.open(mContext, 2);
		}
	}
	

	

	private void setMsgCount(int count){
		if(null == msg_count_tv)return;
		if(count == 0){
			msg_count_tv.setVisibility(View.GONE);
		} else {
			msg_count_tv.setVisibility(View.VISIBLE);
			msg_count_tv.setText(String.valueOf(count));
		}
	}
	
	/*********************处理登录********************/
	/**
	 * 登录成功
	 */
	public void loginSuccess(){
		if(null != my_fragment){
			my_fragment.loginSuccess();
		}
		//加载是否有新消息
	}
	
	/**
	 * 退出成功
	 */
	public void loginOutSuccess(){
		if(null != my_fragment){
			my_fragment.loginOutSuccess();
		}

	}
	
	/***************处理推送token***************/
	private void saveToken(String token){
		Loger.debug("连接信鸽成功,设备token："+token);
		SharedUserInfo.saveToken(mContext, token);
	}


	private void registerPush(){
		// 1.获取设备Token
		Handler handler = new HandlerExtension(mContext);
		m = handler.obtainMessage();
		// 注册接口
		XGPushManager.registerPush(getApplicationContext(),
				new XGIOperateCallback() {
					@Override
					public void onSuccess(Object data, int flag) {
						m.what = 0;
						m.obj =  data;
						m.sendToTarget();
					}

					@Override
					public void onFail(Object data, int errCode, String msg) {
						m.what = 1;
						m.obj =  data;
						m.sendToTarget();
					}
				});
	}
	private Message m = null;

	private static class HandlerExtension extends Handler {
		WeakReference<MainActivity> mActivity;

		HandlerExtension(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			MainActivity theActivity = mActivity.get();
			if (theActivity == null) {
				theActivity = new MainActivity();
			}
			if(msg.what == 0){
				//成功
				theActivity.saveToken( msg.obj.toString());
			}else{
				//失败
				Loger.debug("获取设备token失败");
			}
		}
	}
}
