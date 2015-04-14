package com.project.projectmusic;

import java.util.ArrayList;

public class ExpandListGroup {
	private String name;
	private ArrayList<ExpandListChild> items;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<ExpandListChild> getItems() {
		return items;
	}
	public void setItems(ArrayList<ExpandListChild> Items) {
		this.items = Items;
	}
}
