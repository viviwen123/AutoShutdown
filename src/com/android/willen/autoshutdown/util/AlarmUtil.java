package com.android.willen.autoshutdown.util;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.willen.autoshutdown.server.AlarmReceiver;

public class AlarmUtil {
	public static void setAlarm(Context context, String time, int requestCode,
			boolean repeat) {
		Calendar c = Calendar.getInstance();
		int hour = Integer.valueOf(time.substring(0, 2));
		int minute = Integer.valueOf(time.substring(3, 5));
		Calendar now = Calendar.getInstance();

		c.set(Calendar.YEAR, now.get(Calendar.YEAR));
		c.set(Calendar.MONTH, now.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);

		Intent intent1 = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent1, requestCode);
		AlarmManager am = (AlarmManager) context.getSystemService("alarm");
		if (repeat) {
			long repeatingTime = 86400 * 1000;
			am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
					repeatingTime, pi);
		} else {
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		}
		Log.i("setAlarm", time);
	}

	public static void setAlarm(Context context, long time, int requestCode) {
		Intent intent1 = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent1, requestCode);
		AlarmManager am = (AlarmManager) context.getSystemService("alarm");
		am.set(AlarmManager.RTC_WAKEUP, time, pi);

		Log.i("setAlarm", time + "");
	}

	public static String getTime(int hour, int minute) {
		String t1 = (hour > 9 ? ("" + hour) : ("0" + hour));
		String t2 = (minute > 9 ? ("" + minute) : ("0" + minute));
		return (t1 + ":" + t2);
	}
}