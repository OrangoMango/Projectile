package com.orangomango.projectile.gunbuilder;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Player{
	private GraphicsContext gc;
	private double x = 100, y = 400;
	public static final double startW = 30;
	public static double w = startW;
	
	public Player(GraphicsContext gc){
		this.gc = gc;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void draw(){
		this.gc.setFill(Color.RED);
		this.gc.fillOval(this.x-w/2, this.y-w/2, w, w);
	}
}
