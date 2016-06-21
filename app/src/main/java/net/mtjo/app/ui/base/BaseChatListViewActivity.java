package net.mtjo.app.ui.base;

import com.aframe.ui.widget.xlistview.XChatListView;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.ui.LoadStateView;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BaseChatListViewActivity extends BaseActivity{
	protected LoadStateView loadview;
	protected XChatListView listview;
	protected EditText chat_input;
	
	protected int pageSize = 10;
	protected boolean isReflash;
	protected boolean isInit = true;
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.chat);
	}
	
	@Override
	protected void onInitViews() {
		loadview = (LoadStateView) findViewById(R.id.loadingStateBox);
		chat_input = (EditText) findViewById(R.id.chat_input);
		listview = (XChatListView) findViewById(R.id.chat_listview);
		
		if(null != listview){
			listview.setDivider(null);
			listview.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
			listview.setCacheColorHint(getResources().getColor(android.R.color.transparent));
			listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			
			listview.setPullRefreshEnable(false);
			listview.setPullLoadEnable(false);
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
		if(null != listview){
			listview.setAdapter(adapter);
		}
	}
	
	/**
	 * 设置加载成功
	 */
	public void setLoadSuccess(){
		if(isInit){
			if(null != loadview)
				loadview.setVisibility(View.GONE);
			if(null != listview)
				listview.setVisibility(View.VISIBLE);
		}
		isInit = false;
		isReflash = false;
		if(null != listview)
			listview.loadOk(true);
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
		if(null != listview)
			listview.loadOk(false);
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
		if(null != listview)
			listview.loadOk(false);
	}
	
	/**
	 * 设置是否允许刷新，和加载更多功能
	 * @param isreflash
	 * 		  是否允许刷新
	 */
	public void setReflashAble(boolean isreflash){
		if(null != listview){
			listview.setPullRefreshEnable(isreflash);
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
	 * 设置可发送
	 */
	public void sendAble(boolean sendable){
		findViewById(R.id.chat_bottom_lay).setVisibility(
				sendable ? View.VISIBLE:View.GONE);
	}
	
	/**
	 * 获取发送文本
	 */
	public String getSendText(){
		return StrUtils.strToString(chat_input.getText().toString());
	}
	
	/**
	 * 清除文本
	 */
	public void clenSendText(){
		chat_input.setText("");
	}
	
	/**
	 * 返回
	 */
	public void goBack(View v){
		finish();
	}
	
	/**
	 * 发送消息
	 */
	public void sendMsg(View v){
		
	}
	
	
	/**
	 * 子类需重写此方法，加载失败后，可以通过此方法重新加载
	 */
	public void reload(){	
	}
}
