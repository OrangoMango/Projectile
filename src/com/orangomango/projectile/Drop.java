package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Drop{
	private double x, y;
	private String color;
	private BulletConfig.Rarity rarity;
	private long startTime;
	private static final int TIME_TO_TAKE = 20000;
	private static final double WIDTH = 20;
	public boolean mustRemove;
	
	public Drop(double x, double y, BulletConfig.Rarity rarity){
		this.x = x;
		this.y = y;
		this.rarity = rarity;
		this.startTime = System.currentTimeMillis();
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
		gc.fillRect(this.x-WIDTH/2, this.y-WIDTH/2, WIDTH, WIDTH);
		gc.setFill(Color.web("#8ABAEA"));
		double difference = (double)System.currentTimeMillis()-this.startTime;
		gc.fillRect(this.x-WIDTH/2, this.y+WIDTH/2+5, WIDTH*((difference)/TIME_TO_TAKE >= 1 ? 1 : (difference)/TIME_TO_TAKE), 5);
		gc.setStroke(Color.web("#338EE6"));
		gc.save();
		gc.setLineWidth(0.65);
		gc.strokeRect(this.x-WIDTH/2, this.y+WIDTH/2+5, WIDTH, 5);
		gc.restore();
		if (difference >= TIME_TO_TAKE){
			this.mustRemove = true;
			MainApplication.playSound(MainApplication.SCORE_LOST_SOUND, false, null, false);
		}
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
