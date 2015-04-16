package com.project.projectmusic;
import java.util.ArrayList;
import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MusicListShow extends Activity implements MusicManager.ManagerMusicListener, SeekBar.OnSeekBarChangeListener {
	TextView title;
	ListView listView;
	MusicManager managerMusic;
	private ImageView btnPlay;
	private ImageView btnNext;
	private ImageView btnPrevious;
	private TextView songTitleLabel;
	private SeekBar songProgressBar;
	private Utilities utils;
	private Handler handler;
	AdapterListShow listSongAdapter;
	@Override
	protected void onResume() {
		super.onResume();
		listSongAdapter.notifyDataSetChanged();
		listView.setAdapter(listSongAdapter);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_list_show);
		title = (TextView) findViewById(R.id.content_title);
		listView = (ListView) findViewById(R.id.content_list);
		managerMusic = new MusicManager(this);
		managerMusic.setActivity(this);
		View viewInclude = findViewById(R.id.content_layout_playmusic);
		btnPlay =(ImageView) viewInclude.findViewById(R.id.btnPlay);
		btnNext = (ImageView) viewInclude.findViewById(R.id.btnNext);
		btnPrevious = (ImageView) viewInclude.findViewById(R.id.btnPrevious);
		songTitleLabel = (TextView) viewInclude.findViewById(R.id.songTitle);
		songProgressBar = (SeekBar) viewInclude.findViewById(R.id.songProgressBar);
		songProgressBar.setProgress(0);
		songProgressBar.setMax(100);
		utils = new Utilities();
		handler = new Handler();
		checkMusicPlay();
		getActionBar().setDisplayHomeAsUpEnabled(true);//kich hoat actionbar home
		try {
			// get Intent extras
			Bundle bundle = getIntent().getExtras();
			String name = bundle.getString("name");
			String value = bundle.getString("value");
			long id = bundle.getLong("id");
			title.setText(name + " " + value);
			// handle data listview
			ArrayList<String> arrayListSong;
			Cursor cursor = managerMusic.getListMusic(name, value, id);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				cursor.moveToFirst();
				arrayListSong = new ArrayList<String>();
				while (!cursor.isAfterLast()) {
					try {	
						if(name.equalsIgnoreCase("Playlist"))
						{
							//Log.e("","this is Playlist");
							long idSong = cursor.getLong(0);
							String titleSong = managerMusic.getSong(idSong);
							if(titleSong != null)
							{
								arrayListSong.add(titleSong);
								//Log.e("",String.valueOf(idSong));
							}
						}
						else 
							arrayListSong.add(cursor.getString(0).toString());
						
						cursor.moveToNext();
					} catch (Exception e) {
						Log.e("", "Content.java: error read cursor");
					}
				}
				listSongAdapter = new AdapterListShow(
						getApplicationContext(), MusicListShow.this,
						R.layout.list_item, arrayListSong);
				listView.setAdapter(listSongAdapter);
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("", "info Content: not getIntent().getExtras//handle data listview");
		}
		//
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(managerMusic.playButton())
				{
					btnPlay.setImageResource(R.drawable.btn_play);
					Main.btnPlay.setImageResource(R.drawable.btn_play);
				}
				else
				{
					btnPlay.setImageResource(R.drawable.btn_pause);
					Main.btnPlay.setImageResource(R.drawable.btn_pause);
				}
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerMusic.nextButton();
			}
		});
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				managerMusic.previousButton();
			}
		});
		songTitleLabel.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(managerMusic.getIsPlaying())
				{
				Intent in = new Intent(getApplicationContext(), MusicPlayer.class);
				startActivity(in);
				}
			}
		});
	}
	
	public void buttonDong(View v)
	{
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		int id = item.getItemId();
		if (id == R.id.action_back) {	
			finish();
			return true;
		}else if (id == android.R.id.home)
		{
			onBackPressed();
			return true;
		}
		else return super.onOptionsItemSelected(item);
	}

	@Override
	public void playMusic() {
		try {
			songProgressBar.setVisibility(SeekBar.VISIBLE);
			Main.songProgressBar.setVisibility(SeekBar.VISIBLE);
			String titleMusic = managerMusic.getSongTitle();
			if(titleMusic != null)
				{songTitleLabel.setText(titleMusic);
				Main.songTitleLabel.setText(titleMusic);}
			else songTitleLabel.setText("Not title");							
			Main.btnPlay.setImageResource(R.drawable.btn_pause);	
			btnPlay.setImageResource(R.drawable.btn_pause);
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			updateProgressBar();
		} catch (Exception e) {
			Log.e("","error in Main.playMusic");
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
		int totalDuration = managerMusic.getMediaPlayer().getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);		
		managerMusic.getMediaPlayer().seekTo(currentPosition);
		updateProgressBar();	
	}
	
	public void updateProgressBar() {
		//songTitleLabel.setText(managerMusic.songTitlePlaying);
		//Main.songTitleLabel.setText(managerMusic.songTitlePlaying);
        handler.postDelayed(mUpdateTimeTask, 100);        
    }		
	// Background Runnable thread
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   long totalDuration = managerMusic.getMediaPlayer().getDuration();
			   long currentDuration = managerMusic.getMediaPlayer().getCurrentPosition();			  			   
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   if (managerMusic.getCount() >= (managerMusic.getArrayPlay().size() - 1)
						|| managerMusic.getCount() >= (managerMusic.getArraySelect().size() - 1)) {
					managerMusic.repeatButton();
				} else {
					managerMusic.nextButton();
				}
			   String titleMusic = managerMusic.getSongTitle();
			   songTitleLabel.setText(titleMusic);
			   Main.songTitleLabel.setText(titleMusic);
			   songProgressBar.setProgress(progress);		
		       handler.postDelayed(this, 100);	    
		   }
		};
	public void checkMusicPlay()
	{
		if(managerMusic.getMediaPlayer().isPlaying() || managerMusic.getIsPlaying())
		{
			songProgressBar.setVisibility(SeekBar.VISIBLE);
			Main.songProgressBar.setVisibility(SeekBar.VISIBLE);
			updateProgressBar();
			btnPlay.setImageResource(R.drawable.btn_play);
			//songTitleLabel.setText(managerMusic.songTitlePlaying);
		}
	}
}
