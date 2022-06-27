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
		gc.setFont(Font.loadFont(MAIN_FONT, 25));
		gc.fillText(this.username+"\n"+this.xp+" xp", 80, 20);
		gc.restore();
	}
}
