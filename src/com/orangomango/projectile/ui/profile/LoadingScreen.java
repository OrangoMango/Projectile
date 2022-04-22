package com.orangomango.projectile.ui.profile;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
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
		"font/main_font.ttf"
	};
	
	private static void downloadFile(String link, String path) {
		try (InputStream in = new URL(link).openStream()) {
			Files.copy(in, Paths.get(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public LoadingScreen(Stage exStage){
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setOnCloseRequest(e -> {
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
					updateProgress(i, files.length*2);
					updateMessage(files[i]+"... "+(new DecimalFormat("##.##").format((double)i/files.length*100))+"%");
				}
				
				updateProgress(files.length, files.length*2);
				updateMessage("Loading sounds...");
				
				SCORE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/score.wav");
				updateProgress(files.length+1, files.length*2);
				SHOOT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/shoot.wav");
				updateProgress(files.length+2, files.length*2);
				DAMAGE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/damage.wav");
				updateProgress(files.length+3, files.length*2);
				EXPLOSION_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/explosion.wav");
				updateProgress(files.length+4, files.length*2);
				BACKGROUND_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/background.mp3");
				updateProgress(files.length+5, files.length*2);
				DEATH_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/death.wav");
				updateProgress(files.length+6, files.length*2);
				EXTRA_LIFE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/extra_life.wav");
				updateProgress(files.length+7, files.length*2);
				BOSS_DEATH_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_death.wav");
				updateProgress(files.length+8, files.length*2);
				BOSS_BATTLE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_battle.wav");
				updateProgress(files.length+9, files.length*2);
				BOSS_HP_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_hp.wav");
				updateProgress(files.length+10, files.length*2);
				BOSS_SUPER_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_super.wav");
				updateProgress(files.length+11, files.length*2);
				BOSS_HIT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_hit.wav");
				updateProgress(files.length+12, files.length*2);
				MENU_BACKGROUND_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/menu_background.wav");
				updateProgress(files.length+13, files.length*2);
				CONFIRM_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/confirm.wav");
				updateProgress(files.length+14, files.length*2);
				SELECT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/select.wav");
				updateProgress(files.length+15, files.length*2);
				SHOW_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/show.wav");
				updateProgress(files.length+16, files.length*2);
				NOTIFICATION_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/notification.wav");
				updateProgress(files.length+17, files.length*2);
				SCORE_LOST_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/score_lost.wav");
				updateProgress(files.length+18, files.length*2);
				
				updateMessage("Done");
				
				Platform.runLater(() -> {
					stage.close();
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
