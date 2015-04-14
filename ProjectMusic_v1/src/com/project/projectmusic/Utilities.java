package com.project.projectmusic;


public class Utilities {
	
	/**
	 * HÃ m chuyá»ƒn Ä‘á»•i milliseconds  Ä‘áº¿n
	 * Ä‘á»‹nh dáº¡ng Timer
	 * Giá»�:PhÃºt:GiÃ¢y
	 * */
	public String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		   // ThÃªm giá»� náº¿u cÃ³
		   if(hours > 0){
			   finalTimerString = hours + ":";
		   }
		   
		   // ThÃªm 0 náº¿u nhÆ° giÃ¢y cÃ³ thÃªm má»™t chá»¯ sá»‘
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   
		   finalTimerString = finalTimerString + minutes + ":" + secondsString;
		
		return finalTimerString;
	}
	
	/**
	 * HÃ m tÃ­nh % progress
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		// tÃ­nh pháº§n trÄƒm
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		return percentage.intValue();
	}

	/**
	 * HÃ m thay Ä‘á»•i progress Ä‘áº¿n timer
	 * @param progress - 
	 * @param totalDuration
	 * tráº£ vá»� giÃ¢y hiá»‡n hÃ nh Ä‘ang chÆ¡i
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		
		return currentDuration * 1000;
	}
}
