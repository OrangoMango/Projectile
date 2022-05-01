package com.orangomango.projectile;

public class BulletConfig{
	private int speed;
	private int cooldown;
	private Integer damage;
	private int count;
	private double[] angles; // In degrees
	private int[] timing;
	private boolean bounce;
	private boolean damageOnDistance;
	private int minDamage, maxDamage, dpf;
	private int ammo;
	
	public BulletConfig(Integer speed, Integer cooldown, Integer damage, double[] angles, boolean bounce, Integer ammo, int[] timing){
		this.speed = speed == null ? 10 : speed;
		this.cooldown = cooldown == null ? 230 : cooldown;
		this.damage = damage == null ? 10 : damage;
		this.bounce = bounce;
		setAngles(angles == null ? new double[]{0} : angles);
		this.ammo = ammo == null ? 10 : ammo;
		this.timing = timing == null ? new int[]{1, 0} : timing;
	}
	
	// Damage on distance function
	public void setDamageOnDistance(int min, int max, int dpf){
		this.damageOnDistance = true;
		this.minDamage = min;
		this.maxDamage = max;
		this.dpf = dpf;
	}
	
	public int[] getDamageData(){
		return new int[]{this.minDamage, this.maxDamage, this.dpf};
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
