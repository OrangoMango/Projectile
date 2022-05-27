package com.orangomango.projectile.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;

import com.orangomango.projectile.multiplayer.*;
import com.orangomango.projectile.ui.profile.Logger;
import com.orangomango.projectile.MainApplication;

public class JoinConfig extends Application{
	private Runnable onDone;
	private Stage stage;
	
	public void setOnDone(Runnable r){
		this.onDone = r;
	}
	
	public GridPane getLayout(){
		GridPane layout = new GridPane();
		
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setVgap(5);
		layout.setHgap(5);
		Label hostL = new Label("Host: ");
		hostL.setFocusTraversable(true);
		Label portL = new Label("Port: ");
		Label userL = new Label("Username: ");
		TextField host = new TextField();
		host.setPromptText("127.0.0.1");
		TextField port = new TextField();
		port.setPromptText("1234");
		TextField user = new TextField();
		user.setPromptText("Guest");
		Button join = new Button("Join game");
		join.setOnAction(ev -> {
			Client client = new Client("127.0.0.1", 1234, "Guest"+(new java.util.Random()).nextInt(20));
			MainApplication.client = client;
			if (this.onDone != null){
				this.stage.close();
				this.onDone.run();
			} else {
				Logger.error("client connect onDone is set to null");
			}
		});
		
		layout.add(hostL, 0, 0);
		layout.add(portL, 0, 1);
		layout.add(userL, 0, 2);
		layout.add(host, 1, 0);
		layout.add(port, 1, 1);
		layout.add(user, 1, 2);
		layout.add(join, 0, 3);
		
		return layout;
	}
	
	public void start(Stage stage){
		this.stage = stage;
		stage.setTitle("Projectile join game");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(getLayout(), 500, 300));
		stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
