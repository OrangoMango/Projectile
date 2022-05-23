package com.orangomango.projectile.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class ServerConfig extends Application{
	public GridPane getLayout(){
		GridPane layout = new GridPane();
		return layout;
	}
	
	public void start(Stage stage){
		stage.setTitle("Projectile server config");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(getLayout(), 500, 400));
		stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
