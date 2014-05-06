package com.android.willen.autoshutdown.server;

import java.util.Calendar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.willen.autoshutdown.util.AirModSet;
import com.android.willen.autoshutdown.util.AlarmUtil;
import com.android.willen.autoshutdown.util.FileUtil;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		String timeString= AlarmUtil.getTime(hour, minute);
		String restTime=FileUtil.read("restTime");
		if (restTime.equals(timeString)) {
			AirModSet.close(context);
		}
		//第一时段
		String tempString = FileUtil.read("fuctionOpen");
		if (tempString.equals("open")) {
			String closeTime=FileUtil.read("closeTime");
			String openTime=FileUtil.read("openTime");
			if (closeTime.equals(timeString)) {
				tempString = FileUtil.read("PhoneState");				
				if (tempString.equals("calling")) {
					Log.i("AlarmReceiver", "NeedFly");
					FileUtil.write("NeedFly", "Need");
				}else {
					AirModSet.open(context);
				}
			}else if (openTime.equals(timeString)){
				AirModSet.close(context);
			}
		}
		//第二时段
		tempString = FileUtil.read("fuctionOpenSec");
		if (tempString.equals("open")) {
			String closeTimeSec=FileUtil.read("closeTimeSec");
			String openTimeSec=FileUtil.read("openTimeSec");			
			if (closeTimeSec.equals(timeString)) {				
				tempString = FileUtil.read("PhoneState");
				if (tempString.equals("calling")) {
					Log.i("AlarmReceiver", "NeedFly");
					FileUtil.write("NeedFly", "Need");
				}else {
					AirModSet.open(context);
				}
			}else if (openTimeSec.equals(timeString)){
				AirModSet.close(context);
			}
		}
		
		
	}
}