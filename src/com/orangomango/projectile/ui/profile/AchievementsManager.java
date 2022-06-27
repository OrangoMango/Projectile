package com.orangomango.projectile.ui.profile;

import java.io.*;
import org.json.*;

public class AchievementsManager{
	private String userHome = System.getProperty("user.home");
	public static final int AMOUNT = 10;
	private JSONObject json;
	
	public AchievementsManager(){
		File profile = new File(userHome+File.separator+".projectile"+File.separator+"userData"+File.separator+"achievementsManager.json");
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
	
	public static JSONObject getAchievement(int id){
		JSONObject ob = new JSONObject();
		switch (id){
			case 0:
				ob.put("name", "killer");
				ob.put("level-1", 2500);
				ob.put("level-2", 5000);
				ob.put("level-3", 10000);
				break;
			case 1:
				ob.put("name", "bonus-taker");
				ob.put("level-1", 750);
				ob.put("level-2", 1400);
				ob.put("level-3", 3000);
				break;
			case 2:
				ob.put("name", "boss-killer");
				ob.put("level-1", 1);
				ob.put("level-2", 30);
				ob.put("level-3", 70);
				break;
			case 3:
				ob.put("name", "master-killer");
				ob.put("level-1", 200);
				ob.put("level-2", 500);
				ob.put("level-3", 1000);
				break;
			case 4:
				ob.put("name", "unstoppable");
				ob.put("level-1", 18000000); //5h
				ob.put("level-2", 36000000); //10h
				ob.put("level-3", 86400000); //24h
				break;
			case 5:
				ob.put("name", "easy-score");
				ob.put("level-1", 10000);
				ob.put("level-2", 20000);
				ob.put("level-3", 50000);
				break;
			case 6:
				ob.put("name", "medium-score");
				ob.put("level-1", 8000);
				ob.put("level-2", 15000);
				ob.put("level-3", 30000);
				break;
			case 7:
				ob.put("name", "hard-score");
				ob.put("level-1", 7000);
				ob.put("level-2", 14000);
				ob.put("level-3", 25000);
				break;
			case 8:
				ob.put("name", "extreme-score");
				ob.put("level-1", 5000);
				ob.put("level-2", 10000);
				ob.put("level-3", 20000);
				break;
			case 9:
				ob.put("name", "retarded");
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
	public synchronized void updateOnFile(){
		File profile = new File(userHome+File.separator+".projectile"+File.separator+"userData"+File.separator+"achievementsManager.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
			writer.write(this.json.toString(4));
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}