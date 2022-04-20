package com.orangomango.projectile;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import static com.orangomango.projectile.MainApplication.*;

public class BossSuper {
	private GraphicsContext gc;
	private double x, y;
	
	public BossSuper(GraphicsContext gc, double x, double y){
		this.gc = gc;
		this.x = x;
		this.y = y;
	}
	
	public void spawn(){
		gc.setStroke(Color.web("#896717"));
		gc.save();
		gc.setLineWidth(10);
		gc.strokeLine(x, y-SCREEN_HEIGHT, x, y+SCREEN_HEIGHT);
		gc.strokeLine(x-SCREEN_WIDTH, y, x+SCREEN_WIDTH, y);
		gc.restore();
	}
	
	public boolean detectCollision(Entity e){
		return ((e.x-e.w/2 >= x-5 && e.x-e.w/2 <= x+5 && e.y-e.w/2 >= y-SCREEN_HEIGHT && e.y-e.w/2 <= y+SCREEN_HEIGHT) || (e.x+e.w/2 >= x-5 && e.x+e.w/2 <= x+5 && e.y+e.w/2 >= y-SCREEN_HEIGHT && e.y+e.w/2 <= y+SCREEN_HEIGHT)) || ((e.x-e.w/2 >= x-SCREEN_WIDTH && e.x-e.w/2 <= x+SCREEN_WIDTH && e.y-e.w/2 >= y-5 && e.y-e.w/2 <= y+5) || (e.x+e.w/2 >= x-SCREEN_WIDTH && e.x+e.w/2 <= x+SCREEN_WIDTH && e.y+e.w/2 >= y-5 && e.y+e.w/2 <= y+5));
	}
}
