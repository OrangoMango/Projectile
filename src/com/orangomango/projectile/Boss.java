package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;

public class Boss extends Entity{
	private Player player;
	public int startHP;
	public int damageHP;
	public boolean drawDamage;
	private boolean moving;
	private boolean playerTookDamage;
	private boolean superAllowed = true;
	private boolean superDamage;
	public static ArrayList<BossSuper> supers = new ArrayList<>();
	
	public Boss(GraphicsContext gc, double x, double y, String color, String damageColor, Player player){
		super(gc, x, y, color, damageColor);
		this.hp = 450;
		this.startHP = this.hp;
		this.w = 100;
		this.player = player;
		this.speed = 7;
	}
	
	@Override
	public void draw(){
		if (!this.moving){
			this.moving = true;
			Random random = new Random();
			final int choice = random.nextInt(4);
			if (random.nextInt(100) <= 2 && this.hp > this.startHP/2){ // 2% chance to restore hp
				this.drawDamage = true;
				if (this.hp <= this.startHP-20){
					this.damageHP = -20;
					this.hp += 20;
				} else {
					this.damageHP = -(this.startHP-this.hp);
					this.hp = this.startHP;
				}
				MainApplication.playSound(BOSS_HP_SOUND, false, null, false);
				new Timer().schedule(new TimerTask(){
					@Override
					public void run(){
						drawDamage = false;
					}
				}, 750);
			}
			if (random.nextInt(100) <= 9 && superAllowed){ // 9% chance for super power
				BossSuper bs = new BossSuper(gc, this.x, this.y);
				supers.add(bs);
				new Timer().schedule(new TimerTask(){
					@Override
					public void run(){
						supers.remove(bs);
					}
				}, 1000);
				MainApplication.playSound(BOSS_SUPER_SOUND, false, null, false);
				superAllowed = false;
				new Timer().schedule(new TimerTask(){
					@Override
					public void run(){
						superAllowed = true;
					}
				}, 5000);
			}
			if (this.hp <= this.startHP/2 && !this.superDamage){
				this.superDamage = true;
				this.speed *= 1.5;
				MainApplication.notification.setText("Boss is faster!");
				MainApplication.notification.mustShow = true;
			}
			Timeline mover = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), e -> {
				switch (choice){
					case 0:
						this.y -= this.speed;
						if (this.y-this.w <= 0) this.y += this.speed;
						for (Entity en : MainApplication.entities){
							if (en != this && collided(en)){
								this.y += this.speed;
								break;
							}
						}
						break;
					case 1:
						this.x += this.speed;
						if (this.x+this.w >= SCREEN_WIDTH) this.x -= this.speed;
						for (Entity en : MainApplication.entities){
							if (en != this && collided(en)){
								this.x -= this.speed;
								break;
							}
						}
						break;
					case 2:
						this.y += this.speed;
						if (this.y+this.w >= SCREEN_HEIGHT) this.y -= this.speed;
						for (Entity en : MainApplication.entities){
							if (en != this && collided(en)){
								this.y -= this.speed;
								break;
							}
						}
						break;
					case 3:
						this.x -= this.speed;
						if (this.x-this.w <= 0) this.x += this.speed;
						for (Entity en : MainApplication.entities){
							if (en != this && collided(en)){
								this.x += this.speed;
								break;
							}
						}
						break;
				}
				double distance = Math.sqrt(Math.pow(this.x-player.getX(), 2)+Math.pow(this.y-player.getY(), 2));
				if (distance <= 100 && !playerTookDamage){
					player.takeDamage(15);
					playerTookDamage = true;
					new Timer().schedule(new TimerTask(){
						@Override
						public void run(){
							playerTookDamage = false;
						}
					}, 500);
				}
			}));
			mover.setCycleCount(15);
			mover.setOnFinished(e -> this.moving = false);
			mover.play();
		}
		gc.setFill(Color.web(this.color));
		gc.fillRect(this.x-this.w/2, this.y-this.w/2, this.w, this.w);
	}
	
	public int getHP(){
		return this.hp;
	}
	
	public void takeDamage(int damage, int index){
		drawDamage = true;
		super.takeDamage(damage);
		this.damageHP = damage;
		MainApplication.playSound(BOSS_HIT_SOUND, false, null, false);
		new Timer().schedule(new TimerTask(){
			@Override
			public void run(){
				drawDamage = false;
			}
		}, 750);
		if (this.hp <= 0){
			die(index);
			player.hp = 100;
			userGamedata.put("bosses", userGamedata.getOrDefault("bosses", 0.0)+1);
			MainApplication.playSound(EXTRA_LIFE_SOUND, false, null, false);
			MainApplication.score += 100;
			MainApplication.bossCount = MainApplication.score;
			playSound(BOSS_DEATH_SOUND, false, null, false);
			MainApplication.stopAllSounds();
			MainApplication.playSound(BACKGROUND_SOUND, true, 1.0, false);
		}
	}
}
