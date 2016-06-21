package net.mtjo.app.ui.base;

import com.aframe.ui.widget.xlistview.DataQueryInerface;
import com.aframe.ui.widget.xlistview.XListView;
import net.mtjo.app.R;
import net.mtjo.app.ui.LoadStateView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class BaseListFragment extends Fragment implements DataQueryInerface{
	protected ViewGroup viewGroup;
	private View mMainView;
	private LoadStateView load_lt;
	protected XListView xlistview;
	protected int pageSize = 10;
	protected boolean isReflash;
	protected boolean isInit = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.listview_fragment, viewGroup, false);
		init();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 
		
		return mMainView;
	}
	
	/**
	 * 初始化，子类有需要可重写此方法，但必须先调用父类的此方法
	 */
	public void init(){
		xlistview = (XListView) mMainView.findViewById(R.id.xlistview);
		load_lt = (LoadStateView) mMainView.findViewById(R.id.loadingStateBox);
		if(xlistview != null){
			xlistview.setDataQueryInerface(this);
		}
		if(null != load_lt){
			load_lt.setOnReloadClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoad();
				}
			});
		}
	}
	
	public void startLoad(){
		isInit = true;
		isReflash = true;
		if(null != load_lt){
			load_lt.setVisibility(View.VISIBLE);
			load_lt.startLoad();
		}
		if(null != xlistview){
			xlistview.setReload();
			xlistview.setVisibility(View.GONE);
		}
		reload();
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
			if(null != load_lt)
				load_lt.setVisibility(View.GONE);
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
			if(null != load_lt)
				load_lt.showError(null);
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
			if(null != load_lt)
				load_lt.showEmpty(emptytip);
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
	 * 刷新
	 * 子类需要刷新功能时，可重写此方法即可
	 */
	@Override
	public void onRefresh(int pageNo, int pageSize) {
		isReflash = true;
	}

	/**
	 *加载更多 
	 *子类需要加载更多功能时，可重写此方法即可
	 */
	@Override
	public void onLoadMore(int pageNo, int pageSize) {
	}
	
	/**
	 * 点击重新加载按钮时，调用此方法，子类必须重写此方法
	 */
	public void reload(){}
}
