package com.orangomango.projectile.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;

import com.orangomango.projectile.multiplayer.*;
import com.orangomango.projectile.ui.profile.Logger;

public class ServerConfig extends Application{
	private static Server server;
	private Stage stage;
	
	private void updateDisabled(Node... nodes){
		boolean cond = Server.server == null || (Server.server != null && Server.server.isClosed());
		for (Node node : nodes){
			node.setDisable(!cond);
		}
	}
	
	public GridPane getLayout(){
		GridPane layout = new GridPane();
		
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setVgap(5);
		layout.setHgap(5);
		Label hostL = new Label("Host: ");
		hostL.setFocusTraversable(true);
		Label portL = new Label("Port: ");
		TextField host = new TextField();
		host.setPromptText("127.0.0.1");
		TextField port = new TextField();
		port.setPromptText("1234");
		Label maxPlayersL = new Label("Max players: ");
		Slider maxPlayers = new Slider(2, 4, 1);
		maxPlayers.setShowTickLabels(true);
		maxPlayers.setShowTickMarks(true);
		maxPlayers.setMajorTickUnit(1);
		maxPlayers.setMinorTickCount(0);
		maxPlayers.valueProperty().addListener((obs, oldval, newVal) -> maxPlayers.setValue(newVal.intValue()));
		Label gameModeL = new Label("Game mode: ");
		ToggleGroup group = new ToggleGroup();
		RadioButton op1 = new RadioButton("PvP");
		op1.setTooltip(new Tooltip("Players against players"));
		op1.setToggleGroup(group);
		op1.setSelected(true);
		RadioButton op2 = new RadioButton("PvE");
		op2.setTooltip(new Tooltip("Players against Enemies"));
		op2.setToggleGroup(group);
		RadioButton op3 = new RadioButton("PvPvE");
		op3.setTooltip(new Tooltip("Players against players and enemies"));
		op3.setToggleGroup(group);
		Separator sep = new Separator();
		sep.setMinWidth(400);
		Label info = new Label("Please check log file for server logs");
		Label serverState = new Label("Server: "+(Server.server == null || (Server.server != null && Server.server.isClosed()) ? "STOPPED" : "STARTED"));
		Button start = new Button("Start server");
		start.setOnAction(e -> {
			if (Server.server == null || Server.server.isClosed()){
				ServerConfig.server = new Server("127.0.0.1", 1234, 2, 0); // To be changed
				serverState.setText("Server: STARTED");
				updateDisabled(host, port, maxPlayers, op1, op2, op3, hostL, portL, maxPlayersL, gameModeL);
				stage.close();
			}
		});
		Button stop = new Button("Stop server");
		stop.setOnAction(e -> {
			if (Server.server != null && !Server.server.isClosed()){
				try {
					Server.server.close();
				} catch (Exception ex){
					ex.printStackTrace();
				}
				serverState.setText("Server: STOPPED");
				Logger.info("Server stopped");
				updateDisabled(host, port, maxPlayers, op1, op2, op3, hostL, portL, maxPlayersL, gameModeL);
			}
		});
		
		updateDisabled(host, port, maxPlayers, op1, op2, op3);
		
		layout.add(hostL, 0, 0);
		layout.add(portL, 0, 1);
		layout.add(maxPlayersL, 0, 2);
		layout.add(gameModeL, 0, 3);
		layout.add(host, 1, 0);
		layout.add(port, 1, 1);
		layout.add(maxPlayers, 1, 2);
		layout.add(op1, 0, 4, 2, 1);
		layout.add(op2, 0, 5, 2, 1);
		layout.add(op3, 0, 6, 2, 1);
		layout.add(sep, 0, 7, 2, 1);
		layout.add(serverState, 0, 8);
		layout.add(info, 0, 9, 2, 1);
		layout.add(new HBox(5, start, stop), 0, 10, 2, 1);
		
		return layout;
	}
	
	public void start(Stage stage){
		this.stage = stage;
		stage.setTitle("Projectile server config");
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(getLayout(), 500, 300));
		stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
