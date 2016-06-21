package net.mtjo.app.service;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.utils.StrUtils;
import net.mtjo.app.api.HttpPostManager;

public class VersionService  extends Service{
	
	private CheckVersionListener checklistener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return new VersionBinder();
	}
	
	public class VersionBinder extends Binder{
		/**
		 * 获取当前service的实例
		 */
		public VersionService getService(){
			return VersionService.this;
		}
	}

	
	/**********************************************************************************/
	/**
	 *检查版本更新
	 */
	public void checkVersion(Context context){
		try{
			
			StringCallBack callback = new StringCallBack(){
				@Override
				public void onSuccess(Object t) {
					try {
						JSONObject obj = this.getJsonContent();
						if (this.getState() == 0 && this.getJsonContent() != null) {
							int code =0;
							String url = null;
							if(obj.has("android.code"))code = StrUtils.strToInt(obj.getString("android.code"));
							if(obj.has("android.app.url"))url = obj.getString("android.app.url");
							if(getVersion() < code && url.length() > 0){
								if(null != checklistener)
									checklistener.isDownLoad(url, obj.isNull("android.version") ? "":obj.getString("android.version"));
							}else if(getVersion() >= code){
								if(null != checklistener)//已经是最新版本
									checklistener.isNew();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			HttpPostManager.getNowView(callback, (Activity)context, "正在检查版本信息，请稍后...");
		}catch(Exception e){
			if(Loger.DEBUG_LOG)
				e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前版本名称
	 */
	public int getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0); 
			int version = info.versionCode;
			return version; 
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 下载新版本
	 */
	@SuppressLint("NewApi")
	public void downLoadVersion(String url,String versions){
		//获取下载服务  
		DownloadManager manager =(DownloadManager)getSystemService(DOWNLOAD_SERVICE);  
		//创建下载请求  
		DownloadManager.Request down=new DownloadManager.Request (Uri.parse(url));  
		//设置允许使用的网络类型，这里是移动网络和wifi都可以  
		down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);  
		//禁止发出通知，既后台下载  
		//down.setShowRunningNotification(false);  
		//不显示下载界面  
		down.setVisibleInDownloadsUi(true);  
		down.setDescription("LSCN" + versions );
		down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  
		//设置下载后文件存放的位置  
		down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "lscn_" + versions + ".apk");
		//将下载请求放入队列  
		manager.enqueue(down);
	}
	
	public interface CheckVersionListener{
		//当前版本以经是最新版本
		public abstract void isNew();
		//询问是否下载
		public abstract void isDownLoad(String url,String versions);
	}
	
	public void setChecklistener(CheckVersionListener checklistener) {
		this.checklistener = checklistener;
	}
}
