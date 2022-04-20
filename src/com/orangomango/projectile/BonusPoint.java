package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class BonusPoint{
	private GraphicsContext gc;
	private double x, y;
	
	public BonusPoint(GraphicsContext gc, double x, double y){
		this.gc = gc;
		this.x = x;
		this.y = y;
	}
	
	public void setX(double value){
		this.x = value;
	}
	
	public void setY(double value){
		this.y = value;
	}
	
	public void draw(){
		this.gc.setFill(Color.web("#F5E120"));
		this.gc.fillOval(this.x-30/2, this.y-30/2, 30, 30);
	}
	
	public boolean isOnPlayer(Player p){
		return (this.x >= p.x-p.w/2 && this.x <= p.x+p.w/2) && (this.y >= p.y-p.w/2 && this.y <= p.y+p.w/2);
	}
}
