package net.mtjo.app.ui.my;

import android.util.Log;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import com.aframe.Loger;
import com.aframe.http.StringCallBack;

import com.aframe.ui.ViewInject;
import com.aframe.ui.widget.xlistview.DataQueryInerface;
import com.aframe.ui.widget.xlistview.XListView;
import com.alibaba.fastjson.JSONException;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.FavoriteArticles;
import net.mtjo.app.ui.LoadStateView;
import net.mtjo.app.ui.article.FavoriteArticleAdapter;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.base.WebViewActivity;
import net.mtjo.app.utils.SharedUserInfo;

import org.json.JSONArray;

import java.util.ArrayList;

public class MyFavoriteActivity extends BaseActivity implements DataQueryInerface {
    private MyFavoriteActivity mContext;
    private String uid;
    private ArrayList<FavoriteArticles> datalist = new ArrayList<FavoriteArticles>();
    public boolean isReflash;
    public FavoriteArticleAdapter adapter;

    public XListView xlistview;
    public boolean isInit = true;
    private LoadStateView load_lt;



    @Override
    protected void onInit() {
        setContentLayout(R.layout.my_favorite);

        super.onInit();
        mContext = this;
        loadData();
        initview();
        initListener();

    }
    public void initview(){
        setTitle("我的收藏");
        xlistview = (XListView)findViewById(R.id.my_favorite_xlixtview);
        load_lt = (LoadStateView) findViewById(R.id.loadingStateBox);
        adapter = new FavoriteArticleAdapter(this, datalist);
        xlistview.setAdapter(adapter);
        xlistview.setDataQueryInerface(this);
        xlistview.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                if(arg2 > 0 && arg2 <= datalist.size()){
                    FavoriteArticles article = datalist.get(arg2 -1);
                    if(null == article){
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    WebViewActivity.ShareWebView(mContext, article.getUrl(), article.getTitle(), article.getTitle(), null);
                }
            }
        });

    }

    @Override
    public void onRefresh(int pageNo, int pageSize) {
        isReflash = true;
        loadData();

    }

    @Override
    public void onLoadMore(int pageNo, int pageSize) {
        loadData();
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
    }

    private void showData(JSONArray arr){
        try {
            ArrayList<FavoriteArticles> list = FavoriteArticles.jsonToList(arr);

            if(isReflash){
                datalist.clear();
            }
            if(list.size() > 0)
                datalist.addAll(list);
            if(null != adapter)
                adapter.notifyDataSetChanged();
            setLoadMoreAble(list.size() < 100? false:true);
        } catch (JSONException e) {
            Loger.debug("解析文章出错", e);
        } catch (Exception e) {
            Loger.debug("解析文章出错", e);
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
     * 设置是否允许刷新，和加载更多功能
     * * @param isloadmore
     * 		是否允许加载更多
     */
    public void setLoadMoreAble(boolean isloadmore){
        if(null != xlistview){
            xlistview.setPullLoadEnable(isloadmore);
        }
    }
    private void loadData(){
        uid = SharedUserInfo.getUserInfo(this).getId();
        StringCallBack favoritecallback =  new StringCallBack(){
            @Override
            public void onSuccess(Object t) {
                showData(this.getJsonArray());
                setLoadSuccess();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        };
        HttpPostManager.getFavorite(uid,favoritecallback,this,null);
    }

    /**
     * 设置listview的单击监听
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        if(null != xlistview){
            xlistview.setOnItemClickListener(listener);
        }
    }

    private void initListener(){
        setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                if(arg2 > 0 && arg2 <= datalist.size()){
                    FavoriteArticles article = datalist.get(arg2 -1);
                    if(null == article){
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    Log.i("url", article.getUrl());

                    WebViewActivity.ShareWebView(mContext, article.getUrl(), article.getTitle(), article.getTitle(), null);

                }
            }
        });
    }

}