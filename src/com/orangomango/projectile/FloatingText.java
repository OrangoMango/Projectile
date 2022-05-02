package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FloatingText{
	private double x, y;
	private String text;
	private int movements;
	
	public FloatingText(String text, double x, double y){
		this.x = x;
		this.y = y;
		this.text = text;
	}
	
	public void draw(GraphicsContext gc){
		gc.setFont(Font.loadFont(MainApplication.MAIN_FONT, 30));
		gc.setFill(Color.web("#FFED00"));
		gc.fillText(this.text, this.x, this.y);
		this.y -= 5;
		this.movements++;
	}
	
	public int getMovements(){
		return this.movements;
	}
}
