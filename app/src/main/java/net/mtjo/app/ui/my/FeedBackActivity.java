package net.mtjo.app.ui.my;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.aframe.ui.ViewInject;
import com.aframe.ui.widget.CustomProgressDialog;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.ui.base.BaseActivity;

/**
 * 反馈
 * @author zxp
 *
 */
public class FeedBackActivity extends BaseActivity {
	private FeedbackAgent agent;
	private Conversation conversation;
	
	private EditText content;
	private CustomProgressDialog pd;
	
	public static void open(Activity aty, int requestCode){
		Intent intent = new Intent(aty, FeedBackActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.me_feed_back);
	}
	
	@Override
	protected void onInitViews() {
		content = (EditText) findViewById(R.id.content);
		content.setHint(R.string.me_feed_back_hint);
		setTitle(R.string.me_feed_back_title_text);
		agent = new FeedbackAgent(this);
		conversation = agent.getDefaultConversation();
	}
	
	/**
	 * 提交
	 */
	public void submit(View v){
		try {
			if (StrUtils.isEmpty(content.getText().toString())) {
				ViewInject.showToast(this, getString(R.string.me_feed_back_msg));
			}else{
				pd = ViewInject.getprogress(this, "正在发送，请稍后...", true);
				conversation.addUserReply(content.getText().toString());
				sync();
			}
		} catch (Exception e) {
			
		}
	}
	
	void sync() {
		Conversation.SyncListener listener = new Conversation.SyncListener() {
			@Override
			public void onReceiveDevReply(List<DevReply> arg0) {
				pd.dismiss();
				ViewInject.showToast(FeedBackActivity.this, "已收录，谢谢您的反馈！");
				FeedBackActivity.this.finish();
			}

			@Override
			public void onSendUserReply(List<Reply> arg0) {
			}
		};
		conversation.sync(listener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.submit, menu);
		return true;
	}
	
	@Override  
	public boolean onMenuItemClick(MenuItem menuItem) { 
		if(menuItem.getItemId() == R.id.action_submit){
			submit(null);
		}
	    return true;  
	}
	
}
