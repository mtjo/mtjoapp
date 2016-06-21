package net.mtjo.app.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
/**
 * 分享
 * @author zxp
 *
 */
public class SelectPicPopupShare extends PopupWindow implements OnClickListener{
	private View shareView;
	private Context context;
	private TextView sms,weixin,qq,friend, sina, weibo;
	private TextView exit;
	private ColorDrawable color = new ColorDrawable(0xb0000000);
	private UMSocialService mController;
	String appId = "wxce0ffbc086f11e69";
	String appSecret = "aacd70ab25bb0675983469b441ccaaac";
//	private String contentUrl = "http://www.baidu.com";
	private String contentUrl = "http://app.ls.cn/download";
	private String title = null;
	
	
	public SelectPicPopupShare(Context context, String thumb) {
		super(context);
		this.context = context;
		 mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		 if (title == null) {
			 title = context.getResources().getString(R.string.me_share_text);//需要分享的内容
		}
		 if (StrUtils.isEmpty(thumb)) {
			 mController.setShareImage(new UMImage(context, R.drawable.about_logo));
		}else{
			 mController.setShareImage(new UMImage(context, thumb));
			
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		shareView = inflater.inflate(R.layout.me_share_popup_window, null);
		 sms = (TextView)shareView.findViewById(R.id.sms);
		 sms.setOnClickListener(this);
		 weixin = (TextView)shareView.findViewById(R.id.weixin);
		 weixin.setOnClickListener(this);
		 qq = (TextView)shareView.findViewById(R.id.qq);
		 qq.setOnClickListener(this);
		 friend = (TextView)shareView.findViewById(R.id.friend);
		 friend.setOnClickListener(this);
		 sina = (TextView)shareView.findViewById(R.id.sina);
		 sina.setOnClickListener(this);
		 weibo = (TextView)shareView.findViewById(R.id.weibo);
		 weibo.setOnClickListener(this);
		 exit = (TextView)shareView.findViewById(R.id.exit);
		 exit.setOnClickListener(this);
		 
		 this.setContentView(shareView);
		 this.setWidth(LayoutParams.MATCH_PARENT);
		 this.setHeight(LayoutParams.WRAP_CONTENT);
		 this.setFocusable(true);
		 this.setAnimationStyle(R.style.AppBaseTheme);
		 this.setBackgroundDrawable(color);
		 
		 shareView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int height = shareView.findViewById(R.id.pop_layout).getTop();
				int y = (int)arg1.getY();
				if (MotionEvent.ACTION_UP == arg1.getAction()) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit:
			dismiss();
			break;
		case R.id.sms:
			initUM(R.id.sms);
			break;
		case R.id.weixin:
			initUM(R.id.weixin);
			break;
		case R.id.qq:
			initUM(R.id.qq);
			break;
		case R.id.friend:
			initUM(R.id.friend);
			break;
		case R.id.sina:
			initUM(R.id.sina);
			break;
		case R.id.weibo:
			initUM(R.id.weibo);
			break;
		}
		
	}
	
	private void initUM(int id){
		SHARE_MEDIA media = null;
		switch (id) {
		case R.id.sms:
			media = SHARE_MEDIA.SMS;
			SmsHandler sms = new SmsHandler();
			sms.addToSocialSDK();
			 mController.setShareContent(title);
			mController.getConfig().setSsoHandler(sms);
			break;
		case R.id.weixin:
			media = SHARE_MEDIA.WEIXIN;
			UMWXHandler wxHandler = new UMWXHandler(context,appId,appSecret);
			wxHandler.addToSocialSDK();
			wxHandler.setTitle(title);
			wxHandler.setTargetUrl(contentUrl);
			mController.setShareContent(title);
			mController.getConfig().setSsoHandler(wxHandler);
			break;
		case R.id.qq:
			media = SHARE_MEDIA.QQ;
			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "100424468","c7394704798a158208a74ab60104f0ba");
			qqSsoHandler.setTargetUrl(contentUrl);
			qqSsoHandler.setTitle(title);
			qqSsoHandler.addToSocialSDK(); 
			mController.setShareContent(title);
			mController.getConfig().setSsoHandler(qqSsoHandler);
			break;
		case R.id.friend:
			media = SHARE_MEDIA.WEIXIN_CIRCLE;
			UMWXHandler wxCircleHandler = new UMWXHandler(context,appId,appSecret);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.setTitle(title);
			wxCircleHandler.addToSocialSDK();
			wxCircleHandler.setTargetUrl(contentUrl);
			 mController.setShareContent(title);
			mController.getConfig().setSsoHandler(wxCircleHandler);
			break;
		case R.id.sina:
			media = SHARE_MEDIA.SINA;
			 mController.setShareContent(title);
			mController.getConfig().setSsoHandler(new SinaSsoHandler());
			break;
		case R.id.weibo:
			media= SHARE_MEDIA.TENCENT;
			 mController.setShareContent(title);
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		}
			mController.postShare(context, media,  new SnsPostListener(){

				@Override
				public void onComplete(SHARE_MEDIA arg0, int arg1,
						SocializeEntity arg2) {
					dismiss();
				}

				 
				@Override
				public void onStart() {
				}});
			
		}
	
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
