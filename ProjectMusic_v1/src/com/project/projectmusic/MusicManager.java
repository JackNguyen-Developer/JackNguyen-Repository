package com.project.projectmusic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.media.MediaPlayer;
import android.net.Uri;

public class MusicManager {
	//....
	private Context context;
	private ArrayList<Song> arrayPlaying;
	ContentResolver cr;
	//static
	public static int count = -1;
	public static MediaPlayer mediaPlayer  = new MediaPlayer();
	public static String songTitlePlaying;
	public static ArrayList<String> arrayPlay = new ArrayList<String>();
	public static ArrayList<String> arrayPick = new ArrayList<String>();
	public static String albumArt;
	public static boolean isRepeat = false;
	public static boolean isShuffle = false;
	public static boolean isPlaying = false;
	//interface
	interface ManagerMusicListener
	{
		public void playMusic();
		//public void getArrayPlaying(ArrayList<Song> arrayPlaying);
	}
	ManagerMusicListener listener;//call interface
	//
	public MusicManager(Context context)
	{
		this.context = context;
		cr = context.getContentResolver();
	}
	//set Activity for interface
	public void setActivity(Activity activity)
	{
		try{
			listener = (ManagerMusicListener) activity;
		} catch(Exception e){
			Log.e("","error in ManagerMusic.setActivity" + activity.toString());
		}
	}
	//play song
	public boolean play() {
		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				return true;
			} else//not play
			{
				if(arrayPlay.size() > 0 && !mediaPlayer.isPlaying())
				{	
					mediaPlayer.start();
					return false;
				}
				else
				{
					System.out.println("1111");
					arrayPlay = getPlayListDefault();
					count = 0;
					playSong();
					return false;
				}
				
			}
		} catch (Exception e) {
			Log.e("", "error in MusicManager.playSong");
			return false;
		}
	}
	//next song
	public void nextSong() {
		try {
			count++;
			playSong();
		} catch (Exception e) {
			Log.e("", "error in MusicManager.nextSong");
		}
	}
	//previous song
	public void previousSong() {
		try {
			count--;
			playSong();
		} catch (Exception e) {
			Log.e("", "error in MusicManager.previousSong");
		}
	}
	//
	public void shuffleMusic()
	{
		String songPlaying = arrayPlay.get(count);
		Collections.shuffle(arrayPlay);
		for(int i = 0; i <= arrayPlay.size(); i++)
		{
			if(arrayPlay.get(i) == songPlaying)
			{
				count = i;
				break;
			}
		}
	}
	public void repeatMusic()
	{
		count = -1;
		nextSong();
	}
	public void sortArrayMusic()
	{
		String songPlaying = arrayPlay.get(count);
		Collections.sort(arrayPlay);
		for(int i = 0; i <= arrayPlay.size(); i++)
		{
			if(arrayPlay.get(i) == songPlaying)
			{
				count = i;
				break;
			}
		}
	}
	//
	public ArrayList<String> getPlayListDefault()
	{
		ArrayList<String> arrayList = new ArrayList<String>();
		Cursor cur = getAllSong();
		if(cur.moveToFirst())
		{
			cur.moveToFirst();
			System.out.println("2222");
			while(!cur.isAfterLast())
			{
				String title = cur.getString(1).toString();
				System.out.println(title);
				arrayList.add(title);
				cur.moveToNext();
			}
		}
		cur.close();
		return arrayList;
	}
	public ArrayList<Song> getPlayList()
	{
		ArrayList<Song> arraySong = new ArrayList<Song>();
		Cursor cur = getAllSong();
		cur.moveToFirst();
		String title, path = null, album = null, artist = null;
		long id = -1;
		while(!cur.isAfterLast())
		{
			try {
				id = cur.getLong(0);
				title = cur.getString(1).toString();
				path = cur.getString(2).toString();
				album = cur.getString(3).toString();
				artist = cur.getString(4).toString();
				Song song = new Song();
				song.setId(id); song.setTitle(title); song.setPath(path); song.setAlbum(album); song.setArtist(artist);
				arraySong.add(song);
				break;
			} catch (Exception e) {
				Log.e("", "error MusicManager.getPlayList" + e.toString());
			}
		}
		cur.close();
		return arraySong;
	}
	public Cursor getListMusic(String select, String titleChild, long idChild)
	{
		Cursor cursor = null;	
		//ContentResolver cr = context.getApplicationContext().getContentResolver();
		String[] projection = {MediaStore.Audio.Media.TITLE};
		String selection = null;
		String[] selectionArgs = {titleChild};	
		if(select.equalsIgnoreCase("All"))
		{		
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		} else if(select.equalsIgnoreCase("Album"))
		{					
			selection = MediaStore.Audio.Media.ALBUM + " = ?";
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		} else if(select.equalsIgnoreCase("Artist"))
		{					
			selection = MediaStore.Audio.Media.ARTIST + " = ?";
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		} else if(select.equalsIgnoreCase("Genre"))
		{													
			cursor = cr.query(MediaStore.Audio.Genres.Members.getContentUri("external", idChild), projection, null, null, null);			
			//MediaStore.Audio.Artists.
		} else if(select.equalsIgnoreCase("Playlist"))
		{								
			projection = new String[]{MediaStore.Audio.Playlists.Members.AUDIO_ID};
			//selection = MediaStore.Audio.Media._ID + " = ?";
			//selectionArgs = new String[]{"1940"};
			cursor = cr.query(MediaStore.Audio.Playlists.Members.getContentUri("external", idChild), projection, null, null, null);
		} else if(select.equalsIgnoreCase("Type"))
		{					
			selection = MediaStore.Audio.Media.MIME_TYPE + " = ?";				
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		}
		return cursor;
	}
	public Cursor getAllMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID};
		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		return cursor;
	}
	public Cursor getAlbumMusic()
	{	
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};
		Cursor cur = cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, null);
		return  cur;
	}
	public Cursor getArtistMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID};
		Cursor cur = cr.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, null);
		return  cur;
	}
	public Cursor getGenreMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
		Cursor cur = cr.query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, null);
		return  cur;
	}
	public Cursor getPlayListMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Playlists.NAME, MediaStore.Audio.Playlists._ID};
		Cursor cur = cr.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, null);
		return  cur;
	}
	public Cursor getTypeMusic()
	{
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Media.MIME_TYPE/*, MediaStore.Audio.Media._ID*/};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, null);
		return  cur;
	}
	public Cursor getSong(String title)
	{
		//cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST};
		String selection = MediaStore.Audio.Media.TITLE + " = ?";
		String[] selectionArgs = new String[] {title};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		return cur;
	}
	public String getSong(long id)
	{
		String[] projection = {MediaStore.Audio.Media.TITLE};
		String selection = MediaStore.Audio.Media._ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(id)};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		cur.moveToFirst(); String title = cur.getString(0);
		cur.close();
		return title;
	}
	public Cursor getAllSong()
	{
		//cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST};
		//String selection = MediaStore.Audio.Media._ID + " = ?";
		//String[] selectionArgs = new String[] {String.valueOf(item)};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		return cur;
	}
	//get Path with title song
	public String getSongPath(String title)
	{
		String path = null;
		String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID};	
		String selection = MediaStore.Audio.Media.TITLE + " = ?";
		String[] selectionArgs = new String[] {title};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		if(cur.moveToFirst())
		{
			try {
				cur.moveToFirst();
				path = cur.getString(0);
				long idAlbumArt = cur.getLong(1);
				albumArt = getAlbumArt(idAlbumArt);
			} catch (Exception e) {
				Log.i("", "info MusicManager.getSongPath problem with: "
						+ title);
			}
		}
		cur.close();
		return path;
	}
	//get albumArt with albumId
	public String getAlbumArt(long id)
	{
		String albumArt = null;
		String[] projection = {MediaStore.Audio.Albums.ALBUM_ART};
		String selection = MediaStore.Audio.Albums._ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(id)};
		Cursor cur = cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		if(cur.moveToFirst())
		{
			try {
				cur.moveToFirst();
				albumArt = cur.getString(0);
				// System.out.println(cur.getString(0));
			} catch (Exception e) {
				Log.i("", "info: no album art of - " + id);
			}
		}
		cur.close();
		return albumArt;
	}
	//use interface
	public void playSong()
	{
		try {
			if (arrayPlay.size() > 0 || arrayPick.size() > 0) {
				String songTitle = null;
				if(arrayPick.size() > 0)
				{
					songTitle = arrayPick.get(count);
				} else
				{
					songTitle = arrayPlay.get(count);
				}
				songTitlePlaying = songTitle;
				System.out.println("MusicManager.playSong:" + songTitlePlaying);
				String songPath = getSongPath(songTitle);
				System.out.println("B1: MusicManager.playSong");
				mediaPlayer.reset();
				mediaPlayer.setDataSource(songPath);
				System.out.println("B2: MusicManager.playSong");
				mediaPlayer.prepare();
				mediaPlayer.start();
				System.out.println("B3: MusicManager.playSong");
				isPlaying = true;
				listener.playMusic();
			}
		} catch (Exception e) {
			Log.e("","error in ManagerMusic.playSong: " + e.toString());
		}	
	}
	public void addPlayList(String name)
	{
		//long id = 0;
		ContentValues cv = new ContentValues();
		cv.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
		cv.put(MediaStore.Audio.Playlists.NAME, name);
		//cv.put(MediaStore.Audio.Playlists._ID, id);
		//Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
		Uri uri = cr.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cv);
		Log.e("","Add new Playlist: " + uri);
	}
	public void addItemPlayList(long idPlayList,ArrayList<String> arrayMusic, String titleMusic)
	{
		if(arrayMusic != null && titleMusic == null)/*.size() > 0 && titleMusic == null)*/
		{
			for(int i = 0; i < arrayMusic.size(); i++)
			{
				System.out.println("arrayMusic");
				String titleSong = arrayMusic.get(i).toString();
				Cursor cur = getSong(titleSong);
				cur.moveToFirst();
				long idSong = cur.getLong(0);
				cur.close();
				insertItemPlayList(idPlayList, idSong);
			}
		} 
		else
		{
			System.out.println("titleMusic");
			Cursor cur = getSong(titleMusic);
			cur.moveToFirst();
			long idSong = cur.getLong(0);
			cur.close();
			insertItemPlayList(idPlayList, idSong);
		}
		/*String title = titleMusic;
		ContentValues cv = new ContentValues();
		cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 2);
		cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, idSong);
		Uri uri = cr.insert(MediaStore.Audio.Playlists.Members.getContentUri("external", idPlayList), cv);*/
		//Log.e("","Add new item Playlist: " + uri);	
	}
	private void insertItemPlayList(long idPlayList, long idSong)
	{
		ContentValues cv = new ContentValues();
		cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 2);
		cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, idSong);
		Uri uri = cr.insert(MediaStore.Audio.Playlists.Members.getContentUri("external", idPlayList), cv);
	}
}
	
