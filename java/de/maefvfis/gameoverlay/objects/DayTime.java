package de.maefvfis.gameoverlay.objects;


public class DayTime {
	public static String getTimeString(Long ltime) {
		
		String returnStunden;
		String returnMinuten;
		
		int Stunden = (int) (((ltime + 6000) % 24000) / 1000);
		int Minuten = (int) ((((ltime + 6000) % 24000) % 1000) * 6 / 100);
		
		returnStunden = "0" + String.valueOf(Stunden);
		returnStunden = returnStunden.substring(returnStunden.length() - 2, returnStunden.length());
		
		returnMinuten = "0" + String.valueOf(Minuten);
		returnMinuten = returnMinuten.substring(returnMinuten.length() - 2, returnMinuten.length());
		
		
		return returnStunden + ":" + returnMinuten;
		
	}
	
	public static boolean getDaytime(Long ltime) {
	        
        int Stunden = (int) (((ltime + 6000) % 24000) / 1000);
        if ((Stunden >= 19) || (Stunden < 6)) {
            return false;
        } else {
            return true;
        }
	}
	
	public static int getDayLightValue(Long ltime) {
		int value = 0;
		int h = (int) (((ltime + 6000) % 24000) / 1000);
		if(h < 7 || h > 21) { return 0; }
		if(h > 9 && h < 18) { return 15; }
		if(h < 10) { return (h-6)*3; }
		if(h > 17) { return 15 - ((h-17) * 3); }
		return 15; 
	}
	
	
}