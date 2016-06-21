package net.mtjo.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReciver extends BroadcastReceiver {
	public static SmsREciverListener smslisterner;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();  
        SmsMessage msg = null;  
        if (null != bundle) {  
            Object[] smsObj = (Object[]) bundle.get("pdus");  
            for (Object object : smsObj) {  
                msg = SmsMessage.createFromPdu((byte[]) object);  
                if(null != smslisterner){
                	smslisterner.onReceive(msg.getMessageBody());
                }
            }  
        }
	}
	
	public interface SmsREciverListener{
		public void onReceive(String msg);
	}
}
