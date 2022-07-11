package com.orangomango.projectile.ui.profile;

import java.io.*;
import org.json.*;

import com.orangomango.projectile.MainApplication;

public class AchievementsManager{
	public static final int AMOUNT = 10;
	private JSONObject json;
	
	public AchievementsManager(){
		File profile = new File(MainApplication.prefixPath+File.separator+"userData"+File.separator+"achievementsManager.json");
		String textData = null;
		if (!profile.exists()){
			try {
				profile.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
				textData = "{\"id-0\":0,\"id-1\":0,\"id-2\":0,\"id-3\":0,\"id-4\":0,\"id-5\":0,\"id-6\":0,\"id-7\":0,\"id-8\":0,\"id-9\":0}";
				this.json = new JSONObject(textData);
				writer.write(this.json.toString(4));
				writer.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		} else {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(profile));
				StringBuilder builder = new StringBuilder();
				reader.lines().forEach(line -> builder.append(line).append("\n"));
				textData = builder.toString();
				this.json = new JSONObject(textData);
				reader.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	public synchronized int incrementPoints(int id, long value){
		int lvl = this.getLevel(id);
		this.json.put("id-"+id, this.json.getLong("id-"+id)+value);
		int newlvl = this.getLevel(id);
		this.updateOnFile();
		return newlvl-lvl;
	}
	
	public int getLevel(int id){
		long value = this.json.getLong("id-"+id);
		int lvl = 0;
		if (value < getAchievement(id).getLong("level-1")){
			lvl = 0;
		} else if (value < getAchievement(id).getLong("level-2")){
			lvl = 1;
		} else if (value < getAchievement(id).getLong("level-3")){
			lvl = 2;
		} else {
			lvl = 3;
		}
		return lvl;
	}
	
	public static JSONObject getAchievement(int id){
		JSONObject ob = new JSONObject();
		switch (id){
			case 0:
				ob.put("name", "killer");
				ob.put("description", "Kill %s enemies");
				ob.put("level-1", 5000);
				ob.put("level-2", 10000);
				ob.put("level-3", 20000);
				break;
			case 1:
				ob.put("name", "bonus-taker");
				ob.put("description", "Get %s bonuspoints");
				ob.put("level-1", 750);
				ob.put("level-2", 1400);
				ob.put("level-3", 3000);
				break;
			case 2:
				ob.put("name", "boss-killer");
				ob.put("description", "Kill %s bosses");
				ob.put("level-1", 1);
				ob.put("level-2", 30);
				ob.put("level-3", 70);
				break;
			case 3:
				ob.put("name", "master-killer");
				ob.put("description", "Kill %s green enemies");
				ob.put("level-1", 200);
				ob.put("level-2", 500);
				ob.put("level-3", 1000);
				break;
			case 4:
				ob.put("name", "unstoppable");
				ob.put("description", "Play this game %s hours");
				ob.put("level-1", 18000000); //5h
				ob.put("level-2", 36000000); //10h
				ob.put("level-3", 86400000); //24h
				break;
			case 5:
				ob.put("name", "easy-score");
				ob.put("description", "Make %s score in easy mode");
				ob.put("level-1", 10000);
				ob.put("level-2", 20000);
				ob.put("level-3", 50000);
				break;
			case 6:
				ob.put("name", "medium-score");
				ob.put("description", "Make %s score in medium mode");
				ob.put("level-1", 8000);
				ob.put("level-2", 15000);
				ob.put("level-3", 30000);
				break;
			case 7:
				ob.put("name", "hard-score");
				ob.put("description", "Make %s score in hard mode");
				ob.put("level-1", 7000);
				ob.put("level-2", 14000);
				ob.put("level-3", 25000);
				break;
			case 8:
				ob.put("name", "extreme-score");
				ob.put("description", "Make %s score in extreme mode");
				ob.put("level-1", 5000);
				ob.put("level-2", 10000);
				ob.put("level-3", 20000);
				break;
			case 9:
				ob.put("name", "retarded");
				ob.put("description", "Miss %s bonus points");
				ob.put("level-1", 60);
				ob.put("level-2", 120);
				ob.put("level-3", 250);
				break;
			default:
				return null;
		}
		return ob;
	}
	
	/**
	 * @return The json object containing player's achievements
	 */
	public JSONObject getJSON(){
		return this.json;
	}
	
	/**
	 * Update file with the current {@link json}
	 */
	private void updateOnFile(){
		File profile = new File(MainApplication.prefixPath+File.separator+"userData"+File.separator+"achievementsManager.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
			writer.write(this.json.toString(4));
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}
