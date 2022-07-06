package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.*;
import org.json.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class TasksScreen extends Screen{
	
	private int selectedX = 0;
	private int selectedY = 0;
	private boolean showCompleted;
	private String selectedTask;
	
	// Selected task coords
	private int taskX = -1;
	private int taskY = -1;
	
	public TasksScreen(boolean comp){
		this.showCompleted = comp;
	}

	@Override
	public TilePane getScene(){
		TilePane layout = new TilePane();
		
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		layout.getChildren().add(canvas);
		TaskManager tm = new TaskManager();

		if (this.showCompleted){
			if (tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-1").getJSONObject("progress").getBoolean("started")){
				selectedTask = "0;1";
			} else if (tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-2").getJSONObject("progress").getBoolean("started")){
				selectedTask = "0;2";
			} else if (tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-3").getJSONObject("progress").getBoolean("started")){
				selectedTask = "0;3";
			} else if (tm.getJSON().getJSONObject("generalTasks").getJSONObject("task-1").getJSONObject("progress").getBoolean("started")){
				selectedTask = "1;1";
			} else if (tm.getJSON().getJSONObject("generalTasks").getJSONObject("task-2").getJSONObject("progress").getBoolean("started")){
				selectedTask = "1;2";
			} else if (tm.getJSON().getJSONObject("generalTasks").getJSONObject("task-3").getJSONObject("progress").getBoolean("started")){
				selectedTask = "1;3";
			} else if (tm.getJSON().getJSONObject("generalTasks").getJSONObject("task-4").getJSONObject("progress").getBoolean("started")){
				selectedTask = "1;4";
			} else if (tm.getJSON().getJSONObject("generalTasks").getJSONObject("task-5").getJSONObject("progress").getBoolean("started")){
				selectedTask = "1;5";
			}
		}
		update(gc);
		
		Timeline loop = new Timeline(new KeyFrame(Duration.millis(500), ev -> update(gc)));
		loop.setCycleCount(Animation.INDEFINITE);
		if (!this.showCompleted) loop.play();
		
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE){
				loop.stop();
				if (this.showCompleted) stopAllSounds();
				Platform.runLater(startPage);
			} else if (!this.showCompleted){
				switch (e.getCode()){
					case UP:
						selectedY--;
						playSound(SELECT_SOUND, false, 1.0, true);
						break;
					case DOWN:
						selectedY++;
						playSound(SELECT_SOUND, false, 1.0, true);
						break;
					case LEFT:
						selectedX--;
						playSound(SELECT_SOUND, false, 1.0, true);
						break;
					case RIGHT:
						selectedX++;
						playSound(SELECT_SOUND, false, 1.0, true);
						break;
					case SPACE:
						if (selectedX == 2 && selectedY == 2){
							playSound(CONFIRM_SOUND, false, null, true);
							achievementsPage.run();
							return;
						}
						if (taskX != -1 || taskY != -1) return;
						taskX = selectedX;
						taskY = selectedY;
						playSound(CONFIRM_SOUND, false, null, true);
						break;
				}
				if (selectedX > 2 || selectedX < 0) selectedX = 0;
				if (selectedY < 0 || selectedY > 2) selectedY = 0;
				/*
				if ((selectedX > 1 && selectedY == 2) || (selectedY > 1 && selectedX == 2)){
					selectedX = 0;
					selectedY = 0;
				}*/
				update(gc);
			}
		});
		
		return layout;
	}
	
	private synchronized void update(GraphicsContext gc){
		gc.save();
		gc.clearRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
		gc.setFill(Color.web("#676C69"));
		gc.fillRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
		gc.scale(xScale, yScale);
		gc.setFill(Color.BLACK);
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText(this.showCompleted ? "Your current task:" : "Those tasks must be completed in 1 round", 50, 50);
		TaskManager tm = new TaskManager();
		
		if (this.showCompleted && selectedTask != null){
			gc.save();
			gc.scale(2, 2);
			gc.setLineWidth(10);
			int type = Integer.parseInt(selectedTask.split(";")[0]);
			int number = Integer.parseInt(selectedTask.split(";")[1]);
			JSONObject task = tm.getJSON().getJSONObject(type == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+number);
			int taskID = task.getJSONObject("task").getInt("taskID");
			double progress = 0.0;
			boolean expired = false;
			if (type == 0){
				expired = System.currentTimeMillis() >= task.getJSONObject("progress").getLong("timeRerolled")+24*60*60*1000;
			}
			if (!expired){
				expired = System.currentTimeMillis() >= task.getJSONObject("progress").getLong("timeAvailable")+1*60*60*1000;
			}
			switch (taskID){
				case 0:
					if (task.getJSONObject("task").getInt("type") == 0){
						progress = (double)taskState.getJSONObject("enemies").getInt("red")/task.getJSONObject("task").getInt("enemies");
					} else {
						progress = (double)taskState.getJSONObject("enemies").getInt("green")/task.getJSONObject("task").getInt("enemies");
					}
					break;
				case 1:
					progress = (double)taskState.getInt("score")/task.getJSONObject("task").getInt("score");
					break;
				case 2:
					progress = (double)taskState.getInt("totalDamage")/task.getJSONObject("task").getInt("damage");
					break;
				case 3:
					progress = (double)taskState.getInt("totalTime")/task.getJSONObject("task").getInt("time");
					break;
				case 4:
					progress = (double)taskState.getInt("hpRecovered")/task.getJSONObject("task").getInt("hp");
					break;
				case 5:
					if (task.getJSONObject("task").getInt("option") == 0){
						progress = (double)taskState.getJSONObject("collected").getInt("guns")/task.getJSONObject("task").getInt("number");
					} else {
						progress = (double)taskState.getJSONObject("collected").getInt("bonusPoints")/task.getJSONObject("task").getInt("number");
					}
					break;
				case 6:
					progress = (double)taskState.getInt("bulletsShot")/task.getJSONObject("task").getInt("amount");
					break;
				case 7:
					progress = (double)taskState.getInt("rechargeTimes")/task.getJSONObject("task").getInt("times");
					break;
				case 8:
					progress = (double)taskState.getInt("bossesKilled")/task.getJSONObject("task").getInt("bosses");
					break;
				case 9:
					progress = taskState.getDouble("damageRatio")/task.getJSONObject("task").getInt("ratio");
					break;
			}
			
			drawTask(100, 80, 250, 150, type, number, tm, gc, -1, -1, expired);
			gc.restore();
			if (progress >= 1.0 || expired){
				int completedXP = 0;
				switch (difficulty){
					case "easy":
						completedXP = 100;
						break;
					case "medium":
						completedXP = 150;
						break;
					case "hard":
						completedXP = 200;
						break;
					case "extreme":
						completedXP = 250;
						break;
				}
				
				final int gotXP = completedXP;
				if (progress >= 1.0){
					schedule(() -> {
						gc.setFill(Color.BLACK);
						gc.setFont(Font.loadFont(MAIN_FONT, 35));
						gc.fillText("Task completed! +"+gotXP+"xp", 70, 575);
					}, 750);
				}
				task.getJSONObject("progress").put("started", false);
				task.getJSONObject("progress").put("timeAvailable", -1);
				if (type == 0){
					task.getJSONObject("progress").put("timeRerolled", System.currentTimeMillis());
				}
				tm.getJSON().getJSONObject(type == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+number).put("task", TaskManager.createTask(type == 0));
				tm.getJSON().put("xp", tm.getJSON().getInt("xp")+completedXP);
				System.out.println(task.toString(4));
				System.out.println(tm.getJSON().toString(4));
				tm.updateOnFile();
			}
			if (progress > 1.0) progress = 1.0;
			gc.setLineWidth(3);
			gc.setStroke(Color.web("#650D17"));
			gc.setFill(Color.RED);
			gc.fillRect(100, 600, 350*progress, 50);
			gc.strokeRect(100, 600, 350, 50);
			gc.setFill(Color.BLACK);
			gc.setFont(Font.loadFont(MAIN_FONT, 30));
			gc.fillText(String.format("%.2f", progress*100)+"% completed", 125, 630);
		} else if (selectedTask == null && this.showCompleted){
			gc.setFill(Color.BLACK);
			gc.setFont(Font.loadFont(MAIN_FONT, 25));
			gc.fillText("No tasks selected, select a task to get rewards!", 70, 250);
		} else {
			gc.setLineWidth(5);
			gc.setFill(Color.web("#D3F79B"));
			gc.setFont(Font.loadFont(MAIN_FONT, 20));
			
			// Draw daily tasks
			drawTask(85, 100, 250, 150, 0, 1, tm, gc, 0, 0, false);
			drawTask(385, 100, 250, 150, 0, 2, tm, gc, 1, 0, false);
			drawTask(685, 100, 250, 150, 0, 3, tm, gc, 2, 0, false);
			
			// Draw general tasks
			drawTask(85, 350, 250, 150, 1, 1, tm, gc, 0, 1, false);
			drawTask(385, 350, 250, 150, 1, 2, tm, gc, 1, 1, false);
			drawTask(685, 350, 250, 150, 1, 3, tm, gc, 2, 1, false);
			drawTask(85, 550, 250, 150, 1, 4, tm, gc, 0, 2, false);
			drawTask(385, 550, 250, 150, 1, 5, tm, gc, 1, 2, false);
		}
		
		// Draw xp
		XPDisplay xpd = new XPDisplay(tm);
		xpd.drawDefault(gc, 685, 550);
		
		gc.setFill(Color.web("#FDE4C8"));
		gc.setFont(Font.loadFont(MAIN_FONT, 30));
		gc.fillText("Press ESC to return home"+(!this.showCompleted ? " and SPACE to select" : ""), 50, 760);
		
		// Draw achievements button
		if (!this.showCompleted){
			//gc.setFill(Color.YELLOW);
			gc.setStroke(selectedX == 2 && selectedY == 2 ? Color.WHITE : Color.web("#906F03"));
			//gc.fillRect(685, 650, 200, 50);
			gc.drawImage(new javafx.scene.image.Image("file:///home/paul/Documents/Projectile_Assets/achievements.png"), 685, 650, 200, 50);
			gc.strokeRect(685, 650, 200, 50);
			
		}
		gc.restore();
	}
	
	private void drawTask(int x, int y, int w, int h, int taskType, int taskNumber, TaskManager tm, GraphicsContext gc, int relX, int relY, boolean expired){
		gc.setStroke(relX == selectedX && relY == selectedY ? Color.WHITE : Color.web("#ABCBB8"));
		
		boolean started = tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").getBoolean("started");
		if (started){
			taskX = relX;
			taskY = relY;
		}
		
		gc.strokeRect(x, y, w, h);
		String task = TaskManager.getTextForTask(tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("task"));
		double maxAmount = 15+2*(double)SCREEN_WIDTH/DEFAULT_WIDTH;
		int yOffset = 0;
		if (task.length() > maxAmount){
			StringBuilder builder = new StringBuilder();
			int counter = 0;
			for (String word : task.split(" ")){
				if (counter+word.length()+1 > maxAmount){
					builder.append("\n");
					counter = 0;
					yOffset += 20;
				} else if (counter != 0){
					builder.append(" ");
				}
				builder.append(word);
				counter += word.length()+1;
			}
			task = builder.toString();
		}
		gc.setFont(Font.loadFont(MAIN_FONT, 20));
		gc.setFill(Color.web("#D3F79B"));
		gc.fillText(task, x+20, y+h-h/5-yOffset);
		if (taskType == 0){
			long difference = System.currentTimeMillis()-tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").getLong("timeRerolled");
			difference = 3*60*60*1000-difference;
			if (difference <= 0){
				difference = 3*60*60*1000;
				tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-"+taskNumber).put("task", TaskManager.createTask(true));
				tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("timeRerolled", System.currentTimeMillis());
				tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("started", false);
				tm.getJSON().getJSONObject("dailyTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("timeAvailable", -1);
				tm.updateOnFile();
			}
			String hours = Long.toString(difference/3600000);
			String minutes = Long.toString(difference/60000%60);
			String seconds = Long.toString(difference/1000%60);
			String time = expired ? "0:0:0" : String.format("%s:%s:%s\n", hours.length() == 1 ? "0"+hours : hours, minutes.length() == 1 ? "0"+minutes : minutes, seconds.length() == 1 ? "0"+seconds : seconds);
			gc.setFill(Color.web("#E76868"));
			gc.setFont(Font.loadFont(MAIN_FONT, 23));
			gc.fillText(time, x+15, y+27);
		}
		long timeAv = tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").getLong("timeAvailable");
		if (timeAv != -1){
			long difference = System.currentTimeMillis()-timeAv;
			difference = 1*60*60*1000-difference;
			if (difference <= 0){
				taskX = -1;
				taskY = -1;
				tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("started", false);
				tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("timeAvailable", -1);
				tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).put("task", TaskManager.createTask(taskType == 0));
				tm.updateOnFile();
			}
			String minutes = Long.toString(difference/60000);
			String seconds = Long.toString(difference/1000%60);
			String time = expired ? "0:00" : String.format("%s:%s\n", minutes.length() == 1 ? "0"+minutes : minutes, seconds.length() == 1 ? "0"+seconds : seconds);
			gc.setFill(Color.BLUE);
			gc.setFont(Font.loadFont(MAIN_FONT, 23));
			gc.fillText(time, x+165, y+27);
		}
		if (taskX == relX && taskY == relY){
			gc.setStroke(Color.GREEN);
			gc.strokeRect(x, y, w, h);
			started = tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").getBoolean("started");
			if (!started){
				tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("started", true);
				tm.getJSON().getJSONObject(taskType == 0 ? "dailyTasks" : "generalTasks").getJSONObject("task-"+taskNumber).getJSONObject("progress").put("timeAvailable", System.currentTimeMillis());
				tm.updateOnFile();
			}
		}
		if (expired){
			schedule(() -> {
				gc.setFill(Color.RED);
				gc.setFont(Font.loadFont(MAIN_FONT, 60));
				gc.fillText("EXPIRED", x-10, y+h-h/2);
			}, 750);
		}
	}
}
