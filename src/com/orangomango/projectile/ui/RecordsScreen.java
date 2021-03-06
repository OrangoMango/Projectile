package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class RecordsScreen extends Screen{

	@Override
	public TilePane getScene(){
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(RENDER_WIDTH, RENDER_HEIGHT);
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ESCAPE){
				Platform.runLater(startPage);
			}
		});
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.web("#676C69"));
		gc.fillRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
		
		gc.scale(xScale, yScale);
		
		ProfileManager pm = new ProfileManager();
		gc.setStroke(Color.web("#ABCBB8"));
		gc.setLineWidth(5);
		
		// Every rectangle is translated by 160 px to the left in order to make space for the general stats.
		
		// Easy
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(210-160, 65, 250, 280);
		gc.fillText("EASY", 240-160, 125);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 225-160, 160);
		gc.fillText("Best time", 225-160, 250);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("easy")), 225-160, 200);
		int time = pm.getJSON().getJSONObject("bestTime").getInt("easy");
		gc.fillText(String.format("%s:%s.%s", time/60000, time/1000%60, time%1000), 225-160, 290);
		
		// Medium
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(540-200, 65, 250, 280);
		gc.fillText("MEDIUM", 570-200, 125);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 555-200, 160);
		gc.fillText("Best time", 555-200, 250);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("medium")), 555-200, 200);
		int time2 = pm.getJSON().getJSONObject("bestTime").getInt("medium");
		gc.fillText(String.format("%s:%s.%s", time2/60000, time2/1000%60, time2%1000), 555-200, 290);
		
		// Hard
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(210-160, 400, 250, 280);
		gc.fillText("HARD", 250-160, 460);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 225-160, 495);
		gc.fillText("Best time", 225-160, 585);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("hard")), 225-160, 535);
		int time3 = pm.getJSON().getJSONObject("bestTime").getInt("hard");
		gc.fillText(String.format("%s:%s.%s", time3/60000, time3/1000%60, time3%1000), 225-160, 625);
		
		// Extreme
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(540-200, 400, 250, 280);
		gc.fillText("EXTREME", 555-200, 460);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 555-200, 495);
		gc.fillText("Best time", 555-200, 585);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("extreme")), 555-200, 535);
		int time4 = pm.getJSON().getJSONObject("bestTime").getInt("extreme");
		gc.fillText(String.format("%s:%s.%s", time4/60000, time4/1000%60, time4%1000), 555-200, 625);
		
		// General stats
		gc.strokeRect(630, 65, 330, 615);
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.fillText("STATS", 645, 125);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Time played", 645, 160);
		gc.fillText("Rounds done", 645, 250);
		gc.fillText("Bosses killed", 645, 340);
		gc.fillText("Enemies killed", 645, 430);
		gc.fillText("Bonus taken", 645, 520);
		gc.fillText("Bonus missed", 645, 610);
		gc.setFill(Color.RED);
		int totalTime = pm.getJSON().getJSONObject("stats").getInt("timePlayed");
		gc.fillText(String.format("%sh %sm %ss", totalTime/3600000, totalTime/60000%60, totalTime/1000%60), 645, 200);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("stats").getInt("roundsDone")), 645, 290);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("stats").getInt("bossesKilled")), 645, 380);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("stats").getInt("enemiesKilled")), 645, 470);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("stats").getInt("bonusTaken")), 645, 560);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("stats").getInt("bonusMissed")), 645, 650);
		
		gc.setFill(Color.web("#FDE4C8"));
		gc.fillText("Press SPACE to go back", 50, 760);

		layout.getChildren().add(canvas);
		return layout;
	}
}
