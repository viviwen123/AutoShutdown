package com.android.willen.autoshutdown.server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.willen.autoshutdown.util.AirModSet;
import com.android.willen.autoshutdown.util.FileUtil;

public class PhoneReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {		
		TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Service.TELEPHONY_SERVICE);
		 switch (tm.getCallState()) {
		 case TelephonyManager.CALL_STATE_OFFHOOK://电话打进来接通状态；电话打出时首先监听到的状态。
			 Log.i("onCallStateChanged", "CALL_STATE_OFFHOOK");
			 setPhoneState(context,true);
				break;
			case TelephonyManager.CALL_STATE_RINGING://电话打进来状态
				Log.i("onCallStateChanged", "CALL_STATE_RINGING");
				setPhoneState(context,true);
				break;
			case TelephonyManager.CALL_STATE_IDLE://不管是电话打出去还是电话打进来都会监听到的状态。
				Log.i("onCallStateChanged", "CALL_STATE_IDLE");
				setPhoneState(context,false);
				break;
		 }		
	}
	
	private void setPhoneState(Context c,boolean state) {
		if (state) {
			FileUtil.write("PhoneState", "calling");
			Log.i("PhoneState", "calling");
		}else {
			Log.i("PhoneState", "NotCalling");
			FileUtil.write("PhoneState", "NotCalling");
			String tempString = FileUtil.read("NeedFly");
			if (tempString.equals("Need")) {
				FileUtil.write("NeedFly", "NotNeed");
				AirModSet.open(c);				
			}
		}
		
	}
}

