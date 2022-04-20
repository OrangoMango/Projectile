package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Bullet{
	private GraphicsContext gc;
	private double angle; // In radians
	private double speed = 10;
	private double x, y;
	public boolean doExplosion = false;
	
	public Bullet(GraphicsContext gc, double x, double y, double angle){
		this.gc = gc;
		this.angle = angle;
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void travel(){
		this.gc.setFill(Color.web(doExplosion ? "#B65656" : "#867070"));
		this.gc.fillOval(this.x-20/2, this.y-20/2, 20, 20);
		this.x += this.speed * Math.cos(this.angle);
		this.y += this.speed * Math.sin(this.angle);
	}
}
