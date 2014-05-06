package com.android.willen.autoshutdown;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.willen.autoshutdown.server.ServiceStatus;
import com.android.willen.autoshutdown.util.AirModSet;
import com.android.willen.autoshutdown.util.AirplaneMode_SDK17;
import com.android.willen.autoshutdown.util.AlarmUtil;
import com.android.willen.autoshutdown.util.FileUtil;
import com.android.willen.autoshutdown.util.ProcessHelper;

public class MainActivity extends Activity implements OnClickListener {
	private RelativeLayout relativeLayout1 = null;
	private RelativeLayout relativeLayout2 = null;
	private RelativeLayout relativeLayout3 = null;
	private RelativeLayout relativeLayout4 = null;
	private RelativeLayout relativeLayout5 = null;
	private RelativeLayout relativeLayout8 = null;
	private static final String TAG = "MainActivity";
	private ImageView fuctionImageView;
	private TextView openTime;
	private TextView closeTime;
	private Activity context;
	private String timeString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		relativeLayout1 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout2);
		relativeLayout2 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout3);
		relativeLayout3 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout4);
		relativeLayout4 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout5);
		relativeLayout5 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout6);
		relativeLayout8 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout8);
		fuctionImageView = (ImageView) findViewById(R.id.functionImage);
		openTime = (TextView) findViewById(R.id.OpenTime);
		closeTime = (TextView) findViewById(R.id.CloseTime);

		relativeLayout1.setOnClickListener(this);
		relativeLayout2.setOnClickListener(this);
		relativeLayout3.setOnClickListener(this);
		relativeLayout4.setOnClickListener(this);
		relativeLayout5.setOnClickListener(this);
		relativeLayout8.setOnClickListener(this);
		fuctionImageView.setOnClickListener(this);
		initContent();
	}

	private void initContent() {
		String tempString = FileUtil.read("fuctionOpen");
		if (tempString.equals("open")) {
			fuctionImageView.setImageResource(R.drawable.icon_switch_on);
		} else {
			fuctionImageView.setImageResource(R.drawable.icon_switch_off);
		}

		tempString = FileUtil.read("closeTime");
		if (!tempString.equals("")) {
			closeTime.setText(tempString);
			AlarmUtil.setAlarm(context, tempString, 1, true);
		} else {
			FileUtil.write("closeTime", "00:00");
			AlarmUtil.setAlarm(context, "00:00", 1, true);
		}

		tempString = FileUtil.read("openTime");
		if (!tempString.equals("")) {
			openTime.setText(tempString);
			AlarmUtil.setAlarm(context, tempString, 2, true);
		} else {
			FileUtil.write("openTime", "07:00");
			AlarmUtil.setAlarm(context, "07:00", 2, true);
		}
	}

	public void onClick(View v) {
		if (v == relativeLayout1) {
			String tempString = FileUtil.read("fuctionOpen");
			if (!tempString.equals("open")) {
				Toast.makeText(context, "定时开关机功能未开启。", Toast.LENGTH_SHORT).show();
				return;
			}
			timeString = FileUtil.read("closeTime");
			int hour = 0;
			int minute = 0;
			if (!timeString.equals("")) {
				hour = Integer.valueOf(timeString.substring(0, 2));
				minute = Integer.valueOf(timeString.substring(3, 5));
			}
			new TimePickerDialog(MainActivity.this, new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minuteOfHour) {
					timeString = AlarmUtil.getTime(hourOfDay, minuteOfHour);
					FileUtil.write("closeTime", timeString);
					closeTime.setText(timeString);
					Toast.makeText(context, "已设置" + timeString + "定时关机。", Toast.LENGTH_SHORT)
							.show();
					AlarmUtil.setAlarm(context, timeString, 1, true);
					ServiceStatus.getInstance().setRunning(true);
				}
			}, hour, minute, true).show();
		} else if (v == relativeLayout2) {
			String tempString = FileUtil.read("fuctionOpen");
			if (!tempString.equals("open")) {
				Toast.makeText(context, "定时开关机功能未开启。", Toast.LENGTH_SHORT).show();
				return;
			}
			timeString = FileUtil.read("openTime");
			int hour = 7;
			int minute = 0;
			if (!timeString.equals("")) {
				hour = Integer.valueOf(timeString.substring(0, 2));
				minute = Integer.valueOf(timeString.substring(3, 5));
			}
			new TimePickerDialog(MainActivity.this, new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minuteOfHour) {
					timeString = AlarmUtil.getTime(hourOfDay, minuteOfHour);
					FileUtil.write("openTime", timeString);
					openTime.setText(timeString);
					Toast.makeText(context, "已设置" + timeString + "定时开机。", Toast.LENGTH_SHORT)
							.show();
					AlarmUtil.setAlarm(context, timeString, 2, true);
					ServiceStatus.getInstance().setRunning(true);
				}
			}, hour, minute, true).show();

		} else if (v == relativeLayout3) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(intent);
		} else if (v == relativeLayout4) {
			showNumberPicker("0", 0);
		} else if (v == fuctionImageView) {
			String functionOpen = FileUtil.read("fuctionOpen");
			if (functionOpen.equals("open")) {
				fuctionImageView.setImageResource(R.drawable.icon_switch_off);
				FileUtil.write("fuctionOpen", "close");
				Toast.makeText(context, "定时开关机功能关闭。", Toast.LENGTH_SHORT).show();
			} else {
				fuctionImageView.setImageResource(R.drawable.icon_switch_on);
				FileUtil.write("fuctionOpen", "open");
				Toast.makeText(context, "定时开关机功能开启。", Toast.LENGTH_SHORT).show();
			}
		} else if (v == relativeLayout5) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SecondTimeActivity.class);
			startActivity(intent);
		}else if (v == relativeLayout8) {
			if (!AirModSet.isSDK17()) {
				Toast.makeText(context, "非Android 4.2以上手机无须Root即可正常使用此应用，谢谢！", Toast.LENGTH_LONG).show();
			}else {
				systemAppMoverDialog();
			}			
		}
	}
	private void showNumberPicker(String number, int mode) {
		String nowNumber = "0";
		if (!number.equals("")) {
			nowNumber = number;
		}
		new NumberPickerDialog(this, listener, nowNumber, mode).show();
	}

	private NumberPickerDialog.OnMyNumberSetListener listener = new NumberPickerDialog.OnMyNumberSetListener() {
		@Override
		public void onNumberSet(String number, int mode) {
			if (number.equals("") || number.equals("0")) {
				Toast.makeText(context, "时间不能为0。", 0).show();
				return;
			}
			int time = Integer.valueOf(number);
			if (time > (24 * 60)) {
				Toast.makeText(context, "设置时长不能超过一天。", 0).show();
				return;
			}
			int hour, minute, second;
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(now.getTimeInMillis() + time * 60 * 1000);

			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			second = c.get(Calendar.SECOND);

			timeString = AlarmUtil.getTime(hour, minute);
			FileUtil.write("restTime", timeString);
			Toast.makeText(
					context,
					"飞行模式开启。将在" + hour + "点" + minute + "分" + second
							+ "秒结束飞行模式。", 1).show();
			AlarmUtil.setAlarm(context, c.getTimeInMillis(), 3);
			ServiceStatus.getInstance().setRunning(true);
			AirModSet.open(context);
		}
	};

	private void systemAppMoverDialog() {
		if (!AirplaneMode_SDK17.isSystemApp(this)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Add the buttons
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});

			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Pair<Integer, String> ret = moveDataSystemApp(
									"com.android.willen.autoshutdown-*.apk",
									"/data/app", "/system/app", "root", false);
							if (0 != ret.first) {
								errorDialog(ret.second);
							}
						}
					});

			builder.setMessage(R.string.system_app_move_message);
			builder.setTitle(R.string.system_app_move_title);

			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();

		} else {
			final boolean updatedSystemApp = AirplaneMode_SDK17
					.isUpdatedSystemApp(this);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Add the buttons
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});

			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							Pair<Integer, String> ret = moveDataSystemApp(
									"com.android.willen.autoshutdown-*.apk",
									"/system/app", "/data/app", "system",
									updatedSystemApp);
							if (0 != ret.first) {
								errorDialog(ret.second);
							}
						}
					});

			builder.setMessage(R.string.system_app_remove_message);
			builder.setTitle(R.string.system_app_remove_title);

			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	private void errorDialog(final String errorMessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

		builder.setMessage("Error:\n" + errorMessage);
		builder.setTitle(R.string.system_app_move_title);

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	static boolean isSuperUser() {
		boolean isSuperUser = false;
		Pair<Integer, String> ret;

		// check for /system/xbin/su
		ret = ProcessHelper.runCmd(false, "sh", "-c", "[ -f /system/xbin/su ]");
		isSuperUser = (0 == ret.first);
		if (!isSuperUser) {
			// check for /system/bin/su
			ret = ProcessHelper.runCmd(false, "sh", "-c",
					"[ -f /system/bin/su ]");
			isSuperUser = (0 == ret.first);
		}
		Log.d(TAG, "isSuperUser = " + isSuperUser);
		return isSuperUser;
	}

	static private Pair<Integer, String> moveDataSystemApp(String app,
			String from, String to, String owner, boolean updatedSystemApp) {
		int exitCode = -1;
		StringBuilder sb = new StringBuilder();
		Pair<Integer, String> ret;

		// check su command by running ls on from and to directory
		ret = ProcessHelper.runCmd(true, "su", "-c", "ls " + from);
		sb.append(ret.second);
		if (0 != (exitCode = ret.first))
			return new Pair<Integer, String>(exitCode, sb.toString());
		ret = ProcessHelper.runCmd(true, "su", "-c", "ls " + to);
		sb.append(ret.second);
		if (0 != (exitCode = ret.first))
			return new Pair<Integer, String>(exitCode, sb.toString());

		// make /system/app writable
		ret = ProcessHelper.runCmd(true, "su", "-c",
				"mount -oremount,rw /system");
		sb.append(ret.second);
		if (0 != (exitCode = ret.first)) {
			// try another mount command line
			ret = ProcessHelper.runCmd(true, "su", "-c",
					"mount -oremount,rw APP /system");
			sb.append(ret.second);
			if (0 != (exitCode = ret.first))
				return new Pair<Integer, String>(exitCode, sb.toString());
		}

		if (!updatedSystemApp) {
			// move MiniStatus to /to/app
			ret = ProcessHelper.runCmd(true, "su", "-c", "cp " + from + "/"
					+ app + " " + to);
			sb.append(ret.second);
			if (0 != (exitCode = ret.first))
				return new Pair<Integer, String>(exitCode, sb.toString());

			// set permissions
			ret = ProcessHelper.runCmd(true, "su", "-c", "chmod 644 " + to
					+ "/" + app);
			sb.append(ret.second);
			if (0 != (exitCode = ret.first))
				return new Pair<Integer, String>(exitCode, sb.toString());

			// set owner
			ret = ProcessHelper.runCmd(true, "su", "-c", "chown " + owner + ":"
					+ owner + " " + to + "/" + app);
			sb.append(ret.second);
			if (0 != (exitCode = ret.first))
				return new Pair<Integer, String>(exitCode, sb.toString());
		}

		// remove MiniStatus from /from/app
		ret = ProcessHelper.runCmd(true, "su", "-c", "rm " + from + "/" + app);
		sb.append(ret.second);
		if (0 != (exitCode = ret.first))
			return new Pair<Integer, String>(exitCode, sb.toString());

		// reboot
		ret = ProcessHelper.runCmd(true, "su", "-c", "reboot");
		sb.append(ret.second);
		if (0 != (exitCode = ret.first))
			return new Pair<Integer, String>(exitCode, sb.toString());

		return new Pair<Integer, String>(exitCode, sb.toString());
	}
}
