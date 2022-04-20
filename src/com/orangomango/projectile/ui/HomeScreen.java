package com.orangomango.projectile.ui;

import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

import static com.orangomango.projectile.MainApplication.*;

public class HomeScreen{
	public static ArrayList<Selection> buttons = new ArrayList<>();
	private static final int DISTANCE = 410;
	private static String finalText = "";
	public Runnable startEvent;
	
	public HomeScreen(){
		playSound(MENU_BACKGROUND_SOUND, true, 0.65);
	}
	
	public Scene getScene(){
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Cursor cursor = new Cursor(gc);
		cursor.x = 100;
		cursor.y = 300;
		cursor.relX = 0;
		cursor.relY = 0;
		cursor.fY = 0;
		canvas.setOnKeyPressed(event -> {
			switch (event.getCode()){
				case UP:
					playSound(SELECT_SOUND, false, 1.0);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY-1)){
						cursor.y -= 100;
						if (cursor.relX == 0) cursor.fY--;
						cursor.relY--;
						update(gc, cursor);
					}
					break;
				case RIGHT:
					playSound(SELECT_SOUND, false, 1.0);
					if (checkForSelectionWith(cursor, cursor.relX+1, null)){
						cursor.x += DISTANCE;
						cursor.relY = 0;
						cursor.y = 300;
						cursor.relX++;
						update(gc, cursor);
					}
					break;
				case DOWN:
					playSound(SELECT_SOUND, false, 1.0);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY+1)){
						cursor.y += 100;
						if (cursor.relX == 0) cursor.fY++;
						cursor.relY++;
						update(gc, cursor);
					}
					break;
				case LEFT:
					playSound(SELECT_SOUND, false, 1.0);
					if (checkForSelectionWith(cursor, cursor.relX-1, null)){
						cursor.x -= DISTANCE;
						cursor.relY = cursor.fY;
						cursor.y = 300+cursor.fY*100;
						cursor.relX--;
						update(gc, cursor);
					}
					break;
				case SPACE:
					if (cursor.relX == 0) break;
					for (Selection selection : buttons){
						if (selection.relX == cursor.relX && (selection.comesFromY == null || selection.comesFromY == cursor.fY)){
							selection.choosed = false;
						}
					}
					for (Selection selection : buttons){
						if (selection.relX == cursor.relX && selection.relY == cursor.relY && (selection.comesFromY == null || selection.comesFromY == cursor.fY)){
							if (selection.getText().equals("START") && this.startEvent != null){
								stopAllSounds();
								playSound(CONFIRM_SOUND);
								this.startEvent.run();
								return;
							} else if (selection.getText().equals("TUTORIAL")){
								
							} else {
								selection.choosed = true;
							}
							playSound(CONFIRM_SOUND);
						}
					}
					update(gc, cursor);
					break;
			}
		});
		
		Selection playButton = new Selection(gc, "PLAY", 150, 300, 0, 0, null, null);
		Selection difficultyButton = new Selection(gc, "DIFFICULTY", 150, 400, 0, 1, null, null);
		Selection helpButton = new Selection(gc, "CONTROLS", 150, 500, 0, 2, null, null);
		Selection controlsButton = new Selection(gc, "MOVEMENT", 150, 600, 0, 3, null, null);
		
		// Play [Don't change the text "START" or the text "TUTORIAL"]
		Selection startButton = new Selection(gc, "START", 150+DISTANCE, 300, 1, 0, 0, 0);
		Selection tutorialButton = new Selection(gc, "TUTORIAL", 150+DISTANCE, 400, 1, 1, 0, 0);
		
		// Difficulty
		Selection easy = new Selection(gc, "EASY", 150+DISTANCE, 300, 1, 0, 0, 1);
		easy.choosed = true;
		Selection medium = new Selection(gc, "MEDIUM", 150+DISTANCE, 400, 1, 1, 0, 1);
		Selection hard = new Selection(gc, "HARD", 150+DISTANCE, 500, 1, 2, 0, 1);
		Selection extreme = new Selection(gc, "EXTREME", 150+DISTANCE, 600, 1, 3, 0, 1);
		
		// Help
		
		// Controls
		Selection shortPress = new Selection(gc, "SHORT PRESS", 150+DISTANCE, 300, 1, 0, 0, 3);
		shortPress.choosed = true;
		Selection longPress = new Selection(gc, "LONG PRESS", 150+DISTANCE, 400, 1, 1, 0, 3);

		buttons.add(playButton);
		buttons.add(difficultyButton);
		buttons.add(helpButton);
		buttons.add(controlsButton);
		buttons.add(startButton);
		buttons.add(tutorialButton);
		buttons.add(easy);
		buttons.add(medium);
		buttons.add(hard);
		buttons.add(extreme);
		buttons.add(shortPress);
		buttons.add(longPress);
		
		updateFinalText();
		update(gc, cursor);
		
		return new Scene(new TilePane(canvas), SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	private static boolean checkForSelectionWith(Cursor cursor, int x, Integer y){
		for (Selection selection : buttons){
			if (selection.relX == x && (y == null || selection.relY == y) && (selection.comesFromY == null || selection.comesFromY == cursor.fY)){
				return true;
			}
		}
		return false;
	}
	
	private static void update(GraphicsContext gc, Cursor cursor){
		gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.setFill(Color.web("#383535"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		cursor.show();
		updateFinalText();
		for (Selection selection : buttons){
			selection.setSelectedBasedOnCursor(cursor);
		}
		showButtons(cursor);
		gc.setFill(Color.web("#4E78E9"));
		gc.setFont(new Font("sans-serif", 25));
		gc.fillText(finalText, 50, 700);
		if (cursor.fY == 2){
			gc.setFill(Color.web("#B9C5E5"));
			gc.setFont(new Font("sans-serif", 25));
			gc.fillText("Movement: WASD\nShoot: left-click\nShoot grenade (4.5s): right-click\nPause/Resume: P\nRestore hp (25s): Q", 150+DISTANCE-20, 300);
		}
	}
	
	private static void updateFinalText(){
		StringBuilder builder = new StringBuilder();
		String[] keys = new String[]{"Difficulty: ", "Control method: "};
		int counter = 0;
		builder.append("--Your settings--\n");
		for (Selection selection : buttons){
			if (selection.choosed){
				builder.append(keys[counter++]);
				builder.append(selection.getText()).append(" ");
			}
		}
		finalText = builder.toString();
	}
	
	private static void showButtons(Cursor cursor){
		for (Selection selection : buttons){
			if (selection.comesFromX == null || selection.comesFromY == null || (selection.comesFromX == 0 && cursor.fY == selection.comesFromY)){
				selection.show();
			}
		}
	}
}
