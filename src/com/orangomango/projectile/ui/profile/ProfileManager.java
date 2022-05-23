package com.orangomango.projectile.ui.profile;

import java.io.*;
import org.json.*;

import com.orangomango.projectile.MainApplication;

public class ProfileManager {
	private static String home = System.getProperty("user.home");
	private JSONObject json;
	
	private static void checkAndCreateDir(File path){
		if (!path.exists()){
			path.mkdir();
		}
	}
	
	public static void setupDirectory(){
		checkAndCreateDir(new File(home+File.separator+".projectile"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"userData"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"userData"+File.separator+"customGuns"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"assets"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"assets"+File.separator+"audio"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"assets"+File.separator+"font"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"assets"+File.separator+"image"));
		checkAndCreateDir(new File(home+File.separator+".projectile"+File.separator+"assets"+File.separator+"gun"));
	}

	public ProfileManager(){
		File userData = new File(home+File.separator+".projectile"+File.separator+"userData"+File.separator+"data.json");
		String textData = null;
		if (!userData.exists()){
			try {
				userData.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(userData));
				textData = "{highscore:{easy:0; medium:0; hard:0; extreme:0;}; bestTime:{easy:0; medium:0; hard:0; extreme:0;}; stats:{timePlayed:0; roundsDone:0; bossesKilled:0; enemiesKilled:0; bonusTaken: 0;}; input: 0; firstTimeBoss:true; tutorialComplete:false;}";
				writer.write(textData);
				writer.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		} else {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(userData));
				textData = reader.readLine();
				reader.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
		this.json = new JSONObject(textData);
	}
	
	public JSONObject getJSON(){
		return this.json;
	}
	
	private void updateOnFile(){
		File userData = new File(home+File.separator+".projectile"+File.separator+"userData"+File.separator+"data.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(userData));
			writer.write(this.json.toString());
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void updateInput(int value){
		this.json.put("input", value);
		updateOnFile();
	}
	
	public void updateHighScore(String difficulty, int value){
		this.json.getJSONObject("highscore").put(difficulty, value);
		updateOnFile();
	}
	
	public void updateBestTime(String difficulty, int value){
		this.json.getJSONObject("bestTime").put(difficulty, value);
		updateOnFile();
	}
	
	public void updateStats(String key, int value){
		this.json.getJSONObject("stats").put(key, this.json.getJSONObject("stats").getInt(key)+value);
		updateOnFile();
	}
	
	public void setFirstTimeBoss(boolean value){
		this.json.put("firstTimeBoss", value);
		updateOnFile();
	}
	
	public void setTutorialComplete(boolean value){
		this.json.put("tutorialComplete", value);
		updateOnFile();
	}
}
