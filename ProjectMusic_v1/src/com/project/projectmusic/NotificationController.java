package com.project.projectmusic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NotificationController extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String value = (String) getIntent().getExtras().get("value");
		System.out.println(value);
		Log.e("",value);
	}
}
