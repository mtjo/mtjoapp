package net.mtjo.app.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.aframe.Loger;
import net.mtjo.app.R;
import net.mtjo.app.application.SysApplication;
import net.mtjo.app.config.Config;

public class MessageReceiver extends XGPushBaseReceiver {

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		if (context == null || message == null) {
			return;
		}
	}
	
	/**
	 * 判断当前app是否在运行
	 * @param context
	 * @return
	 */
	private boolean isAppRunning(Context context){
		boolean isStarted = false;
		String pname = context.getPackageName();
		try{
			ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			if(null != pname && null != cn && cn.getPackageName().equals(pname))
			{
				isStarted = true;
			}
		}catch(Exception e){
			isStarted = false;
		}
		return isStarted;
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		if (context == null || message == null) {
			return ;
		}
		if (isAppRunning(context)) {
			SysApplication.getInstance().newMsg();
//			return ;
		}
		// APP自主处理消息的过程...
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = message.getContent();
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//Intent intent = new Intent(context, AskLawyerDetailChatActivity.class);
		JSONObject json;
		try {
			json = new JSONObject(message.getCustomContent());
			if(null != json){
/*				intent.putExtra("qid", json.has("qid") ? json.getString("qid"):"");
				intent.putExtra("toUid", json.has("fromuid") ? json.getString("fromuid"):"");
				intent.putExtra("lawName", json.has("fromUsername") ? json.getString("fromUsername"):"");*/
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
//		Bundle bd = new Bundle();
//		bd.putString("nofication", message.getCustomContent());
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtras(bd);
		Loger.debug("消息推送" + message.getCustomContent());
		Loger.debug("消息推送内容" + message.getContent());
		/*PendingIntent pending = PendingIntent.getActivity(context, Config.NoticeID, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);*/
/*
		notification.setLatestEventInfo(context, message.getTitle(), message.getContent(), pending);*/
		manager.notify(Config.NoticeID++, notification);
	}
}
