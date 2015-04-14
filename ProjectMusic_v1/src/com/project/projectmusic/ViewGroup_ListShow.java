package com.project.projectmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ViewGroup_ListShow extends LinearLayout{
	public TextView title;
	public LinearLayout contain;
	public ViewGroup_ListShow(Context context) {	
		super(context);
		//gan' giao dien cho playlist_item.xml
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);//khoi tao doi tuong Inflater
		li.inflate(R.layout.list_item, this, true);		
		title = (TextView) findViewById(R.id.title);
		contain = (LinearLayout) findViewById(R.id.contain);
	}
}
