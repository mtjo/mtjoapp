package net.mtjo.app.ui.ask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.utils.AppUtils;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.AskLawyerDetailModel;
import net.mtjo.app.ui.base.BaseListViewActivity;
import net.mtjo.app.utils.SharedUserInfo;

/**
 * 问律师列表详情
 * 
 * @author zxp
 * 
 */
public class AskLawyerDetailActivity extends BaseListViewActivity {
	private AskLawyerDetailActivity mContext;
	private String qid;
	private AskLawyerDetailAdapter adapter;
	private List<AskLawyerDetailModel> detailList = new ArrayList<AskLawyerDetailModel>();
//	private AskLawyerDetailModel model;//用于显示初次发布咨询提示
	private TextView tv_sended_tips;//发送完提示等待回复
	private LinearLayout ll_time_and_tips;//离律师上线剩余时间提示
	private LinearLayout ll_risk_test;//风险自测提示
	private TextView tv_wait_time;//离律师上线的剩余时间


	public static void open(String qid, Activity aty, int requestCode){
		Intent intent = new Intent(aty, AskLawyerDetailActivity.class);
		intent.putExtra("qid", qid);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.ask_detail_layout, R.color.ask_detail_itme_bg);
	}
	
	@Override
	public void initView() {
		super.initView();

		tv_sended_tips = (TextView)this.findViewById(R.id.tv_sended_tips);
		ll_time_and_tips = (LinearLayout) this.findViewById(R.id.ll_time_and_tips);
		ll_risk_test = (LinearLayout) this.findViewById(R.id.ll_risk_test);
		tv_wait_time = (TextView) this.findViewById(R.id.tv_wait_time);

		mContext = this;
		setTitle(getString(R.string.ask_lawyer_detail_title_text));
		xlistview.setDivider(null);
		xlistview.setDividerHeight(0);

		Intent intent = getIntent();
		if(intent.hasExtra("qid"))
			qid = intent.getStringExtra("qid");
		
		setLoadMoreAble(false);
		adapter = new AskLawyerDetailAdapter(this, detailList);
		setAdapter(adapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 <= 1 || detailList.size() < arg2)
					return;
				AskLawyerDetailModel model = detailList.get(arg2 - 1);
				if (null != model)
					AskLawyerDetailChatActivity.open(
							AskLawyerDetailActivity.this, model.getFromUid(),
							qid, model.getLawyerName());
			}
		});

		showTip();
		loadDate(1);
	}

	/**
	 * 有律师回复，提示全部隐藏
	 */
	private void tipsAndRisktestGone(){
		if(tv_sended_tips == null || ll_time_and_tips == null || ll_risk_test == null || tv_wait_time == null)
			return;
		tv_sended_tips.setVisibility(View.GONE);
		ll_time_and_tips.setVisibility(View.GONE);
		ll_risk_test.setVisibility(View.GONE);
		tv_wait_time.setVisibility(View.GONE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRefresh(int pageNo, int pageSize) {
		super.onRefresh(pageNo, pageSize);
		loadDate(1);
	}

	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadDate(pageNo);
	}

	private void loadDate(final int pageNo) {
		StringCallBack callback = new StringCallBack() {
			@Override
			public void onSuccess(Object t) {
				showData(this.getJsonContent());
				
				if (detailList.size() == 0) {
					setEmpty("暂无律师回复该问题!");
					return;
				}
				setLoadSuccess();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				setLoadError(this.getDesc());
			}
		};

		HttpPostManager.getAskLawyerListDetail(SharedUserInfo.getUserInfo(this)
				.getAuthtoken(), qid, callback, this, null);
	}
	
	/**
	 * 数据解析
	 */
	private void showData(JSONObject obj){
		detailList.clear();
		if(null == obj) return;
		try {
			JSONObject qjosn = obj.has("question") ? obj.getJSONObject("question") : null;
			if(null == qjosn){
				return;
			}
			AskLawyerDetailModel question = new AskLawyerDetailModel();
			question.setQid(qjosn.has("id") ? qjosn.getString("id") : null);
			question.setCreateTime(qjosn.has("createTime")?qjosn.getString("createTime"):"00:00");
			question.setVoice(qjosn.has("content")?qjosn.getString("content"):"");
			question.setLawyerName("我");
			detailList.add(question);
			
			if(obj.has("answerList")){
				List<AskLawyerDetailModel> list = ParseJson.getEntityList(
						obj.getString("answerList"),
						AskLawyerDetailModel.class);
				if(null != list && list.size() > 0)
					detailList.addAll(list);
			}
			
			adapter.notifyDataSetChanged();
			
			if(detailList.size() > 1){
				xlistview.setSelection(1);
			}
			
		} catch (JSONException e) {
			Loger.debug("问律师详情", e);
		}
	}
	
	/************************首次使用功能引导提示**************************/
	/**
	 * 显示提示
	 */
	private void showTip(){
		String isMainGuid = AppUtils.getLocalCache(mContext, "isAskDetailGuid");
		if(!"true".equals(isMainGuid)){
			showtipView(R.drawable.asklist_guid);
			AppUtils.saveLocalCache(mContext, "isAskDetailGuid", "true");
		}
	}
}
