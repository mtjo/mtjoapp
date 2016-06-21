package net.mtjo.app.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class UMengUtil {
	
	public static void onEvtent(Context context, String id){
		if(null != context && null != id){
			MobclickAgent.onEvent(context, id);
//			MobclickAgent.onEventValue(context, id, null, 0);
		}
	};

}
