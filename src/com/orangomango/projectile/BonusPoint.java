package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;

public class BonusPoint{
	private GraphicsContext gc;
	private double x, y;
	private long startTime;
	private boolean timeCheck;
	private boolean tutorial;
	private static int TIME_TO_TAKE = 35000;
	private static int WIDTH = 20;
	public boolean show = true;
	
	public BonusPoint(GraphicsContext gc, double x, double y, boolean tutorial){
		this.gc = gc;
		this.x = x;
		this.y = y;
		this.startTime = System.currentTimeMillis();
		this.tutorial = tutorial;
		TIME_TO_TAKE = currentDiff[14];
	}
	
	public void setRandomPosition(Random random){
		setX(random.nextInt(SCREEN_WIDTH-20)+10);
		setY(random.nextInt(SCREEN_HEIGHT-95)+95-20);
		timeCheck = false;
	}
	
	public void addToStartTime(long value){
		this.startTime += value;
	}
	
	public void startTimer(){
		this.startTime = System.currentTimeMillis();
	}
	
	private void setX(double value){
		this.x = value;
	}
	
	private void setY(double value){
		this.y = value;
	}
	
	public void draw(){
		if (!show) return;
		this.gc.setFill(Color.web("#F5E120"));
		this.gc.fillOval(this.x-WIDTH/2, this.y-WIDTH/2, WIDTH, WIDTH);
		if (!MainApplication.bossInGame){
			this.gc.setFill(Color.web("#ABFF32"));
			double difference = (double)System.currentTimeMillis()-this.startTime;
			this.gc.fillRect(this.x-WIDTH/2, this.y+WIDTH/2+5, WIDTH*((difference)/TIME_TO_TAKE >= 1 ? 1 : (difference)/TIME_TO_TAKE), 5);
			this.gc.setStroke(Color.GREEN);
			this.gc.save();
			this.gc.setLineWidth(0.65);
			this.gc.strokeRect(this.x-WIDTH/2, this.y+WIDTH/2+5, WIDTH, 5);
			this.gc.restore();
			if (difference >= TIME_TO_TAKE*5/7 && !timeCheck){
				timeCheck = true;
				MainApplication.notification.setText("Yellow points!");
				MainApplication.notification.mustShow = true;
			}
			if (difference >= TIME_TO_TAKE){
				setRandomPosition(new Random());
				if (MainApplication.score <= 150){
					MainApplication.score = 0;
				} else {
					MainApplication.score -= 150;
				}
				playSound(SCORE_LOST_SOUND, false, null, false);
				startTimer();
			}
		}
	}
	
	public boolean isOnPlayer(Player p){
		return (this.x >= p.x-p.w/2 && this.x <= p.x+p.w/2) && (this.y >= p.y-p.w/2 && this.y <= p.y+p.w/2);
	}
}
