package net.mtjo.app.ui.my;

import android.view.View;
import android.widget.AdapterView;

import com.aframe.http.StringCallBack;
import com.aframe.ui.ViewInject;
import com.aframe.ui.widget.xlistview.XListView;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.ui.base.BaseActivity;

import java.util.ArrayList;

public class MyFavoriteActivity extends BaseActivity {
    private MyFavoriteActivity mContext;
    private XListView xlistview;
    private ArrayList datalist = new ArrayList<>();


    @Override
    protected void onInit() {
        setContentLayout(R.layout.my_favorite);
    }

    @Override
    protected void onInitViews() {
        mContext = this;
        setTitle("我的收藏");
        getdata();
        initView();

    }
    private  void  initView(){
        xlistview = (XListView)findViewById(R.id.my_favorite_xlixtview);

        StringCallBack favoritecallback = new StringCallBack(){
            @Override
            public void onSuccess(Object t) {
                ViewInject.showToast(mContext,this.getStrContent());
            }
        };
        HttpPostManager.getFavorite(favoritecallback,null,null);



        xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(arg2 <= 0 || arg2 > datalist.size()){
                    return;
                }

            }
        });


    }
    public void getdata (){


    }
}