package com.project.projectmusic;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MusicService extends IntentService {
	
	public MusicService() {
		super("Service Music!");
	}

	private ViewGroup mView;
	private LayoutInflater inflater;

	@Override
	protected void onHandleIntent(Intent intent) {		
		/*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
		           WindowManager.LayoutParams.WRAP_CONTENT,
		           WindowManager.LayoutParams.WRAP_CONTENT,
		           WindowManager.LayoutParams.TYPE_PHONE,
		           WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
		                   | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
		                   | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		           PixelFormat.JPEG
		   );
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView =  (ViewGroup) inflater.inflate(R.layout.player_bottom, null);
		wm.addView(mView,params);*/
	}
}
