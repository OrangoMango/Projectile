package com.orangomango.projectile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Pointer{
	private GraphicsContext gc;
	private BonusPoint bp;
	private Player player;
	
	public Pointer(GraphicsContext gc, Player player, BonusPoint bp){
		this.gc = gc;
		this.player = player;
		this.bp = bp;
	}
	
	public void draw(){
		this.gc.setFill(this.bp.isImportant() ? Color.web("#FFAA4A") : Color.web("#4AF7FF"));
		double angle = Math.atan2(bp.getY()-this.player.getY(), bp.getX()-this.player.getX());
		this.gc.save();
		this.gc.translate(this.player.getX(), this.player.getY());
		this.gc.rotate(Math.toDegrees(angle));
		this.gc.setFont(Font.loadFont(MainApplication.MAIN_FONT, 35));
		this.gc.fillText(">", this.player.w/2+2, 10);
		this.gc.restore();
	}
}
