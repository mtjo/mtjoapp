package net.mtjo.app.ui.ask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import com.aframe.ui.widget.CustomDialog;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.application.SysApplication;
import net.mtjo.app.entity.AskLawyerDetailChat;
import net.mtjo.app.entity.AskLawyerModel;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.base.BaseChatListViewActivity;
import net.mtjo.app.ui.msg.cache.MessageCache;
import net.mtjo.app.utils.SharedUserInfo;

public class AskLawyerDetailChatActivity extends BaseChatListViewActivity{
	private AskLawyerDetailChatActivity mContext;
	private ChatAdapter adapter;
	private ArrayList<AskLawyerDetailChat> datalist = new ArrayList<AskLawyerDetailChat>();
	private String toUid,qid,lawName;
	private UserInfo info;
	
	public static void open(Activity aty,String toUid,String qid,String lawyerName){
		if(null != aty && null != qid && null != toUid){
			Intent intent = new Intent(aty, AskLawyerDetailChatActivity.class);
			intent.putExtra("qid", qid);
			intent.putExtra("toUid", toUid);
			intent.putExtra("lawName", lawyerName);
			aty.startActivity(intent);
		}
	}
	
	@Override
	protected void onInitViews() {
		super.onInitViews();
		mContext = this;
		
		info = SharedUserInfo.getUserInfo(this);
		Intent intent = getIntent();
		if(null != intent){
			if(intent.hasExtra("qid"))
				qid = intent.getStringExtra("qid");
			if(intent.hasExtra("toUid"))
				toUid = intent.getStringExtra("toUid");
			if(intent.hasExtra("lawName"))
				lawName = intent.getStringExtra("lawName");
		}
		
		setTitle(lawName+getString(R.string.law));
		datalist.clear();
		adapter = new ChatAdapter(mContext, datalist);
		setAdapter(adapter);
		
		initData();
	}
	
	/**
	 * 发送消息
	 */
	public void sendMsg(View v){
		String s = getSendText(); //chat_input == null ? "":StrUtils.strToString(chat_input.getText().toString());
		if(s.length() > 0 && s.length() <= 500){
			sendMsg(s);
			clenSendText();
		}else if(s.length() > 500){
			ViewInject.showToast(mContext, "允许最长发送500字，请精简您的问题...");
		}else{
			ViewInject.showToast(mContext, "请输入回复内容");
		}
	}
	
	/**
	 * 加载数据
	 */
	private void initData(){
		StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				praseData(this.getJsonContent());
				setLoadSuccess();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				ViewInject.showToast(mContext, this.getDesc());
				setLoadError();
			}
		};
		Loger.debug("问律师对话框参数：qid"+qid+"toUid"+toUid);
		HttpPostManager.getAskLawyerDetailChat(info.getAuthtoken(),
				qid, toUid, callback, this, null);
	}
	
	private void praseData(JSONObject obj){
		if(null == obj)return;
		Loger.debug("问律师对话框数据："+obj.toString());
		try {
			AskLawyerModel asklawyer = ParseJson.getEntity(obj.getString("question"), AskLawyerModel.class);
			List<AskLawyerDetailChat> l = ParseJson.getEntityList(obj.getString("dialogue"), AskLawyerDetailChat.class);
			AskLawyerDetailChat chat = new AskLawyerDetailChat();
			chat.setFromUid(info.getUid());
			chat.setVoice(asklawyer.getContent());
			chat.setCreateTime(asklawyer.getCreateTime());
			if(null != adapter){
				adapter.setLawUrl(obj.getString("lawyerImage"));
			}
			l.add(0, chat);
			datalist.clear();
			datalist.addAll(l);
			adapter.notifyDataSetChanged();
			
			if(!obj.isNull("lawyerName")){
				lawName = obj.getString("lawyerName");
				setTitle(lawName+getString(R.string.law));
			}
			
			//更新未读消息
			MessageCache.create().updateTypeByqid(toUid, qid, SharedUserInfo.getUserInfo(mContext).getUid());
			SysApplication.getInstance().updateMsg();
			
			/**
			 * 显示是否邀请评价
			 */
			boolean falg = false;
			if(StrUtils.strToString(asklawyer.getStatusWord()).length() == 10 &&
					asklawyer.getStatusWord().substring(8, 9).equals("0")){
				int ascount = 0;
				for(int i=0,j=l.size(); i<j; i++){
					if(!l.get(i).getFromUid().equals(info.getUid())){
						ascount++;
						if(ascount==2){
							falg = true;
							break;
						}
					}
				}
			}
			if(falg){
				((TextView)findViewById(R.id.prise_tv)).setText(lawName+"律师的回答对您是否有帮助？点击评价");
				findViewById(R.id.prise_tv).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.prise_tv).setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			Loger.debug("对话数据解析", e);
		}
	}
	
	
	/**
	 * 发送内容
	 */
	private void sendMsg(String msg){
		StringCallBack callback = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				AskLawyerDetailChat chat  = (AskLawyerDetailChat) this.getResourceObj();
				chat.setIssending(false);
				if (this.getStrContent() != null) {
					chat.setId(this.getStrContent());
				}else{
					ViewInject.showToast(mContext, "发送失败，未知错误!");
				}
				if(null != adapter)adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				AskLawyerDetailChat chat  = (AskLawyerDetailChat) this.getResourceObj();
				chat.setIssending(false);
				ViewInject.showToast(mContext, this.getDesc());
				if(null != adapter)adapter.notifyDataSetChanged();
			}
		};
		
		AskLawyerDetailChat chat = new AskLawyerDetailChat();
		chat.setFromUid(info.getUid());
		chat.setCreateTime((new Date().getTime()/1000)+"");
		chat.setVoice(msg);
		chat.setIssending(true);
		datalist.add(chat);
		if(null != adapter)adapter.notifyDataSetChanged();
		callback.setResourceObj(chat);
		HttpPostManager.insertVoice(info.getAuthtoken(), msg, qid, toUid, "0", callback, this, null);
	}
	
	private Dialog dialog;
	public void onPrise(View v){
		if(null != dialog){
			dialog = null;
		}
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.prise_dialog_layout, null);
		((TextView)layout.findViewById(R.id.prise_title_tv)).setText(lawName+"律师的回答对您是否有帮助？");
		builder.setContentView(layout);
		layout.findViewById(R.id.help_tv).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != dialog)
					dialog.dismiss();
				setPrise("1");
				dialog  = null;
			}
		});
		layout.findViewById(R.id.unhelp_tv).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != dialog)
					dialog.dismiss();
				setPrise("2");
				dialog  = null;
			}
		});
		dialog= builder.create();
		dialog.show();
	}
	
	private void setPrise(String status){
		HttpPostManager.setResove(info.getAuthtoken(), qid, status, new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				ViewInject.showToast(mContext, "评价成功，谢谢您的反馈...");
				findViewById(R.id.prise_tv).setVisibility(View.GONE);
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				ViewInject.showToast(mContext, "评价失败，请重新提交...");
			}
		}, mContext, "正在提交，请稍后...");
	}
}
