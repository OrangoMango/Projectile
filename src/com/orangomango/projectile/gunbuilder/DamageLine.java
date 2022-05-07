package com.orangomango.projectile.gunbuilder;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class DamageLine{
	private GraphicsContext gc;
	private double y;
	
	public DamageLine(GraphicsContext gc, double y){
		this.gc = gc;
		this.y = y;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void draw(){
		this.gc.setStroke(Color.LIME);
		this.gc.setLineWidth(8);
		this.gc.strokeLine(0, this.y, 300, this.y);
	}
}
