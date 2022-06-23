package com.orangomango.projectile.ui.profile;

import org.json.*;
import java.io.*;

public class TaskManager{
	private static final String userHome = System.getProperty("user.home");
	private JSONObject json;
	
	public TaskManager(){
		File profile = new File(userHome+File.separator+".projectile"+File.separator+"userData"+File.separator+"taskManager.json");
		String textData = null;
		if (!profile.exists()){
			try {
				profile.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
				textData = "{userName : \"Player\",xp: 0,generalTasks : {1 : {progress : [0, false, 0],task : {}},2 : {progress : [0, false, 0],task : {}},3 : {progress : [0, false, 0],task : {}}},dailyTasks : {1 : [0, false],2 : [0, false]}}";
				this.json = new JSONObject(textData);
				writer.write(this.json.toString(4));
				writer.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		} else {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(profile));
				textData = reader.readLine();
				this.json = new JSONObject(textData);
				reader.close();
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
}
