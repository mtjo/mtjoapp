package net.mtjo.app.ui.base;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.aframe.ui.widget.xlistview.DataQueryInerface;
import com.aframe.ui.widget.xlistview.XListView;
import net.mtjo.app.R;
import net.mtjo.app.ui.LoadStateView;

public class BaseListViewActivity extends BaseActivity implements DataQueryInerface{
	protected LoadStateView loadview;
	protected XListView xlistview;
	protected int pageSize = 10;
	protected boolean isReflash;
	protected boolean isInit = true;
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.listview_activity);
	}
	
	@Override
	protected void onInitViews() {
		initView();
	}
	
	public void initView(){
		xlistview = (XListView) findViewById(R.id.xlistview);
		loadview = (LoadStateView) findViewById(R.id.loadingStateBox);
		if(null != xlistview){
			xlistview.setDataQueryInerface(this); 
		}
		if(null != loadview){
			loadview.setOnReloadClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					reTry(v);
				}
			});
		}
		setTitle(getTitle().toString());
	}
	
	/**
	 * 设置listview的适配器
	 */
	public void setAdapter(BaseAdapter adapter){
		if(null != xlistview){
			xlistview.setAdapter(adapter);
		}
	}
	
	/**
	 * 设置listview的单击监听
	 */
	public void setOnItemClickListener(OnItemClickListener listener){
		if(null != xlistview){
			xlistview.setOnItemClickListener(listener);
		}
	}
	
	/**
	 * 设置加载成功
	 */
	public void setLoadSuccess(){
		if(isInit){
			if(null != loadview)
				loadview.setVisibility(View.GONE);
			if(null != xlistview)
				xlistview.setVisibility(View.VISIBLE);
		}
		isInit = false;
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(true);
	}
	
	/**
	 * 设置加载失败
	 */
	public void setLoadError(){
		if(isInit){
			if(null != loadview)
				loadview.showError(null);
		}
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
	}
	
	/**
	 * 设置加载失败
	 */
	public void setLoadError(String reason){
		if(isInit){
			if(null != loadview)
				loadview.showError(reason);
		}
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
	}
	
	/**
	 * 设置空内容状态
	 * @param emptytip
	 * 				空白内容提示
	 */
	public void setEmpty(String emptytip){
		if(isInit){
			if(null != loadview)
				loadview.showEmpty(emptytip);
		}
		isInit = false;
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
	}
	
	/**
	 * 设置是否允许刷新，和加载更多功能
	 * @param isreflash
	 * 		  是否允许刷新
	 */
	public void setReflashAble(boolean isreflash){
		if(null != xlistview){
			xlistview.setPullRefreshEnable(isreflash);
		}
	}
	
	/**
	 * 设置是否允许刷新，和加载更多功能
	 * * @param isloadmore
	 * 		是否允许加载更多
	 */
	public void setLoadMoreAble(boolean isloadmore){
		if(null != xlistview){
			xlistview.setPullLoadEnable(isloadmore);
		}
	}
	
	/**
	 * 设置头部内容
	 */
	public void setTopContent(View v){
		if(null != v && null != findViewById(R.id.top_content_lt)){
			findViewById(R.id.top_content_lt).setVisibility(View.VISIBLE);
			((LinearLayout)findViewById(R.id.top_content_lt)).addView(v,
					new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT));
		}
	}
	
	/**
	 * 重新加载
	 */
	public void reTry(View v){
		if(null != loadview)
			loadview.startLoad();
		reload();
	}
	
	/**
	 * 子类需重写此方法，加载失败后，可以通过此方法重新加载
	 */
	public void reload(){	
	}
	
	/**
	 * 刷新，子类是否需要刷新功能重写此方法
	 */
	@Override
	public void onRefresh(int pageNo, int pageSize) {
		isReflash = true;
	}

	/**
	 * 加载更多，子类是否需要分页加载更多功能重写此方法
	 */
	@Override
	public void onLoadMore(int pageNo, int pageSize) {
	}
}
