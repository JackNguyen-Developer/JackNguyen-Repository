package com.project.projectmusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

public class MusicNotification extends Notification {
	private Context context;
	private NotificationManager notificationManager;
	private MusicManager musicManager;
	private RemoteViews notificationView;
	private Notification notification;
	public MusicNotification(Context context) {
		this.context = context;
		musicManager = new MusicManager(context);
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence titleSticker = "JN Music";
		Notification.Builder builder = new Notification.Builder(context);
		notification = builder.getNotification();
		notification.tickerText = titleSticker;
		notification.icon = R.drawable.ic_launcher;
		//notification.when = System.currentTimeMillis();
		notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
		
		Intent intentLayout = new Intent(context, Main.class);
		intentLayout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentLayout.putExtra("value", "layout");
		PendingIntent pendingLayout = PendingIntent.getActivity(context, 0, intentLayout, 0);
		//PendingIntent pendingLayout = PendingIntent.getActivity(context, 0, intentLayout, 0);
		notificationView.setOnClickPendingIntent(R.id.contain, pendingLayout);
		
		Intent intentPrevious = new Intent();
		intentPrevious.putExtra("value", "previous");
		intentPrevious.setAction("com.project.projectmusic.PREVIOUS");
		PendingIntent pendingPrevious = PendingIntent.getBroadcast(context, 0, intentPrevious, 0);
		notificationView.setOnClickPendingIntent(R.id.btnPrevious, pendingPrevious);
		
		Intent intentPlay = new Intent();
		intentPlay.putExtra("value", "play");
		intentPlay.setAction("com.project.projectmusic.PLAY");
		PendingIntent pendingPlay = PendingIntent.getBroadcast(context, 0, intentPlay, 0);
		notificationView.setOnClickPendingIntent(R.id.btnPlay, pendingPlay);
		Intent intentNext = new Intent();
		intentNext.putExtra("value", "next");
		intentNext.setAction("com.project.projectmusic.NEXT");
		PendingIntent pendingNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
		notificationView.setOnClickPendingIntent(R.id.btnNext, pendingNext);
		
		notification.contentView = notificationView;
		notificationManager.notify(2,notification);
	}
	
	public void play()
	{
		notificationView.setImageViewResource(R.id.btnPlay, R.drawable.btn_pause);
		notificationManager.notify(2, notification);
	}
	public void pause()
	{
		notificationView.setImageViewResource(R.id.btnPlay, R.drawable.btn_play);
		notificationManager.notify(2, notification);
	}
}
