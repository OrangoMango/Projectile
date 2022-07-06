package com.orangomango.projectile.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.orangomango.projectile.MainApplication.*;

public class TutorialMessage{
	private GraphicsContext gc;
	private boolean direction, speaker;
	private String text;
	private static double SPEAKER_WIDTH = 100;
	private boolean alpha = true;
	
	public TutorialMessage(GraphicsContext gc, boolean direction, boolean speaker, String text){
		this.gc = gc;
		this.direction = direction;
		this.speaker = speaker;
		this.text = text;
	}
	
	public void setText(String value){
		if (value == null){
			return;
		}
		this.text = value;
	}
	
	public void setMakeAlpha(boolean value){
		this.alpha = value;
	}
	
	public void show(){
		if (this.alpha){
			gc.save();
			gc.setFill(Color.BLACK);
			gc.setGlobalAlpha(0.75);
			gc.fillRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
			gc.restore();
		}
		gc.setFill(Color.web(this.speaker ? "#5228FC" : "#F1AF16"));
		double x = this.direction ? 50 : RENDER_WIDTH-30-SPEAKER_WIDTH;
		if (this.speaker){
			gc.fillOval(x, RENDER_HEIGHT-60-SPEAKER_WIDTH, SPEAKER_WIDTH, SPEAKER_WIDTH);
		} else {
			gc.fillRect(x, RENDER_HEIGHT-60-SPEAKER_WIDTH, SPEAKER_WIDTH, SPEAKER_WIDTH);
		}
		gc.setFill(Color.WHITE);
		gc.fillRoundRect(200, RENDER_HEIGHT-60-SPEAKER_WIDTH, 650, SPEAKER_WIDTH, 20, 20);
		gc.setStroke(this.speaker ? Color.web("#B8FEA3") : Color.web("#B1CA74"));
		gc.setLineWidth(5);
		gc.strokeRoundRect(200, RENDER_HEIGHT-60-SPEAKER_WIDTH, 650, SPEAKER_WIDTH, 20, 20);
		gc.setFont(Font.loadFont(MAIN_FONT, 30));
		gc.setFill(Color.web("#0BCFC2"));
		gc.fillText(this.text, 220, RENDER_HEIGHT-20-SPEAKER_WIDTH);
		gc.setFill(Color.RED);
		gc.setFont(Font.loadFont(MAIN_FONT, 20));
		gc.fillText("Press ENTER to continue", 550, RENDER_HEIGHT-35);
	}
}
