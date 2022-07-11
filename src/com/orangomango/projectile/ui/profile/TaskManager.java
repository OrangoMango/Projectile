package com.orangomango.projectile.ui.profile;

import org.json.*;
import java.io.*;
import java.util.Random;

import com.orangomango.projectile.MainApplication;

public class TaskManager{
	private JSONObject json;
	
	public TaskManager(){
		File profile = new File(MainApplication.prefixPath+File.separator+"userData"+File.separator+"taskManager.json");
		String textData = null;
		if (!profile.exists()){
			try {
				profile.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
				textData = "{\"dailyTasks\": {\"task-2\": {\"task\": {},\"progress\": {\"timeRerolled\": -1,\"timeAvailable\": -1,\"started\": false}},\"task-3\": {\"task\": {},\"progress\": {\"timeRerolled\": -1,\"timeAvailable\": -1,\"started\": false}},\"task-1\": {\"task\": {},\"progress\": {\"timeRerolled\": -1,\"timeAvailable\": -1,\"started\": false}}},\"xp\": 0,\"generalTasks\": {\"task-4\": {\"task\": {},\"progress\": {\"timeAvailable\": -1,\"started\": false}},\"task-5\": {\"task\": {},\"progress\": {\"timeAvailable\": -1,\"started\": false}},\"task-2\": {\"task\": {},\"progress\": {\"timeAvailable\": -1,\"started\": false}},\"task-3\": {\"task\": {},\"progress\": {\"timeAvailable\": -1,\"started\": false}},\"task-1\": {\"task\": {},\"progress\": {\"timeAvailable\": -1,\"started\": false}}},\"userName\": \"Player\"}";
				this.json = new JSONObject(textData);
				fillTasks();
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
	
	private void fillTasks(){
		this.json.getJSONObject("dailyTasks").getJSONObject("task-1").put("task", createTask(true));
		this.json.getJSONObject("dailyTasks").getJSONObject("task-2").put("task", createTask(true));
		this.json.getJSONObject("dailyTasks").getJSONObject("task-3").put("task", createTask(true));
		this.json.getJSONObject("dailyTasks").getJSONObject("task-1").getJSONObject("progress").put("timeRerolled", System.currentTimeMillis());
		this.json.getJSONObject("dailyTasks").getJSONObject("task-2").getJSONObject("progress").put("timeRerolled", System.currentTimeMillis());
		this.json.getJSONObject("dailyTasks").getJSONObject("task-3").getJSONObject("progress").put("timeRerolled", System.currentTimeMillis());
		this.json.getJSONObject("generalTasks").getJSONObject("task-1").put("task", createTask(false));
		this.json.getJSONObject("generalTasks").getJSONObject("task-2").put("task", createTask(false));
		this.json.getJSONObject("generalTasks").getJSONObject("task-3").put("task", createTask(false));
		this.json.getJSONObject("generalTasks").getJSONObject("task-4").put("task", createTask(false));
		this.json.getJSONObject("generalTasks").getJSONObject("task-5").put("task", createTask(false));
	}

	/**
	 * @return The json object containing player's tasks
	 */
	public JSONObject getJSON(){
		return this.json;
	}
	
	/**
	 * Update file with the current {@link json}
	 */
	public synchronized void updateOnFile(){
		File profile = new File(MainApplication.prefixPath+File.separator+"userData"+File.separator+"taskManager.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(profile));
			writer.write(this.json.toString(4));
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static JSONObject createTask(boolean daily){
		/*
		 * 20% Kill x enemies (y type)
		 * 20% Get x score
		 * 13% Deal x damage
		 * 12% Survive x time
		 * 8% Recover hp x times
		 * 8% Collect x guns/bonuspoints
		 * 8% Shoot x bullets
		 * 7% Recharge ammo x times
		 * 4% Kill x bosses
		 * ----EXTRA-----
		 * 15% Get at least x aim ratio
		*/
		Random random = new Random();
		JSONObject task = new JSONObject();
		final double factor = 0.75;
		int chance = random.nextInt(115);
		if (chance < 20){
			int enemies = random.nextInt(50-30)+30;
			int type = 0; // 0 -> red, 1 -> green
			if (random.nextInt(5) <= 1){
				type = 1;
			}
			task.put("taskID", 0);
			task.put("enemies", daily ? (int)Math.round(enemies*factor) : enemies);
			task.put("type", type);
		} else if (chance >= 20 && chance < 40){
			int score = random.nextInt(1200-800)+800;
			task.put("taskID", 1);
			task.put("score", daily ? (int)Math.round(score*factor) : score);
		} else if (chance >= 40 && chance < 53){
			int damage = random.nextInt(500-400)+400;
			task.put("taskID", 2);
			task.put("damage", daily ? (int)Math.round(damage*factor) : damage);
		} else if (chance >= 53 && chance < 65){
			int time = random.nextInt(100-70)+70;
			task.put("taskID", 3);
			task.put("time", daily ? (int)Math.round(time*factor) : time);
		} else if (chance >= 65 && chance < 73){
			int hp = random.nextInt(5-3)+3;
			task.put("taskID", 4);
			task.put("hp", daily ? (int)Math.round(hp*factor) : hp);
		} else if (chance >= 73 && chance < 81){
			int number = random.nextInt(16-10)+10;
			int option = random.nextInt(2);
			task.put("taskID", 5);
			task.put("number", daily ? (int)Math.round(number*factor) : number);
			task.put("option", option); // 0 -> guns and 1 -> bonusPoints
		} else if (chance >= 81 && chance < 89){
			int bullets = random.nextInt(200-120)+120;
			task.put("taskID", 6);
			task.put("amount", daily ? (int)Math.round(bullets*factor) : bullets);
		} else if (chance >= 89 && chance < 96){
			int times = random.nextInt(11-5)+5;
			task.put("taskID", 7);
			task.put("times", daily ? (int)Math.round(times*factor) : times);
		} else if (chance >= 96 && chance < 100){
			int bosses = random.nextInt(2)+1;
			task.put("taskID", 8);
			task.put("bosses", daily ? (int)Math.round(bosses*factor) : bosses);
		} else {
			double ratio = random.nextDouble()*(1.0-0.6)+0.6;
			task.put("taskID", 9);
			task.put("ratio", daily ? ratio-0.3 : ratio);
		}
		
		return task;
	}
	
	public static String getTextForTask(JSONObject task){
		switch (task.getInt("taskID")){
			case 0:
				return String.format("Kill %s %s enemies", task.getInt("enemies"), task.getInt("type") == 0 ? "red" : "green");
			case 1:
				return String.format("Get %s score", task.getInt("score"));
			case 2:
				return String.format("Deal %s damage", task.getInt("damage"));
			case 3:
				return String.format("Survive %s seconds", task.getInt("time"));
			case 4:
				return String.format("Recover hp %s times", task.getInt("hp"));
			case 5:
				return String.format("Collect %s %s", task.getInt("number"), task.getInt("option") == 0 ? "guns" : "bonus points");
			case 6:
				return String.format("Shoot %s bullets", task.getInt("amount"));
			case 7:
				return String.format("Recharge ammo %s times", task.getInt("times"));
			case 8:
				return String.format("Defeat %s bosses", task.getInt("bosses"));
			case 9:
				return String.format("Get at least %.2f aim ratio", task.getDouble("ratio"));
		}
		return null;
	}
}
