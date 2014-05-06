package com.android.willen.autoshutdown.server;

import com.android.willen.autoshutdown.util.AlarmUtil;
import com.android.willen.autoshutdown.util.FileUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenOnReceiver extends BroadcastReceiver {
	private static final String TAG = "ScreenOnReceiver";
	private String timeString;

	public void onReceive(Context context, Intent paramIntent) {
		if (ServiceStatus.getInstance().isRunning()) {
			Log.i(TAG, "Running!");
			return;
		}
		Log.i(TAG, "NotRunning!");
		int hour = 0;
		int minute = 0;
		// 关机
		timeString = FileUtil.read("closeTime");
		if (!timeString.equals("")) {
			hour = Integer.valueOf(timeString.substring(0, 2));
			minute = Integer.valueOf(timeString.substring(3, 5));
			timeString = AlarmUtil.getTime(hour, minute);
			AlarmUtil.setAlarm(context, timeString, 1, true);
		}
		// 开机
		timeString = FileUtil.read("openTime");
		if (!timeString.equals("")) {
			hour = Integer.valueOf(timeString.substring(0, 2));
			minute = Integer.valueOf(timeString.substring(3, 5));
			timeString = AlarmUtil.getTime(hour, minute);
			AlarmUtil.setAlarm(context, timeString, 2, true);
		}

		// 关机2
		timeString = FileUtil.read("closeTimeSec");
		if (!timeString.equals("")) {
			hour = Integer.valueOf(timeString.substring(0, 2));
			minute = Integer.valueOf(timeString.substring(3, 5));
			timeString = AlarmUtil.getTime(hour, minute);
			AlarmUtil.setAlarm(context, timeString, 3, true);
		}
		// 开机2
		timeString = FileUtil.read("openTimeSec");
		if (!timeString.equals("")) {
			hour = Integer.valueOf(timeString.substring(0, 2));
			minute = Integer.valueOf(timeString.substring(3, 5));
			timeString = AlarmUtil.getTime(hour, minute);
			AlarmUtil.setAlarm(context, timeString, 4, true);
		}

		ServiceStatus.getInstance().setRunning(true);
	}
}