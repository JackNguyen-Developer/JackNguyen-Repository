package com.project.projectmusic;

public class Song {
	private long id;
	private String title, album, artist, path;
	
	public Song()
	{
		id = -1;
		title = null; album = null; artist = null; path = null;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public void setTitle(String s)
	{
		this.title = s;
	}
	public void setAlbum(String s)
	{
		this.album = s;
	}
	public void setArtist(String s)
	{
		this.artist = s;
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
	public String getTitle()
	{
		return title;
	}
	public String getAlbum()
	{
		return album;
	}
	public String getArtist()
	{
		return artist;
	}
	public String getPath()
	{
		return path;
	}
}
