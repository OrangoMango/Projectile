package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class RecordsScreen extends Screen{

	@Override
	public Scene getScene(){
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE){
				Platform.runLater(startPage);
			}
		});
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.web("#676C69"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		ProfileManager pm = new ProfileManager();
		gc.setStroke(Color.web("#ABCBB8"));
		gc.setLineWidth(5);
		
		
		// Easy
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(210, 65, 250, 280);
		gc.fillText("EASY", 240, 125);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 225, 160);
		gc.fillText("Best time", 225, 250);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("easy")), 225, 200);
		int time = pm.getJSON().getJSONObject("bestTime").getInt("easy");
		gc.fillText(String.format("%s:%s:%s", time/60000, time/1000%60, time%1000), 225, 290);
		
		// Medium
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(540, 65, 250, 280);
		gc.fillText("MEDIUM", 570, 125);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 555, 160);
		gc.fillText("Best time", 555, 250);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("medium")), 555, 200);
		int time2 = pm.getJSON().getJSONObject("bestTime").getInt("medium");
		gc.fillText(String.format("%s:%s:%s", time2/60000, time2/1000%60, time2%1000), 555, 290);
		
		// Hard
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(210, 400, 250, 280);
		gc.fillText("HARD", 250, 460);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 225, 495);
		gc.fillText("Best time", 225, 585);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("hard")), 225, 535);
		int time3 = pm.getJSON().getJSONObject("bestTime").getInt("hard");
		gc.fillText(String.format("%s:%s:%s", time3/60000, time3/1000%60, time3%1000), 225, 625);
		
		// Extreme
		gc.setFill(Color.WHITE);
		gc.setFont(Font.loadFont(MAIN_FONT, 45));
		gc.strokeRect(540, 400, 250, 280);
		gc.fillText("EXTREME", 555, 460);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Best score", 555, 495);
		gc.fillText("Best time", 555, 585);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(pm.getJSON().getJSONObject("highscore").getInt("extreme")), 555, 535);
		int time4 = pm.getJSON().getJSONObject("bestTime").getInt("extreme");
		gc.fillText(String.format("%s:%s:%s", time4/60000, time4/1000%60, time4%1000), 555, 625);
		
		gc.setFill(Color.web("#FDE4C8"));
		gc.fillText("Press SPACE to go back", 50, SCREEN_HEIGHT-40);

		layout.getChildren().add(canvas);
		return new Scene(layout, SCREEN_WIDTH, SCREEN_HEIGHT);
	}
}
