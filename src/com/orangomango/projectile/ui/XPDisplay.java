package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
		gc.setFont(Font.loadFont(MAIN_FONT, 90));
		gc.fillText(Integer.toString(this.xp/3000), 10, 60);
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
