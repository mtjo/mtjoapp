package net.mtjo.app.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import com.aframe.http.CookieManager;
import com.aframe.http.FormAgent;
import com.aframe.ui.ViewInject;
import com.aframe.ui.widget.CustomProgressDialog;
import com.aframe.utils.AppUtils;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.config.Config;
import net.mtjo.app.ui.SelectPicPopupShare;
import net.mtjo.app.ui.article.ArticlesActivity;


public class WebViewActivity extends BaseActivity {
	private WebViewActivity mContext;
	private WebView webview;
	private String url,title,sharetitle;
	private boolean isShare;
	private String thumb;
	private String flag;
	private CustomProgressDialog pd;
	
	public static void WebView(Activity act, String url, String title){
		if(null == act || null == title || null== url){
			return;
		}
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		act.startActivity(intent);
	}
	
	/**
	 * 加载网页，并分享
	 */
	public static void ShareWebView(Activity act, String url, String title, String sharetitle, String thumb){
		if(null == act || null == title || null== url){
			return;
		}
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		intent.putExtra("share", true);
		intent.putExtra("sharetitle", sharetitle);
		intent.putExtra("thumb", thumb);
		act.startActivity(intent);
	}
	
	public static void ShareWebViewMain(Activity act, String url, String title, String sharetitle, String thumb, String flag){
		if(null == act || null == title || null== url){
			return;
		}
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		intent.putExtra("share", true);
		intent.putExtra("sharetitle", sharetitle);
		intent.putExtra("thumb", thumb);
		intent.putExtra("flag", flag);
		act.startActivity(intent);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.webview_activity);
	}
	
	@Override
	protected void onInitViews() {
		mContext = this;
		
		Intent intent = getIntent();
		if(null != intent){
			if(intent.hasExtra("url"))url = intent.getStringExtra("url");
			if(intent.hasExtra("title"))title = intent.getStringExtra("title");
			if(intent.hasExtra("share")) isShare = intent.getBooleanExtra("share", false);
			if(intent.hasExtra("sharetitle")) sharetitle = intent.getStringExtra("sharetitle");
			if(intent.hasExtra("thumb")) thumb = intent.getStringExtra("thumb");
			if(intent.hasExtra("flag")) flag = intent.getStringExtra("flag");
			
		}
		initview();
	}
	
	@Override
	protected void onDestroy() {
		if(null != webview)webview.destroy();
		super.onDestroy();
	}
	
	/**
	 * 分享
	 */
//	@Override
	private void doShare() {
		SelectPicPopupShare shareWindow = new SelectPicPopupShare(mContext,thumb);
		if(null != shareWindow){
			shareWindow.setContentUrl(url+"&share=1");
			shareWindow.setTitle(sharetitle + url+"&share=1");
			shareWindow.showAtLocation(mContext.findViewById(R.id.layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}
	
	private void initview(){
		setTitle(title);
//		if(isShare){
//			setActionIcon(R.drawable.title_share);
//		}
		//CookieManager cookieManager = FormAgent.getCookieContainer();

		CookieSyncManager.createInstance(mContext);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		cookieManager.setCookie(Config.API_HOST, AppUtils.getLocalCache(mContext,"PHPSESSID"));
		CookieSyncManager.getInstance().sync();

		System.out.println(cookieManager);



		inflateMenu(R.menu.share);
		
		webview = (WebView) findViewById(R.id.webview);
		if(null != webview){
			webview.setWebViewClient(new WebViewClient(){
		         @Override
		         public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        	 if (url != null && url.contains("more")) {
		        		 if (flag != null) {
		        			 ArticlesActivity.LawArticles(WebViewActivity.this);
						}
							WebViewActivity.this.finish();
						
					}else{
						view.loadUrl(url);   //在当前的webview中跳转到新的url
					}
		 
		          return true;
		         }
		         
		         @Override
		        public void onPageStarted(android.webkit.WebView view,
		        		String url, Bitmap favicon) {
		        	super.onPageStarted(view, url, favicon);
		        	
		        	pd = ViewInject.getprogress(WebViewActivity.this, "正在加载中，请稍后...", true);
		        }
		         
		         @Override
		        public void onPageFinished(android.webkit.WebView view,
		        		String url) {
		        	super.onPageFinished(view, url);
		        	pd.dismiss();
		        }
		         
		        });
			webview.getSettings().setJavaScriptEnabled(true);
			
			webview.setOnKeyListener(new View.OnKeyListener() {    
	            @Override    
	            public boolean onKey(View v, int keyCode, KeyEvent event) {    
	                if (event.getAction() == KeyEvent.ACTION_DOWN) {    
	                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键  时的操作  
	                        webview.goBack();   //后退    
	  
	                        //webview.goForward();//前进  
	                        return true;    //已处理    
	                    }    
	                }    
	                return false;    
	            }   
	        });  
		}
			
		if(StrUtils.strToString(url).length()>0 && null != webview)
			webview.loadUrl(url);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(isShare)
			getMenuInflater().inflate(R.menu.share, menu);
		return true;
	}
	
	@Override  
	public boolean onMenuItemClick(MenuItem menuItem) { 
		if(menuItem.getItemId() == R.id.action_share){
			doShare();
		}
	    return true;  
	}
}
