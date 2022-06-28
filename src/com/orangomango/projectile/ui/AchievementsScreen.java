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
	
	public static class AchievementNotification{
		public boolean mustShow = false;
		public int id = 0;
		private int y = 0;
		private GraphicsContext gc;
		private AchievementsManager am;
		private boolean scheduled;
		
		public AchievementNotification(GraphicsContext gc, AchievementsManager am){
			this.gc = gc;
			this.am = am;
		}
		
		public void show(){
			if (!this.mustShow) return;
			this.y += 1;
			gc.setFill(Color.YELLOW);
			gc.fillRect(400, 740-this.y, 250, 50);
			gc.setFill(Color.BLACK);
			gc.setFont(Font.loadFont(MAIN_FONT, 22));
			gc.fillText(AchievementsManager.getAchievement(this.id).getString("name").replace("-", " ")+"("+this.am.getLevel(this.id)+")", 420, 760-this.y);
			if (!scheduled){
				schedule(() -> {
					this.mustShow = false;
					this.y = 0;
					this.scheduled = false;
				}, 2000);
				scheduled = true;
			}
		}
	}
	
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
		final int height = 130;
		final int paddingX = 75;
		final int paddingY = 50;
		final int spacingX = 55;
		final int spacingY = 45;
		for (int i = 0; i < AchievementsManager.AMOUNT; i++){
			gc.setStroke(Color.web("#ABCBB8"));
			gc.setLineWidth(5);
			int x = i%3*(width+spacingX)+paddingX;
			int y = i/3*(height+spacingY)+paddingY;
			gc.strokeRect(x, y, width, height);
			int lvl = am.getLevel(i);
			int value = AchievementsManager.getAchievement(i).getInt("level-"+(lvl == 3 ? 3 : lvl+1));
			gc.setFont(Font.loadFont(MAIN_FONT, 20));
			gc.setFill(Color.web("#D3F79B"));
			long p1 = i == 4 ? am.getJSON().getLong("id-"+i)/(1000*60*60) : am.getJSON().getLong("id-"+i);
			int p2 = i == 4 ? value/(1000*60*60) : value;
			gc.fillText(AchievementsManager.getAchievement(i).getString("name").replace("-", " ")+"("+lvl+")"+String.format("\n(%s/%s)", p1, p2), x+15, y+25);
			gc.setFont(Font.loadFont(MAIN_FONT, 15));
			gc.setFill(Color.web("#B6C998"));
			String desc = AchievementsManager.getAchievement(i).getString("description");
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < desc.length(); j++){
				builder.append(Character.toString(desc.charAt(j)));
				if (j % (18+2*(double)SCREEN_WIDTH/DEFAULT_WIDTH) == 0 && j != 0 && j != desc.length()-1) builder.append("-\n");
			}
			desc = builder.toString();
			gc.fillText(String.format(desc, i == 4 ? value/(1000*60*60) : value), x+15, y+65);
			
			// Draw progress bar
			gc.setLineWidth(2);
			gc.setFill(Color.web("#7DE002"));
			gc.fillRect(x+140, y+110, 100*((double)p1/p2), 10);
			gc.setStroke(Color.web("#375B0B"));
			gc.strokeRect(x+140, y+110, 100, 10);
		}
		
		gc.setFill(Color.web("#FDE4C8"));
		gc.setFont(Font.loadFont(MAIN_FONT, 35));
		gc.fillText("Press SPACE to go back", 50, 760);
		
		return layout;
	}
}
