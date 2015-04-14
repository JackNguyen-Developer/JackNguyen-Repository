package com.project.projectmusic;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<ExpandListGroup> groups;
	public ExpandableListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}	
	public void addItem(ExpandListChild item, ExpandListGroup group)
	{
		if(!groups.contains(group))
		{
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ExpandListChild> child = groups.get(index).getItems();
		child.add(item);
		groups.get(index).setItems(child);
	}
	
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ExpandListChild> childList = groups.get(groupPosition).getItems();			
		return childList.size();
		
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ExpandListChild> childList = groups.get(groupPosition).getItems();	
		return childList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {	
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View view, ViewGroup parent) {
		ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
		if(view == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.expandlist_group_item, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.titleGroup);
		textView.setText(group.getName());	
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {			
		ExpandListChild child = (ExpandListChild) getChild(groupPosition,childPosition);	
		if(view == null)
		{		
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.expandlist_child_item, null);		
		}					
		TextView textView = (TextView) view.findViewById(R.id.titleChild);
		textView.setText(child.getName().toString());
		textView.setTag(child.getTag());
		return view;
		
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
