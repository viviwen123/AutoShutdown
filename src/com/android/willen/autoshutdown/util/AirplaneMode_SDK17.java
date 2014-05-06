package com.android.willen.autoshutdown.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.util.Log;
import android.widget.Toast;

@TargetApi(17)
public class AirplaneMode_SDK17 {
	private static final String TAG = AirplaneMode_SDK17.class.getSimpleName();

	public static boolean isSystemApp(Context context) {
		ApplicationInfo ai = context.getApplicationInfo();
		// Log.d(TAG, ai.publicSourceDir);
		if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
			Log.d(TAG, "isSystemApp == true");
			return true;
		}
		return false;
	}

	public static boolean isUpdatedSystemApp(Context context) {
		ApplicationInfo ai = context.getApplicationInfo();
		// Log.d(TAG, ai.publicSourceDir);
		if ((ai.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			Log.d(TAG, "isUpdatedSystemApp == true");
			return true;
		}
		return false;
	}

	static boolean isAdminUser(Context context) {
		UserHandle uh = Process.myUserHandle();
		UserManager um = (UserManager) context
				.getSystemService(Context.USER_SERVICE);
		if (null != um) {
			long userSerialNumber = um.getSerialNumberForUser(uh);
			Log.d(TAG, "userSerialNumber = " + userSerialNumber);
			return 0 == userSerialNumber;
		} else
			return false;
	}

	static boolean getAirplaneMode(Context context) {
		// Log.d(TAG, "getAirplaneMode");
		return Settings.Global.getInt(context.getContentResolver(),
				Global.AIRPLANE_MODE_ON, 0) != 0;
	}

	static void setAirplaneMode(Context context, boolean mode) {
		Log.d(TAG, "setAirplaneMode");
		if (mode != getAirplaneMode(context)) {
			// it is only possible to set AIRPLANE_MODE programmatically for
			// Android >= 4.2.x if app runs
			// - as system app, e.g. APK is located in /system/app
			// - and if current user is the admin user (not sure about that ...)
			if (isSystemApp(context) && isAdminUser(context)) {
				Settings.Global.putInt(context.getContentResolver(),
						Global.AIRPLANE_MODE_ON, mode ? 1 : 0);
				Intent newIntent = new Intent(
						Intent.ACTION_AIRPLANE_MODE_CHANGED);
				newIntent.putExtra("state", getAirplaneMode(context));
				context.sendBroadcast(newIntent);
			} else {
				Toast.makeText(context, "Android 4.2以上手机需要将应用升级为系统应用方能使用此功能。", Toast.LENGTH_LONG).show();
				// for "normal" apps it is only possible to open the system
				// settings dialog
				Intent i = new Intent(
						android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}
}