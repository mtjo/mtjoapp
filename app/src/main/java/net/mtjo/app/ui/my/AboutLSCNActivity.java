package net.mtjo.app.ui.my;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.aframe.Loger;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import net.mtjo.app.R;
import net.mtjo.app.service.VersionService;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.base.WebViewActivity;

/**
 * 关于好律师
 * @author zxp
 *
 */
public class AboutLSCNActivity extends BaseActivity{
	private AboutLSCNActivity mContext;
	private TextView version_tv;
	
	public static void open(Activity aty, int requestCode){
		Intent intent = new Intent(aty, AboutLSCNActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.me_about_lscn);
	}
	
	@Override
	protected void onInitViews() {
		initView();
	}
	
	VersionService versionService = null;
	/**
	 * 版本更新
	 */
	public void checkVersion(View v){
		if (versionService == null) {
			Intent intent = new Intent(this, VersionService.class);
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
		}else{
			versionService.checkVersion(this);
		}
	}
	
	ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			versionService = ((VersionService.VersionBinder)service).getService();
			versionService.setChecklistener(listener);
			versionService.checkVersion(AboutLSCNActivity.this);
		}
	};
	
	 VersionService.CheckVersionListener listener = new VersionService.CheckVersionListener(){

			@Override
			public void isNew() {
				ViewInject.showToast(mContext, "当前已经是最新版本");
			}

			@Override
			public void isDownLoad(final String url, final String versions) {
				
				ViewInject.getConfirmDialog(mContext, "发现新版本，是否及时下载更新", new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						versionService.downLoadVersion(url, versions);
					}}, null);
			}
	    };
	
	/**
	 * 用户协议
	 */
	public void userAgreement(View v){
		WebViewActivity.WebView(this, "http://china.findlaw.cn/appwebview/index.php?m=Lscnapp&a=pact", getString(R.string.me_about_lscn_user_protocol_text));
	}
	
	private void initView(){
		mContext = this;
		setTitle(getString(R.string.me_about_lscn_title));
		version_tv = (TextView) findViewById(R.id.now_version);
		if(null != version_tv){
			try {
				version_tv.setText(AppUtils.getVersion(mContext));
			} catch (NameNotFoundException e) {
				Loger.debug("获取版本信息出错", e);
			} catch (Exception e) {
				Loger.debug("获取版本信息出错", e);
			}
		}
	}
}
