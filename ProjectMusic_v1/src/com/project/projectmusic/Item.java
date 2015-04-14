package com.project.projectmusic;

public class Item {
	private long id;
	private String name, title, image, path;
	
	public Item()
	{
		id = -1;
		name = null; title = null; image = null; path = null;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	public void setName(String s)
	{
		this.name = s;
	}
	public void setTitle(String s)
	{
		this.title = s;
	}
	public void setImage(String s)
	{
		this.image = s;
	}
	public void setPath(String s)
	{
		this.path = s;
	}
	//////
	public long getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getTitle()
	{
		return title;
	}
	public String getImage()
	{
		return image;
	}
	public String getPath()
	{
		return path;
	}
}
