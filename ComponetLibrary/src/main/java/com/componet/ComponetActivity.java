package com.componet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.componet.R;

public class ComponetActivity extends Activity {
	 UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this, 
		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		
		
//		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
//		 mController.openShare(MainActivity.this, true);
		qq();
		mController.postShare(this, SHARE_MEDIA.QQ, null);
	}

	
	private void weixin(){
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx967daebe835fbeac";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,appId);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,appId);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	
	private void qq(){
		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
		                "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK(); 
		mController.getConfig().setSsoHandler(qqSsoHandler);
	}
	
	private void sina(){
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}
	
	private void weibo(){
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}

}
