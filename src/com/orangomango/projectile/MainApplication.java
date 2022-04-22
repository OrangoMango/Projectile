package com.orangomango.projectile;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.util.*;
import java.io.File;

import com.orangomango.projectile.ui.*;
import com.orangomango.projectile.ui.profile.*;

/**
 * MainApplication for the game. This class contains the main method
 * Code entirly written by OrangoMango (orangomango.github.io)
 * Licence MIT
 * @author OrangoMango
 * @version 1.0
 */
public class MainApplication extends Application {
	
	public static List<Entity> entities = Collections.synchronizedList(new ArrayList<Entity>());
	private static List<Explosion> explosions = Collections.synchronizedList(new ArrayList<Explosion>());
	private static ArrayList<AudioClip> clips = new ArrayList<>();
	public static HashMap<String, Double> userGamedata = new HashMap<>();
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 800;
	public static final int FPS = 40;
	public static Runnable startPage;
	public static Runnable gameoverPage;
	public static Runnable recordsPage;
	
	public static volatile boolean addingAllowed = true;
	private static boolean audioAllowed = true;
	public static int score;
	private static int counter = 3;
	private static boolean gameStarted;
	public static volatile boolean threadRunning;
	private static long explosionStart;
	private static long rechargeStart;
	private static long gameStart;
	public static Timeline loop;
	private static volatile boolean paused;
	private static BonusPoint point;
	private static BonusPoint point2;
	public static int bossCount;
	public static int bulletCount;
	public static int enemyDamageCount;
	public static Notification notification;
	private static boolean hpCheck;
	public static boolean bossCheck;
	public static String difficulty;
	public static boolean firstTime;
	
	public static final String MAIN_FONT = "file://"+System.getProperty("user.home")+"/.projectile/assets/font/main_font.ttf";
	
	public static Media SCORE_SOUND;
	public static Media SHOOT_SOUND;
	public static Media DAMAGE_SOUND;
	public static Media EXPLOSION_SOUND;
	public static Media BACKGROUND_SOUND;
	public static Media DEATH_SOUND;
	public static Media EXTRA_LIFE_SOUND;
	public static Media BOSS_DEATH_SOUND;
	public static Media BOSS_BATTLE_SOUND;
	public static Media BOSS_HP_SOUND;
	public static Media BOSS_SUPER_SOUND;
	public static Media BOSS_HIT_SOUND;
	public static Media MENU_BACKGROUND_SOUND;
	public static Media CONFIRM_SOUND;
	public static Media SELECT_SOUND;
	public static Media SHOW_SOUND;
	public static Media NOTIFICATION_SOUND;
	public static Media SCORE_LOST_SOUND;
		
	private static final int[] diffEasy = new int[]{10, 15, 20, 10, 20, 40, 50, 60, 60, 70, 1000, 2000, 500, 500, 40000, 25000, 450, 15, 8};
	private static final int[] diffMedium = new int[]{15, 20, 25, 10, 20, 50, 60, 70, 70, 80, 900, 1900, 400, 400, 36000, 30000, 550, 22, 7};
	private static final int[] diffHard = new int[]{20, 25, 30, 20, 30, 60, 70, 80, 80, 90, 850, 1850, 300, 300, 34000, 35000, 700, 30, 6};
	private static final int[] diffExtreme = new int[]{30, 35, 40, 30, 40, 70, 80, 90, 90, 100, 750, 1750, 300, 250, 30000, 40000, 850, 45, 5};	
	public static int[] currentDiff;
	
	public static double enemySpeedDiff;
	
	public static void main(String[] args){
		firstTime = !((new File(System.getProperty("user.home")+File.separator+".projectile")).exists());
		ProfileManager.setupDirectory();
		launch(args);
	}
	
	private static Canvas getCanvas(){
		
		// Reset static variables
		score = 0;
		bossCount = 0;
		bulletCount = 0;
		enemyDamageCount = 0;
		explosionStart = 0;
		rechargeStart = 0;
		threadRunning = true;
		gameStarted = false;
		hpCheck = false;
		bossCheck = false;
		explosions.clear();
		entities.clear();
		bulletCount = 0;
		enemyDamageCount = 0;
		
		if (difficulty.equals("easy")){
			currentDiff = diffEasy;
			enemySpeedDiff = 3;
		} else if (difficulty.equals("medium")){
			currentDiff = diffMedium;
			enemySpeedDiff = 3.1;
		} else if (difficulty.equals("hard")){
			currentDiff = diffHard;
			enemySpeedDiff = 3.2;
		} else {
			currentDiff = diffExtreme;
			enemySpeedDiff = 3.3;
		}
		
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		notification = new Notification(gc);
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.loadFont(MAIN_FONT, 25));
		gc.fillText("Press SPACE to start\n- Collect yellow circles to earn 50 points\n- Shoot the enemies once they are completely spawned\n- Remember to use grenades\n- Defeat the boss(es) once you arrive at 1700 points\n- Recharge your hp when you think it's time to\n- Go outside the screen to come from the other side\n- Survive as much time as possible\nGood luck!", 70, 250);
		
		ProfileManager pm = new ProfileManager();
		Player player = new Player(gc, 400, 400, "#0000ff", "#E2F5F6", pm);
		
		Random random = new Random();
		
		point = new BonusPoint(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-95)+95-20);
		point2 = new BonusPoint(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-95)+95-20);
		
		entities.add(player);
		
		canvas.setOnKeyPressed(e -> {
			switch (e.getCode()){
				case W:
					if (!gameStarted || paused) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveY(-1);
					} else {
						player.setY(player.getY()-player.getSpeed());
					}
					break;
				case A:
					if (!gameStarted || paused) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveX(-1);
					} else {
						player.setX(player.getX()-player.getSpeed());
					}
					break;
				case S:
					if (!gameStarted || paused) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveY(1);
					} else {
						player.setY(player.getY()+player.getSpeed());
					}
					break;
				case D:
					if (!gameStarted || paused) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveX(1);
					} else {
						player.setX(player.getX()+player.getSpeed());
					}
					break;
				case SPACE:
					if (paused) return;
					if (!gameStarted){
						startSpawning(gc, player);
						update(gc, player);
						gameStarted = true;
						playSound(BACKGROUND_SOUND, true, 1.0, false);
						gameStart = System.currentTimeMillis();
						point.startTimer();
						point2.startTimer();
						System.out.println(difficulty);
					}
					break;
				case Q:
					if (System.currentTimeMillis() < rechargeStart+currentDiff[15] || paused || player.hp == 100) return;
					rechargeStart = System.currentTimeMillis();
					userGamedata.put("recharges", userGamedata.getOrDefault("recharges", 0.0)+1);
					if (player.hp+40 > 100){
						player.hp = 100;
					} else {
						player.hp += 40;
					}
					playSound(EXTRA_LIFE_SOUND, false, null, false);
					break;
				case P:
					if (!paused){
						loop.pause();
						if (player.movement != null){
							player.movement.pause();
						}
						paused = !paused;
					} else {
						gc.save();
						Timeline resume = new Timeline(new KeyFrame(Duration.millis(1000), rE -> {
							String text;
							if (counter == 0){
								text = "Go!";
							} else {
								text = counter+"..";
								counter--;
							}
							/*gc.setTextAlign(TextAlignment.CENTER);
							gc.setFont(new Font("sans-serif", 60));
							gc.setFill(Color.WHITE);
							gc.fillText(text, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
							*/
							System.out.println(text);
						}));
						resume.setCycleCount(4);
						resume.setOnFinished(rE -> {
							counter = 3;
							paused = !paused;
							gc.restore();
							loop.play();
							if (player.movement != null){
								player.movement.play();
							}
						});
						resume.play();
					}
					break;
			}
		});
		
		canvas.setOnMousePressed(e -> {
			if (!gameStarted || paused) return;
			if (!player.shooting){
				player.shooting = true;
				player.speed /= 2;
			}
			switch (e.getButton()){
				case PRIMARY:
					if (player.shootingAllowed) bulletCount++;
					player.shoot(e.getX(), e.getY(), false);
					break;
				case SECONDARY:
					if (System.currentTimeMillis() < explosionStart+4500 || !player.shootingAllowed) return;
					explosionStart = System.currentTimeMillis();
					userGamedata.put("explosions", userGamedata.getOrDefault("explosions", 0.0)+1);
					player.shoot(e.getX(), e.getY(), true);
					break;
			}
		});
		
		canvas.setOnMouseDragged(e -> {
			if (!gameStarted || paused) return;
			if (!player.shooting){
				player.shooting = true;
				player.speed /= 2;
			}
			switch (e.getButton()){
				case PRIMARY:
					if (player.shootingAllowed) bulletCount++;
					player.shoot(e.getX(), e.getY(), false);
					break;
				case SECONDARY:
					if (System.currentTimeMillis() < explosionStart+4500 || !player.shootingAllowed) return;
					explosionStart = System.currentTimeMillis();
					userGamedata.put("explosions", userGamedata.getOrDefault("explosions", 0.0)+1);
					player.shoot(e.getX(), e.getY(), true);
					break;
			}
		});
		
		canvas.setOnMouseReleased(e -> {
			if (!gameStarted || paused) return;
			player.speed *= 2;
			player.shooting = false;
		});
		
		return canvas;
	}
	
	@Override
	public void start(Stage stage){
		stage.setOnCloseRequest(cr -> System.exit(0));
		stage.setResizable(false);
		
		gameoverPage = () -> {
			GameoverScreen screen = new GameoverScreen();
			stage.setScene(screen.getScene());
		};
		
		startPage = () -> {
			HomeScreen homescreen = new HomeScreen();
			HomeScreen.buttons.clear();
			homescreen.startEvent = () -> stage.setScene(new Scene(new TilePane(getCanvas()), SCREEN_WIDTH, SCREEN_HEIGHT));
			stage.setScene(homescreen.getScene());
		};
		
		recordsPage = () -> {
			RecordsScreen records = new RecordsScreen();
			stage.setScene(records.getScene());
		};

		if (firstTime){
			LoadingScreen ls = new LoadingScreen(stage);
		} else {
			SCORE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/score.wav");
			SHOOT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/shoot.wav");
			DAMAGE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/damage.wav");
			System.out.println("Done 3");
			EXPLOSION_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/explosion.wav");
			BACKGROUND_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/background.mp3");
			DEATH_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/death.wav");
			EXTRA_LIFE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/extra_life.wav");
			BOSS_DEATH_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_death.wav");
			BOSS_BATTLE_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_battle.wav");
			BOSS_HP_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_hp.wav");
			BOSS_SUPER_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_super.wav");
			BOSS_HIT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/boss_hit.wav");
			MENU_BACKGROUND_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/menu_background.wav");
			CONFIRM_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/confirm.wav");
			SELECT_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/select.wav");
			SHOW_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/show.wav");
			NOTIFICATION_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/notification.wav");
			SCORE_LOST_SOUND = new Media("file://"+System.getProperty("user.home")+"/.projectile/assets/audio/score_lost.wav");
			startPage.run();
			stage.show();
		}
	}

	private static void startSpawning(GraphicsContext gc, Player player){
		Random random = new Random();
		final int MIN = currentDiff[10];
		final int MAX = currentDiff[11];
		new Thread(() -> {
			try {
				Thread.sleep(5000); // Give player 5s time
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
			while (threadRunning){
				if (paused || entities.size() == currentDiff[18]) continue;
				boolean bossFound = false;
				for (int i = 0; i < entities.size(); i++){
					if (entities.get(i) instanceof Boss){
						bossFound = true;
						break;
					}
				}
				Enemy en = new Enemy(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-20)+10, "#ff0000", "#FFA3B2", player);
				if (score >= 1700 && score >= bossCount+1500 && !bossFound){
					Boss boss = new Boss(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-20)+10, "#F69E43", "#F4C99C", player);
					bossFound = true;
					entities.add(boss);
					stopAllSounds();
					playSound(BOSS_BATTLE_SOUND, true, 0.35, false);
				}
				if (score < 500){
					en.setHP(currentDiff[3]); // You can one-shot them
					en.setDamage(currentDiff[0]);
				} else if (score >= 500 && score < 700){
					if (random.nextInt(100) <= 10 && !bossFound){ // Spawn mini-boss with 10% probability
						en.setHP(currentDiff[5]);
						en.setWidth(70);
						en.setDamage(currentDiff[1]);
					} else {
						en.setHP(10);
						en.setDamage(currentDiff[0]);
					}
				} else if (score >= 700 && score < 1000){
					if (random.nextInt(100) <= 10 && !bossFound){
						en.setHP(currentDiff[6]);
						en.setWidth(70);
						en.setDamage(currentDiff[0]);
					} else {
						en.setHP(currentDiff[3]);
						en.setDamage(currentDiff[1]);
					}
				} else if (score >= 1000 && score < 1500){
					int number = random.nextInt(100);
					if (number <= 7 && !bossFound){ // Spawn mini-boss2 with 7% probability
						en.setHP(currentDiff[8]);
						en.setWidth(85);
						en.setDamage(currentDiff[2]);
					} else if (number > 7 && number <= 17 && !bossFound){
						en.setHP(currentDiff[6]);
						en.setWidth(70);
						en.setDamage(15);
					} else {
						en.setHP(currentDiff[3]);
						en.setDamage(currentDiff[0]);
					}
				} else {
					int number = random.nextInt(100);
					if (number <= 7 && !bossFound){
						en.setHP(currentDiff[9]);
						en.setWidth(85);
						en.setDamage(20);
					} else if (number > 7 && number <= 17 && !bossFound){
						en.setHP(currentDiff[7]);
						en.setWidth(70);
						en.setDamage(20);
					} else {
						en.setHP(bossFound ? currentDiff[3] : currentDiff[4]);
						en.setDamage(currentDiff[0]);
					}
				}
				
				if (addingAllowed){
					entities.add(en);
				} else {
					while (!addingAllowed){
						entities.add(en);
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex){
							ex.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(random.nextInt(MAX-MIN)+MIN);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	private static void update(GraphicsContext gc, Player player){
		userGamedata.clear();
		Random random = new Random();
		MainApplication.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), ev -> {
			long now = System.currentTimeMillis();
			long cooldown = now-explosionStart < 4500 ? now-explosionStart : 4500;
			long cooldown2 = now-rechargeStart < currentDiff[15] ? now-rechargeStart : currentDiff[15];
			
			gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
			for (int i = 0; i < entities.size(); i++){
				entities.get(i).draw();
			}
			Iterator<Explosion> explosionIterator = explosions.iterator();
			while (explosionIterator.hasNext()){
				try {
					Explosion exp = explosionIterator.next();
					exp.explode();
					if (exp.radius >= 150){
						explosionIterator.remove();
						for (int i = 0; i < entities.size(); i++){
							try {
								Entity ent = entities.get(i);
								if (exp.collided(ent)){
									if (ent instanceof Enemy && !((Enemy)ent).spawning){
										((Enemy)ent).takeDamage(exp.damage, i);
									} else if (ent instanceof Boss){
										((Boss)ent).takeDamage(exp.damage, i);
									} else {
										ent.takeDamage(exp.damage);
									}
								}
							} catch (ConcurrentModificationException ex){
								System.out.println("-- error (2.1)");
							}
						}
					}
				} catch (ConcurrentModificationException ex){
					System.out.println("-- error (2.2)");
				}
			}
			if (point.isOnPlayer(player)){
				point.setRandomPosition(random);
				point.startTimer();
				score += 50;
				userGamedata.put("bonusPoints", userGamedata.getOrDefault("bonusPoints", 0.0)+1);
				playSound(SCORE_SOUND, false, null, false);
			}
			point.draw();
			if (point2.isOnPlayer(player)){
				point2.setRandomPosition(random);
				point2.startTimer();
				score += 50;
				userGamedata.put("bonusPoints", userGamedata.getOrDefault("bonusPoints", 0.0)+1);
				playSound(SCORE_SOUND, false, null, false);
			}
			point2.draw();
			Iterator<Bullet> iterator = Player.bullets.iterator();
			boolean removed = false;
			while (iterator.hasNext()){
				Bullet b = iterator.next();
				if (b.getX() <= 0 || b.getX() >= SCREEN_WIDTH || b.getY() <= 0 || b.getY() >= SCREEN_HEIGHT){
					iterator.remove();
				}
				for (int i = 0; i < entities.size(); i++){
					try {
						Entity e = entities.get(i);
						if (e instanceof Enemy && e.collided(b.getX(), b.getY(), 20) && !((Enemy)e).spawning){
							if (!b.doExplosion){
								((Enemy)e).takeDamage(10, i);
								enemyDamageCount++;
							}
							if (!removed){
								if (b.doExplosion){
									Explosion explosion = new Explosion(gc, b.getX(), b.getY());
									explosion.damage = 20;
									explosions.add(explosion);
								}
								iterator.remove();
								removed = true;
							}
						} else if (e instanceof Boss && e.collided(b.getX(), b.getY(), 20)){
							if (!b.doExplosion){
								((Boss)e).takeDamage(10, i);
								enemyDamageCount++;
							}
							if (!removed){
								if (b.doExplosion){
									Explosion explosion = new Explosion(gc, b.getX(), b.getY());
									explosion.damage = 20;
									explosions.add(explosion);
								}
								iterator.remove();
								removed = true;
							}
						}
					} catch (ConcurrentModificationException exc){
						exc.printStackTrace();
						System.out.println("-- error (3)");
						System.exit(0);
					}
				}
				b.travel();
				removed = false;
			}
			for (int i = 0; i < Boss.supers.size(); i++){
				BossSuper bs = Boss.supers.get(i);
				bs.spawn();
				for (int j = 0; j < entities.size(); j++){
					Entity ent = entities.get(j);
					if (bs.detectCollision(ent)){
						if (ent instanceof Enemy){
							((Enemy)ent).takeDamage(25, j);
						} else if (ent instanceof Player){
							ent.takeDamage(5);
						}
					}
				}
			}
			
			// Draw hp bar
			gc.setFill(Color.web(getHPColor(player.hp)));
			gc.fillRect(20, 20, 200*player.hp/100 <= 0 ? 0 : 200*player.hp/100, 30);
			gc.setStroke(Color.web("#41D4DD"));
			gc.setLineWidth(4);
			gc.strokeRect(20, 20, 200, 30);
			
			// Draw cooldown bar
			gc.setFill(Color.web("#DCCA20"));
			gc.fillRect(20, 60, 200*((int)cooldown)/4500, 15);
			gc.setStroke(Color.web("#7F7518"));
			gc.setLineWidth(4);
			gc.strokeRect(20, 60, 200, 15);
			
			// Draw cooldown2 bar
			gc.setFill(Color.web("#60EFC6"));
			gc.fillRect(20, 85, 200*((int)cooldown2)/currentDiff[15], 15);
			gc.setStroke(Color.web("#0BA77A"));
			gc.strokeRect(20, 85, 200, 15);
			
			// Draw score
			userGamedata.put("score", (double)score);
			gc.setFill(Color.WHITE);
			gc.setFont(Font.loadFont(MAIN_FONT, 35));
			gc.fillText(Integer.toString(score), 230, 50);
			
			// Draw boss hp bar
			Boss bossFound = null;
			for (int i = 0; i < entities.size(); i++){
				if (entities.get(i) instanceof Boss){
					bossFound = (Boss)entities.get(i);
					break;
				}
			}			
			if (bossFound != null){
				gc.setFill(Color.web("#F13755"));
				gc.fillRect(400, 40, 400*bossFound.getHP()/bossFound.startHP, 30);
				if (bossFound.drawDamage){
					gc.setFill(Color.web("#F7ACBA"));
					double barWidth = 400*bossFound.damageHP/bossFound.startHP;
					gc.fillRect(400+400*bossFound.getHP()/bossFound.startHP, 40, barWidth, 30);
				}
				gc.setStroke(Color.web("#A71A31"));
				gc.strokeRect(400, 40, 400, 30);
			}
					
			// Draw progress bar for boss
			int scoreForBoss = score+(score <= 1700 ? 1700-score : 1500-(score-bossCount));
			if (score < scoreForBoss){
				gc.setFill(Color.web("#980F40"));
				gc.fillRect(20, 110, 200*(score > 1700 ? (score-bossCount)/1500.0 : score/1700.0), 20);
				gc.setStroke(Color.web("#620929"));
				gc.strokeRect(20, 110, 200, 20);
			}
			if (scoreForBoss-score <= 100 && !bossCheck){
				bossCheck = true;
				notification.setText("Boss is arriving!");
				notification.mustShow = true;
				playSound(NOTIFICATION_SOUND, false, 1.0, false);
			}
			
			// Draw timer
			userGamedata.put("gameTime", (double)(now-gameStart));
			gc.setFont(Font.loadFont(MAIN_FONT, 30));
			gc.setFill(Color.WHITE);
			gc.fillText(String.format("%s:%s", (now-gameStart)/60000, (now-gameStart)/1000%60), 30, SCREEN_HEIGHT-20);

			if (player.hp <= 40 && !hpCheck){
				hpCheck = true;
				notification.setText("HP is low!");
				notification.mustShow = true;
				playSound(NOTIFICATION_SOUND, false, 1.0, false);
			}
			if (player.hp >= 70){
				hpCheck = false;
			}

			// Show notification
			notification.show();
			
		}));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();
	}

	public static void playSound(Media sound, boolean loop, Double volume, boolean skip){
		if (!audioAllowed && skip) return;
		AudioClip ac = new AudioClip(sound.getSource());
		clips.add(ac);
		if (loop){
			ac.setCycleCount(AudioClip.INDEFINITE);
		} else {
			ac.setVolume(0.9);
		}
		if (volume != null){
			ac.setVolume(volume);
		}
		ac.play();
		audioAllowed = false;
		new Timer().schedule(new TimerTask(){
			@Override
			public void run(){
				audioAllowed = true;
			}
		}, 150);
	}
	
	public static void stopAllSounds(){
		for (int i = 0; i < clips.size(); i++){
			clips.get(i).stop();
		}
	}
	
	/**
	 * @param hp in percentage
	 * HP bar colors
	 * 0-10 Red
	 * 10-30 Orange
	 * 30-50 Yellow
	 * 50-70 Dark green
	 * 70-100 Lime
	 */
	private static String getHPColor(int hp){
		if (hp >= 0 && hp < 10){
			return "#F52F20";
		} else if (hp >= 10 && hp < 30){
			return "#E48B40";
		} else if (hp >= 30 && hp < 50){
			return "#EDDD34";
		} else if (hp >= 50 && hp < 70){
			return "#569B21";
		} else {
			return "#65E85C";
		}
	}
}
