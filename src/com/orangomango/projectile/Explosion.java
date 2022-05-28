package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Explosion implements Serializable{
	private transient GraphicsContext gc;
	private double x, y;
	public double radius = 20;
	public int damage = 10;
	
	public Explosion(GraphicsContext gc, double x, double y){
		this.gc = gc;
		this.x = x;
		this.y = y;
	}
	
	public void setGC(GraphicsContext gc){
		this.gc = gc;
	}
	
	public void explode(){
		gc.setFill(Color.web("#FCB55C"));
		gc.fillOval(x-radius/2, y-radius/2, radius, radius);
		this.radius += 10;
	}
	
	public boolean collided(Entity e){
		return ((e.x-e.w/2 >= this.x-this.radius/2 && e.x-e.w/2 <= this.x+this.radius/2) || (e.x+e.w/2 >= this.x-this.radius/2 && e.x+e.w/2 <= this.x+this.radius/2)) && ((e.y-e.w/2 >= this.y-this.radius/2 && e.y-e.w/2 <= this.y+this.radius/2) || (e.y+e.w/2 >= this.y-this.radius/2 && e.y+e.w/2 <= this.y+this.radius/2));
	}
}
