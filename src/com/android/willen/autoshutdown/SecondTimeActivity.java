package com.android.willen.autoshutdown;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.willen.autoshutdown.server.ServiceStatus;
import com.android.willen.autoshutdown.util.AlarmUtil;
import com.android.willen.autoshutdown.util.FileUtil;

public class SecondTimeActivity extends Activity implements OnClickListener {
	private RelativeLayout relativeLayout1 = null;
	private RelativeLayout relativeLayout2 = null;
	private static final String TAG = "SecondTimeActivity";
	private ImageView fuctionImageView;
	private TextView openTimeSec;
	private TextView closeTimeSec;
	private Activity context;
	private String timeString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		context = this;
		relativeLayout1 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout2);
		relativeLayout2 = (RelativeLayout) this
				.findViewById(R.id.RelativeLayout3);
		fuctionImageView = (ImageView) findViewById(R.id.functionImage);
		openTimeSec = (TextView) findViewById(R.id.OpenTime);
		closeTimeSec = (TextView) findViewById(R.id.CloseTime);

		relativeLayout1.setOnClickListener(this);
		relativeLayout2.setOnClickListener(this);
		fuctionImageView.setOnClickListener(this);
		initContent();
	}

	private void initContent() {
		String tempString = FileUtil.read("fuctionOpenSec");
		if (tempString.equals("open")) {
			fuctionImageView.setImageResource(R.drawable.icon_switch_on);
		} else {
			fuctionImageView.setImageResource(R.drawable.icon_switch_off);
		}

		tempString = FileUtil.read("closeTimeSec");
		if (!tempString.equals("")) {
			closeTimeSec.setText(tempString);
			AlarmUtil.setAlarm(context, tempString, 3,true);
		} else {
			FileUtil.write("closeTimeSec", "00:00");
			AlarmUtil.setAlarm(context, "00:00", 3,true);
		}

		tempString = FileUtil.read("openTimeSec");
		if (!tempString.equals("")) {
			openTimeSec.setText(tempString);
			AlarmUtil.setAlarm(context, tempString, 4,true);
		} else {
			FileUtil.write("openTimeSec", "07:00");
			AlarmUtil.setAlarm(context, "07:00", 4,true);
		}
	}
	
	public void onClick(View v) {
		if (v == relativeLayout1) {
			String tempString = FileUtil.read("fuctionOpenSec");
			if (!tempString.equals("open")) {
				Toast.makeText(context, "定时开关机功能未开启。", 0).show();
				return;
			}
			timeString = FileUtil.read("closeTimeSec");
			int hour = 0;
			int minute = 0;
			if (!timeString.equals("")) {
				hour = Integer.valueOf(timeString.substring(0, 2));
				minute = Integer.valueOf(timeString.substring(3, 5));
			}
			new TimePickerDialog(SecondTimeActivity.this, new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minuteOfHour) {
					timeString = AlarmUtil.getTime(hourOfDay, minuteOfHour);
					FileUtil.write("closeTimeSec", timeString);
					closeTimeSec.setText(timeString);
					Toast.makeText(context, "已设置" + timeString + "定时关机。", 0)
							.show();
					AlarmUtil.setAlarm(context, timeString, 3,true);
					ServiceStatus.getInstance().setRunning(true);
				}
			}, hour, minute, true).show();
		} else if (v == relativeLayout2) {
			String tempString = FileUtil.read("fuctionOpenSec");
			if (!tempString.equals("open")) {
				Toast.makeText(context, "定时开关机功能未开启。", 0).show();
				return;
			}
			timeString = FileUtil.read("openTimeSec");
			int hour = 7;
			int minute = 0;
			if (!timeString.equals("")) {
				hour = Integer.valueOf(timeString.substring(0, 2));
				minute = Integer.valueOf(timeString.substring(3, 5));
			}
			new TimePickerDialog(SecondTimeActivity.this, new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay,
						int minuteOfHour) {
					timeString = AlarmUtil.getTime(hourOfDay, minuteOfHour);
					FileUtil.write("openTimeSec", timeString);
					openTimeSec.setText(timeString);
					Toast.makeText(context, "已设置" + timeString + "定时开机。", 0)
							.show();
					AlarmUtil.setAlarm(context, timeString, 4,true);
					ServiceStatus.getInstance().setRunning(true);
				}
			}, hour, minute, true).show();

		}else if (v == fuctionImageView) {
			String functionOpen = FileUtil.read("fuctionOpenSec");
			if (functionOpen.equals("open")) {
				fuctionImageView.setImageResource(R.drawable.icon_switch_off);
				FileUtil.write("fuctionOpenSec", "close");
				Toast.makeText(context, "定时开关机功能关闭。", 0).show();
			} else {
				fuctionImageView.setImageResource(R.drawable.icon_switch_on);
				FileUtil.write("fuctionOpenSec", "open");
				Toast.makeText(context, "定时开关机功能开启。", 0).show();
			}
		}

	}
}
