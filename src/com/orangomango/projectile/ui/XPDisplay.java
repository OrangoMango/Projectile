package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import com.orangomango.projectile.ui.profile.TaskManager;
import static com.orangomango.projectile.MainApplication.MAIN_FONT;

public class XPDisplay{
	private int xp;
	private String username;
	
	public XPDisplay(TaskManager tm){
		this.xp = tm.getJSON().getInt("xp");
		this.username = tm.getJSON().getString("userName");
	}
	
	public void drawDefault(GraphicsContext gc, int x, int y){
		gc.save();
		gc.translate(x, y);
		gc.setFill(Color.CYAN);
		gc.fillRect(0, 0, 70, 70);
		gc.setFill(Color.web("#051378"));
		int size = 0;
		switch (Integer.toString(this.xp/3000).length()){
			case 1:
				size = 90;
				break;
			case 2:
				size = 65;
				break;
			case 3:
				size = 40;
				break;
		}
		gc.setFont(Font.loadFont(MAIN_FONT, size));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(Integer.toString(this.xp/3000), 35, 55);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setFont(Font.loadFont(MAIN_FONT, 25));
		gc.fillText(this.username+"\n"+(this.xp % 3000)+" xp", 80, 15);
		gc.setFill(Color.web("#9283F8"));
		gc.fillRect(80, 55, 100*(this.xp%3000/3000.0), 15);
		gc.setLineWidth(2);
		gc.setStroke(Color.web("#2511AA"));
		gc.strokeRect(80, 55, 100, 15);
		gc.restore();
	}
}
