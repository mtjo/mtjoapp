package net.mtjo.app.ui.start;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.aframe.Loger;
import com.aframe.utils.AppUtils;
import net.mtjo.app.R;
import net.mtjo.app.ui.base.BaseActivity;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
	private SplashActivity mContext;
	private Timer timer = null;
	private TimerTask task = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		mContext = this;
		
		findViewById(R.id.img).setBackgroundResource(R.drawable.splash);
		
		//启动定时器
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = handler.obtainMessage(); 
		        message.what = 1; 
		        handler.sendMessage(message);
			}
		};
		timer = new Timer();
		timer.schedule(task, 3000);
		
//		Loger.debug(getDeviceInfo(mContext));
	}
	
	private Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) { 
	    	doGotoHomepage(null);
	    }
	};
	
	public void doGotoHomepage(View v) {
		try {
			if(timer != null)
			{
				timer.cancel();
				timer = null;
			}
			if(task != null)
			{
				task.cancel();
				task = null;
			}
			//首次使用进入引导页
			String isfirst = AppUtils.getLocalCache(mContext, "isShowGuide");
			if("true".equals(isfirst)){
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
			} else {
				AppUtils.saveLocalCache(mContext, "isShowGuide", "true");
				Intent intent = new Intent(mContext, GuideActivity.class);
				startActivity(intent);
			}
			finish();
		} catch(Exception e) {
			Loger.debug("启动页", e);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		try {
			if(timer != null)
			{
				timer.cancel();
				timer = null;
			}
			if(task != null)
			{
				task.cancel();
				task = null;
			}
		} catch(Exception e) {
			Loger.debug("启动页", e);
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		setIntent(intent);// 必须要调用这句
	}
	
//	public static String getDeviceInfo(Context context) {
//	    try{
//	      org.json.JSONObject json = new org.json.JSONObject();
//	      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//	          .getSystemService(Context.TELEPHONY_SERVICE);
//
//	      String device_id = tm.getDeviceId();
//
//	      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//	      String mac = wifi.getConnectionInfo().getMacAddress();
//	      json.put("mac", mac);
//
//	      if( TextUtils.isEmpty(device_id) ){
//	        device_id = mac;
//	      }
//
//	      if( TextUtils.isEmpty(device_id) ){
//	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
//	      }
//
//	      json.put("device_id", device_id);
//
//	      return json.toString();
//	    }catch(Exception e){
//	      e.printStackTrace();
//	    }
//	  return null;
//	}
}
