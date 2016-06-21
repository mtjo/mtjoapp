package net.mtjo.app.ui.ask;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.AskLawyerModel;
import net.mtjo.app.ui.base.BaseListViewActivity;
import net.mtjo.app.utils.SharedUserInfo;

/**
 * 问律师列表
 * @author zxp
 *
 */
public class AskLawyerListActivity extends BaseListViewActivity {
	private AskLawyerListActivity mContext;
	private List<AskLawyerModel> modelList = new ArrayList<AskLawyerModel>(); 
	private AskLawyerAdapter adapter;
	
	public static void open(Activity aty, int requestCode){
		if (aty == null) {
			return;
		}
		Intent intent = new Intent(aty, AskLawyerListActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void initView() {
		super.initView();
		mContext = this;
		
		setTitle("我的咨询");
		setLoadMoreAble(false);
		adapter = new AskLawyerAdapter(this, modelList);
		setAdapter(adapter);
		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AskLawyerModel model = (AskLawyerModel) arg0.getItemAtPosition(arg2);
				AskLawyerDetailActivity.open(model.getId(), mContext, 0);
			}
		});
		
		loadData(1);
	}
	
	@Override
	public void reload() {
		loadData(1);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void loadData( int pageNo){
		StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				if (this.getJsonArray() != null) {
					List<AskLawyerModel> list = ParseJson.getEntityList(this.getJsonArray().toString(), AskLawyerModel.class);
					if(isReflash || isInit)
						modelList.clear();
					modelList.addAll(list) ;
					adapter.notifyDataSetChanged();
					
					setLoadMoreAble(list.size() < pageSize ? false:true);
				}
				
				if(modelList.size() == 0){
					setEmpty("您暂时还没有咨询记录!");
					return;
				}
				
				setLoadSuccess();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				setLoadError(this.getDesc());
			}
		};
		HttpPostManager.getAskLawyerList(SharedUserInfo.getUserInfo(this).getAuthtoken(),
				pageNo, pageSize, callback, this, null);
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
}
