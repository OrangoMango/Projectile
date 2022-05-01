package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Bullet{
	private GraphicsContext gc;
	private double angle; // In radians
	private double speed = 10;
	private double x, y;
	public boolean doExplosion = false;
	public BulletConfig config;
	private int framesTravelled;
	
	public Bullet(GraphicsContext gc, double x, double y, double angle, BulletConfig config){
		this.gc = gc;
		this.angle = angle;
		this.x = x;
		this.y = y;
		this.config = config;
		configure();
	}
	
	private void configure(){
		this.speed = this.config.getSpeed();
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public int getFrames(){
		return this.framesTravelled;
	}
	
	public void travel(){
		this.gc.setFill(Color.web(doExplosion ? "#B65656" : "#867070"));
		this.gc.fillOval(this.x-20/2, this.y-20/2, 20, 20);
		if ((getX() <= 0 || getX() >= MainApplication.SCREEN_WIDTH || getY() <= 0 || getY() >= MainApplication.SCREEN_HEIGHT) && config.willBounce()){
			double ang = 180-this.angle*2;
			this.angle += Math.toRadians(ang);
		}
		this.x += this.speed * Math.cos(this.angle);
		this.y += this.speed * Math.sin(this.angle);
		this.framesTravelled++;
	}
}
