package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.Logger;

public class Enemy extends Entity{
	private Player player;
	private int damage2player = 10;
	public boolean spawning = true;
	private double brightness;
	private boolean boss;
	private boolean dmgCooldown;
	private int totalDamage;
	private boolean shoots;
	private boolean shootingAllowed = true;
	private long spawnStart;
	
	public Enemy(GraphicsContext gc, double x, double y, String color, String damageColor, Player player, boolean shoots){
		super(gc, x, y, color, damageColor);
		this.player = player;
		this.speed = playWithTutorial ? 2 : enemySpeedDiff;
		this.shoots = shoots;
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
		this.spawnStart = System.currentTimeMillis();
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
	
	public void takeDamage(int damage){
		super.takeDamage(damage);
		totalDamage += damage;
		Random random = new Random();
		if (this.hp <= 0){
			die();
			MainApplication.taskState.getJSONObject("enemies").put(this.shoots ? "green" : "red", MainApplication.taskState.getJSONObject("enemies").getInt(this.shoots ? "green" : "red")+1);	
			userGamedata.put("enemies", userGamedata.getOrDefault("enemies", 0.0)+1);
			MainApplication.incrementAchievement(0, 1);
			if (this.shoots){
				MainApplication.incrementAchievement(3, 1);
			}
			if (this.boss && random.nextInt(100) <= 15+this.damage2player){ // 15% probability + extra %
				MainApplication.playSound(EXTRA_LIFE_SOUND, false, null, false);
				if (player.hp <= player.getStartHP()-10){
					player.hp += 10;
				} else {
					player.hp = player.getStartHP();
				}
			}
			if (random.nextInt(100) <= 12){
				Random randomN = new Random();
				int prob = randomN.nextInt(100)+1;
				BulletConfig.Rarity gotRarity = null;
				if (prob <= BulletConfig.Rarity.COMMON.getChance()){
					gotRarity = BulletConfig.Rarity.COMMON;
				}
				if (prob <= BulletConfig.Rarity.EPIC.getChance()){
					gotRarity = BulletConfig.Rarity.EPIC;
				}
				if (prob <= BulletConfig.Rarity.LEGGENDARY.getChance()){
					gotRarity = BulletConfig.Rarity.LEGGENDARY;
				}
				Drop drop = new Drop(this.x, this.y, gotRarity);
				MainApplication.drops.add(drop);
			}
			MainApplication.score += totalDamage;
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
		double distance2player = Math.sqrt(Math.pow(this.x+speedX-this.player.x, 2)+Math.pow(this.y+speedY-this.player.y, 2));
		if (distance2player <= 350 && this.shoots){
			if (this.shootingAllowed && System.currentTimeMillis() >= this.spawnStart+1200){
				BulletConfig bcf = new BulletConfig(15, 700, (int)Math.round(damage2player*0.65), null, false, null, null, null, false, null, null, null, BulletConfig.Rarity.COMMON);
				bcf.loadMedia();
				Bullet bullet = new Bullet(gc, this.x, this.y, Math.atan2(this.player.y-this.y, this.player.x-this.x), bcf);
				bullet.continueCond = e -> e instanceof Enemy;
				MainApplication.player.bullets.add(bullet);
				MainApplication.playSound(bcf.getShootSound(), false, null, true);
				shootingAllowed = false;
				MainApplication.schedule(() -> shootingAllowed = true, bcf.getCooldown()+MainApplication.currentDiff[13]);
			}
		} else if (distance2player <= 40){
			if (!this.spawning && !this.dmgCooldown && !MainApplication.playWithTutorial){
				this.player.takeDamage(this.damage2player);
				this.dmgCooldown = true;
				MainApplication.schedule(() -> dmgCooldown = false, MainApplication.currentDiff[13]);
			}
		} else {
			this.x += speedX;
			try {
				for (Entity en : MainApplication.entities){
					if (en != this && collided(en)){
						this.x -= speedX+2;
						break;
					}
				}
			} catch (ConcurrentModificationException ex){
				Logger.warning("ConcurrentModificationException occured while checking collision of an enemy");
			}
			this.y += speedY;
			try {
				for (Entity en : MainApplication.entities){
					if (en != this && collided(en)){
						this.y -= speedY+2;
						break;
					}
				}
			} catch (ConcurrentModificationException ex){
				Logger.warning("ConcurrentModificationException occured while checking collision of an enemy");
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
