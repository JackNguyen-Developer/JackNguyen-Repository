package com.project.projectmusic;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventLongClick extends DialogFragment implements OnLongClickListener{
	Context context;
	Cursor cursor;
	Activity act;
	TextView addPlayList, addPlaying;
	String item;
	ArrayList<String> arrayItem = new ArrayList<String>();
	ArrayList<String> arrayPlaylist;
	MusicManager musicManager;
	TextView addNewPlayList, add;
	EditText addEdit;
	ListView listPlayList;
	LinearLayout dialogLayoutAddNew;
	SimpleCursorAdapter adapter;
	String value, name;
	long id;
	public EventLongClick(Context context, Activity act, String item, String name, String value, long id)
	{
		this.context = context;
		this.act = act;
		this.item = item;
		//this.arrayItem = arrayItem;
		musicManager = new MusicManager(context);
		this.name = name; this.value = value; this.id = id;
		
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_event_long_list, container, false);
		getDialog().setTitle("Setting");
		addPlayList = (TextView) view.findViewById(R.id.dialog_addPlayList);
		addPlaying = (TextView) view.findViewById(R.id.dialog_addPlaying);
		addNewPlayList = (TextView) view.findViewById(R.id.dialog_addNewPlayList);
		add = (TextView) view.findViewById(R.id.dialog_Add);
		addEdit = (EditText) view.findViewById(R.id.dialog_addEdit);
		listPlayList = (ListView) view.findViewById(R.id.dialog_listPlayList);
		dialogLayoutAddNew = (LinearLayout) view.findViewById(R.id.dialog_layout_AddNew);
		//handle arrayItem if data is array
		if (name != null && value != null) {
			Cursor cursorSub = musicManager.getListMusic(name, value, id);
			cursorSub.moveToFirst();
			do {
				String titleMusic = cursorSub.getString(0);
				arrayItem.add(titleMusic);
			} while (cursorSub.moveToNext());
			cursorSub.close();
		}
		//event
		addPlaying.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (item == null && arrayItem != null) {
					for (String item : arrayItem) {
						musicManager.arraySelect.add(item);
					}
				} else {
					musicManager.arraySelect.add(item);

				}
				if (!musicManager.getIsPlayMusicWithArraySelect()) {
					musicManager.resetCount();
					musicManager.isPlayMusicWithArraySelect();
				}
				getDialog().cancel();
			}
		});
		addPlayList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addNewPlayList.getVisibility() == TextView.GONE) {
					addNewPlayList.setVisibility(TextView.VISIBLE);
					listPlayList.setVisibility(ListView.VISIBLE);
				} else {
					addNewPlayList.setVisibility(TextView.GONE);
					listPlayList.setVisibility(ListView.GONE);
					dialogLayoutAddNew.setVisibility(LinearLayout.GONE);
				}
			}
		});
		addNewPlayList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialogLayoutAddNew.getVisibility() == LinearLayout.GONE) {
					dialogLayoutAddNew.setVisibility(LinearLayout.VISIBLE);

				} else {
					dialogLayoutAddNew.setVisibility(LinearLayout.GONE);
				}
			
			}
		});
		add.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				musicManager.addPlayList(addEdit.getText().toString());
				System.out.println("add playlist");
				adapter.notifyDataSetChanged();
				listPlayList.setAdapter(adapter);	
			}
		});
		//list
		cursor = musicManager.getPlayListMusic();
		arrayPlaylist = new ArrayList<String>();
		adapter = new SimpleCursorAdapter(context, R.layout.list_item, cursor, new String[]{ MediaStore.Audio.Playlists.NAME}, new int[]{R.id.title});		
		listPlayList.setAdapter(adapter);
		listPlayList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cursor.moveToPosition(position);
				long idPlayList = cursor.getLong(1);
				//
				/*if (name != null && value != null) {
					Cursor cursorSub = musicManager.getListMusic(name, value,
							id);
					cursorSub.moveToFirst();
					do {
						String titleMusic = cursorSub.getString(0);
						arrayItem.add(titleMusic);
					} while (cursorSub.moveToNext());
					cursorSub.close();
				}*/
				//
				System.out.println("data item: "+item);
				musicManager.addItemPlayList(idPlayList, arrayItem, item);
				getDialog().cancel();
			}
		});
		return view;
	}
	@Override
	public boolean onLongClick(View v) {
		System.out.println("event long press");
		this.show(act.getFragmentManager(),"");
		return true;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Intent in = new Intent();
		//in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		//context.startActivity(in);
	}
}
