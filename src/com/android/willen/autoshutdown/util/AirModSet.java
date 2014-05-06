package com.android.willen.autoshutdown.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

public class AirModSet {
	public static void open(Context context) {
		/*Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", true);
        context.sendBroadcast(intent);*/
		setAirplaneMode(context,true);
        Log.e("AirModSet", "AirModOpen!!");
	}
	public static void close(Context context) {
		 /*Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
         Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
         intent.putExtra("state", false);
         context.sendBroadcast(intent);*/
		 setAirplaneMode(context,false);
         Log.e("AirModSet", "AirModClose!!");
	}
	
	static void setAirplaneMode(Context context, boolean mode) {
		if (isSDK17())
			AirplaneMode_SDK17.setAirplaneMode(context, mode);
		else
			AirplaneMode_SDK8.setAirplaneMode(context, mode);
	}
	
	public static boolean isSDK17()
    {
            return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
	
	public static void setAirplaneMode(final Context context,int time) {
		open(context);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				close(context);
			}
		}, time * 1000);

	}
}
