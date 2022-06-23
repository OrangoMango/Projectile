package com.orangomango.projectile.ui.profile;

import java.io.*;
import org.json.*;

import com.orangomango.projectile.MainApplication;

public class ProfileManager {
	private static String home = System.getProperty("user.home");
	private JSONObject json;
	
	/**
	 * If the given directory does not exists, it will be created
	 * @param path The directory to create
	 */
	private static void checkAndCreateDir(File path){
		if (!path.exists()){
			path.mkdir();
		}
	}
	
	/**
	 * Setup the game main <code>.projectile</code> directory. The directory is located in the user's home
	 */
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

	/**
	 * Setup the json file that is contained in the game main directory
	 */
	public ProfileManager(){
		File userData = new File(home+File.separator+".projectile"+File.separator+"userData"+File.separator+"data.json");
		String textData = null;
		if (!userData.exists()){
			try {
				userData.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(userData));
				textData = "{highscore:{easy:0; medium:0; hard:0; extreme:0;}; bestTime:{easy:0; medium:0; hard:0; extreme:0;}; stats:{timePlayed:0; roundsDone:0; bossesKilled:0; enemiesKilled:0; bonusTaken: 0; bonusMissed: 0;}; input: 0; firstTimeBoss:true; tutorialComplete:false;}";
				this.json = new JSONObject(textData);
				writer.write(this.json.toString(4));
				writer.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		} else {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(userData));
				textData = reader.readLine();
				this.json = new JSONObject(textData);
				reader.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @return The json object containing player's stats
	 */
	public JSONObject getJSON(){
		return this.json;
	}
	
	/**
	 * Update file with the current {@link json}
	 */
	private void updateOnFile(){
		File userData = new File(home+File.separator+".projectile"+File.separator+"userData"+File.separator+"data.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(userData));
			writer.write(this.json.toString(4));
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Update input mode.
	 * @param value 0 for short press or 1 for long press
	 */
	public void updateInput(int value){
		this.json.put("input", value);
		updateOnFile();
	}
	
	/**
	 * Update highscore for given difficulty
	 * @param difficulty The difficulty where the highscore has been done
	 * @param value The highscore
	 */
	public void updateHighScore(String difficulty, int value){
		this.json.getJSONObject("highscore").put(difficulty, value);
		updateOnFile();
	}
	
	/**
	 * Update best time for given difficulty
	 * @param difficulty The difficulty where the best time has been done
	 * @param value The best time
	 */
	public void updateBestTime(String difficulty, int value){
		this.json.getJSONObject("bestTime").put(difficulty, value);
		updateOnFile();
	}
	
	/**
	 * Update general stats given a String key:
	 * <ul>
	 * 	<li>timePlayed</li>
	 * 	<li>roundsDone</li>
	 * 	<li>bossesKilled</li>
	 * 	<li>enemiesKilled</li>
	 * 	<li>bonusTaken</li>
	 * 	<li>bonusMissed</li>
	 * </ul>
	 * @param key The key that contains the current stats
	 * @param value The value associated to the key
	 */
	public void updateStats(String key, int value){
		this.json.getJSONObject("stats").put(key, this.json.getJSONObject("stats").getInt(key)+value);
		updateOnFile();
	}
	
	/**
	 * Update the firstTimeBoss flag.
	 * @param value true if a boss has never been killed, false otherwise
	 */
	public void setFirstTimeBoss(boolean value){
		this.json.put("firstTimeBoss", value);
		updateOnFile();
	}
	
	/**
	 * Update the tutorialComplete flag.
	 * @param value true if the tutorial has been completed, false otherwise
	 */
	public void setTutorialComplete(boolean value){
		this.json.put("tutorialComplete", value);
		updateOnFile();
	}
}
