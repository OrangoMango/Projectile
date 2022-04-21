package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.orangomango.projectile.MainApplication.MAIN_FONT;

public class Selection {
	private GraphicsContext gc;
	private double x, y;
	public int relX, relY;
	public Integer comesFromX, comesFromY;
	private String text;
	private String color = "#E6E0E0";
	private String selectedColor = "#F16B6B";
	private String choosedColor = "#63EE76";
	private boolean selected;
	public boolean choosed;
	
	public Selection(GraphicsContext gc, String text, double x, double y, int relX, int relY, Integer comesFromX, Integer comesFromY){
		this.gc = gc;
		this.text = text;
		this.x = x;
		this.y = y;
		this.relX = relX;
		this.relY = relY;
		this.comesFromX = comesFromX;
		this.comesFromY = comesFromY;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void show(){
		gc.setFill(Color.web(this.selected ? this.selectedColor : (this.choosed ? this.choosedColor : this.color)));
		gc.setFont(Font.loadFont(MAIN_FONT, 50));
		gc.fillText(this.text, this.x, this.y);
	}
	
	public void setSelectedBasedOnCursor(Cursor cursor){
		this.selected = cursor.relX == this.relX && cursor.relY == this.relY;
	}
}
