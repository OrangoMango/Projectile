package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.AchievementsManager;

public class AchievementsScreen extends Screen{
	@Override
	public TilePane getScene(){
		TilePane layout = new TilePane();
		
		AchievementsManager am = new AchievementsManager();
		
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		layout.getChildren().add(canvas);
		
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.SPACE){
				Platform.runLater(tasksPage);
			}
		});
		
		gc.setFill(Color.web("#676C69"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		gc.scale((double)SCREEN_WIDTH/DEFAULT_WIDTH, (double)SCREEN_HEIGHT/DEFAULT_HEIGHT);
		
		final int width = 250;
		final int height = 75;
		final int paddingX = 75;
		final int paddingY = 50;
		final int spacingX = 55;
		final int spacingY = 55;
		gc.setLineWidth(5);
		gc.setFill(Color.web("#D3F79B"));
		gc.setFont(Font.loadFont(MAIN_FONT, 20));
		for (int i = 0; i < AchievementsManager.AMOUNT; i++){
			gc.setStroke(Color.web("#ABCBB8"));
			int x = i%3*(width+spacingX)+paddingX;
			int y = i/3*(height+spacingY)+paddingY;
			gc.strokeRect(x, y, width, height);
			gc.fillText(AchievementsManager.getAchievement(i).getString("name").replace("-", " "), x+15, y+25);
		}
		
		gc.setFill(Color.web("#FDE4C8"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Press SPACE to go back", 50, 760);
		
		return layout;
	}
}
