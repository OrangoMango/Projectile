package com.orangomango.projectile;

import javafx.scene.media.Media;

public class BulletConfig{
	private int speed;
	private int cooldown;
	private Integer damage;
	private int count;
	private double[] angles; // In degrees
	private int[] timing;
	private boolean bounce;
	private boolean damageOnDistance;
	private boolean goPast;
	private int minDamage, maxDamage, dpf;
	private int ammo;
	private int[] rechargeFrames;
	private Media shootSound;
	public boolean allowMultipleExplosions;
	private double maxDistance;
	public int ammoAmount;
	private int startAmmoAmount;
	
	public BulletConfig(Integer speed, Integer cooldown, Integer damage, double[] angles, boolean bounce, Integer ammo, int[] timing, int[] rechargeFrames, boolean goPast, Double maxDistance, Integer ammoAmount, Media shootSound){
		this.speed = speed == null ? 10 : speed;
		this.cooldown = cooldown == null ? 230 : cooldown;
		this.damage = damage == null ? 10 : damage;
		this.bounce = bounce;
		setAngles(angles == null ? new double[]{0} : angles);
		this.ammo = ammo == null ? 10 : ammo;
		this.timing = timing == null ? new int[]{1, 1} : timing;
		this.rechargeFrames = rechargeFrames == null ? new int[]{300, 3000} : rechargeFrames;
		this.shootSound = shootSound == null ? MainApplication.SHOOT_SOUND : shootSound;
		this.goPast = goPast;
		this.maxDistance = maxDistance == null ? 300 : maxDistance;
		this.ammoAmount = ammoAmount == null ? 5 : ammoAmount;
		this.startAmmoAmount = this.ammoAmount;
	}
	
	// Damage on distance function
	public void setDamageOnDistance(int min, int max, int dpf){
		this.damageOnDistance = true;
		this.minDamage = min;
		this.maxDamage = max;
		this.dpf = dpf;
	}
	
	public int getStartAmmoAmount(){
		return this.startAmmoAmount;
	}
	
	public Media getShootSound(){
		return this.shootSound;
	}
	
	public double getMaxDistance(){
		return this.maxDistance;
	}
	
	public int[] getDamageData(){
		return new int[]{this.minDamage, this.maxDamage, this.dpf};
	}
	
	public int[] getRechargeFrames(){
		return this.rechargeFrames;
	}
	
	public int[] getTiming(){
		return this.timing;
	}
	
	public int getAmmo(){
		return this.ammo;
	}
	
	public boolean willDoDamageOnDistance(){
		return this.damageOnDistance;
	}
	
	public boolean willGoPast(){
		return this.goPast;
	}
	
	public void setAngles(double[] angles){
		this.angles = angles;
		this.count = angles.length;
	}
	
	public double[] getAngles(){
		return this.angles;
	}
	
	public int getSpeed(){
		return this.speed;
	}
	
	public boolean willBounce(){
		return this.bounce;
	}
	
	public int getCooldown(){
		return this.cooldown;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public int getCount(){
		return this.count;
	}
}
