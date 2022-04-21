package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import org.json.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class HomeScreen extends Screen{
	public static ArrayList<Selection> buttons = new ArrayList<>();
	private static final int DISTANCE = 410;
	private static String finalText = "";
	public Runnable startEvent;
	private ProfileManager pm;
	
	public HomeScreen(){
		playSound(MENU_BACKGROUND_SOUND, true, 0.65, false);
		buttons.clear();
		finalText = "";
		this.pm = new ProfileManager();
	}
	
	@Override
	public Scene getScene(){
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Cursor cursor = new Cursor(gc);
		cursor.x = 70;
		cursor.y = 300;
		cursor.relX = 0;
		cursor.relY = 0;
		cursor.fY = 0;
		canvas.setOnKeyPressed(event -> {
			switch (event.getCode()){
				case UP:
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY-1)){
						cursor.y -= 100;
						if (cursor.relX == 0) cursor.fY--;
						cursor.relY--;
						update(gc, cursor);
					}
					break;
				case RIGHT:
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX+1, null)){
						cursor.x += DISTANCE;
						cursor.relY = 0;
						cursor.y = 300;
						cursor.relX++;
						update(gc, cursor);
					}
					break;
				case DOWN:
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY+1)){
						cursor.y += 100;
						if (cursor.relX == 0) cursor.fY++;
						cursor.relY++;
						update(gc, cursor);
					}
					break;
				case LEFT:
					playSound(SELECT_SOUND, false, 1.0, true);
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
								playSound(CONFIRM_SOUND, false, null, true);
								this.startEvent.run();
								return;
							} else if (selection.getText().equals("TUTORIAL")){
								
							} else if (selection.getText().equals("RECORDS")){
								Platform.runLater(recordsPage);
							} else {
								selection.choosed = true;
							}
							playSound(CONFIRM_SOUND, false, null, true);
						}
					}
					update(gc, cursor);
					break;
			}
		});
		
		// Don't change any text
		
		Selection playButton = new Selection(gc, "PLAY", 120, 300, 0, 0, null, null);
		Selection difficultyButton = new Selection(gc, "DIFFICULTY", 120, 400, 0, 1, null, null);
		Selection helpButton = new Selection(gc, "CONTROLS", 120, 500, 0, 2, null, null);
		Selection controlsButton = new Selection(gc, "MOVEMENT", 120, 600, 0, 3, null, null);
		
		// Play
		Selection startButton = new Selection(gc, "START", 120+DISTANCE, 300, 1, 0, 0, 0);
		Selection tutorialButton = new Selection(gc, "TUTORIAL", 120+DISTANCE, 400, 1, 1, 0, 0);
		Selection recordsButton = new Selection(gc, "RECORDS", 120+DISTANCE, 500, 1, 2, 0, 0);
		
		// Difficulty
		Selection easy = new Selection(gc, "EASY", 120+DISTANCE, 300, 1, 0, 0, 1);
		easy.choosed = true;
		Selection medium = new Selection(gc, "MEDIUM", 120+DISTANCE, 400, 1, 1, 0, 1);
		Selection hard = new Selection(gc, "HARD", 120+DISTANCE, 500, 1, 2, 0, 1);
		Selection extreme = new Selection(gc, "EXTREME", 120+DISTANCE, 600, 1, 3, 0, 1);
		
		// Help
		
		// Controls
		Selection shortPress = new Selection(gc, "SHORT PRESS", 120+DISTANCE, 300, 1, 0, 0, 3);
		shortPress.choosed = this.pm.getJSON().getInt("input") == 0;
		Selection longPress = new Selection(gc, "LONG PRESS", 120+DISTANCE, 400, 1, 1, 0, 3);
		longPress.choosed = this.pm.getJSON().getInt("input") == 1;

		buttons.add(playButton);
		buttons.add(difficultyButton);
		buttons.add(helpButton);
		buttons.add(controlsButton);
		buttons.add(startButton);
		buttons.add(tutorialButton);
		buttons.add(recordsButton);
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
	
	private void update(GraphicsContext gc, Cursor cursor){
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
		gc.setFont(Font.loadFont(MAIN_FONT, 25));
		gc.fillText(finalText, 50, 700);
		if (cursor.fY == 2){
			gc.setFill(Color.web("#B9C5E5"));
			gc.setFont(Font.loadFont(MAIN_FONT, 25));
			gc.fillText("Movement: WASD\nShoot: left-click\nShoot grenade (4.5sec): right-click\nPause/Resume: P\nRestore hp (25sec): Q", 150+DISTANCE-85, 300);
		}
	}
	
	private void updateFinalText(){
		StringBuilder builder = new StringBuilder();
		String[] keys = new String[]{"Difficulty: ", "Control method: "};
		int counter = 0;
		builder.append("--Your settings--\n");
		for (Selection selection : buttons){
			if (selection.choosed){
				String k = keys[counter++];
				builder.append(k);
				builder.append(selection.getText()).append(" ");
				if (k.equals(keys[1])){
					this.pm.updateInput(selection.getText().equals("SHORT PRESS") ? 0 : 1);
				}
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
