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
	private static int TIME_TO_TAKE = 35000;
	
	public BonusPoint(GraphicsContext gc, double x, double y){
		this.gc = gc;
		this.x = x;
		this.y = y;
		this.startTime = System.currentTimeMillis();
		TIME_TO_TAKE = currentDiff[14];
	}
	
	public void setRandomPosition(Random random){
		setX(random.nextInt(SCREEN_WIDTH-20)+10);
		setY(random.nextInt(SCREEN_HEIGHT-95)+95-20);
		timeCheck = false;
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
		this.gc.setFill(Color.web("#F5E120"));
		this.gc.fillOval(this.x-30/2, this.y-30/2, 30, 30);
		this.gc.setFill(Color.web("#ABFF32"));
		double difference = (double)System.currentTimeMillis()-this.startTime;
		this.gc.fillRect(this.x-30/2, this.y+30/2+5, 30*((difference)/TIME_TO_TAKE >= 1 ? 1 : (difference)/TIME_TO_TAKE), 5);
		this.gc.setStroke(Color.GREEN);
		this.gc.save();
		this.gc.setLineWidth(0.65);
		this.gc.strokeRect(this.x-30/2, this.y+30/2+5, 30, 5);
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
	
	public boolean isOnPlayer(Player p){
		return (this.x >= p.x-p.w/2 && this.x <= p.x+p.w/2) && (this.y >= p.y-p.w/2 && this.y <= p.y+p.w/2);
	}
}
