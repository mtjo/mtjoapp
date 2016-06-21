package net.mtjo.app.ui.start;

import com.aframe.Loger;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import net.mtjo.app.R;
import net.mtjo.app.config.Config;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.ask.AskLawyerListActivity;
import net.mtjo.app.ui.my.FeedBackActivity;
import net.mtjo.app.ui.my.SetAndHelpActivity;
import net.mtjo.app.utils.SharedUserInfo;
import net.mtjo.app.utils.UMengUtil;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment implements OnClickListener{
	private static int TOLOGIN = 0;
	private Activity aty;
	
	public MyFragment(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		aty = getActivity();
		initView();
		initData();
		showTip();
	}
	
	private void initView(){
		getActivity().findViewById(R.id.tologin_btn).setOnClickListener(this);
		getActivity().findViewById(R.id.unlogin_head).setOnClickListener(this);
		getActivity().findViewById(R.id.my_ask_tv).setOnClickListener(this);
//		getActivity().findViewById(R.id.my_collect_tv).setOnClickListener(this);
		getActivity().findViewById(R.id.set_help_tv).setOnClickListener(this);
		getActivity().findViewById(R.id.feed_back_tv).setOnClickListener(this);
		getActivity().findViewById(R.id.contanct_us_tv).setOnClickListener(this);
	}
	
	private void initData(){
		UserInfo user = SharedUserInfo.getUserInfo(aty);
		if(null != user) {
			setLogin(user);
		} else {
			setUnLogin();
		}
	}
	
	private void setLogin(UserInfo user){
		getActivity().findViewById(R.id.nologin_head_lt).setVisibility(View.GONE);
		getActivity().findViewById(R.id.login_head_lt).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.login_content_lt).setVisibility(View.VISIBLE);
		((TextView)getActivity().findViewById(R.id.mobile_tv)).setText(user.getMobile());
	}
	
	private void setUnLogin(){
		getActivity().findViewById(R.id.nologin_head_lt).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.login_head_lt).setVisibility(View.GONE);
		getActivity().findViewById(R.id.login_content_lt).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tologin_btn:
				RegistActivity.open(aty, TOLOGIN);
				break;
			case R.id.unlogin_head:
				RegistActivity.open(aty, TOLOGIN);
				break;
			case R.id.my_ask_tv:
				if(null == SharedUserInfo.getUserInfo(aty)){
					RegistActivity.open(aty, TOLOGIN);
				} else {
					//我的咨询
					AskLawyerListActivity.open(aty, 1);
				}
				break;
			case R.id.set_help_tv:
				SetAndHelpActivity.open(aty, 2);
				break;
			case R.id.feed_back_tv:
				FeedBackActivity.open(aty, 3);
				break;
			case R.id.contanct_us_tv:
				//拨打400电话统计
				UMengUtil.onEvtent(aty.getApplicationContext(), Config.UMENG_SEND_MOBILE);
				
				ViewInject.getConfirmDialog(aty, "确定要拨打我们的客服咨询电话吗？", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						try {
							AppUtils.doCall(aty, Config.SERVICE_CALL);
						} catch (Exception e) {
							Loger.debug("打电话给律师", e);
						}
					}
					
				}, null);
				break;
	
			default:
				break;
		}
	}
	
	/**
	 * 登录成功
	 */
	public void loginSuccess(){
		setLogin(SharedUserInfo.getUserInfo(aty));
	}
	
	/**
	 * 退出成功
	 */
	public void loginOutSuccess(){
		setUnLogin();
	}
	
	/************************首次使用功能引导提示**************************/
	/**
	 * 显示提示
	 */
	private void showTip(){
		String isMainGuid = AppUtils.getLocalCache(aty, "isMyGuid");
		if(!"true".equals(isMainGuid)){
			((MainActivity)getActivity()).showtipView(R.drawable.my_guid);
			AppUtils.saveLocalCache(aty, "isMyGuid", "true");
		}
	}
}
