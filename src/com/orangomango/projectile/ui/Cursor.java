package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Cursor {
	private GraphicsContext gc;
	public double x, y;
	public int relX, relY, fY;
	
	public Cursor(GraphicsContext gc){
		this.gc = gc;
	}
	
	public void show(){
		gc.setFill(Color.web("#F5E3E3"));
		gc.setFont(new Font("sans-serif", 50));
		gc.fillText(">", this.x, this.y);
	}
}
