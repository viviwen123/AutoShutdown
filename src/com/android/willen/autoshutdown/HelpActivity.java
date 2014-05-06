package com.android.willen.autoshutdown;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity {
//	private LinearLayout linearLayout = null;
	private static final String TAG = "HelpActivity";
	private WebView mWebView;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ebupt.advancedcallmanager.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		mWebView = (WebView) findViewById(R.id.myWebView);
		mWebView.loadUrl("file:///android_asset/help.htm");
	}
}
