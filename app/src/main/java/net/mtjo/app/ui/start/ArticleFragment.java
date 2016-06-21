package net.mtjo.app.ui.start;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.aframe.Loger;
import com.aframe.bitmap.ImageLoader;
import com.aframe.http.StringCallBack;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.db.ArticleDBManager;
import net.mtjo.app.entity.Articles;
import net.mtjo.app.ui.article.ArticleAdapter;
import net.mtjo.app.ui.base.BaseListFragment;
import net.mtjo.app.ui.base.WebViewActivity;

public class ArticleFragment extends BaseListFragment {
	
	private Activity mContext;
	private ArticleAdapter adapter;
	private ArrayList<Articles> datalist = new ArrayList<Articles>();
	private static ArticleDBManager manager;
	
	public ArticleFragment(){}
	@SuppressLint("ValidFragment")
	public ArticleFragment(ViewGroup group){
		this.viewGroup = group;
	}

	@Override
	public void init() {
		super.init();
		mContext = getActivity();
		manager = new ArticleDBManager(mContext);
		
		initView();
		initListener();
		loadData(1);
	}
	
	@Override
	public void onDestroy() {
		if (manager != null) {
			manager.close();
		}
		ImageLoader.getInstance(mContext).stop();
		super.onDestroy();
	}
	
	@Override
	public void onRefresh(int pageNo, int pageSize) {
		super.onRefresh(pageNo, pageSize);
		loadData(1);
	}
	
	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadData(pageNo);
	}
	
	private void initView(){
		setLoadMoreAble(false);
		adapter = new ArticleAdapter(mContext, datalist);
		setAdapter(adapter);
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
					WebViewActivity.ShareWebView(mContext, article.getUrl(), "文章列表", article.getTitle(), article.getThumb());
				}
			}
		});
		if(null != xlistview){
			xlistview.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					switch (scrollState) {
						case OnScrollListener.SCROLL_STATE_FLING:
							adapter.setFlagBusy(true);
							break;
						case OnScrollListener.SCROLL_STATE_IDLE:
							adapter.setFlagBusy(false);
							break;
						case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
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
	
	private void loadData(int page){
		HttpPostManager.getArticleList(page, pageSize,
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
}
