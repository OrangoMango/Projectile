package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;

public class Enemy extends Entity{
	private Player player;
	private int damage2player = 10;
	public boolean spawning = true;
	private double brightness;
	private boolean boss;
	private boolean dmgCooldown;
	
	public Enemy(GraphicsContext gc, double x, double y, String color, String damageColor, Player player){
		super(gc, x, y, color, damageColor);
		this.player = player;
		this.speed = playWithTutorial ? 2 : enemySpeedDiff;
		new Thread(() -> {
			for (double i = -1.0; i <= 0; i += 0.1){
				brightness = i;
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
			spawning = false;
		}).start();
	}
	
	public void setHP(int value){
		this.hp = value;
		if (this.hp >= 50){
			this.boss = true;
		} else {
			this.boss = false;
		}
	}
	
	public void setDamage(int value){
		this.damage2player = value;
	}
	
	public void takeDamage(int damage, int index){
		super.takeDamage(damage);
		Random random = new Random();
		if (this.hp <= 0){
			die(index);			
			userGamedata.put("enemies", userGamedata.getOrDefault("enemies", 0.0)+1);
			if (this.boss && random.nextInt(100) <= 15+this.damage2player){ // 15% probability + extra %
				MainApplication.playSound(EXTRA_LIFE_SOUND, false, null, false);
				if (player.hp <= 85){
					player.hp += 10;
				} else {
					player.hp = 100;
				}
			}
			MainApplication.score += 10;
			playSound(SCORE_SOUND, false, null, false);
		}
	}
	
	/**
	 * Algorithm:
	 * 1. Calculate the angle (on the X axes) between the enemy and the player
	 * 2. Move the player by @link this.speed along the x and y axes components
	 */
	@Override
	public void draw(){
		double angle = Math.atan2(this.player.y-this.y, this.player.x-this.x);
		double speedX = this.speed * Math.cos(angle);
		double speedY = this.speed * Math.sin(angle);
		if (Math.sqrt(Math.pow(this.x+speedX-this.player.x, 2)+Math.pow(this.y+speedY-this.player.y, 2)) <= 60){
			if (!this.spawning && !this.dmgCooldown && !MainApplication.playWithTutorial){
				this.player.takeDamage(this.damage2player);
				this.dmgCooldown = true;
				new Timer().schedule(new TimerTask(){
					@Override
					public void run(){
						dmgCooldown = false;
					}
				}, MainApplication.currentDiff[13]);
			}
		} else {
			this.x += speedX;
			for (Entity en : MainApplication.entities){
				if (en != this && collided(en)){
					this.x -= speedX+2;
					break;
				}
			}
			this.y += speedY;
			for (Entity en : MainApplication.entities){
				if (en != this && collided(en)){
					this.y -= speedY+2;
					break;
				}
			}
		}
		if (this.spawning){
			gc.save();
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(this.brightness);
			gc.setEffect(effect);
			
		}
		super.draw();
		gc.restore();
	}
}