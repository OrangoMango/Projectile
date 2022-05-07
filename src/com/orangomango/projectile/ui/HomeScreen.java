package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.*;

import java.util.*;
import org.json.*;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.gunbuilder.MainApp;
import com.orangomango.projectile.ui.profile.*;

public class HomeScreen extends Screen{
	public static ArrayList<Selection> buttons = new ArrayList<>();
	private static final int DISTANCE = 410;
	private static String finalText = "";
	public Runnable startEvent;
	private ProfileManager pm;
	private String diff;
	private static boolean doingTutorial;
	private String[] texts = new String[]{"Well.. why are you here?", "You want to help me, right?", "Oh right, I'm Ruby. I need to escape\nfrom a black room...", "...but many enemies are attacking\nme.", "Use the right arrow to select the\ntutorial, and space to confirm"};
	private int messagePosition;
	private TutorialMessage message;
	private boolean messageSkipped;
	private static Image LOGO_IMG = new Image("file://"+userHome+"/.projectile/assets/image/projectile_logo.png");
	
	public HomeScreen(){
		stopAllSounds();
		playSound(MENU_BACKGROUND_SOUND, true, 0.65, false);
		buttons.clear();
		finalText = "";
		this.pm = new ProfileManager();
		doingTutorial = !this.pm.getJSON().getBoolean("tutorialComplete");
	}
	
	@Override
	public TilePane getScene(){
		TilePane layout = new TilePane();
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if (doingTutorial){
			this.message = new TutorialMessage(gc, true, true, texts[messagePosition++]);
		}
		Cursor cursor = new Cursor(gc);
		cursor.x = 70;
		cursor.y = 300;
		cursor.relX = 0;
		cursor.relY = 0;
		cursor.fY = 0;
		canvas.setOnKeyPressed(event -> {
			switch (event.getCode()){
				case UP:
					if (doingTutorial) return;
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY-1)){
						cursor.y -= 100;
						if (cursor.relX == 0) cursor.fY--;
						cursor.relY--;
						update(gc, cursor);
					}
					break;
				case RIGHT:
					if (doingTutorial && messagePosition < 5) return;
					this.message = null;
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
					if (doingTutorial) return;
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX, cursor.relY+1)){
						cursor.y += 100;
						if (cursor.relX == 0) cursor.fY++;
						cursor.relY++;
						update(gc, cursor);
					}
					break;
				case LEFT:
					if (doingTutorial) return;
					playSound(SELECT_SOUND, false, 1.0, true);
					if (checkForSelectionWith(cursor, cursor.relX-1, null)){
						cursor.x -= DISTANCE;
						cursor.relY = cursor.fY;
						cursor.y = 300+cursor.fY*100;
						cursor.relX--;
						update(gc, cursor);
					}
					break;
				case ENTER:
					if (!doingTutorial || messageSkipped) return;
					messageSkipped = true;
					schedule(() -> messageSkipped = false, 500);
					if (messagePosition == 5){
						this.message = null;
					} else {
						message.setText(texts[messagePosition++]);
					}
					playSound(CONFIRM_SOUND, false, null, true);
					update(gc, cursor);
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
							if (selection.getText().equals("START") || selection.getText().equals("TUTORIAL") && this.startEvent != null){
								stopAllSounds();
								playSound(CONFIRM_SOUND, false, null, true);
								difficulty = this.diff.toLowerCase();
								if (selection.getText().equals("TUTORIAL")){
									playWithTutorial = true;
								}
								this.startEvent.run();
								return;
							} else if (selection.getText().equals("RECORDS")){
								Platform.runLater(recordsPage);
							} else if (selection.getText().equals("BUILDER")){
								new MainApp().start(new Stage());
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
		Selection helpButton = new Selection(gc, "HELP", 120, 500, 0, 2, null, null);
		Selection controlsButton = new Selection(gc, "CONTROLS", 120, 600, 0, 3, null, null);
		
		// Play
		Selection tutorialButton = null;
		if (!this.pm.getJSON().getBoolean("tutorialComplete")){
			tutorialButton = new Selection(gc, "TUTORIAL", 120+DISTANCE, 300, 1, 0, 0, 0);
		}
		Selection startButton = new Selection(gc, "START", 120+DISTANCE, this.pm.getJSON().getBoolean("tutorialComplete") ? 300 : 400, 1, this.pm.getJSON().getBoolean("tutorialComplete") ? 0 : 1, 0, 0);
		Selection recordsButton = new Selection(gc, "RECORDS", 120+DISTANCE, this.pm.getJSON().getBoolean("tutorialComplete") ? 400 : 500, 1, this.pm.getJSON().getBoolean("tutorialComplete") ? 1 : 2, 0, 0);
		Selection gunBuilderButton = new Selection(gc, "BUILDER", 120+DISTANCE, this.pm.getJSON().getBoolean("tutorialComplete") ? 500 : 600, 1, this.pm.getJSON().getBoolean("tutorialComplete") ? 2 : 3, 0, 0);
		
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
		if (!this.pm.getJSON().getBoolean("tutorialComplete")){
			buttons.add(tutorialButton);
		}
		buttons.add(startButton);
		buttons.add(recordsButton);
		buttons.add(gunBuilderButton);
		buttons.add(easy);
		buttons.add(medium);
		buttons.add(hard);
		buttons.add(extreme);
		buttons.add(shortPress);
		buttons.add(longPress);
		
		updateFinalText();
		update(gc, cursor);
		
		return new TilePane(canvas);
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
		
		gc.save();
		gc.translate((SCREEN_WIDTH-1000)/2, (SCREEN_HEIGHT-800)/2);
		
		gc.drawImage(LOGO_IMG, 90, 30, 768, 200);
		
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
		if (this.message != null){
			this.message.show();
		}
		gc.restore();
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
				if (k.equals(keys[0])){
					this.diff = selection.getText();
				}
				else if (k.equals(keys[1])){
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
