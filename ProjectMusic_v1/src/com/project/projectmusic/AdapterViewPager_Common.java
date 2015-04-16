package com.project.projectmusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AdapterViewPager_Common extends PagerAdapter {
	ArrayList<View> arrayView;
	Context context;
	Activity act;
	//
	private ListView listView;
	private View includeExplorer;
	private AdapterCommon adapter;
	private Cursor cursor;
	private ArrayList<Item> arraySong = new ArrayList<Item>();
	private ArrayList<Item> arrayAlbum = new ArrayList<Item>();
	private ArrayList<Item> arrayArtist = new ArrayList<Item>();
	private ArrayList<Item> arrayPlaylist = new ArrayList<Item>();
	private ArrayList<Item> arrayGenre = new ArrayList<Item>();
	private ArrayList<Item> arrayType = new ArrayList<Item>();
	private MusicManager managerMusic;

	public AdapterViewPager_Common(ArrayList<View> arrayView, Context context, Activity act) {
		this.arrayView = arrayView;
		this.context = context;
		this.act = act;
		//
		managerMusic = new MusicManager(context);
		managerMusic.setActivity(act);
	}
	@Override
	public Object instantiateItem(View v, int position) {
		switch (position) {
		case 0: {
			listView = (ListView) arrayView.get(0).findViewById(R.id.list_common);
			viewSong();
		}

		case 1: {
			listView = (ListView) arrayView.get(1).findViewById(R.id.list_common);
			viewAlbum();
		}

		case 2: {
			listView = (ListView) arrayView.get(2).findViewById(R.id.list_common);
			viewArtist();
		}

		case 3: {
			listView = (ListView) arrayView.get(3).findViewById(R.id.list_common);
			viewPlayList();
		}

		case 4: {
			listView = (ListView) arrayView.get(4).findViewById(R.id.list_common);
			viewGenre();
		}

		case 5: {
			listView = (ListView) arrayView.get(5).findViewById(R.id.list_common);
			viewType();
		}
		case 6: {
			includeExplorer =(View) arrayView.get(6).findViewById(R.id.musicCategory_explorer);
			listView = (ListView) arrayView.get(6).findViewById(R.id.list_common);
			explorerList =(ListView) includeExplorer.findViewById(R.id.explorer_list);
			listView.setVelocityScale(ListView.GONE);
			includeExplorer.setVisibility(View.VISIBLE);
			explorerMyPath = (TextView) arrayView.get(6).findViewById(R.id.path);
			explorerHandle();
		}
		}
		((ViewPager) v).addView(arrayView.get(position));
		return arrayView.get(position);
	}
	@Override
	public int getCount() {
		return arrayView.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object o) {
		return v == o;
	}
        
	@Override
	public void destroyItem(View v, int position, Object object) {
		((ViewPager)v).removeView(arrayView.get(position));
	}
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	private void viewSong()
	{
		cursor = managerMusic.getAllMusic();
		arraySong = new ArrayList<Item>();
		if(cursor.moveToFirst())
		{
			while(!cursor.isAfterLast())
			{
				try {
					Item item = new Item();
					item.setId(cursor.getLong(1));
					item.setName("Song");
					item.setTitle(cursor.getString(0));
					cursor.moveToNext();
					arraySong.add(item);
				} catch (Exception e) {
					Log.i("", "info Fragment_Song");
				}
			}
		}
		cursor.close();
		adapter = new AdapterCommon(context, act, R.layout.list_item, arraySong);
		listView.setAdapter(adapter);
	}
	private void viewAlbum()
	{
		cursor = managerMusic.getAlbumMusic();
		arrayAlbum = new ArrayList<Item>();
		if(cursor.moveToFirst())
		{
			while(!cursor.isAfterLast())
			{
				try {
					Item item = new Item();
					item.setId(cursor.getLong(1));
					item.setName("Album");
					item.setTitle(cursor.getString(0));
					item.setImage(cursor.getString(2));
					cursor.moveToNext();
					arrayAlbum.add(item);
				} catch (Exception e) {
					Log.i("","info Fragment_Album");
				}
			}
		}
		cursor.close();
		adapter = new AdapterCommon(context, act, R.layout.list_item, arrayAlbum);
		listView.setAdapter(adapter);
	}
	private void viewArtist()
	{
		cursor = managerMusic.getArtistMusic();
		arrayArtist = new ArrayList<Item>();
		try {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					Item item = new Item();
					item.setId(cursor.getLong(1));
					item.setName("Artist");
					item.setTitle(cursor.getString(0));
					cursor.moveToNext();
					arrayArtist.add(item);
				}
			}
			cursor.close();
			adapter = new AdapterCommon(context, act,
					R.layout.list_item, arrayArtist);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			Log.e("", "Error in Fragment_Artist: " + e.toString());
		}
	}
	private void viewPlayList()
	{
		try {
			cursor = managerMusic.getPlayListMusic();
			arrayPlaylist = new ArrayList<Item>();
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {

					Item item = new Item();
					//long id = cursor.getLong(1);
					//Log.d("",String.valueOf(id));
					item.setId(cursor.getLong(1));
					item.setName("Playlist");
					//String title = cursor.getString(0);
					//Log.d("",title);
					item.setTitle(cursor.getString(0));
					cursor.moveToNext();
					arrayPlaylist.add(item);

				}
			}
			cursor.close();
			adapter = new AdapterCommon(context, act, R.layout.list_item,
					arrayPlaylist);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			Log.i("", "info Fragment_Playlist");
		}
	}
	private void viewGenre()
	{
		cursor = managerMusic.getGenreMusic();
		arrayGenre = new ArrayList<Item>();
		if(cursor.moveToFirst())
		{
			while(!cursor.isAfterLast())
			{
				try {
					Item item = new Item();
					item.setId(cursor.getLong(1));
					item.setName("Genre");
					item.setTitle(cursor.getString(0));
					cursor.moveToNext();
					arrayGenre.add(item);
				} catch (Exception e) {
					Log.i("","info Fragment_Genre");
				}
			}
		}
		cursor.close();
		adapter = new AdapterCommon(context, act, R.layout.list_item, arrayGenre);
		listView.setAdapter(adapter);
	}
	private void viewType()
	{
		cursor = managerMusic.getTypeMusic();
		arrayType = new ArrayList<Item>();
		if(cursor.moveToFirst())
		{
			while(!cursor.isAfterLast())
			{
				try {
					Item item = new Item();
					item.setName("Type");
					item.setTitle(cursor.getString(0));
					cursor.moveToNext();
					arrayType.add(item);
				} catch (Exception e) {
					Log.i("", "info Fragment_Type");
				}
			}
		}
		cursor.close();
		adapter = new AdapterCommon(context, act, R.layout.list_item, arrayType);
		listView.setAdapter(adapter);
	}
	private List<String> item = null;
	private List<String> path = null;
	private String root;
	private TextView explorerMyPath;
	private ListView explorerList;
	private void explorerHandle()
	{
		
		root = "storage";
		getDir(root);
		explorerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				File file = new File(path.get(position));
				if (file.isDirectory()) {
					if (file.canRead()) {
						getDir(path.get(position));
					} else {
						new AlertDialog.Builder(context)
								.setIcon(R.drawable.ic_launcher)
								.setTitle(
										"[" + file.getName()
												+ "] folder can't be read!")
								.setPositiveButton("OK", null).show();
					}
				} else {
					MediaMetadataRetriever mmr = new MediaMetadataRetriever();
					mmr.setDataSource(file.getPath());
					String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
					Log.e("",title);
					managerMusic.isPlayMusicWithArraySelect();
					managerMusic.resetArraySelect();
					managerMusic.addArraySelect(title);
					managerMusic.firstCount();
					//managerMusic.setCount(position);
					managerMusic.setActivity(act);			
					managerMusic.playSong();
				}
				
			}
			
		});
	}
	private void getDir(String dirPath) {
		explorerMyPath.setText("Location: " + dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (!dirPath.equals(root)) {
			item.add(root);
			path.add(root);
			item.add("../");
			// if(!dirPath.equals(root))
			path.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];

			if (!file.isHidden() && file.canRead()) {
				path.add(file.getPath());
				if (file.isDirectory()) {
					/* if (ReadSDCard() == true) { */
					item.add(file.getName());
				} else {
					item.add(file.getName());
				}

			}
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(context,
				R.layout.explorer_row, item);
		explorerList.setAdapter(fileList);
	}
}
