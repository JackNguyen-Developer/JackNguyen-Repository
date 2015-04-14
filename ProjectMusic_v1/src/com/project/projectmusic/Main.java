package com.project.projectmusic;

import java.util.ArrayList;

import android.R.anim;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;

public class Main extends FragmentActivity implements
		MusicManager.ManagerMusicListener, SeekBar.OnSeekBarChangeListener,
		ActionBar.TabListener {
	private DrawerLayout drawerLayout;
	private RelativeLayout drawerRelative;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	Cursor cur;
	ArrayList<ExpandListGroup> listGroup;
	ArrayList<ExpandListChild> listChild;
	private ListView listView;
	private ListSongAdapter listSongAdapter;
	private ArrayList<String> arrayListSong = new ArrayList<String>();
	private ViewPager viewPager;// ViewPager
	// private ViewPagerAdapter viewPagerAdapter;
	private ViewPagerAdapter_Common viewAdapter;
	private ArrayList<View> arrayView;
	private ActionBar actionBar;
	private String[] tabs = { "Song", "Album", "Artist", "Playlist", "Genre",
			"Type" };
	// component layout
	private ImageView btnNext;
	private ImageView btnPrevious;
	public static SeekBar songProgressBar;
	// handle
	MusicManager managerMusic;
	private Handler handler;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 ms
	private int seekBackwardTime = 5000; // 5000 ms
	private String songPath = null, songTitle = null;
	// component drawer
	private TextView fill_all, fill_album, fill_artist, fill_playlist,
			fill_genre, fill_type;
	// static
	// public static MediaPlayer mediaPlayer;
	public static ImageView btnPlay;
	public static TextView songTitleLabel;
	// public static ArrayList<Song> arrayPlaying, arrayPlayingDefault;
	public static String songTitlePlaying;
	private MusicNotification musicNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//
		musicNotification = new MusicNotification(this);
		// component layout
		View viewInclude = findViewById(R.id.main_layout_playmusic);
		btnPlay = (ImageView) viewInclude.findViewById(R.id.btnPlay);
		btnNext = (ImageView) viewInclude.findViewById(R.id.btnNext);
		btnPrevious = (ImageView) viewInclude.findViewById(R.id.btnPrevious);
		songTitleLabel = (TextView) viewInclude.findViewById(R.id.songTitle);
		songProgressBar = (SeekBar) viewInclude
				.findViewById(R.id.songProgressBar);
		listView = (ListView) findViewById(R.id.listMusic);
		// component drawer
		fill_all = (TextView) findViewById(R.id.fill_all);
		fill_album = (TextView) findViewById(R.id.fill_album);
		fill_artist = (TextView) findViewById(R.id.fill_artist);
		fill_playlist = (TextView) findViewById(R.id.fill_playlist);
		fill_genre = (TextView) findViewById(R.id.fill_genre);
		fill_type = (TextView) findViewById(R.id.fill_type);
		// handle
		managerMusic = new MusicManager(this);
		managerMusic.setActivity(Main.this);
		handler = new Handler();
		utils = new Utilities();
		// handle ViewPager and ActionBar
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		arrayView = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		arrayView.add(inflater.inflate(R.layout.fragment, null));
		viewAdapter = new ViewPagerAdapter_Common(arrayView,
				getApplicationContext(), Main.this);
		// viewPager.setAdapter(viewPagerAdapter);
		viewPager.setAdapter(viewAdapter);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String tab : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab)
					.setTabListener(this));
		}
		actionBar.setSelectedNavigationItem(0);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		// hanlde DrawerLayout
		handleDrawerLayout();
		// event btnPlay
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * if(managerMusic.play()) {
				 * btnPlay.setImageResource(R.drawable.btn_play); } else {
				 * btnPlay.setImageResource(R.drawable.btn_pause); }
				 */
				checkMusicPlaying();
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
		songTitleLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (managerMusic.isPlaying) {
					Intent in = new Intent(getApplicationContext(),
							Player.class);
					startActivity(in);
				}
			}
		});
		// Item Drawer Event
		fill_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(0);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		fill_album.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(1);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		fill_artist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(2);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		fill_playlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(3);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		fill_genre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(4);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		fill_type.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionBar.setSelectedNavigationItem(5);
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
		// get value BroadcastReceive
		registerReceiver(br, new IntentFilter(
				"com.project.projectmusic.PREVIOUS"));
		registerReceiver(br, new IntentFilter("com.project.projectmusic.PLAY"));
		registerReceiver(br, new IntentFilter("com.project.projectmusic.NEXT"));
	}

	// check have playing music not or no
	private void checkMusicPlaying() {
		if (managerMusic.play()) {
			btnPlay.setImageResource(R.drawable.btn_play);
			musicNotification.pause();
		} else {
			btnPlay.setImageResource(R.drawable.btn_pause);
			musicNotification.play();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			// drawerLayout.openDrawer(expandableListView);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void handleDrawerLayout() {
		mTitle = mDrawerTitle = getTitle();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerRelative = (RelativeLayout) findViewById(R.id.drawer_left);
		drawerLayout.closeDrawer(drawerRelative);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.menu_item, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}

	@Override
	public void playMusic() {
		try {
			musicNotification.play();
			songProgressBar.setVisibility(SeekBar.VISIBLE);
			if (managerMusic.songTitlePlaying != null)
				songTitleLabel.setText(managerMusic.songTitlePlaying);
			else
				songTitleLabel.setText("___");
			btnPlay.setImageResource(R.drawable.btn_pause);
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			updateProgressBar();
		} catch (Exception e) {
			Log.e("", "error in Main.playMusic");
		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		handler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		handler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = managerMusic.mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);
		managerMusic.mediaPlayer.seekTo(currentPosition);
		updateProgressBar();
	}

	public void updateProgressBar() {
		songTitleLabel.setText(managerMusic.songTitlePlaying);
		handler.postDelayed(mUpdateTimeTask, 100);
	}

	// Background Runnable thread
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = managerMusic.mediaPlayer.getDuration();
			long currentDuration = managerMusic.mediaPlayer
					.getCurrentPosition();
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			songProgressBar.setProgress(progress);
			if (progress >= 99) {
				if(managerMusic.count >= (managerMusic.arrayPlay.size() - 1)
						|| managerMusic.count >= (managerMusic.arrayPick.size() - 1)) {
					managerMusic.repeatMusic();
				} else 
				{
					managerMusic.nextSong();
				}
			}
			handler.postDelayed(this, 100);

		}
	};

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	// handle data send from getBroadcast in MusicNotification
	BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// registerReceiver(this, new
			// IntentFilter("com.project.projectmusic.NO"));
			String value = intent.getExtras().getString("value");
			if (value.equalsIgnoreCase("layout")) {

			} else if (value.equalsIgnoreCase("previous")) {
				managerMusic.previousSong();
			} else if (value.equalsIgnoreCase("play")) {
				checkMusicPlaying();
			} else if (value.equalsIgnoreCase("next")) {
				managerMusic.nextSong();
			}
		}
	};

}
