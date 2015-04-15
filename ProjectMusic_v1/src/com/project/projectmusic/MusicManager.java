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
	//private ArrayList<Song> arrayPlaying;
	ContentResolver cr;
	//static
	//handle count
	public static int count = -1;
	private final int RESET_COUNT = -1;
	private final int FIRST_COUNT = 0;
	//
	public static MediaPlayer mediaPlayer  = new MediaPlayer();
	public static String songTitlePlaying;//musicTitle
	public static String albumArt;//image album
	//
	public static ArrayList<String> arrayPlay = new ArrayList<String>();
	public static ArrayList<String> arraySelect = new ArrayList<String>();
	
	public static boolean isRepeat = false;
	public static boolean isShuffle = false;
	public static boolean isPlaying = false;
	public static boolean isPlayMusicWithArraySelect = false;
	//manage Count
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public void resetCount()
	{
		count = RESET_COUNT;
	}
	public void firstCount()
	{
		count = FIRST_COUNT;
	}
	public void increaseCount()
	{
		count++;
	}
	public void decreaseCount()
	{
		count--;
	}
	//manage songTitle
	public String getSongTitle()
	{
		return songTitlePlaying;
	}
	public void setSongTitle(String songTitle)
	{
		this.songTitlePlaying = songTitle;
	}
	//manage albumArt
	public String getAlbumArt()
	{
		return albumArt;
	}
	public void setAlbumArt(String albumArt)
	{
		this.albumArt = albumArt;
	}
	//manage arrayPlay
	public ArrayList<String> getArrayPlay()
	{
		return arrayPlay;
	}
	public void setArrayPlay(ArrayList<String> arrayPlay)
	{
		this.arrayPlay = arrayPlay;
	}
	//manage arrayPlay
		public ArrayList<String> getArraySelect()
		{
			return arraySelect;
		}
		public void setArraySelect(ArrayList<String> arraySelect)
		{
			this.arraySelect = arraySelect;
		}
	//
	public void setPlayListDefault(ArrayList<String> arrayPlay)
	{
		this.arrayPlay = arrayPlay;
		if(arrayPlay.size() > 0) {Log.e("","arrayPlay have data");}
		firstCount();
	}
	//manage playing
	public void isPlaying()
	{
		isPlaying = true;
	}
	public void notPlaying()
	{
		isPlaying = false;
	}
	//manage play arraySelect
	public void isPlayMusicWithArraySelect()
	{
		isPlayMusicWithArraySelect = true;
	}
	public void notPlayMusicWithArraySelect()
	{
		isPlayMusicWithArraySelect = false;
	}
	public boolean getIsPlayMusicWithArraySelect()
	{
		return isPlayMusicWithArraySelect;
	}
	//interface
	interface ManagerMusicListener
	{
		public void playMusic();	
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
	//play button
	public boolean playButton() {
		try {
			if (mediaPlayer.isPlaying()) {
				System.out.println("1111");
				mediaPlayer.pause();
				return true;
			} else//not play
			{
				if(arrayPlay.size() > 0 && isPlaying || arraySelect.size() > 0 && isPlaying )
				{	
					System.out.println("22222");
					mediaPlayer.start();
					return false;
				}
				else
				{
					System.out.println("3333");
					//arrayPlay = getPlayListDefault();
					//count = 0;
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
	public void nextButton() {
		try {
			count++;
			playSong();
		} catch (Exception e) {
			Log.e("", "error in MusicManager.nextSong");
		}
	}
	//previous song
	public void previousButton() {
		try {
			count--;
			playSong();
		} catch (Exception e) {
			Log.e("", "error in MusicManager.previousSong");
		}
	}
	//
	public void shuffleButton()
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
	public void repeatButton()
	{
		count = -1;
		nextButton();
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
	
	
	/*public ArrayList<Song> getPlayList() {
		ArrayList<Song> arraySong = new ArrayList<Song>();
		Cursor cur = getAllSong();
		cur.moveToFirst();
		String title, path = null, album = null, artist = null;
		long id = -1;
		while (!cur.isAfterLast()) {
			try {
				id = cur.getLong(0);
				title = cur.getString(1).toString();
				path = cur.getString(2).toString();
				album = cur.getString(3).toString();
				artist = cur.getString(4).toString();
				Song song = new Song();
				song.setId(id);
				song.setTitle(title);
				song.setPath(path);
				song.setAlbum(album);
				song.setArtist(artist);
				arraySong.add(song);
				break;
			} catch (Exception e) {
				Log.e("", "error MusicManager.getPlayList" + e.toString());
			}
		}
		cur.close();
		return arraySong;
	}*/
	
	public Cursor getListMusic(String select, String titleChild, long idChild)
	{
		Cursor cursor = null;	
		//ContentResolver cr = context.getApplicationContext().getContentResolver();
		String[] projection = {MediaStore.Audio.Media.TITLE};
		String selection = null;
		String[] selectionArgs = {titleChild};	
		if(select.equalsIgnoreCase("All"))
		{		
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		} else if(select.equalsIgnoreCase("Album"))
		{					
			selection = MediaStore.Audio.Media.ALBUM + " = ?";
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		} else if(select.equalsIgnoreCase("Artist"))
		{					
			selection = MediaStore.Audio.Media.ARTIST + " = ?";
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		} else if(select.equalsIgnoreCase("Genre"))
		{													
			cursor = cr.query(MediaStore.Audio.Genres.Members.getContentUri("external", idChild), projection, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);			
			//MediaStore.Audio.Artists.
		} else if(select.equalsIgnoreCase("Playlist"))
		{								
			projection = new String[]{MediaStore.Audio.Playlists.Members.AUDIO_ID};
			//selection = MediaStore.Audio.Media._ID + " = ?";
			//selectionArgs = new String[]{"1940"};
			cursor = cr.query(MediaStore.Audio.Playlists.Members.getContentUri("external", idChild), projection, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		} else if(select.equalsIgnoreCase("Type"))
		{					
			selection = MediaStore.Audio.Media.MIME_TYPE + " = ?";				
			cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}
		return cursor;
	}
	public Cursor getAllMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID};
		String selection = MediaStore.Audio.Media.IS_MUSIC;
		//String[] selectionArgs;// = {titleChild};	
		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return cursor;
	}
	public Cursor getAlbumMusic()
	{	
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};
		Cursor cur = cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
		return  cur;
	}
	public Cursor getArtistMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID};
		Cursor cur = cr.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
		return  cur;
	}
	public Cursor getGenreMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
		Cursor cur = cr.query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, MediaStore.Audio.Genres.DEFAULT_SORT_ORDER);
		return  cur;
	}
	public Cursor getPlayListMusic()
	{
		//ContentResolver cr = context.getContentResolver();
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Playlists.NAME, MediaStore.Audio.Playlists._ID};
		Cursor cur = cr.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
		return  cur;
	}
	public Cursor getTypeMusic()
	{
		String[] arrayAlbum = {"DISTINCT " + MediaStore.Audio.Media.MIME_TYPE/*, MediaStore.Audio.Media._ID*/};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayAlbum, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return  cur;
	}
	public Cursor getSong(String title)
	{
		//cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST};
		String selection = MediaStore.Audio.Media.TITLE + " = ?";
		String[] selectionArgs = new String[] {title};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return cur;
	}
	public String getSong(long id)
	{
		String title = null;
		try{
		String[] projection = {MediaStore.Audio.Media.TITLE};
		String selection = MediaStore.Audio.Media._ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(id)};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cur.moveToFirst(); title = cur.getString(0);
		cur.close();
		} catch(Exception e)
		{
			Log.e("","error in MusicManager.getSong(long id)");
		}
		return title;
	}
	public Cursor getAllSong()
	{
		//cr = context.getContentResolver();
		String[] projection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST};
		//String selection = MediaStore.Audio.Media._ID + " = ?";
		//String[] selectionArgs = new String[] {String.valueOf(item)};
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
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
			if (arrayPlay.size() > 0 || arraySelect.size() > 0) {
				String songTitle = null;
				if(arraySelect.size() > 0)
				{
					songTitle = arraySelect.get(count);
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
	
