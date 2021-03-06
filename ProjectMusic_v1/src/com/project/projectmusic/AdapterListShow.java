package com.project.projectmusic;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class AdapterListShow extends ArrayAdapter<String> {
	ArrayList<String> arrayList;
	private ArrayList<Song> arrayPlaying;
	int resource; //list|R.layout.list
	Context context; //activity_start_up2|Context
	Activity act;
	MusicManager musicManager;
	public AdapterListShow(Context context, Activity act, int textViewResourceID /*R.layout.list*/, ArrayList<String> objects)
	{
		super(context, textViewResourceID, objects);
		this.context = context;
		this.act = act;
		resource = textViewResourceID;
		arrayList = objects;
		arrayPlaying = new ArrayList<Song>();
		musicManager = new MusicManager(context);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View workView = convertView;	
		if(workView == null){workView = new ViewGroup_ListShow(getContext());}
		final String item = arrayList.get(position);
		if(item != null) {
			TextView songTitle = ((ViewGroup_ListShow) workView).title;
			LinearLayout contain = ((ViewGroup_ListShow) workView).contain;
			songTitle.setText(item);
			contain.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					musicManager.notPlayMusicWithArraySelect();
					musicManager.setArrayPlay(arrayList);
					musicManager.setCount(position);
					musicManager.setActivity(act);			
					musicManager.playSong();
				}
			});
			contain.setOnLongClickListener(new EventLongClick(context, act, item, null, null, -1));	
		}	
		return workView;
	}
	
}
