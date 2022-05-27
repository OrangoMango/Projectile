package com.orangomango.projectile.multiplayer;

import com.orangomango.projectile.*;

import java.io.*;
import java.util.*;

public class GameState implements Serializable{
	public List<Entity> entities;
	public List<Explosion> explosions;
	public ArrayList<FloatingText> texts;
	public ArrayList<Drop> drops;
	public BonusPoint point1;
	public BonusPoint point2;
	private String message;
	
	public GameState(){
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public void sendMessage(String msg, Client c){
		System.out.println("Sending msg");
		this.message = msg;
		c.send(this);
		this.message = null;
	}
	
	public GameState(List<Entity> entities, List<Explosion> explosions, ArrayList<FloatingText> texts, ArrayList<Drop> drops, BonusPoint p1, BonusPoint p2){
		this.entities = entities;
		this.explosions = explosions;
		this.texts = texts;
		this.drops = drops;
		this.point1 = p1;
		this.point2 = p2;
	}
}
