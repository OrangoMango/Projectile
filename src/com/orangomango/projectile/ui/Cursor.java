package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.orangomango.projectile.MainApplication.MAIN_FONT;

public class Cursor {
	private GraphicsContext gc;
	public double x, y;
	public int relX, relY, fY;
	
	public Cursor(GraphicsContext gc){
		this.gc = gc;
	}
	
	public void show(){
		gc.setFill(Color.web("#F5E3E3"));
		gc.setFont(Font.loadFont(MAIN_FONT, 50));
		gc.fillText(">", this.x, this.y);
	}
}
