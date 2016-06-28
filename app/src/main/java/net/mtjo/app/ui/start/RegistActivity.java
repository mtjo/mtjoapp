package net.mtjo.app.ui.start;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.application.SysApplication;
import net.mtjo.app.config.Config;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.utils.SharedUserInfo;
import net.mtjo.app.utils.UMengUtil;

/**
 * 注册页面，手机号，验证码注册
 * @author zxp 
 *
 */
public class RegistActivity extends BaseActivity {
	
	private boolean isAsk;
	
	/**
	 * 登录
	 */
	public static void open(Activity aty, int requestCode){
		if (aty == null) {
			return;
		}
		Intent intent = new Intent(aty,RegistActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 登录
	 */
	public static void open(Activity aty, boolean isAsk, int requestCode){
		if (aty == null) {
			return;
		}
		Intent intent = new Intent(aty,RegistActivity.class);
		intent.putExtra("isAsk", isAsk);
		aty.startActivityForResult(intent, requestCode);
	}
	
	
	private RegistActivity mContext;
	private Button repeate_send;
	private EditText phone_number;
	private EditText test_code;
	private Button send;
	private Handler handler;
	private int count;
	private SmsContent smsContent;//短信监听
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.my_login_activity);
	}
	
	@Override
	protected void onInitViews() {
		mContext = this;
		initVal();
		smsContent = new SmsContent(smshandler);
		//注册短信变化监听
		this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
	}
	
	//组建的初始化方法。。。。。
	private void initVal(){
		repeate_send = (Button)this.findViewById(R.id.repeate_send);
		repeate_send.setText(getString(R.string.regist_get_vcode_text));
		phone_number = (EditText)this.findViewById(R.id.regist_phone_number);
		test_code = (EditText)this.findViewById(R.id.regist_test_code);
		send = (Button)this.findViewById(R.id.send);
		handler = new Handler();
		
		if(isAsk){
			setTitle("问律师");
		} else {
			setTitle(getString(R.string.regist_title_msg));
		}
	}

	public boolean isMobile(String mobile){
		return mobile != null && mobile.length() == 11;
	}
	
	
	public void doLogin(View v){
		String s = phone_number.getText().toString();
		if (StrUtils.isEmpty(s)) {
			ViewInject.showToast(this, getString(R.string.regist_not_empty_msg));
		}else if (StrUtils.isEmpty(test_code.getText().toString())) {
			ViewInject.showToast(this, getString(R.string.regist_not_empty_vecode_msg));
		}else{
			if (isMobile(phone_number.getText().toString()))  {
				sendRegist();
			}else{
				ViewInject.showToast(this, getString(R.string.regist_mobile_rule__msg));
			}
		}
		
	}
	
	public void doCode(View v){
		String s = phone_number.getText().toString();
		if (s.trim() == null) {
			ViewInject.showToast(this, getString(R.string.regist_not_empty_msg));
		}else{
			if (isMobile(phone_number.getText().toString())) {
				sendVecode();
			}else{
				ViewInject.showToast(this, getString(R.string.regist_mobile_rule__msg));
			}
		}
	}
	
	/**
	 * 登录
	 */
	private void sendRegist(){
		//登录统计
		UMengUtil.onEvtent(mContext.getApplicationContext(), Config.UMENG_LOGIN);
		
		StringCallBack regist = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				send.setEnabled(true);
				if (this.getState() == 0 && this.getJsonContent() != null) {
					UserInfo user = ParseJson.getEntity(this.getJsonContent().toString(), UserInfo.class);
					if (user != null && user.getStatus().equals("01")) {
						UMengUtil.onEvtent(RegistActivity.this, Config.UMENG_LOGIN);
						SharedUserInfo.saveUserinfo(RegistActivity.this,user);
						ViewInject.showToast(mContext, getString(R.string.regist_succ));
						RegistActivity.this.setResult(RESULT_OK);
						RegistActivity.this.finish();
						//上传用户token
						SysApplication.getInstance().loginSuccess();
						SysApplication.getInstance().uploadToken();
					}else{
						ViewInject.showToast(RegistActivity.this, this.getDesc());
					}
				}else{
					ViewInject.showToast(RegistActivity.this, this.getDesc());
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				send.setEnabled(true);
				ViewInject.showToast(RegistActivity.this, this.getDesc());
			}
		};
		
		send.setEnabled(false);
		HttpPostManager.regist(phone_number.getText().toString(), test_code.getText().toString(), regist, this, "正在登录，请稍后...");
	}
	
	/**
	 * 倒计时
	 */
	private void countHandler(){
		if(null != handler)
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (count == 0) {
						repeate_send.setEnabled(true);
						repeate_send.setText(getString(R.string.regist_send_repeat_text));
					}else{
						repeate_send.setText(getString(R.string.regist_send_repeat_text)+"("+String.valueOf(--count) +")");
						if(null != handler)
							handler.postDelayed(this, 1000);
					}
				}
			}, 1000);
	}
	
	
	/**
	 * send vecode
	 */
	private void sendVecode(){
		final StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				String date = this.getStrContent();
				if (date != null && ("SUCC").equals(date)) {
					count = 60;
					repeate_send.setEnabled(false);
					countHandler();
					ViewInject.showToast(RegistActivity.this, getString(R.string.regist_test_code_msg));
				}else{
					ViewInject.showToast(RegistActivity.this, this.getDesc());
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ViewInject.showToast(RegistActivity.this, RegistActivity.this.getString(R.string.regist_error_test_code_msg));
			}
		};
		
		 StringCallBack callBack = new StringCallBack(){
				@Override
				public void onSuccess(Object t) {
					HttpPostManager.sendVcode(phone_number.getText().toString(),this.getStrContent(),callback, RegistActivity.this, "正在为您发送验证码，请稍后...");
				}
				
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					super.onFailure(t, errorNo, strMsg);
				}
			};
			
			HttpPostManager.getFetchChallengePlaintext(callBack, this, "正在为您发送验证码，请稍后...");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(isAsk)
			getMenuInflater().inflate(R.menu.submit, menu);
		return true;
	}
	
	@Override  
	public boolean onMenuItemClick(MenuItem menuItem) { 
		if(menuItem.getItemId() == R.id.action_submit){
			doLogin(null);
		}
	    return true;  
	}
	
	@Override
	protected void onDestroy() {
		//取消短信变化监听
		this.getContentResolver().unregisterContentObserver(smsContent);
		handler = null;
		smshandler = null;
		smsContent = null;
		super.onDestroy();
	}
	
	//处理接收的短信
	Handler smshandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Pattern pattern = Pattern.compile("[0-9]{6}");
            Matcher matcher = pattern.matcher((String) msg.obj);
            if(matcher.find()){
          	  test_code.setText(matcher.group());
            }
		}
	};

	//短信监听
	class SmsContent extends ContentObserver{
		private Handler handler;
		public SmsContent(Handler handler) {
			super(handler);
			this.handler = handler;
		}
		
		private Cursor cursor = null;

		/**
		* @Description 当短信表发送改变时，调用该方法
		* 需要权限
		* android.permission.READ_SMS 读取短信
		*/
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			//读取收件箱中指定号码的短信
			cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read","body"}, "read=?", new String[]{"0"}, "date desc");
			if (cursor != null && cursor.moveToFirst()){
				System.out.println("接收到短信。。。。。。");
				String body = cursor.getString(cursor.getColumnIndex("body"));
				Message msg = handler.obtainMessage();
				msg.obj = body;
				cursor.close();
				if(null != handler)
					handler.sendMessage(msg);
			}
		}
	}
}
