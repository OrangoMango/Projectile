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
	
	public void show(){
		gc.save();
		gc.setFill(Color.BLACK);
		gc.setGlobalAlpha(0.75);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.restore();
		gc.setFill(Color.web(this.speaker ? "#5228FC" : "#F1AF16"));
		double x = this.direction ? 50 : SCREEN_WIDTH-50-SPEAKER_WIDTH;
		if (this.speaker){
			gc.fillOval(x, SCREEN_HEIGHT-60-SPEAKER_WIDTH, SPEAKER_WIDTH, SPEAKER_WIDTH);
		} else {
			gc.fillRect(x, SCREEN_HEIGHT-60-SPEAKER_WIDTH, SPEAKER_WIDTH, SPEAKER_WIDTH);
		}
		gc.setFill(Color.WHITE);
		gc.fillRoundRect(200, SCREEN_HEIGHT-60-SPEAKER_WIDTH, 650, SPEAKER_WIDTH, 20, 20);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(5);
		gc.strokeRoundRect(200, SCREEN_HEIGHT-60-SPEAKER_WIDTH, 650, SPEAKER_WIDTH, 20, 20);
		gc.setFont(Font.loadFont(MAIN_FONT, 30));
		gc.setFill(Color.web("#0BCFC2"));
		gc.fillText(this.text, 220, SCREEN_HEIGHT-20-SPEAKER_WIDTH);
		gc.setFill(Color.RED);
		gc.setFont(Font.loadFont(MAIN_FONT, 20));
		gc.fillText("Press ENTER to continue", 550, SCREEN_HEIGHT-35);
	}
}
