package net.mtjo.app.ui.find;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aframe.Loger;
import com.aframe.bitmap.FailReason;
import com.aframe.bitmap.ImageCallBack;
import com.aframe.bitmap.ImageLoader;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.config.Config;
import net.mtjo.app.entity.FindLawResult;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.base.WebViewActivity;
import net.mtjo.app.utils.UMengUtil;

public class FindLawDetailActivity extends BaseActivity {
	private static final int BOOK_SUCCESS = 0;
	private FindLawDetailActivity mContext;
	private ImageView heda_img;
	private TextView count_tv,name_tv,room_tv,good_tv;
	private FindLawResult data;
	private String city,type; //预约律师的地区和纠纷类型
	private Button btn;
	private TextView waring;
	
	public static void finLawDetail(Activity aty, FindLawResult result, String city, String type,int requestCode){
		if(null != aty && null != result && null != city){
			Intent intent = new Intent(aty, FindLawDetailActivity.class);
			intent.putExtra("city", city);
			intent.putExtra("type", type);
			intent.putExtra("data", result);
			aty.startActivityForResult(intent, requestCode);
		}
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.findlaw_detail_activity);
	}
	
	@Override
	protected void onInitViews() {
		mContext = this;
		
		Intent intent = getIntent();
		if(null != intent){
			if(intent.hasExtra("city")){
				city = intent.getStringExtra("city");
			}
			if(intent.hasExtra("type")){
				type = intent.getStringExtra("type");
			}
			if(intent.hasExtra("data")){
				data = (FindLawResult) intent.getSerializableExtra("data");
			}
		}
		
		init();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK != resultCode){
			return;
		}
		if(requestCode == BOOK_SUCCESS){
			setResult(RESULT_OK);
			finish();
		}
	}
	
	/**
	 * 律师个人页面
	 */
	public void lawDetail(View v){
		if(null != data)
			WebViewActivity.WebView(mContext, Config.LAW_DETAIL + data.getUid() + "&ishls=1", data.getName() + getString(R.string.law));
	}
	
	/**
	 * 预约律师
	 */
	public void bookLaw(View v){
		if(null != data && StrUtils.strToString(data.getMobile()).length() > 0){
			ViewInject.getConfirmDialog(mContext, "确定要拨打"+data.getName()+"律师号码吗？", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					try {
						UMengUtil.onEvtent(getApplicationContext(), Config.UMENG_CALL_LAWYER);
						AppUtils.doCall(mContext, data.getMobile());
					} catch (Exception e) {
						Loger.debug("打电话给律师", e);
					}
				}
				
			}, null);
		}else {
			ViewInject.showToast(mContext, "电话号码不正确!");
		}
//		UMengUtil.onEvtent(mContext, Config.UMENG_FIND_LAWYER);
//		FindLawActivity.findLaw(mContext, city, type,data.getUid(),BOOK_SUCCESS);
	}

	private void init(){
		setTitle(data.getName()+getString(R.string.law));
		heda_img = (ImageView) findViewById(R.id.findlaw_detail_head_img);
		count_tv = (TextView) findViewById(R.id.findlaw_detail_count_tv);
		name_tv = (TextView) findViewById(R.id.findlaw_detail_name_tv);
		room_tv = (TextView) findViewById(R.id.findlaw_detail_room_tv);
		good_tv = (TextView) findViewById(R.id.findlaw_detail_good_tv);
		btn = (Button) findViewById(R.id.btn);
		waring = (TextView) findViewById(R.id.waring);
		waring.setTextColor(Color.RED);
		
		if(null != data){
			
//			if (!data.isYue()) {
//				btn.setBackgroundColor(getResources().getColor(R.color.text_light_color));
//				btn.setClickable(false);
//				waring.setVisibility(View.VISIBLE);
//			}
			
			if(null != count_tv){
				count_tv.setText(StrUtils.strToString(count_tv.getText().toString()).
						replace("[n]",data.getBespeak()+""));
			}
			if(null != name_tv){
				name_tv.setText(data.getName());
			}
			if(null != room_tv){
				room_tv.setText(data.getLawroom());
			}
//			if(null != specail_tv){
//				specail_tv.setText(data.getSpecial());
//			}
			if(null != good_tv){
				good_tv.setText(data.getGood()+"%");
			}
			if(null != heda_img){
				ImageLoader.getInstance(mContext).displayImage(heda_img, data.getPhoto(),new ImageCallBack() {
					@Override
					public void onStarted(String imageUri, View view) {
					}
					
					@Override
					public void onProcess(String imageUri, View view, int percent) {
					}
					
					@Override
					public void onFailed(String imageUri, View view, FailReason failReason) {
						((ImageView)view).setImageResource(R.drawable.head_default);
					}
					
					@Override
					public void onComplete(String imageUri, View view, Bitmap loadedImage) {
						if(null != loadedImage){
							((ImageView)view).setImageBitmap(loadedImage);
						}else{
							((ImageView)view).setImageResource(R.drawable.head_default);
						}
					}
				}, true, getResources().getDimensionPixelSize(R.dimen.head_round),
				getResources().getDimensionPixelSize(R.dimen.head_width), 
				getResources().getDimensionPixelSize(R.dimen.head_height));
			}
		}
	}
}
