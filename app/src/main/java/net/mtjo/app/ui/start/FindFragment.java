package net.mtjo.app.ui.start;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;

import com.aframe.Loger;
import com.aframe.bitmap.ImageLoader;
import com.aframe.http.StringCallBack;
import com.aframe.ui.widget.filtermenu.FilterMenuView;

import com.aframe.ui.widget.xlistview.DataQueryInerface;
import com.aframe.ui.widget.xlistview.XListView;

import net.mtjo.app.R;

import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.db.ArticleDBManager;
import net.mtjo.app.entity.Articles;

import net.mtjo.app.ui.article.ArticleAdapter;

import net.mtjo.app.ui.LoadStateView;
import net.mtjo.app.ui.base.WebViewActivity;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class FindFragment extends Fragment implements DataQueryInerface{
	private Activity aty;

	private int pageSize = 10;
	
	private boolean isReflash;
	private boolean isInit = true;
	
	private LoadStateView load_lt;
	private XListView xlistview;
	private FilterMenuView filterview;

	private EditText keyword;
	ArticleDBManager manager;


	private ArticleAdapter adapter;
	private ArrayList<Articles> datalist =  new ArrayList<Articles>();
	
	public FindFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.find_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		aty = getActivity();
		initView();
		loadData(1);

		initListener();
	}
	
	private void initView(){
		xlistview = (XListView) getActivity().findViewById(R.id.find_xlistview);
		load_lt = (LoadStateView) getActivity().findViewById(R.id.loadingStateBox);
		manager = new ArticleDBManager(aty);
		adapter = new ArticleAdapter(aty, datalist);
		xlistview.setAdapter(adapter);
		xlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 <= 0 || arg2 > datalist.size()){
					return;
				}

			}
		});
		
		xlistview.setDataQueryInerface(this);
		load_lt.setOnReloadClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startLoad();
			}
		});
		keyword =(EditText)getActivity().findViewById(R.id.keyword);
		keyword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {//在输入数据时监听
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,//输入数据之前的监听
										  int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {//输入数据之后监听
				//ViewInject.showToast(getActivity(),keyword.getText().toString());

				//reload();
				startLoad();

			}
		});




	}

	public boolean onBackPressed() {
		if(null == filterview)return false;
		return filterview.onPressBack();
	}


	/**
	 * 设置加载失败
	 */
	private void setLoadError(String msg){
		if(isInit){
			if(null != load_lt)
				load_lt.showError(msg);
		}
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
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

	@Override
	public void onRefresh(int pageNo, int pageSize) {
		loadData(1);
	}

	/**
	 * 点击重新加载按钮时，调用此方法，子类必须重写此方法
	 */
	public void reload(){loadData(1);}

	private void loadData(int page){
		HttpPostManager.getSearchResult(keyword.getText().toString(),page, pageSize,
				new StringCallBack() {
					@Override
					public void onSuccess(Object t) {
						showData(this.getJsonArray());
						if (isReflash || isInit) {
							if (datalist.size() == 0) {
								setEmpty("暂时没有更多的文章!");
								return;
							}
						}
						setLoadSuccess();
					}

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						setLoadError();
					}
				}, null, null);
	}

	private void showData(JSONArray arr){
		try {
			ArrayList<Articles> list = Articles.jsonToList(arr,manager);
			if(isReflash){
				datalist.clear();
			}
			if(list.size() > 0)
				datalist.addAll(list);
			if(null != adapter)
				adapter.notifyDataSetChanged();
			setLoadMoreAble(list.size() < pageSize? false:true);
		} catch (JSONException e) {
			Loger.debug("解析文章出错", e);
		} catch (Exception e) {
			Loger.debug("解析文章出错", e);
		}
	}

	@Override
	public void onDestroy() {
		if (manager != null) {
			manager.close();
		}
		ImageLoader.getInstance(aty).stop();
		super.onDestroy();
	}


	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadData(pageNo);
	}


	private void initListener(){
		setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				if(arg2 > 0 && arg2 <= datalist.size()){
					Articles article = datalist.get(arg2 -1);
					if(null == article){
						return;
					}
					if (article.getIsRead()== 0) {
						manager.addArticles(article.getId());
					}
					article.setIsRead(1);
					adapter.notifyDataSetChanged();
					WebViewActivity.ShareWebView(aty, article.getUrl(), "文章列表", article.getTitle(), article.getThumb());
				}
			}
		});
		if(null != xlistview){
			xlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					switch (scrollState) {
						case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
							adapter.setFlagBusy(true);
							break;
						case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
							adapter.setFlagBusy(false);
							break;
						case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
							adapter.setFlagBusy(false);
							break;
						default:
							break;
					}
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
									 int visibleItemCount, int totalItemCount) {
				}
			});
		}
	}


}
