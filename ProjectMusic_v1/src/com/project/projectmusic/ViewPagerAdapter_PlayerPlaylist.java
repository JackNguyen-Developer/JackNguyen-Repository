package com.project.projectmusic;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerAdapter_PlayerPlaylist extends PagerAdapter implements /*MusicManager.ManagerMusicListener,*/ SeekBar.OnSeekBarChangeListener {

	private ArrayList<View> pageViews;
	private Context context;
	private Activity act;
	//component player
	private ImageView btnNext;
	private ImageView btnPrevious;
	private ImageView btnPlay;
	private ImageView btnForward;
	private ImageView btnBackward;
	private ImageView imageAlbum;
	private ImageView btnRepeat;
	private ImageView btnShuffle;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private TextView songTitle;
	private Handler handler;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 ms
	private int seekBackwardTime = 5000; // 5000 ms
	MusicManager managerMusic;
	//component playlist
	private ListView listView, listView_Pick;
	private ListSongAdapter adapterListView;
	public ViewPagerAdapter_PlayerPlaylist(ArrayList<View> pageViews, Context context, Activity act) {
		super();
		this.pageViews = pageViews;
		this.context = context;
		this.act = act;
	}

	@Override
	public Object instantiateItem(View v, int position) {
		switch (position) {
		case 0:
		{
			btnNext = (ImageView) pageViews.get(position).findViewById(R.id.btnNext);
			btnPrevious = (ImageView) pageViews.get(position).findViewById(R.id.btnPrevious);
			btnPlay = (ImageView) pageViews.get(position).findViewById(R.id.btnPlay);
			btnForward = (ImageView) pageViews.get(position).findViewById(R.id.btnForward);
			btnBackward = (ImageView) pageViews.get(position).findViewById(R.id.btnBackward);
			btnRepeat = (ImageView) pageViews.get(position).findViewById(R.id.btnRepeat);
			btnShuffle = (ImageView) pageViews.get(position).findViewById(R.id.btnShuffle);
			imageAlbum = (ImageView) pageViews.get(position).findViewById(R.id.imageAlbum);
			songCurrentDurationLabel = (TextView) pageViews.get(position).findViewById(R.id.songCurrentDurationLabel);
			songTotalDurationLabel = (TextView) pageViews.get(position).findViewById(R.id.songTotalDurationLabel);
			songTitle = (TextView) pageViews.get(position).findViewById(R.id.songTitle);
			songProgressBar = (SeekBar) pageViews.get(position).findViewById(R.id.songProgressBar);
			managerMusic = new MusicManager(context);
			managerMusic.setActivity(act);		
			handler = new Handler();
			utils = new Utilities();
			handlePlayer();
			checkMusicPlay();
			break;
		}
		case 1:
		{
			listView = (ListView) pageViews.get(position).findViewById(R.id.playlist_common);
			listView_Pick = (ListView) pageViews.get(position).findViewById(R.id.playlist_pick);
			adapterListView = new ListSongAdapter(context, act, R.layout.playlist, managerMusic.arrayPlay);
			listView.setAdapter(adapterListView);
			adapterListView = new ListSongAdapter(context, act, R.layout.playlist, managerMusic.arrayPick);
			listView_Pick.setAdapter(adapterListView);
			break;
		}
		default:
			break;
		}
		((ViewPager) v).addView(pageViews.get(position));
		return pageViews.get(position);
	}

	@Override
	public void destroyItem(View v, int position, Object arg2) {
		((ViewPager) v).removeView(pageViews.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object arg1) {
		return v == arg1;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
	
	public void playMusic() {
		try {
			if(managerMusic.songTitlePlaying != null)
				songTitle.setText(managerMusic.songTitlePlaying);
			else songTitle.setText("___");
			btnPlay.setImageResource(R.drawable.btn_pause);	
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			updateProgressBar();
		} catch (Exception e) {
			Log.e("","error in ViewPagerAdapter_Play....playMusic");
		}
	}
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {	
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		handler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		handler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = managerMusic.mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);		
		managerMusic.mediaPlayer.seekTo(currentPosition);
		updateProgressBar();
	}
	
	public void updateProgressBar() {
		songTitle.setText(managerMusic.songTitlePlaying);
		Main.songTitleLabel.setText(managerMusic.songTitlePlaying);
        handler.postDelayed(mUpdateTimeTask, 100);
        
    }		
	// Background Runnable thread
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   long totalDuration = managerMusic.mediaPlayer.getDuration();
			   long currentDuration = managerMusic.mediaPlayer.getCurrentPosition();			  			   
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   if(progress >= 99 && managerMusic.count >= (managerMusic.arrayPlay.size()-1))
			   {
				   managerMusic.repeatMusic();
			   }
			   updateImageAlbum();
			   songProgressBar.setProgress(progress);		
			   handler.postDelayed(this, 100);	      
		   }
		};

	private void checkMusicPlay() {
		if (managerMusic.mediaPlayer.isPlaying()
				|| managerMusic.isPlaying) {
	
			btnPlay.setImageResource(R.drawable.btn_pause);
			songTitle.setText(managerMusic.songTitlePlaying);
			updateProgressBar();
		}
	}
	private void updateImageAlbum()
	{
		if (managerMusic.albumArt == null) {
			Drawable draw = context.getResources().getDrawable(R.drawable.adele);
			imageAlbum.setImageDrawable(draw);
		} else {
			File file = new File(managerMusic.albumArt);
			if (file.exists()) {
				Bitmap bitImage = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				imageAlbum.setImageBitmap(bitImage);
			}
		}
	}
	private void handlePlayer()
	{
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(managerMusic.play())
				{
					btnPlay.setImageResource(R.drawable.btn_play);
				}
				else
				{
					btnPlay.setImageResource(R.drawable.btn_pause);
				}		
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerMusic.nextSong();				
			}
		});
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerMusic.previousSong();		
			}
		});
		btnForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	
				int currentPosition = managerMusic.mediaPlayer.getCurrentPosition();			
				if(currentPosition + seekForwardTime <= managerMusic.mediaPlayer.getDuration()){		
					managerMusic.mediaPlayer.seekTo(currentPosition + seekForwardTime);
				}else{
					managerMusic.mediaPlayer.seekTo(managerMusic.mediaPlayer.getDuration());
				}
			}
		});
		btnBackward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {		
				int currentPosition = managerMusic.mediaPlayer.getCurrentPosition();			
				if(currentPosition - seekBackwardTime >= 0){		
					managerMusic.mediaPlayer.seekTo(currentPosition - seekBackwardTime);
				}else{			
					managerMusic.mediaPlayer.seekTo(0);
				}
				
			}
		});
		btnRepeat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
						
			}
		});
		btnShuffle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerMusic.shuffleMusic();
				adapterListView.notifyDataSetChanged();
				listView.setAdapter(adapterListView);
			}
		});
	}
}