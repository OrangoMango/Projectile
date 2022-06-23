package com.orangomango.projectile.ui;

import javafx.application.Platform;
import javafx.scene.layout.TilePane;
import javafx.scene.input.KeyCode;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import static com.orangomango.projectile.MainApplication.*;
import com.orangomango.projectile.ui.profile.*;

public class TasksScreen extends Screen{

	@Override
	public TilePane getScene(){
		TilePane layout = new TilePane();
		
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ESCAPE){
				Platform.runLater(startPage);
			}
		});
		GraphicsContext gc = canvas.getGraphicsContext2D();
		layout.getChildren().add(canvas);
		
		update(gc);
		
		return layout;
	}
	
	private void update(GraphicsContext gc){
		TaskManager tm = new TaskManager();
		
		gc.setFill(Color.web("#383535"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc.scale((double)SCREEN_WIDTH/DEFAULT_WIDTH, (double)SCREEN_HEIGHT/DEFAULT_HEIGHT);		
	}
}
