package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.util.*;

public class Entity{
	protected GraphicsContext gc;
	protected double x, y, w=50;
	protected String color;
	protected double speed = 3;
	protected int hp = 100;
	private boolean takingDamage;
	private String normalColor, damageColor;
	
	public Entity(GraphicsContext gc, double x, double y, String color, String damageColor){
		this.gc = gc;
		this.x = x;
		this.y = y;
		this.color = color;
		this.normalColor = this.color;
		this.damageColor = damageColor;
	}
	
	public void takeDamage(int damage){
		if (takingDamage) return;
		takingDamage = true;
		this.hp -= damage;
		this.color = this.damageColor;
		new Timer().schedule(new TimerTask(){
			@Override
			public void run(){
				takingDamage = false;
				color = normalColor;
			}
		}, 300);
	}
	
	public void setWidth(double value){
		this.w = value;
	}
	
	public void setX(double value){
		this.x = value;
	}
	
	public void setY(double value){
		this.y = value;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public double getSpeed(){
		return this.speed;
	}
	
	public void draw(){
		gc.setFill(Color.web(this.color));
		gc.fillOval(this.x-this.w/2, this.y-this.w/2, this.w, this.w);
	}
	
	public void die(int index){
		try {
			MainApplication.addingAllowed = false;
			MainApplication.entities.remove(index);
			MainApplication.addingAllowed = true;
		} catch (ConcurrentModificationException ex){
			System.out.println("-- error");
		}
	}
	
	public boolean collided(Entity other){
		return collided(other.x, other.y, other.w);
	}
	
	public boolean collided(double x1, double y1, double w){
		//return ((this.x-50/2 >= x1-w/2 && this.x-50/2 <= x1+w/2) || (this.x+50/2 >= x1-w/2 && this.x+50/2 <= x1+w/2)) && ((this.y-50/2 >= y1-w/2 && this.y-50/2 <= y1+w/2) || (this.y+50/2 >= y1-w/2 && this.y+50/2 <= y1+w/2));
		return ((x1-w/2 >= this.x-this.w/2 && x1-w/2 <= this.x+this.w/2) || (x1+w/2 >= this.x-this.w/2 && x1+w/2 <= this.x+this.w/2)) && ((y1-w/2 >= this.y-this.w/2 && y1-w/2 <= this.y+this.w/2) || (y1-w/2 >= this.y-this.w/2 && y1-w/2 <= this.y+this.w/2));
	}
}
