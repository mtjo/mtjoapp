package net.mtjo.app.ui.ask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.config.Config;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.start.RegistActivity;
import net.mtjo.app.utils.SharedUserInfo;
import net.mtjo.app.utils.UMengUtil;

/**
 * 问律师
 * @author zxp
 *
 */
public class AskLawyerActivity extends BaseActivity {
	private static final String ASKDRAFT = "ask_draft";//输入框保存草稿
	private static final String PROVIOUS_CONSULT = "provious_consult";//上一条发送成功的咨询
	private static final int DOLOGIN = 1;
	private AskLawyerActivity mContext;
	private EditText content;
	private String areacode;
	private boolean isSendOk = true;//是否上次请求完成（不论成败）

	public static void open(Activity aty, int requestCode){
		if (aty == null) {
			return;
		}
		Intent intent = new Intent(aty,AskLawyerActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.me_feed_back);
	}
	
	@Override
	protected void onInitViews() {
		
		mContext = this;
		setTitle(R.string.ask_lawyer_title_text);
		
		content = (EditText)this.findViewById(R.id.content);
		content.setHint(R.string.ask_lawyer_hint);
		
		//设置缓存
		String msg = AppUtils.getLocalCache(mContext, ASKDRAFT);
		if(null != msg &&  msg.length() > 0){
			content.setText(msg);
			
			CharSequence text = content.getText();
			if (text instanceof Spannable) {
				 Spannable spanText = (Spannable)text;
				 Selection.setSelection(spanText, text.length());
			}
		}
	}
	
	public void submit(View v){
		if (StrUtils.isEmpty(content.getText().toString())) {
			ViewInject.showToast(this, getString(R.string.ask_lawyer_no_empty_msg));
		}else if(content.getText().toString().length() < 10){
			ViewInject.showToast(this, "问题描述过短，请尽可能详细描述");
		}else{
			if(null == SharedUserInfo.getUserInfo(mContext)){
				UMengUtil.onEvtent(getApplicationContext(), Config.UMENG_ASK_LAWYER_NEXT);
				RegistActivity.open(mContext,true, DOLOGIN);
			} else {
				if (areacode == null) {
					//判断是打开gps
//					if(AppUtils.isGpsOpen(mContext)){
//						initLocation();
//					} else {
						location();
//					}
				} else {
					commitAskQuestion();
				}
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if(StrUtils.strToString(content.getText().toString()).length() > 0)
			AppUtils.saveLocalCache(mContext, ASKDRAFT, content.getText().toString());
		else 
			AppUtils.cleanLocalCache(mContext, ASKDRAFT);
		super.onDestroy();
		this.finish();
	}

	public void commitAskQuestion() {
		if(!isSendOk){
			return;//上次请求还未完成，退出这次请求
		}
		//当前咨询内容
		final String cur_consult = content.getText().toString().replaceAll("\\s*", "");
		/**
		 * 获取上一条发送成功的咨询
		 */
		String proviousStr = AppUtils.getLocalCache(this,PROVIOUS_CONSULT);

		if(cur_consult.equals(proviousStr)){//和上一条发送完成的内容一样，弹框提示用户是否继续发送
			String tips = "您刚才已经发布一条咨询,可以在原咨询界面追问律师(点击我的--我的咨询),不需要重复发布咨询!";
			DialogInterface.OnClickListener yesClick = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//重复发送_继续发送统计
					UMengUtil.onEvtent(AskLawyerActivity.this.getApplicationContext(), Config.UMENG_COMMIT_REPEAT_NEXT);
					//继续发送
					sendConsult(cur_consult);
				}
			};
			DialogInterface.OnClickListener noClick = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//重复发送_查看我的咨询统计
					UMengUtil.onEvtent(AskLawyerActivity.this.getApplicationContext(), Config.UMENG_COMMIT_REPEAT_LOOK);
					//查看我的咨询
					AskLawyerListActivity.open(AskLawyerActivity.this, 1);
				}
			};
			ViewInject.getConfirmDialog_textToLeft(this,tips,"继续发送","查看我的咨询",yesClick,noClick).show();
		}else{
			//发送咨询
			sendConsult(cur_consult);
		}

	}

	/**
	 * 发送咨询
	 * @param cur_consult 咨询内容
	 */
	private void sendConsult(final String cur_consult){
		//提交问律师统计
		UMengUtil.onEvtent(AskLawyerActivity.this.getApplicationContext(), Config.UMENG_COMMIT_ASK_LAWYER);

		isSendOk = false;//表示进入请求状态
		StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				isSendOk = true;//表示退出了请求状态
				if (!StrUtils.isEmpty(this.getStrContent())) {
					UMengUtil.onEvtent(getApplicationContext(), Config.UMENG_COMMIT_ASK_LAWYER);
					AskLawyerDetailActivity.open(this.getStrContent(), mContext, 22);

					AppUtils.saveLocalCache(AskLawyerActivity.this,PROVIOUS_CONSULT,cur_consult);

					ViewInject.showToast(mContext, getString(R.string.ask_lawyer_commit_content_succ_msg));
					AppUtils.cleanLocalCache(mContext, ASKDRAFT);
					content.setText("");
					finish();
				}else{
					ViewInject.showToast(mContext, getString(R.string.ask_lawyer_commit_content_fail_msg));
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				isSendOk = true;//表示退出了请求状态
				ViewInject.showToast(mContext, this.getDesc());
			}
		};

		HttpPostManager.commitAskQuestion(SharedUserInfo.getUserInfo(mContext).getAuthtoken(),
				cur_consult, areacode, callback, mContext, "正在提交请稍后...");
	}
	
	/**
	 * 定位
	 */
	private void location(){
		String mobile = null;
		String slocation = null;
		Location  location= AppUtils.getLocation(mContext);
//		Location  location= SharedUserInfo.location;
		if(null != location){
			slocation = location.getLatitude() + "," + location.getLongitude();
			Loger.debug("gps定位："+slocation);
		}
		if(null != SharedUserInfo.getUserInfo(mContext) && 
				null != SharedUserInfo.getUserInfo(mContext).getMobile()){
			mobile = SharedUserInfo.getUserInfo(mContext).getMobile();
		}
		
		HttpPostManager.getAreaCode(mobile, slocation, new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				try {
					JSONObject json = this.getJsonContent();
					if(null != json){
							if(!json.isNull("areacode")){
								 areacode = json.getString("areacode");
							}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				commitAskQuestion();
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				commitAskQuestion();
			}
		}, mContext, "正在提交请稍后...");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DOLOGIN && resultCode == RESULT_OK) {
			submit(null);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(null != SharedUserInfo.getUserInfo(AskLawyerActivity.this)){
			getMenuInflater().inflate(R.menu.submit, menu);
		} else {
			getMenuInflater().inflate(R.menu.next, menu);
		}
		return true;
	}
	
	@Override  
	public boolean onMenuItemClick(MenuItem menuItem) { 
		if(menuItem.getItemId() == R.id.action_submit){
			submit(null);
		}
		if(menuItem.getItemId() == R.id.action_next){
			submit(null);
		}
	    return true;  
	}
	
	//gps定位
	private LocationListener locationListener;
	private LocationManager locationManager;
	/***********************获取gps定位**************************/
	private void initLocation(){
		locationListener = new LocationListener() {  
		    public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发  
		        if (location != null) {  
		        	Loger.debug("SuperMap:Location changed : Lat: "
				              + location.getLatitude() + " Lng: "  
				              + location.getLongitude());
		        	SharedUserInfo.location = location;
		        }
		        locationFinish();
		    }  
		  
		    public void onProviderDisabled(String provider) {  
		    // Provider被disable时触发此函数，比如GPS被关闭  
		    	Loger.debug("Provider被disable时触发此函数，比如GPS被关闭 ");
		    	locationFinish();
		    }  
		  
		    public void onProviderEnabled(String provider) {  
		    //  Provider被enable时触发此函数，比如GPS被打开  
		    	Loger.debug("Provider被enable时触发此函数，比如GPS被打开  ");
		    	locationFinish();
		    }  
		  
		    public void onStatusChanged(String provider, int status, Bundle extras) {  
		    // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		    	Loger.debug("Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数");
		    	locationFinish();
		    }  
		};
		
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();  
		c.setAccuracy(Criteria.ACCURACY_LOW); //精度高  
		c.setPowerRequirement(Criteria.POWER_LOW); //电量消耗低  
		c.setAltitudeRequired(false); //不需要海拔  
		c.setSpeedRequired(false); //不需要速度  
		c.setCostAllowed(false); //不需要费用
		locationManager.getBestProvider(c , false);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
				 1000, 0, locationListener); 
	}
	
	private void locationFinish(){
		if(null != locationListener)
			locationManager.removeUpdates(locationListener);
        locationListener = null;
    	locationManager = null;
    	location();
	}
}
