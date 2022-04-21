package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import java.text.DecimalFormat;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class GameoverScreen extends Screen{
	private volatile String displayText = "";
	private volatile boolean displayFinished;
	
	public GameoverScreen(){
		int score = userGamedata.get("score").intValue();
		int timePlayed = userGamedata.get("gameTime").intValue();
		ProfileManager pm = new ProfileManager();
		int savedScore = pm.getJSON().getJSONObject("highscore").getInt("easy");
		int savedTime = pm.getJSON().getJSONObject("bestTime").getInt("easy");
		if (score > savedScore){
			pm.updateHighScore("easy", score);
		}
		if (timePlayed > savedTime || savedTime == 0){
			pm.updateBestTime("easy", timePlayed);
		}
	}
	
	@Override
	public Scene getScene(){
		
		System.out.println(userGamedata);
		
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE && displayFinished){
				Platform.runLater(startPage);
			}
		});
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		drawCanvas(gc);
		
		new Thread(() -> {
			for (String text : getStringData().split("\n")){
				displayText += text+"\n";
				Platform.runLater(() -> drawCanvas(gc));
				playSound(SHOW_SOUND, false, null, false);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
			displayFinished = true;
			Platform.runLater(() -> drawCanvas(gc));
			playSound(SHOW_SOUND, false, null, false);
		}).start();
		
		return new Scene(new TilePane(canvas), SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	private void drawCanvas(GraphicsContext gc){
		gc.setFill(Color.web("#929292"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.setFill(Color.RED);
		gc.setFont(Font.loadFont(MAIN_FONT, 75));
		gc.fillText("GAME OVER", 100, 120);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(5);
		gc.strokeRect(150, 200, 670, 400);
		gc.setFill(Color.web("#E7D6D6"));
		gc.setFont(Font.loadFont(MAIN_FONT, 40));
		gc.fillText(displayText, 160, 250);
		if (displayFinished){
			gc.setFill(Color.web("#1B8344"));
			gc.setFont(Font.loadFont(MAIN_FONT, 33));
			gc.fillText("Press SPACE to continue", 150, 670);
		}
	}
	
	private static String getStringData(){
		StringBuilder builder = new StringBuilder();
		builder.append("Score: ").append(userGamedata.get("score").intValue()).append("\n");
		builder.append("Enemies killed: ").append(userGamedata.getOrDefault("enemies", 0.0).intValue()).append("\n");
		int timePlayed = userGamedata.get("gameTime").intValue();
		builder.append("Time played: ").append(String.format("%smin %ssec", timePlayed/60000, timePlayed/1000%60)).append("\n");
		builder.append("Aim ratio: ").append(new DecimalFormat("##.####").format(userGamedata.get("damageRatio"))).append("\n");
		builder.append("Bosses killed: ").append(userGamedata.getOrDefault("bosses", 0.0).intValue()).append("\n");
		builder.append("Bonus points: ").append(userGamedata.getOrDefault("bonusPoints", 0.0).intValue()).append("\n");
		builder.append("Grenades launched: ").append(userGamedata.getOrDefault("explosions", 0.0).intValue()).append("\n");
		builder.append("HP Recharged times: ").append(userGamedata.getOrDefault("recharges", 0.0).intValue());
		return builder.toString();
	}
}
