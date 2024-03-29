package com.orangomango.projectile.ui.profile;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.image.*;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.geometry.Insets;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.text.DecimalFormat;

import static com.orangomango.projectile.MainApplication.*;

public class LoadingScreen{
	private static final String[] files = new String[]{
		"audio/ammo_reload.wav",
		"audio/background.wav",
		"audio/boss_battle.wav",
		"audio/boss_death.wav",
		"audio/boss_hit.wav",
		"audio/boss_hp.wav",
		"audio/boss_super.wav",
		"audio/confirm.wav",
		"audio/damage.wav",
		"audio/death.wav",
		"audio/drop.wav",
		"audio/explosion.wav",
		"audio/extra_life.wav",
		"audio/fast_gun.wav",
		"audio/gameover_background.wav",
		"audio/machine_gun.wav",
		"audio/menu_background.wav",
		"audio/notification.wav",
		"audio/no_ammo.wav",
		"audio/score_lost.wav",
		"audio/score.wav",
		"audio/select.wav",
		"audio/shoot.wav",
		"audio/shotgun.wav",
		"audio/show.wav",
		"audio/small_gun.wav",
		"audio/sniper.wav",
		"audio/space_gun.wav",
		"audio/super_sniper.wav",
		"audio/triple_gun.mp3",
		"font/main_font.ttf",
		"image/achievements.png",
		"image/achievement_notification.png",
		"image/bullet_off.png",
		"image/bullet_on.png",
		"image/homescreen.png",
		"image/projectile_logo.png"
	};
	
	public static final String[] guns = new String[]{
		//"bouncy_gun.gbs",
		"double_normal_gun.gbs",
		"grenade_launcher.gbs",
		"machine_gun.gbs",
		"normal_gun.gbs",
		"pirce_gun.gbs",
		"shotgun_epic.gbs",
		"small_gun.gbs",
		"sniper_common.gbs",
		"sniper_epic.gbs",
		"space_gun.gbs",
		"super_shotgun.gbs",
		"super_sniper.gbs",
		"uzi.gbs"
	};
	
	private static void downloadFile(String link, String path){
		try {
			InputStream in = new URL(link).openStream();
			Files.copy(in, Paths.get(path));
			in.close();
		} catch (Exception ex){
			ex.printStackTrace();
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
			deleteDirectory(new File(prefixPath));
			System.exit(0);
		});
		GridPane pane = new GridPane();
		pane.setVgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));
		
		Task task = new Task(){
			@Override
			protected Object call(){
				try {
					updateMessage("Downloading assets...");
					Thread.sleep(500);
					for (int i = 0; i < files.length; i++){
						downloadFile("https://github.com/OrangoMango/Projectile/raw/main/assets/"+files[i], prefixPath+File.separator+"assets"+File.separator+files[i].replace("/", File.separator));
						updateProgress(i, files.length+guns.length);
						updateMessage(files[i]+"... "+(new DecimalFormat("##.##").format((double)i/(files.length+guns.length)*100))+"%");
						Logger.info("Downloaded "+files[i]);
					}
					
					for (int i = 0; i < guns.length; i++){
						downloadFile("https://github.com/OrangoMango/Projectile/raw/main/assets/gun/"+guns[i], prefixPath+File.separator+"assets"+File.separator+"gun"+File.separator+guns[i]);
						updateProgress(files.length+i, files.length+guns.length);
						updateMessage("gun/"+guns[i]+"... "+(new DecimalFormat("##.##").format((double)(files.length+i)/(files.length+guns.length)*100))+"%");
						Logger.info("Downloaded gun/"+guns[i]);
					}
					
					updateProgress(files.length+guns.length, files.length+guns.length);
					setupSounds();
					loadGuns();
					updateMessage("Done");
					
					Platform.runLater(() -> {
						stage.hide();
						exStage.show();
						startPage.run();
					});
				} catch (Exception e){
					Logger.error(e.getMessage());
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText("An error occured");
					alert.setTitle("Unknown error");
					alert.setContentText("An error occured while downloading, ("+e.getMessage()+").\nPlease try again later");
					alert.showAndWait();
				}
				return null;
			}
		};
		Label label = new Label();
		label.textProperty().bind(task.messageProperty());
		ProgressBar bar = new ProgressBar();
		bar.setMinWidth(360);
		bar.progressProperty().bind(task.progressProperty());
		pane.add(new ImageView(new Image("https://github.com/OrangoMango/Projectile/raw/main/assets/image/loadingScreen.png")), 0, 0);
		pane.add(label, 0, 1);
		pane.add(bar, 0, 2);
		
		schedule(() -> new Thread(task).run(), 1000);
		
		stage.setScene(new Scene(pane, 420, 500));
		stage.show();
	}
}
