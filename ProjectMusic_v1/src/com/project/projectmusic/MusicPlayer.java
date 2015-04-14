package com.project.projectmusic;
import java.io.File;
import java.util.ArrayList;
import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MusicPlayer extends Activity implements MusicManager.ManagerMusicListener {
	//component viewpager
	private ImageView[] imageViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews; 	// pages view
	private ImageView imageView;  
	private ViewGroup viewMain;         // layout for view group
	private ViewGroup viewContent;
	AdapterViewPager_PlayerPlaylist viewPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);//kich hoat actionbar home
		//handle viewpager
		LayoutInflater inflater = getLayoutInflater();
		viewMain = (ViewGroup) inflater.inflate(R.layout.player_container, null);
		//set view
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.player, null));
		pageViews.add(inflater.inflate(R.layout.playlist_selection, null));
		//set component
		imageViews = new ImageView[pageViews.size()];
		viewContent = (ViewGroup) viewMain.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) viewMain.findViewById(R.id.guidePages);
		//set images
		for(int i = 0; i < pageViews.size(); i++)
		{
			imageView = new ImageView(MusicPlayer.this);
			imageView.setLayoutParams(new LayoutParams(20,20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			if(i == 0)
				imageViews[i].setBackgroundResource(R.drawable.circle_white);
			else
				imageViews[i].setBackgroundResource(R.drawable.circle_grey);
			viewContent.addView(imageViews[i]);
		}
		setContentView(viewMain);
		viewPagerAdapter = new AdapterViewPager_PlayerPlaylist(pageViews, getApplicationContext(), MusicPlayer.this);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				for (int i = 0; i < imageViews.length; i++) {
					imageViews[position].setBackgroundResource(R.drawable.circle_white);
					if (position != i) {
						imageViews[i].setBackgroundResource(R.drawable.circle_grey);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//end handle viewpager
	}
	public void buttonDong(View v)
	{
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		viewPagerAdapter.playMusic();
	}
}
