package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import java.text.DecimalFormat;
import java.util.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;
import com.orangomango.projectile.ui.profile.Logger;

public class GameoverScreen extends Screen{
	private volatile String displayText = "";
	private volatile String xpText = "";
	private volatile int xpTotal = 0;
	private volatile boolean displayFinished;
	private static boolean newHighscore, newTime;
	
	public GameoverScreen(){
		int score = userGamedata.get("score").intValue();
		int timePlayed = userGamedata.get("gameTime").intValue();
		ProfileManager pm = new ProfileManager();
		int savedScore = pm.getJSON().getJSONObject("highscore").getInt(difficulty);
		int savedTime = pm.getJSON().getJSONObject("bestTime").getInt(difficulty);
		if (score > savedScore){
			pm.updateHighScore(difficulty, score);
			newHighscore = true;
		} else {
			newHighscore = false;
		}
		if (timePlayed > savedTime){
			newTime = true;
		} else {
			newTime = false;
		}
		if (timePlayed > savedTime || savedTime == 0){
			pm.updateBestTime(difficulty, timePlayed);
		}
		pm.updateStats("timePlayed", timePlayed);
		pm.updateStats("roundsDone", 1);
		pm.updateStats("bossesKilled", userGamedata.getOrDefault("bosses", 0.0).intValue());
		pm.updateStats("enemiesKilled", userGamedata.getOrDefault("enemies", 0.0).intValue());
		pm.updateStats("bonusTaken", userGamedata.getOrDefault("bonusPoints", 0.0).intValue());
		pm.updateStats("bonusMissed", userGamedata.getOrDefault("bonusMissed", 0.0).intValue());
	}
	
	@Override
	public TilePane getScene(){
		playSound(GAMEOVER_BACKGROUND_SOUND, true, 0.65, false);
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE && displayFinished){
				Platform.runLater(taskEndPage);
			}
		});
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		drawCanvas(gc);
		TaskManager tm = new TaskManager();
		
		schedule(() -> {
			int counter = 0;
			for (String text : getStringData().split("\n")){
				displayText += text+"\n";
				int xp = getXPData()[counter++];
				xpTotal += xp;
				xpText += xp+"xp\n";
				tm.getJSON().put("xp", tm.getJSON().getInt("xp")+xp);
				drawCanvas(gc);
				playSound(SHOW_SOUND, false, null, false);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
			tm.updateOnFile();
			displayFinished = true;
			drawCanvas(gc);
			playSound(SHOW_SOUND, false, null, false);
		}, 1200);
		
		Logger.info("Drawing gameover screen: "+SCREEN_WIDTH+"x"+SCREEN_HEIGHT);
		return new TilePane(canvas);
	}
	
	private void drawCanvas(GraphicsContext gc){
		gc.setFill(Color.web("#929292"));
		gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.save();
		//gc.translate((SCREEN_WIDTH-1000)/2, (SCREEN_HEIGHT-800)/2);
		gc.scale((double)SCREEN_WIDTH/DEFAULT_WIDTH, (double)SCREEN_HEIGHT/DEFAULT_HEIGHT);
		gc.setFill(Color.RED);
		gc.setFont(Font.loadFont(MAIN_FONT, 75));
		gc.fillText("GAME OVER ("+difficulty+")", 50, 120);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(5);
		gc.strokeRect(150, 200, 710, 450);
		gc.setFill(Color.web("#E7D6D6"));
		gc.setFont(Font.loadFont(MAIN_FONT, 40));
		gc.fillText(displayText, 160, 250);
		gc.setFill(Color.web("#384ACA"));
		gc.setFont(Font.loadFont(MAIN_FONT, 40));
		gc.fillText(xpText+"\nTot:"+xpTotal+"xp", 20, 250);
		if (displayFinished){
			gc.setFill(Color.web("#1B8344"));
			gc.setFont(Font.loadFont(MAIN_FONT, 33));
			gc.fillText("Press SPACE to continue", 150, 750);
		}
		gc.restore();
	}
	
	private static String getStringData(){
		StringBuilder builder = new StringBuilder();
		builder.append("Score: ").append(userGamedata.get("score").intValue());
		if (newHighscore){
			builder.append(" (Highscore!)");
		}
		builder.append("\n");
		builder.append("Enemies killed: ").append(userGamedata.getOrDefault("enemies", 0.0).intValue()).append("\n");
		int timePlayed = userGamedata.get("gameTime").intValue();
		builder.append("Time played: ").append(String.format("%smin %ssec", timePlayed/60000, timePlayed/1000%60));
		if (newTime){
			builder.append(" (Best)");
		}
		builder.append("\n");
		builder.append("Aim ratio: ").append(new DecimalFormat("##.####").format(userGamedata.get("damageRatio"))).append("\n");
		builder.append("Bosses killed: ").append(userGamedata.getOrDefault("bosses", 0.0).intValue()).append("\n");
		builder.append("Bonus points: ").append(userGamedata.getOrDefault("bonusPoints", 0.0).intValue()).append("\n");
		builder.append("Bonus missed: ").append(userGamedata.getOrDefault("bonusMissed", 0.0).intValue()).append("\n");
		builder.append("Grenades shot: ").append(userGamedata.getOrDefault("explosions", 0.0).intValue()).append("\n");
		builder.append("HP Recharged times: ").append(userGamedata.getOrDefault("recharges", 0.0).intValue());
		return builder.toString();
	}
	
	private static int[] getXPData(){
		/*
		 * XP:
		 * 10xp/100score
		 * 1xp/enemy killed
		 * 1xp/3sec played
		 * 5xp if aim ratio > 0.7
		 * 30xp/boss killed
		 * 10xp/bonus point taken
		 * -10xp/bonus point missed
		 * 1xp/grenade shot
		 * 2xp/hp recharged
		 */
		int[] xp = new int[9];
		xp[0] = userGamedata.get("score").intValue()/10;
		xp[1] = userGamedata.getOrDefault("enemies", 0.0).intValue()*1;
		xp[2] = userGamedata.get("gameTime").intValue()/3000;
		xp[3] = userGamedata.get("damageRatio") > 0.7 ? 5 : 0;
		xp[4] = userGamedata.getOrDefault("bosses", 0.0).intValue()*30;
		xp[5] = userGamedata.getOrDefault("bonusPoints", 0.0).intValue()*10;
		xp[6] = userGamedata.getOrDefault("bonusMissed", 0.0).intValue()*(-10);
		xp[7] = userGamedata.getOrDefault("explosions", 0.0).intValue();
		xp[8] = userGamedata.getOrDefault("recharges", 0.0).intValue()*2;
		
		for (int i = 0; i < xp.length; i++){
			switch (difficulty){
				case "easy":
					xp[i] *= 1;
					break;
				case "medium":
					xp[i] = (int)Math.round(xp[i]*1.2);
					break;
				case "hard":
					xp[i] = (int)Math.round(xp[i]*1.3);
					break;
				case "extreme":
					xp[i] = (int)Math.round(xp[i]*1.5);
					break;
			}
		}
		
		return xp;
	}
}
