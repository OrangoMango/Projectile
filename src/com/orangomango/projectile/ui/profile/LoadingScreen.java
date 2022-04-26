package com.orangomango.projectile.ui.profile;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.geometry.Insets;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.text.DecimalFormat;

import static com.orangomango.projectile.MainApplication.*;

public class LoadingScreen{
	private static String[] files = new String[]{
		"audio/background.mp3",
		"audio/boss_battle.wav",
		"audio/boss_death.wav",
		"audio/boss_hit.wav",
		"audio/boss_hp.wav",
		"audio/boss_super.wav",
		"audio/confirm.wav",
		"audio/damage.wav",
		"audio/death.wav",
		"audio/explosion.wav",
		"audio/extra_life.wav",
		"audio/menu_background.wav",
		"audio/notification.wav",
		"audio/score_lost.wav",
		"audio/score.wav",
		"audio/select.wav",
		"audio/shoot.wav",
		"audio/show.wav",
		"font/main_font.ttf",
		"image/projectile_logo.png"
	};
	
	private static void downloadFile(String link, String path) {
		try (InputStream in = new URL(link).openStream()) {
			Files.copy(in, Paths.get(path));
		} catch (Exception ex) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Connection error");
			alert.setTitle("Connection error");
			alert.setContentText("An error occured while downloading, ("+ex.getMessage()+").\nPlease try again later");
			alert.showAndWait();
		}
	}
	
	private static void deleteDirectory(File f){
		File[] contents = f.listFiles();
		if (contents != null){
			for (File file : contents){
				deleteDirectory(file);
			}
		}
		f.delete();
	}
	
	public LoadingScreen(Stage exStage){
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setOnCloseRequest(e -> {
			deleteDirectory(new File(System.getProperty("user.home")+File.separator+".projectile"));
			System.exit(0);
		});
		GridPane pane = new GridPane();
		pane.setVgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		
		Task task = new Task(){
			protected Object call() throws Exception{
				updateMessage("Downloading assets...");
				Thread.sleep(500);
				for (int i = 0; i < files.length; i++){
					downloadFile("https://github.com/OrangoMango/Projectile/raw/main/assets/"+files[i], System.getProperty("user.home")+File.separator+".projectile"+File.separator+"assets"+File.separator+files[i].replace("/", File.separator));
					updateProgress(i, files.length);
					updateMessage(files[i]+"... "+(new DecimalFormat("##.##").format((double)i/files.length*100))+"%");
				}
				
				updateProgress(files.length, files.length);
				setupSounds();
				updateMessage("Done");
				
				Platform.runLater(() -> {
					stage.hide();
					exStage.show();
					startPage.run();
				});
				
				return null;
			}
		};
		Label label = new Label();
		label.textProperty().bind(task.messageProperty());
		ProgressBar bar = new ProgressBar();
		bar.setMinWidth(360);
		bar.progressProperty().bind(task.progressProperty());
		pane.add(label, 0, 0);
		pane.add(bar, 0, 1);
		
		new Timer().schedule(new TimerTask(){
			@Override
			public void run(){
				new Thread(task).run();
			}
		}, 1000);
		
		stage.setScene(new Scene(pane, 400, 300));
		stage.show();
	}
}