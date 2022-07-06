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
	public transient Timeline movement;
	public boolean shootingAllowed = true;
	public boolean shooting;
	private boolean playingSound;
	private boolean gameIsOver;
	public double startSpeed;
	public List<Bullet> bullets = Collections.synchronizedList(new ArrayList<Bullet>());
	public int ammo = 10;
	private int startHP, startShield;
	public String user;
	private long tookDamage;
	
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
		final double xFactor = (double)SCREEN_WIDTH/DEFAULT_WIDTH;
		final double yFactor = (double)SCREEN_HEIGHT/DEFAULT_HEIGHT;
		Bullet b = new Bullet(this.gc, this.x, this.y, Math.atan2(shootY-this.y*yFactor, shootX-this.x*xFactor)+Math.toRadians(config.getAngles()[count]), config);
		b.owner = user;
		b.doExplosion = explosion;
		bullets.add(b);
		playSound(explosion ? EXPLOSION_SOUND : config.getShootSound(), false, null, true);
	}
	
	@Override
	public void takeDamage(int damage){
		if (System.currentTimeMillis()-tookDamage < 400 && MainApplication.client != null) return;
		if (!playingSound){
			playSound(DAMAGE_SOUND, false, null, true);
			playingSound = true;
			MainApplication.schedule(() -> playingSound = false, 500);
		}
		super.takeDamage(damage);
		tookDamage = System.currentTimeMillis();
		if (this.hp <= 0 && !gameIsOver){
			System.out.println("YOU DIED");
			if (client == null || MainApplication.client.getUsername().equals(user)){
				gameIsOver = true;
				doGameOver();
			}
		}
	}
	
	public static void doGameOver(){
		double ratio = (double)MainApplication.enemyDamageCount/MainApplication.bulletCount;
		MainApplication.userGamedata.put("damageRatio", ratio);
		MainApplication.taskState.put("damageRatio", MainApplication.bulletCount == 0 ? 0 : ratio);
		MainApplication.taskState.put("totalDamage", MainApplication.enemyDamageCount);
		MainApplication.taskState.put("bulletsShot", MainApplication.bulletCount);
		Logger.info("Game data: "+userGamedata);
		MainApplication.playSound(DEATH_SOUND, false, null, false);
		MainApplication.loop.stop();
		MainApplication.entities.clear();
		if (MainApplication.client != null){
			MainApplication.client.close();
			MainApplication.client = null;
		}
		MainApplication.threadRunning = false;
		MainApplication.stopAllSounds();
		MainApplication.schedule(() -> Platform.runLater(MainApplication.gameoverPage), 1500);
	}
	
	@Override
	public String toString(){
		String top = super.toString();
		return top+" "+getX()+" "+getY();
	}
	
	@Override
	public void draw(){
		super.draw();
		if (this.x <= 0 || this.x >= RENDER_WIDTH || this.y <= 0 || this.y >= RENDER_HEIGHT){
			if (this.x <= 0 || this.x >= RENDER_WIDTH){
				this.x = Math.abs(this.x-(RENDER_WIDTH-50));
			} else if (this.y <= 0 || this.y >= RENDER_HEIGHT){
				this.y = Math.abs(this.y-(RENDER_HEIGHT-50));
			}
		}
	}
}
