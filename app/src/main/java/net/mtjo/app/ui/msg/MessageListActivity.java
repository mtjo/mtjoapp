package net.mtjo.app.ui.msg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.application.SysApplication;
import net.mtjo.app.entity.MessageModel;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.ask.AskLawyerDetailChatActivity;
import net.mtjo.app.ui.base.BaseListViewActivity;
import net.mtjo.app.ui.base.WebViewActivity;
import net.mtjo.app.ui.msg.cache.MessageCache;
import net.mtjo.app.utils.SharedUserInfo;

public class MessageListActivity extends BaseListViewActivity {
	private MessageListActivity mContext;
	private List<MessageModel> modelList = new ArrayList<MessageModel>(); 
	private MessageAdapter adapter;
	
	public static void open(Activity aty, int requestCode){
		if (aty == null) {
			return;
		}
		Intent intent = new Intent(aty, MessageListActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void initView() {
		super.initView();
		mContext = this;
		
		setTitle("消息中心");
		setLoadMoreAble(false);
		adapter = new MessageAdapter(this, modelList);
		setAdapter(adapter);
		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MessageModel model = (MessageModel) arg0.getItemAtPosition(arg2);
				if(null == model)return;
				
				switch (model.getType()) {
					case 1:
						WebViewActivity.ShareWebView(mContext,model.getParams_url(),"法律讲堂","","");
						MessageCache.create().updateType(model.getFromuid(), model.getParams(), model.getTouid());
						SysApplication.getInstance().updateMsg();
						break;
					case 2:
						AskLawyerDetailChatActivity.open(mContext, model.getFromuid(), 
								model.getParams_qid(), "");
						break;
	
					default:
						break;
				}
				model.setCount(0);
				adapter.notifyDataSetChanged();
			}
		});
		
		loadData();
	}
	
	@Override
	public void reload() {
		loadData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void loadLocalData(int pageNo){
		if(isReflash || isInit)
			modelList.clear();
		List<MessageModel> list = MessageCache.create().getMsgList(pageNo, pageSize, SharedUserInfo.getUserInfo(mContext).getUid());
		if(null != list){
			modelList.addAll(list);
			setLoadMoreAble(list.size() < pageSize ? false:true);
		}
		
		if(modelList.size() == 0){
			setEmpty("您暂时没有消息记录!");
			return;
		}
		setLoadSuccess();
		adapter.notifyDataSetChanged();
	}
	
	private void loadData(){
		final UserInfo user = SharedUserInfo.getUserInfo(mContext);
		if(null == user) return;
		MessageModel model = MessageCache.create().getNewlySearch(user.getUid());
		long lastid = null==model? 0:model.getMsgid();
		
		StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				if (this.getJsonArray() != null) {
					List<MessageModel> list = ParseJson.getEntityList(this.getJsonArray().toString(), MessageModel.class);
					for(int i=0,l=list.size(); i<l; i++){
						MessageCache.create().add(list.get(i),user.getUid());
					}
					if(list.size() > 0)
						SysApplication.getInstance().updateMsg();
					list.clear();
					list = null;
				}
				loadLocalData(1);
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				ViewInject.showToast(mContext, this.getDesc());
				loadLocalData(1);
			}
		};
		HttpPostManager.getNewMsg(user.getAuthtoken(), 
				lastid,callback, this,null);
	}

	@Override
	public void onRefresh(int pageNo, int pageSize) {
		super.onRefresh(pageNo, pageSize);
		loadData();
	}
	
	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadLocalData(pageNo);
	}
}
