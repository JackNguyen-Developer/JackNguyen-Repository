package com.project.projectmusic;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AdapterViewPager_Common extends PagerAdapter {
	ArrayList<View> arrayView;
	Context context;
	Activity act;
	//
	private ListView listView;
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
					long id = cursor.getLong(1);
					item.setId(cursor.getLong(1));
					
					item.setName("Playlist");
					String title = cursor.getString(0);
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
}
