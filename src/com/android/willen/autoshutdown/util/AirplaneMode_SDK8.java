package com.android.willen.autoshutdown.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirplaneMode_SDK8 {
	// private static final String TAG =
	// AirplaneMode_SDK8.class.getSimpleName();

	@SuppressWarnings("deprecation")
	static boolean getAirplaneMode(Context context) {
		// Log.d(TAG, "getAirplaneMode");
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	@SuppressWarnings("deprecation")
	static void setAirplaneMode(Context context, boolean mode) {
		// Log.d(TAG, "setAirplaneMode");
		if (mode != getAirplaneMode(context)) {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, mode ? 1 : 0);
			// documentation says:
			// "This is a protected intent that can only be sent by the system."
			// but works at least for Android 2.3.4 on Xperia Mini
			Intent newIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			newIntent.putExtra("state", mode);
			context.sendBroadcast(newIntent);
		}
	}

}