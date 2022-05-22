package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Drop{
	private double x, y;
	private String color;
	private BulletConfig.Rarity rarity;
	
	public Drop(double x, double y, BulletConfig.Rarity rarity){
		this.x = x;
		this.y = y;
		this.rarity = rarity;
		switch (this.rarity){
			case COMMON:
				this.color = "#14F7C4";
				break;
			case EPIC:
				this.color = "#8200FF";
				break;
			case LEGGENDARY:
				this.color = "#FFE100";
				break;
		}
	}
	
	public BulletConfig.Rarity getRarity(){
		return this.rarity;
	}
	
	public void draw(GraphicsContext gc){
		gc.setFill(Color.web(this.color));
		gc.fillRect(this.x-10, this.y-10, 20, 20);
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public boolean onPlayer(Player p){
		return (this.x >= p.x-p.w/2 && this.x <= p.x+p.w/2) && (this.y >= p.y-p.w/2 && this.y <= p.y+p.w/2);
	}
}
