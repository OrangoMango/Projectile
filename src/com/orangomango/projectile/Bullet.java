package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Bullet implements Serializable{
	private transient GraphicsContext gc;
	private double angle; // In radians
	private double speed = 10;
	private double x, y;
	public static final double startW = 17;
	public static double w = startW;
	public boolean doExplosion = false;
	public BulletConfig config;
	private int framesTravelled;
	public ContinueCond<Entity> continueCond = e -> e instanceof Player;
	public int WIDTH = MainApplication.SCREEN_WIDTH;
	public int HEIGHT = MainApplication.SCREEN_HEIGHT;
	
	@FunctionalInterface
	public static interface ContinueCond<T> extends Serializable{
		public boolean test(T cond);
	}
	
	public Bullet(GraphicsContext gc, double x, double y, double angle, BulletConfig config){
		this.gc = gc;
		this.angle = angle;
		this.x = x;
		this.y = y;
		this.config = config;
		configure();
	}
	
	public void setGC(GraphicsContext gc){
		this.gc = gc;
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
	
	public double getSpeed(){
		return this.speed;
	}
	
	public void travel(){
		this.gc.setFill(Color.web(doExplosion ? "#B65656" : "#867070"));
		this.gc.fillOval(this.x-w/2, this.y-w/2, w, w);
		if ((getX() <= 0 || getX() >= WIDTH || getY() <= 0 || getY() >= HEIGHT) && config.willBounce()){
			double ang = 180-this.angle*2;
			this.angle += Math.toRadians(ang);
		}
		this.x += this.speed * Math.cos(this.angle) * (w/startW);
		this.y += this.speed * Math.sin(this.angle) * (w/startW);
		this.framesTravelled++;
	}
}
