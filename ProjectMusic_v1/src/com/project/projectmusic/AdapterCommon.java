package com.project.projectmusic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterCommon extends ArrayAdapter<Item> {
	Context context;
	int resource;
	ArrayList<Item> items;
	Activity act;
	ArrayList<String> arrayPlay;
	MusicManager managerMusic;
	public AdapterCommon(Context context, Activity act,
			int textViewResourceId, ArrayList<Item> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		resource = textViewResourceId;
		items =  objects;
		this.act = act;
		//get arrayPlay
		arrayPlay = new ArrayList<String>();
		//arrayPlay = null;
		managerMusic = new MusicManager(context);
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view= convertView;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		//setArrayPlay
		if (!(arrayPlay.size() > 0) && items.get(position).getName().equalsIgnoreCase("Song")) {
			for (int i = 0; i < items.size(); i++) {
				String title = items.get(i).getTitle();
				arrayPlay.add(title);
			}
			managerMusic.setPlayListDefault(arrayPlay);
			Log.e("","set finish arrayPlayDefault");
		}
		//
		if(view == null) view = layoutInflater.inflate(R.layout.list_item, null);
		final Item item = items.get(position);
		if(item != null)
		{
			final TextView title = (TextView) view.findViewById(R.id.title);
			LinearLayout contain = (LinearLayout) view.findViewById(R.id.contain);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			title.setText(item.getTitle());
			if (item.getName() != null) {
				if(item.getName().equalsIgnoreCase("Artist"))
				{
					Drawable draw = context.getResources().getDrawable(R.drawable.ic_artists);
					image.setImageDrawable(draw);
				}
				if (item.getImage() != null) {
					File file = new File(item.getImage());
					if (file.exists()) {
						Bitmap bImage = BitmapFactory.decodeFile(file
								.getAbsolutePath());
						image.setImageBitmap(bImage);
					}
				}
			}
			
			contain.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(item.getName().equalsIgnoreCase("Song"))
					{
						managerMusic.notPlayMusicWithArraySelect();
						managerMusic.setArrayPlay(arrayPlay);
						managerMusic.setCount(position);
						managerMusic.setActivity(act);			
						managerMusic.playSong();
					
					} else
					{
						Intent in = new Intent(context,MusicListShow.class);
						in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						in.putExtra("name", item.getName());
						in.putExtra("value", item.getTitle());
						in.putExtra("id", item.getId());
						context.startActivity(in);	
						//set long press
						//title.setOnLongClickListener(new LongPressEvent(context, act, null , arrayPlay));
					}
				}
			});
			/*title.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
					if(item.getName().equalsIgnoreCase("Song"))
					{
						//ArrayList<Song> arrayPlaying = new ArrayList<Song>();
						ArrayList<String> arrayPlay = new ArrayList<String>();
						for(int i = 0; i < items.size(); i++)
						{
							String title = items.get(i).getTitle();
							arrayPlay.add(title);
						}		
						//MusicManager managerMusic = new MusicManager(context);
						managerMusic.arrayPlay = arrayPlay;
						managerMusic.count = position;
						managerMusic.setActivity(act);			
						managerMusic.playSong();
						//set long press
						//title.setOnLongClickListener(new LongPressEvent(context, act, item.getTitle() , null));
					} else
					{
						Intent in = new Intent(context,Content.class);
						in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						in.putExtra("name", item.getName());
						in.putExtra("value", item.getTitle());
						in.putExtra("id", item.getId());
						context.startActivity(in);	
						//set long press
						//title.setOnLongClickListener(new LongPressEvent(context, act, null , arrayPlay));
					}
					
				}
			});*/
			EventLongClick longPress; 
			if (item.getName().equalsIgnoreCase("Song")) {
				longPress = new EventLongClick(context, act, item.getTitle(), null, null, -1);
			} else {
				/*ArrayList<String> arrayMusic = new ArrayList<String>();
				Cursor cursor = managerMusic.getListMusic(item.getName(), item.getTitle(), item.getId());
				cursor.moveToFirst();
				do{
					String titleMusic = cursor.getString(0);
					arrayMusic.add(titleMusic);
				} while(cursor.moveToNext());
				cursor.close();*/
				longPress = new EventLongClick(context, act, null , item.getName(), item.getTitle(), item.getId());
			}
			
			contain.setOnLongClickListener(longPress);
		}
		return view;
	}
}
