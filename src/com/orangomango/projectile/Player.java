package com.orangomango.projectile;

import javafx.application.Platform;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class Player extends Entity{
	public Timeline movement;
	public boolean shootingAllowed = true;
	public boolean shooting;
	private boolean playingSound;
	private boolean gameIsOver;
	public double startSpeed;
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	public int ammo = 10;
	private int startHP, startShield;
	
	public Player(GraphicsContext gc, double x, double y, String color, String damageColor, ProfileManager pm){
		super(gc, x, y, color, damageColor);
		this.startSpeed = pm.getJSON().getInt("input") == 0 ? 4 : 6;
		this.speed = this.startSpeed;
		this.shield = 50;
		this.startHP = this.hp;
		this.startShield = this.shield;
	}
	
	public int getStartHP(){
		return this.startHP;
	}
	
	public int getStartShield(){
		return this.startShield;
	}
	
	public void moveX(int factor){
		if (this.movement != null) this.movement.stop();
		this.movement = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), e -> {
			this.x += this.speed*factor;
		}));
		this.movement.setCycleCount(Animation.INDEFINITE);
		this.movement.play();
	}
	
	public void moveY(int factor){
		if (this.movement != null) this.movement.stop();
		this.movement = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), e -> {
			this.y += this.speed*factor;
		}));
		this.movement.setCycleCount(Animation.INDEFINITE);
		this.movement.play();
	}
	
	public void shoot(double shootX, double shootY, boolean explosion, BulletConfig config, int count){
		if (count == config.getCount()-1 && ammo > 0 && !explosion){
			ammo--;
		}
		Bullet b = new Bullet(this.gc, this.x, this.y, Math.atan2(shootY-this.y, shootX-this.x)+Math.toRadians(config.getAngles()[count]), config);
		b.doExplosion = explosion;
		bullets.add(b);
		playSound(explosion ? EXPLOSION_SOUND : config.getShootSound(), false, null, true);
	}
	
	@Override
	public void takeDamage(int damage){
		super.takeDamage(damage);
		if (!playingSound){
			playSound(DAMAGE_SOUND, false, null, true);
			playingSound = true;
			MainApplication.schedule(() -> playingSound = false, 500);
		}
		if (this.hp <= 0 && !gameIsOver){
			System.out.println("YOU DIED");
			gameIsOver = true;
			userGamedata.put("damageRatio", (double)MainApplication.enemyDamageCount/MainApplication.bulletCount);
			MainApplication.playSound(DEATH_SOUND, false, null, false);
			MainApplication.loop.stop();
			MainApplication.entities.clear();
			MainApplication.threadRunning = false;
			MainApplication.stopAllSounds();
			MainApplication.gameoverPage.run();
		}
	}
	
	@Override
	public void draw(){
		super.draw();
		if (this.x <= 0 || this.x >= SCREEN_WIDTH || this.y <= 0 || this.y >= SCREEN_HEIGHT){
			if (this.x <= 0 || this.x >= SCREEN_WIDTH){
				this.x = Math.abs(this.x-(SCREEN_WIDTH-50));
			} else if (this.y <= 0 || this.y >= SCREEN_HEIGHT){
				this.y = Math.abs(this.y-(SCREEN_HEIGHT-50));
			}
		}
	}
}
