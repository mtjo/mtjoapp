package net.mtjo.app.ui.base;

import com.umeng.analytics.MobclickAgent;
import net.mtjo.app.R;
import net.mtjo.app.application.SysApplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.ImageView;

public class BaseActivity extends ActionBarActivity implements OnInflateListener, OnMenuItemClickListener{
	private Toolbar toolbar;
	private ViewStub viewstub;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity);
		SysApplication.getInstance().addActivity(this);
	
		init();
		onInit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private void init(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		viewstub = (ViewStub) findViewById(R.id.viewstub);
		//设置标题
		setSupportActionBar(toolbar);
		//返回按钮监听
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBack(v);
			}
		});
		toolbar.setNavigationIcon(R.drawable.back_icon);
		toolbar.setOnMenuItemClickListener(this);
		toolbar.setTitleTextColor(0xffffffff);
		
		// 设置成功加载布局资源文件后事件通知的监听器。
		viewstub.setOnInflateListener(this);
	}
	
	/**
	 * 子类重写此方法，调用setContentLayout方法设置当前页面
	 */
	protected void onInit(){}
	
	/**
	 * 子类重写此方法，页面初始化完毕调用此方法
	 */
	protected void onInitViews(){}
	
	/**
	 * 返回点击
	 */
	protected void onBack(View v){
		this.finish();
	}
	
	/**
	 * 设置页面
	 * viewstub只能inflate一次
	 */
	protected void setContentLayout(int layout){
		if(null != viewstub){
			viewstub.setLayoutResource(layout);
			viewstub.inflate();
		}
	}
	
	/**
	 * 设置页面,并设置背景色
	 * viewstub只能inflate一次
	 */
	protected void setContentLayout(int layout, int color){
		if(null != viewstub){
			viewstub.setLayoutResource(layout);
			viewstub.inflate().setBackgroundColor(getResources().getColor(color));;
		}
	}

	/**
	 * 内容页面加载完毕后回
	 */
	@Override
	public void onInflate(ViewStub stub, View inflated) {
		onInitViews();
	}
	
	//设置左边图标
	protected void setBackIcon(int resId){
		if(null != toolbar){
			toolbar.setNavigationIcon(resId);
		}
	}
	
	protected void setBackIcon(Drawable drawable){
		if(null != toolbar) {
			toolbar.setNavigationIcon(drawable);
		}
	}
	
	/**
	 * 设置布局
	 */
	public void inflateMenu(int menu){
		if(null != toolbar) {
			toolbar.inflateMenu(menu);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		return false;
	}
	
	public void showtipView(int bgresid){
		ImageView v = (ImageView) findViewById(R.id.tipview);
		v.setVisibility(View.VISIBLE);
		v.setBackgroundResource(bgresid);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hidetipView();
			}
		});
	}
	
	public void hidetipView(){
		findViewById(R.id.tipview).setVisibility(View.GONE);
	}
}
