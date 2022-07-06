package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;

public class Notification {
	private GraphicsContext gc;
	private String text;
	public boolean mustShow;
	private boolean scheduled;
	
	public Notification(GraphicsContext gc){
		this.gc = gc;
	}
	
	public void setText(String value){
		this.text = value;
	}
	
	public void show(){
		if (!mustShow) return;
		gc.save();
		gc.setFill(Color.web("#F7D79B"));
		gc.setGlobalAlpha(0.7);
		gc.fillRect(RENDER_WIDTH-310, 30, 290, 50);
		gc.setFont(Font.loadFont(MAIN_FONT, 25));
		gc.setFill(Color.web("#7B390E"));
		gc.fillText(this.text, RENDER_WIDTH-290, 62);
		gc.restore();
		if (!scheduled){
			MainApplication.schedule(() -> {
				mustShow = false;
				scheduled = false;
			}, 2000);
			scheduled = true;
		}
	}
}
