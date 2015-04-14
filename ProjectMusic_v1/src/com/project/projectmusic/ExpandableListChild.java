package com.project.projectmusic;

public class ExpandableListChild {
	private long id;
	private String Name;
	private String Tag;
	public ExpandableListChild() {
		id = 0;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String Name) {
		this.Name = Name;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String Tag) {
		this.Tag = Tag;
	}

}
