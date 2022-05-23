package com.orangomango.projectile;

import java.util.Arrays;
import java.lang.reflect.*;
import javafx.scene.media.Media;

import java.io.Serializable;

public class BulletConfig implements Serializable{
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
	private String shootSound;
	private transient Media shootMedia;
	public boolean allowMultipleExplosions;
	private double maxDistance;
	public int ammoAmount;
	private int startAmmoAmount;
	private Rarity rarity;
	public String gunName = "UntitledGun";
	
	public static enum Rarity{
		COMMON(100), EPIC(25), LEGGENDARY(10);
		
		private int chance;
		
		private Rarity(int c){
			this.chance = c;
		}
		
		public int getChance(){
			return this.chance;
		}
	}
	
	public BulletConfig(Integer speed, Integer cooldown, Integer damage, double[] angles, boolean bounce, Integer ammo, int[] timing, int[] rechargeFrames, boolean goPast, Double maxDistance, Integer ammoAmount, String shootSound, Rarity rar){
		this.speed = speed == null ? 10 : speed;
		this.cooldown = cooldown == null ? 230 : cooldown;
		this.damage = damage == null ? 10 : damage;
		this.bounce = bounce;
		setAngles(angles == null ? new double[]{0} : angles);
		this.ammo = ammo == null ? 10 : ammo;
		this.timing = timing == null ? new int[]{1, 1} : timing;
		this.rechargeFrames = rechargeFrames == null ? new int[]{300, 10} : rechargeFrames;
		this.shootSound = shootSound == null ? MainApplication.SHOOT_SOUND.getSource() : shootSound;
		this.goPast = goPast;
		this.maxDistance = maxDistance == null ? 450 : maxDistance;
		this.ammoAmount = ammoAmount == null ? 5 : ammoAmount;
		this.startAmmoAmount = this.ammoAmount;
		this.rarity = rar;
	}
	
	public void loadMedia(){
		this.shootMedia = new Media(this.shootSound);
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
		return this.shootMedia;
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
	
	public Rarity getRarity(){
		return this.rarity;
	}
	
	public static String printList(double[] list, String sep){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.length; i++){
			builder.append(list[i]);
			if (i != list.length-1){
				builder.append(sep);
			}
		}
		return builder.toString();
	}
	
	public static String printList(int[] list, String sep){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.length; i++){
			builder.append(list[i]);
			if (i != list.length-1){
				builder.append(sep);
			}
		}
		return builder.toString();
	}
	
	public static String getFieldName(String path){
		try {
			for (Field field : MainApplication.class.getDeclaredFields()){
				if (field.getName().endsWith("SOUND") && ((Media)field.get(null)).getSource().equals(path)){
					return field.getName();
				}
			}
			int i = 0;
			for (Media m : MainApplication.GUN_SOUNDS){
				if (m.getSource().equals(path)){
					return "GUN_SOUNDS["+i+"]";
				}
				i++;
			}
		} catch (Exception exc){
			exc.printStackTrace();
		}
		return path;
	}
	
	@Override
	public String toString(){
		return "Gun: "+this.gunName+" ["+this.rarity+"]";
	}
	
	public String getDebugString(){
		String soundName = getFieldName(this.shootSound);
		if (!soundName.startsWith("file://")){
			soundName = "MainApplication."+soundName+".getSource()";
		}
		
		// Add extras string
		String extra = "";
		boolean one = false;
		if (allowMultipleExplosions){
			extra += "allowMultipleExplosions = true\n";
			one = true;
		}
		if (damageOnDistance){
			extra += String.format("setDamageOnDistance(%s, %s, %s)", this.minDamage, this.maxDamage, this.dpf)+"\n";
			one = true;
		}
		if (!one) extra += "\n";
		extra += "gunName = \""+gunName+"\"";
		
		return String.format("BulletConfig(%s, %s, %s, new double[]{%s}, %s, %s, new int[]{%s}, new int[]{%s}, %s, %s, %s, %s, BulletConfig.Rarity.%s)", this.speed, this.cooldown, this.damage, printList(this.angles, ", "), this.bounce, this.ammo, printList(this.timing, ", "), printList(this.rechargeFrames, ", "), this.goPast, this.maxDistance, this.ammoAmount, soundName, this.rarity)+extra;
	}
}
