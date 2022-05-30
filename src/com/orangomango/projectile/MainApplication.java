package com.orangomango.projectile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.*;
import javafx.scene.media.*;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.geometry.Rectangle2D;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.io.*;

import com.orangomango.projectile.ui.*;
import com.orangomango.projectile.ui.profile.*;
import com.orangomango.projectile.multiplayer.*;

/**
 * MainApplication for the game. This class contains the main method
 * Code entirely written by OrangoMango (orangomango.github.io)
 * License MIT
 * @author OrangoMango
 * @version 1.1
 */
public class MainApplication extends Application {
	
	public static String userHome = "";
	
	public static List<Entity> entities = Collections.synchronizedList(new ArrayList<Entity>());
	private static List<Explosion> explosions = Collections.synchronizedList(new ArrayList<Explosion>());
	private static ArrayList<AudioClip> clips = new ArrayList<>();
	private static List<FloatingText> floatingTexts = Collections.synchronizedList(new ArrayList<FloatingText>());
	public static List<Drop> drops = Collections.synchronizedList(new ArrayList<Drop>());
	public static HashMap<String, Double> userGamedata = new HashMap<>();
	public static int SCREEN_WIDTH =  1000; // those values are used also in other files
	public static int SCREEN_HEIGHT = 800;
	public static final int FPS = 40;
	public static Runnable startPage;
	public static Runnable gameoverPage;
	public static Runnable recordsPage;
	private static Canvas currentCanvas;
	private static Stage currentStage;

	private static boolean audioAllowed = true;
	public static int score;
	private static int counter = 3;
	private static boolean gameStarted;
	public static volatile boolean threadRunning;
	private static long explosionStart;
	private static long rechargeStart;
	private static long gameStart;
	private static long pausedTime;
	public static Timeline loop;
	private static volatile boolean paused;
	public static BonusPoint point;
	public static BonusPoint point2;
	private static Pointer pointer1;
	private static Pointer pointer2;
	public static int bossCount;
	public static int bulletCount;
	public static int enemyDamageCount;
	public static Notification notification;
	private static boolean hpCheck;
	public static boolean bossCheck;
	public static String difficulty;
	public static boolean firstTime;
	public static boolean bossInGame;
	private static boolean messageSkipped;
	private static boolean doneAlpha;
	private static BulletConfig config;
	private static double ammoDrawing;
	private static Timeline reloading;
	private static ArrayList<BulletConfig> availableGuns = new ArrayList<>();
	private static int savedAmmo;
	private static boolean showingPause;
	public static Player player;
	
	public static boolean playWithTutorial;
	private static TutorialMessage tutorialMsg;
	private static boolean showingTutorialMessage;
	private static String[] messages = new String[]{"Use WASD to move and left-click to\nshoot. This can be changed later", "Use right-click to launch grenades\n- Your cooldown is limited", "Explosion will damage entities\n(including you) in that area", "Use Q to recharge your HP and\nP to pause/resume", "I think now you are ready to\ndefeat everyone!", "The bars at the top show your hp\nand progress for the boss..", "ah yes, collect the yellow points\nin time to earn extra points.", "You are done now.. You will\nlearn other tricks soon"};
	private static String[] bossMessages = new String[]{"player;What shall I do now?!?", "boss;Hahaha you will not beat me!", "player;I must kill you, but you have\nsuper powers :(", "boss;I can spawn deadly lines and if my\nhp is over 50% I can recover it", "player;Anything else?", "boss;Don't worry I will not follow you,\nI'm too good", "player;haha let's see..", "player;Maybe I should use grenades to do\nmore damage.. And recharge my hp", "boss;I will kill you haha"};
	private static boolean bossDialog;
	private static volatile int dimIndex;
	
	public static Client client;
	private static GameState gameState;
	public static boolean host;
	
	public static final String MAIN_FONT;
	
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
	public static Media AMMO_RELOAD_SOUND;
	public static Media NO_AMMO_SOUND;
	public static Media[] GUN_SOUNDS = new Media[6];
	public static Media DROP_SOUND;
		
	private static final int[] diffEasy = new int[]{10, 15, 20, 10, 20, 40, 50, 60, 60, 70, 1000, 2000, 500, 500, 40000, 25000, 450, 15, 8};
	private static final int[] diffMedium = new int[]{15, 20, 25, 10, 20, 50, 60, 70, 70, 80, 900, 1900, 400, 400, 36000, 30000, 550, 22, 7};
	private static final int[] diffHard = new int[]{20, 25, 30, 20, 30, 60, 70, 80, 80, 90, 850, 1850, 300, 300, 34000, 35000, 700, 30, 6};
	private static final int[] diffExtreme = new int[]{30, 35, 40, 30, 40, 70, 80, 90, 90, 100, 750, 1750, 300, 250, 30000, 40000, 850, 45, 5};	
	public static int[] currentDiff;
	
	public static double enemySpeedDiff;
	
	private static int threadCont = 0;
	
	static {
		String home = System.getProperty("user.home");
		if (home.contains("\\")){
			home = "/"+home;
		}
		userHome = home.replace("\\", "/");
		MAIN_FONT = "file://"+userHome+"/.projectile/assets/font/main_font.ttf";
	}
	
	/**
	 * Schedule a runnable that will run after x seconds.
	 * @param r The @link Runnable to run
	 * @param delay The runnable will run after @link delay seconds 
	 */
	public static void schedule(Runnable r, int delay){
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ie){
				ie.printStackTrace();
			}
			r.run();
		}, "Schedule-"+(threadCont++));
		t.start();
	}
	
	/**
	 * Application main method. Here it will start the setup for the user directory
	 */
	public static void main(String[] args){
		firstTime = !((new File(System.getProperty("user.home")+File.separator+".projectile")).exists());
		ProfileManager.setupDirectory();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Logger.info("Application (and server) closed")));
		try {
			launch(args);
		} catch (Exception ex){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			Logger.error(sw.toString());
		}
	}
	
	private static void displayTutorialMessageAfter(String message, int delay, BooleanSupplier condition, GraphicsContext gc, Player player){
		MainApplication.schedule(() -> {
			boolean first = condition.getAsBoolean();
			while (!condition.getAsBoolean()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
			if (!first){
				try {
					Thread.sleep(delay);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
			loop.pause();
			if (player.movement != null){
				player.movement.pause();
			}
			tutorialMsg = new TutorialMessage(gc, true, true, message);
			tutorialMsg.show();
			showingTutorialMessage = true;
		}, delay);
	}
	
	private static void rerollBossMessage(GraphicsContext gc){
		String msg = bossMessages[dimIndex++];
		String data1 = msg.split(";")[0];
		String data2 = msg.split(";")[1];
		loop.pause();
		tutorialMsg = new TutorialMessage(gc, data1.equals("player"), data1.equals("player"), data2);
		if (doneAlpha){
			tutorialMsg.setMakeAlpha(false);
		} else {
			doneAlpha = true;
		}
		tutorialMsg.show();
		showingTutorialMessage = true;
	}
	
	private static int gunByName(String name){
		int i = 0;
		for (BulletConfig c : availableGuns){
			if (c.gunName.equals(name)) return i;
			i++;
		}
		return -1;
	}
	
	public static void loadGuns(){
		try {
			for (String name : LoadingScreen.guns){
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(userHome+File.separator+".projectile"+File.separator+"assets"+File.separator+"gun"+File.separator+name)));
				BulletConfig bc = (BulletConfig)ois.readObject();
				bc.loadMedia();
				availableGuns.add(bc);
				ois.close();
			}
		} catch (IOException|ClassNotFoundException e){
			e.printStackTrace();
		}
		Logger.info("Guns: "+availableGuns);
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
		bossDialog = false;
		bossInGame = false;
		messageSkipped = false;
		doneAlpha = false;
		explosions.clear();
		floatingTexts.clear();
		availableGuns.clear();
		drops.clear();
		bulletCount = 0;
		enemyDamageCount = 0;
		dimIndex = 0;
		showingTutorialMessage = false;
		ammoDrawing = 0;
		reloading = null;
		savedAmmo = 0;
		host = false;

		loadGuns();
		
		// Setup SCREEN_WIDTH and SCREEN_HEIGHT based on the device resolution
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		if (bounds.getWidth() < SCREEN_WIDTH) SCREEN_WIDTH = (int)Math.round(bounds.getWidth());
		if (bounds.getHeight() < SCREEN_HEIGHT) SCREEN_HEIGHT = (int)Math.round(bounds.getHeight());

		if (!currentStage.isFullScreen()){
			currentStage.setFullScreenExitHint("Press F to exit fullscreen");
		}
		
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
		
		if (playWithTutorial) currentDiff = diffEasy;
		
		Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setFocusTraversable(true);
		currentCanvas = canvas;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.web("#CFFF59"));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		notification = new Notification(gc);
		
		ProfileManager pm = new ProfileManager();
		Player tPlayer = new Player(gc, 400, 400, "#0000ff", "#E2F5F6", pm);
		if (client != null) tPlayer.user = client.getUsername();
		player = tPlayer;
		
		if (!playWithTutorial){
			gc.save();
			gc.translate((SCREEN_WIDTH-1000)/2, (SCREEN_HEIGHT-800)/2);
			gc.setFill(Color.BLACK);
			gc.setFont(Font.loadFont(MAIN_FONT, 25));
			gc.fillText("Press SPACE to start\n- Collect yellow circles to earn 50 points\n- Shoot the enemies once they are completely spawned\n- Remember to use grenades\n- Defeat the boss(es) once you arrive at 1700 points\n- Recharge your hp when you think it's time to\n- Go outside the screen to come from the other side\n- Survive as much time as possible\nGood luck!", 70, 250);
			if (!difficulty.equals("easy")){
				gc.setFill(Color.RED);
				gc.fillText("Warning: your difficulty is "+difficulty+". Enemies and boss\nare stronger and deal more damage", 70, 550);
			}
			gc.restore();
		} else {
			gameStarted = true;
			update(gc);
			playSound(BACKGROUND_SOUND, true, 1.0, false);
			gameStart = System.currentTimeMillis();
			displayTutorialMessageAfter(messages[dimIndex], 1200, () -> true, gc, player);
		}
		
		Random random = new Random();
		
		point = new BonusPoint(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-95)+95-20, playWithTutorial);
		point.show = !playWithTutorial;
		point2 = new BonusPoint(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-95)+95-20, playWithTutorial);
		point2.show = !playWithTutorial;

		pointer1 = new Pointer(gc, player, point);
		pointer2 = new Pointer(gc, player, point2);
		
		entities.add(player);
		
		if (client != null){
			System.out.println("HERE");
			int num = getClientsCount();
			System.out.println("NUM: "+num);
			if (num == 1){
				host = true;
				GameState ugs = new GameState(entities, explosions, floatingTexts, drops, point, point2);
				//ugs.sendMessage("set", client);
				gameState = ugs;
			} else {
				loadState(gc);
				client.sendPlayer(tPlayer);
				pointer1 = new Pointer(gc, player, point);
				pointer2 = new Pointer(gc, player, point2);
			}
		}
		
		config = availableGuns.get(gunByName("normal_gun"));

		canvas.setOnKeyPressed(e -> {
			switch (e.getCode()){
				case ENTER:
					if (messageSkipped) return;
					messageSkipped = true;
					MainApplication.schedule(() -> messageSkipped = false, 500);
					if (!showingTutorialMessage) return;
					playSound(CONFIRM_SOUND, false, null, true);
					tutorialMsg = null;
					if (bossDialog){
						if (dimIndex == bossMessages.length){
							loop.play();
							bossDialog = false;
							showingTutorialMessage = false;
							pm.setFirstTimeBoss(false);
						} else {
							rerollBossMessage(gc);
						}
					} else {
						BooleanSupplier cond = () -> entities.size() == 1; // This may be variable (maybe?)
						int timeout = 1400;
						if (dimIndex == 0){
							Enemy enemy = new Enemy(gc, 300, 100, "#ff0000", "#FFA3B2", player, false);
							enemy.setHP(30);
							entities.add(enemy);
							timeout = 5000;
						} else if (dimIndex == 1){
							Enemy enemy = new Enemy(gc, 240, 670, "#ff0000", "#FFA3B2", player, false);
							enemy.setHP(20);
							Enemy enemy2 = new Enemy(gc, 350, 670, "#ff0000", "#FFA3B2", player, false);
							enemy2.setHP(20);
							entities.add(enemy);
							entities.add(enemy2);
							timeout = 1200;
						} else if (dimIndex == 2){
							timeout = 1200;
						} else if (dimIndex == 5){
							point.show = true;
							point2.show = true;
							point.startTimer();
							point2.startTimer();
						} else if (dimIndex == 6){
							timeout = 10000;
						}
						
						if (dimIndex+1 <= messages.length-1){
							displayTutorialMessageAfter(messages[++dimIndex], timeout, cond, gc, player);
						} else {
							// Finish the tutorial
							MainApplication.schedule(() -> {
								pm.setTutorialComplete(true);
								playWithTutorial = false;
								loop.stop();
								threadRunning = false;
								Platform.runLater(startPage);
							}, 1300);
						}
						loop.play();
						if (player.movement != null){
							player.movement.play();
						}
						showingTutorialMessage = false;
					}
					break;
				case W:
					if (!gameStarted || paused || showingTutorialMessage) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveY(-1);
					} else {
						player.setY(player.getY()-player.getSpeed());
					}
					break;
				case A:
					if (!gameStarted || paused || showingTutorialMessage) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveX(-1);
					} else {
						player.setX(player.getX()-player.getSpeed());
					}
					break;
				case S:
					if (!gameStarted || paused || showingTutorialMessage) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveY(1);
					} else {
						player.setY(player.getY()+player.getSpeed());
					}
					break;
				case D:
					if (!gameStarted || paused || showingTutorialMessage) return;
					if (pm.getJSON().getInt("input") == 0){
						player.moveX(1);
					} else {
						player.setX(player.getX()+player.getSpeed());
					}
					break;
				case SPACE:
					if (paused) return;
					if (!gameStarted){
						startSpawning(gc);
						update(gc);
						gameStarted = true;
						playSound(BACKGROUND_SOUND, true, 1.0, false);
						gameStart = System.currentTimeMillis();
						point.startTimer();
						point2.startTimer();
					}
					break;
				case E:
					config = availableGuns.get(gunByName("small_gun"));
					config.ammoAmount = config.getStartAmmoAmount();
					player.ammo = config.getAmmo();
				case Q:
					rechargeHP();
					break;
				case P:
					if (showingTutorialMessage) return;
					if (!paused){
						loop.pause();
						pausedTime = System.currentTimeMillis();
						if (player.movement != null){
							player.movement.pause();
						}
						if (reloading != null){
							reloading.pause();
						}
						paused = !paused;
						Logger.info("Game paused");
					} else {
						if (showingPause) return;
						showingPause = true;
						gc.save();
						Timeline resume = new Timeline(new KeyFrame(Duration.millis(1000), rE -> {
							String text;
							if (counter == 0){
								text = "Go!";
							} else {
								text = counter+"..";
								counter--;
							}
							gc.setFill(Color.web("#FA8808"));
							gc.fillRect(SCREEN_WIDTH/2-60, SCREEN_HEIGHT/2-60, 130, 130);
							gc.setStroke(Color.web("#EED828"));
							gc.strokeRect(SCREEN_WIDTH/2-60, SCREEN_HEIGHT/2-60, 130, 130);
							gc.setFont(new Font("sans-serif", 60));
							gc.setFill(Color.WHITE);
							gc.fillText(text, SCREEN_WIDTH/2-60+15, SCREEN_HEIGHT/2-60+90);
						}));
						resume.setCycleCount(4);
						resume.setOnFinished(rE -> {
							counter = 3;
							paused = !paused;
							gc.restore();
							long timePaused = System.currentTimeMillis()-pausedTime;
							gameStart += timePaused;
							explosionStart += timePaused;
							rechargeStart += timePaused;
							point.addToStartTime(timePaused);
							point2.addToStartTime(timePaused);
							loop.play();
							if (player.movement != null){
								player.movement.play();
							}
							if (reloading != null){
								reloading.play();
							}
							showingPause = false;
						});
						resume.play();
						Logger.info("Game resumed");
					}
					break;
				case R:
					reloadAmmo();
					break;
				case F:
					currentStage.setFullScreen(!currentStage.isFullScreen());
					break;
			}
		});

		player.ammo = config.getAmmo();
		
		// Shoot eventhandler
		EventHandler<MouseEvent> eventHandler = e -> {
			if (!gameStarted || paused || showingTutorialMessage || !player.shootingAllowed) return;
			if (!player.shooting){
				player.shooting = true;
				player.speed = player.startSpeed/2;
			}
			
			
			if (System.currentTimeMillis() < explosionStart+4500 && e.getButton() == MouseButton.SECONDARY) return;

			player.shootingAllowed = false;
			schedule(() -> player.shootingAllowed = true, config.getCooldown()+config.getTiming()[0]*config.getTiming()[1]);
			Timeline shot = new Timeline(new KeyFrame(Duration.millis(config.getTiming()[1]), evt -> {
				for (int i = 0; i < config.getCount(); i++){
					switch (e.getButton()){
						case PRIMARY:
							if (player.ammo == 0){
								playSound(NO_AMMO_SOUND, false, null, true);
								if (reloading == null) reloadAmmo();
								return;
							}
							bulletCount++;
							player.shoot(e.getX(), e.getY(), false, config, i);
							break;
						case SECONDARY:
							if (System.currentTimeMillis() < explosionStart+4500 && !config.allowMultipleExplosions) return;
							explosionStart = System.currentTimeMillis();
							userGamedata.put("explosions", userGamedata.getOrDefault("explosions", 0.0)+1);
							player.shoot(e.getX(), e.getY(), true, config, i);
							break;
					}
				}
			}));
			shot.setCycleCount(config.getTiming()[0]);
			shot.play();
		};
		
		canvas.setOnMousePressed(eventHandler);
		canvas.setOnMouseDragged(eventHandler);
		
		canvas.setOnMouseReleased(e -> {
			if (!gameStarted || paused || showingTutorialMessage || reloading != null) return;
			player.speed = player.startSpeed;
			player.shooting = false;
		});
		
		return canvas;
	}
	
	private static int getClientsCount(){
		GameState gs = new GameState();
		gs.sendMessage("list", client);
		Integer num;
		while (true){
			try {
				// Ignore sync gamestates, use only that one that contains the clients count
				num = (Integer)client.listen();
				break;
			} catch (ClassCastException cce){
				continue;
			}
		}
		return num;
	}
	
	private static void rechargeHP(){
		if (System.currentTimeMillis() < rechargeStart+currentDiff[15] || paused || player.hp == player.getStartHP() || showingTutorialMessage) return;
			rechargeStart = System.currentTimeMillis();
			userGamedata.put("recharges", userGamedata.getOrDefault("recharges", 0.0)+1);
			if (player.hp+40 > player.getStartHP()){
				player.hp = player.getStartHP();
			} else {
				player.hp += 40;
			}
			playSound(EXTRA_LIFE_SOUND, false, null, false);
	}
	
	private static void reloadAmmo(){
		if (player.ammo == config.getAmmo() || paused || showingTutorialMessage || config.ammoAmount == 0) return;
		if (reloading != null){
			reloading.stop();
			reloading = null;
			ammoDrawing = 0;
			config.ammoAmount++;
			player.ammo = savedAmmo;
			player.speed = player.startSpeed;
			return;	
		}
		savedAmmo = player.ammo;
		player.ammo = 0;
		player.speed = player.startSpeed/2;
		playSound(AMMO_RELOAD_SOUND, false, 1.0, false);
		config.ammoAmount--;
		reloading = new Timeline(new KeyFrame(Duration.millis(config.getRechargeFrames()[0]), evt -> {
			ammoDrawing += 1.0/config.getRechargeFrames()[1];
		}));
		reloading.setOnFinished(evt -> {
			player.ammo = config.getAmmo();
			ammoDrawing = 0;
			player.speed = player.startSpeed;
			reloading = null;
		});
		reloading.setCycleCount(config.getRechargeFrames()[1]);
		reloading.play();
	}
		
	@Override
	public void start(Stage stage){
		currentStage = stage;

		stage.setTitle("Projectile by OrangoMango");
		stage.setOnCloseRequest(cr -> System.exit(0));
		stage.setScene(new Scene(new TilePane(new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT)), SCREEN_WIDTH, SCREEN_HEIGHT));
		stage.widthProperty().addListener((o, oldV, newV) -> {
			SCREEN_WIDTH = newV.intValue();
			if (currentCanvas != null) currentCanvas.setWidth(SCREEN_WIDTH);
		});
		stage.heightProperty().addListener((o, oldV, newV) -> {
			SCREEN_HEIGHT = newV.intValue();
			if (currentCanvas != null) currentCanvas.setHeight(SCREEN_HEIGHT);
		});
		
		gameoverPage = () -> {
			GameoverScreen screen = new GameoverScreen();
			stage.setFullScreenExitHint("");
			stage.getScene().setRoot(screen.getScene());
		};
		
		startPage = () -> {
			HomeScreen homescreen = new HomeScreen();
			HomeScreen.buttons.clear();
			stage.setResizable(false);
			stage.setFullScreenExitHint("");
			homescreen.startEvent = () -> {
				stage.setResizable(true);
				stage.getScene().setRoot(new TilePane(getCanvas()));
			};
			stage.getScene().setRoot(homescreen.getScene());
		};
		
		recordsPage = () -> {
			RecordsScreen records = new RecordsScreen();
			stage.setResizable(false);
			stage.setFullScreenExitHint("");
			stage.getScene().setRoot(records.getScene());
		};

		if (firstTime){
			Logger.info("Downloading assets");
			LoadingScreen ls = new LoadingScreen(stage);
		} else {
			setupSounds();
			startPage.run();
			stage.show();
			Logger.info("Application started");
		}
	}
	
	public static void setupSounds(){
		try {
			SCORE_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/score.wav");
			SHOOT_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/shoot.wav");
			DAMAGE_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/damage.wav");
			EXPLOSION_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/explosion.wav");
			BACKGROUND_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/background.wav");
			DEATH_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/death.wav");
			EXTRA_LIFE_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/extra_life.wav");
			BOSS_DEATH_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/boss_death.wav");
			BOSS_BATTLE_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/boss_battle.wav");
			BOSS_HP_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/boss_hp.wav");
			BOSS_SUPER_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/boss_super.wav");
			BOSS_HIT_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/boss_hit.wav");
			MENU_BACKGROUND_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/menu_background.wav");
			CONFIRM_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/confirm.wav");
			SELECT_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/select.wav");
			SHOW_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/show.wav");
			NOTIFICATION_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/notification.wav");
			SCORE_LOST_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/score_lost.wav");
			AMMO_RELOAD_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/ammo_reload.wav");
			NO_AMMO_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/no_ammo.wav");
			GUN_SOUNDS[0] = new Media("file://"+userHome+"/.projectile/assets/audio/machine_gun.wav");
			GUN_SOUNDS[1] = new Media("file://"+userHome+"/.projectile/assets/audio/fast_gun.wav");
			GUN_SOUNDS[2] = new Media("file://"+userHome+"/.projectile/assets/audio/sniper.wav");
			GUN_SOUNDS[3] = new Media("file://"+userHome+"/.projectile/assets/audio/triple_gun.mp3");
			GUN_SOUNDS[4] = new Media("file://"+userHome+"/.projectile/assets/audio/shotgun.wav");
			GUN_SOUNDS[5] = new Media("file://"+userHome+"/.projectile/assets/audio/space_gun.wav");
			DROP_SOUND = new Media("file://"+userHome+"/.projectile/assets/audio/drop.wav");
		} catch (Exception e){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Corrupted game directory");
			alert.setHeaderText("Could not start program");
			alert.setContentText("The directory .projectile in your home is corrupted, please delete it and try again");
			alert.showAndWait();
			System.exit(0);
		}
	}

	private static void startSpawning(GraphicsContext gc){

		// SPAWNING PAUSED FOR DEBUGGING
		if (true) return;
		
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
				if (paused || entities.size() >= currentDiff[18] || bossDialog) continue;
				boolean bossFound = false;
				for (int i = 0; i < entities.size(); i++){
					if (entities.get(i) instanceof Boss){
						bossFound = true;
						break;
					}
				}
				bossInGame = bossFound;
				boolean shoots = random.nextInt(100) <= 20 && score >= 650 ? true : false;
				shoots = shoots && !bossInGame;
				Enemy en = new Enemy(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-20)+10, shoots ? "#90F501" : "#ff0000", shoots ? "#E1FDB8" : "#FFA3B2", player, shoots);
				if (score >= 1700 && score >= bossCount+1500 && !bossFound){
					Boss boss = new Boss(gc, random.nextInt(SCREEN_WIDTH-20)+10, random.nextInt(SCREEN_HEIGHT-20)+10, "#F69E43", "#F4C99C", player);
					bossFound = true;
					bossInGame = bossFound;
					entities.add(boss);
					stopAllSounds();
					playSound(BOSS_BATTLE_SOUND, true, 0.35, false);
					if (new ProfileManager().getJSON().getBoolean("firstTimeBoss")){
						bossDialog = true;
						rerollBossMessage(gc);
					}
				}
				
				final int width1 = 55;
				final int width2 = 65;
				
				if (score < 500){
					en.setHP(currentDiff[3]); // You can one-shot them
					en.setDamage(currentDiff[0]);
				} else if (score >= 500 && score < 700){
					if (random.nextInt(100) <= 10 && !bossFound){ // Spawn mini-boss with 10% probability
						en.setHP(currentDiff[5]);
						en.setWidth(width1);
						en.setDamage(currentDiff[1]);
					} else {
						en.setHP(currentDiff[3]);
						en.setDamage(currentDiff[0]);
					}
				} else if (score >= 700 && score < 1000){
					if (random.nextInt(100) <= 10 && !bossFound){
						en.setHP(currentDiff[6]);
						en.setWidth(width1);
						en.setDamage(currentDiff[0]);
					} else {
						en.setHP(currentDiff[3]);
						en.setDamage(currentDiff[1]);
					}
				} else if (score >= 1000 && score < 1500){
					int number = random.nextInt(100);
					if (number <= 7 && !bossFound){ // Spawn mini-boss2 with 7% probability
						en.setHP(currentDiff[8]);
						en.setWidth(width2);
						en.setDamage(currentDiff[2]);
					} else if (number > 7 && number <= 17 && !bossFound){
						en.setHP(currentDiff[6]);
						en.setWidth(width1);
						en.setDamage(currentDiff[1]);
					} else {
						en.setHP(currentDiff[3]);
						en.setDamage(currentDiff[0]);
					}
				} else {
					int number = random.nextInt(100);
					if (number <= 7 && !bossFound){
						en.setHP(currentDiff[9]);
						en.setWidth(width2);
						en.setDamage(20);
					} else if (number > 7 && number <= 17 && !bossFound){
						en.setHP(currentDiff[7]);
						en.setWidth(width1);
						en.setDamage(currentDiff[2]);
					} else {
						en.setHP(bossFound ? currentDiff[3] : currentDiff[4]);
						en.setDamage(currentDiff[0]);
					}
				}
				
				if (client == null || host) entities.add(en);
				try {
					Thread.sleep(random.nextInt(MAX-MIN)+MIN);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	private static void sendState(){
		try {
			gameState.entities = entities;
			System.out.println("-- Sending entities: "+gameState.entities);
			gameState.texts = floatingTexts;
			gameState.drops = drops;
			gameState.point1 = point;
			gameState.point2 = point2;
			client.send(gameState);
		} catch (ConcurrentModificationException cme){
			System.out.println("--> Error while sending data");
		}
	}
	
	private static void loadState(GraphicsContext gc){
		gameState = (GameState)client.listen();
		List<Entity> gotEnt = gameState.entities;
		List<Entity> finalE = Collections.synchronizedList(new ArrayList<Entity>());
		for (int i = 0; i < gotEnt.size(); i++){
			Entity e = gotEnt.get(i);
			if (e instanceof Player && ((Player)e).user.equals(client.getUsername())){
				finalE.add(player);
			} else {
				finalE.add(e);
			}
		}
		entities = finalE;
		// Get user
		for (int i = 0; i < entities.size(); i++){
			Entity e = entities.get(i);
			if (e instanceof Player){
				if (((Player)e).user.equals(client.getUsername())){
					player = (Player)e;
				}
			}
		}
		System.out.println(">> "+entities.size());
		explosions = gameState.explosions;
		floatingTexts = gameState.texts;
		drops = gameState.drops;
		point = gameState.point1;
		point2 = gameState.point2;
		pointer1 = new Pointer(gc, player, point);
		pointer2 = new Pointer(gc, player, point2);
	}
	
	/**
	 * This method constatly sends/receives data from the server/clients every <code>timing</code> milliseconds.
	 * @param gc The <code>GraphicsContext</code> used to load the state.
	 */
	private static void updateStates(GraphicsContext gc){
		final int timing = 10;
		if (host){
			new Thread(() -> {
				while (true){
					try {
						System.out.println("-> broadcasting");
						sendState();
						Thread.sleep(timing);
					} catch (InterruptedException ex){
						ex.printStackTrace();
					}
				}
			}).start();
			new Thread(() -> {
				while (true){
					Player got = null;
					while (got == null){
						try {
							got = (Player)client.listen();
						} catch (ClassCastException cce){
							continue;
						}
					}
					System.out.println("Got a player");
					boolean found = false;
					for (int i = 0; i < entities.size(); i++){
						Entity e = entities.get(i);
						if (e instanceof Player && ((Player)e).user.equals(got.user)){
							entities.set(i, got);
							found = true;
							break;
						}
					}
					if (!found) entities.add(got);
					System.out.println("Now entities are: "+entities);
				}
			}).start();
		} else {
			new Thread(() -> {
				while (true){
					try {
						System.out.println("-> getting");
						loadState(gc);
						Thread.sleep(timing/2);
					} catch (InterruptedException ex){
						ex.printStackTrace();
					}
				}
			}).start();
			// SEND ONLY THE PLAYER STATE
			new Thread(() -> {
				while (true){
					try {
						client.sendPlayer(player);
						Thread.sleep(timing);
					} catch (InterruptedException ex){
						ex.printStackTrace();
					}
				}
			}).start();
		}
	}

	private static void update(GraphicsContext gc){
		userGamedata.clear();
		Random random = new Random();
		// Load data from gameState and send current state
		if (client != null){
			updateStates(gc);
		}
		MainApplication.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), ev -> {			
			long now = System.currentTimeMillis();
			long cooldown = now-explosionStart < 4500 ? now-explosionStart : 4500;
			long cooldown2 = now-rechargeStart < currentDiff[15] ? now-rechargeStart : currentDiff[15];
			
			gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
			//System.out.println("==== MAIN START ====");
			
			Iterator<Drop> dropIterator = drops.iterator();
			while (dropIterator.hasNext()){
				Drop drop = dropIterator.next();
				if (drop.mustRemove){
					dropIterator.remove();
					continue;
				}
				drop.draw(gc);
				if (drop.onPlayer(player)){
					playSound(DROP_SOUND, false, null, false);
					dropIterator.remove();
					Random randomN = new Random();
					BulletConfig.Rarity gotRarity = drop.getRarity(); 
					if (player.shield <= player.getStartShield()-25){
						player.shield += 25;
					} else {
						player.shield = player.getStartShield();
					}
					ArrayList<BulletConfig> list = new ArrayList<>();
					for (BulletConfig conf : availableGuns){
						if (conf.getRarity() == gotRarity){
							list.add(conf);
						}
					}
					config = list.get(randomN.nextInt(list.size()));
					config.ammoAmount = config.getStartAmmoAmount();
					player.ammo = config.getAmmo();
					Logger.info("Drop: "+gotRarity+" "+config);
					FloatingText ftext = new FloatingText(config.toString().replace("_", " ").split(" \\[")[0], drop.getX(), drop.getY());
					ftext.movementTime = 30;
					floatingTexts.add(ftext);
				}
			}
			//System.out.println(entities.size());
			for (int i = 0; i < entities.size(); i++){
				if (client != null) entities.get(i).setGC(gc);
				entities.get(i).draw();
			}
			Iterator<Explosion> explosionIterator = explosions.iterator();
			while (explosionIterator.hasNext()){
				try {
					Explosion exp = explosionIterator.next();
					if (client != null){
						exp.setGC(gc);
					}
					exp.explode();
					if (exp.radius >= 150){
						explosionIterator.remove();
						for (int i = 0; i < entities.size(); i++){
							Entity ent = entities.get(i);
							if (exp.collided(ent)){
								if (ent instanceof Enemy && !((Enemy)ent).spawning){
									((Enemy)ent).takeDamage(exp.damage, i);
								} else if (ent instanceof Boss){
									((Boss)ent).takeDamage(exp.damage, i);
								} else {
									ent.takeDamage(exp.damage);
								}
								floatingTexts.add(new FloatingText(Integer.toString(exp.damage), ent.getX(), ent.getY()));
								if (ent.getHP() <= 0){
									i--;
								}
							}
						}
					}
				} catch (ConcurrentModificationException ex){
					Logger.error("-- error (2.2)");
				}
			}
			if (client != null){
				point.setGC(gc);
				point2.setGC(gc);
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
			pointer1.draw();
			pointer2.draw();
			for (int j = 0; j < entities.size(); j++){
				if (entities.get(j) instanceof Player){
					Player currentPlayer = (Player)entities.get(j);
					Iterator<Bullet> iterator = currentPlayer.bullets.iterator();
					while (iterator.hasNext()){
						boolean removed = false;
						Bullet b = iterator.next();
						if (client != null){
							b.setGC(gc);
						}
						if ((b.getX() <= 0 || b.getX() >= SCREEN_WIDTH || b.getY() <= 0 || b.getY() >= SCREEN_HEIGHT) && !b.config.willBounce()){
							iterator.remove();
							removed = true;
						}
						int delAmount = 0;
						for (int i = 0; i < entities.size(); i++){
							try {
								Entity e = entities.get(i);
								int dmg = b.config.willDoDamageOnDistance() ? b.config.getDamageData()[0]+b.config.getDamageData()[2]*b.getFrames() : b.config.getDamage();
								if (b.config.willDoDamageOnDistance() && dmg > b.config.getDamageData()[1] && b.config.getDamageData()[1] > b.config.getDamageData()[0]){
									dmg = b.config.getDamageData()[1];
								} else if (dmg < 0){
									dmg = 0;
								}
								if (b.continueCond.test(e)) continue;
								if (e instanceof Enemy && e.collided(b.getX(), b.getY(), Bullet.w) && !((Enemy)e).spawning){
									if (!b.doExplosion){
										((Enemy)e).takeDamage(dmg, i);
										floatingTexts.add(new FloatingText(Integer.toString(dmg), b.getX(), b.getY()));
										enemyDamageCount++;
									}
									if (b.doExplosion){
										Explosion explosion = new Explosion(gc, b.getX(), b.getY());
										explosion.damage = 20;
										explosions.add(explosion);
									}
									if (!removed){
										if (!b.config.willGoPast() || b.doExplosion){
											iterator.remove();
											removed = true;
										}
									}
								} else if (e instanceof Boss && e.collided(b.getX(), b.getY(), Bullet.w)){
									if (!b.doExplosion){
										((Boss)e).takeDamage(dmg, i);
										floatingTexts.add(new FloatingText(Integer.toString(dmg), b.getX(), b.getY()));
										enemyDamageCount++;
									}
									if (b.doExplosion){
										Explosion explosion = new Explosion(gc, b.getX(), b.getY());
										explosion.damage = 20;
										explosions.add(explosion);
									}
									if (!removed){
										if (!b.config.willGoPast() || b.doExplosion){
											iterator.remove();
											removed = true;
										}
									}
								} else if (e.collided(b.getX(), b.getY(), Bullet.w)){
									if (!b.doExplosion){
										e.takeDamage(dmg);
										floatingTexts.add(new FloatingText(Integer.toString(dmg), b.getX(), b.getY()));
									}
									if (!removed){
										if (!b.config.willGoPast() || b.doExplosion){
											iterator.remove();
											removed = true;
										}
									}
								}
								if (e.getHP() <= 0){
									i--;
								}
							} catch (ConcurrentModificationException exc){
								exc.printStackTrace();
								Logger.error("-- error (3)");
								System.exit(0);
							}
						}
						if (b.getFrames()*b.getSpeed() >= b.config.getMaxDistance() && !b.doExplosion){
							if (!removed){
								iterator.remove();
								removed = true;
							}
						}
						b.travel();
						removed = false;
					}
				}
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
							ent.takeDamage(2);
						}
					}
				}
			}
			
			Iterator<FloatingText> floatingIterator = floatingTexts.iterator();
			while (floatingIterator.hasNext()){
				FloatingText ft = floatingIterator.next();
				ft.draw(gc);
				if (ft.getMovements() == ft.movementTime){
					floatingIterator.remove();
				}
			}
			
			// Draw hp bar
			gc.setFill(Color.web(getHPColor(player.hp, player.getStartHP())));
			gc.fillRect(20, 20, 200*player.hp/player.getStartHP() <= 0 ? 0 : 200*player.hp/player.getStartHP(), 30);
			gc.setFill(Color.web("#0148F5"));
			gc.fillRect(20, 20, 200*player.shield/player.getStartShield() <= 0 ? 0 : 200*player.shield/player.getStartShield(), 30);
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
			gc.fillText(Integer.toString(score), 270, 50);
			
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
			
			// Draw ammo
			double ammoHeight = ammoDrawing == 0 ? 90*player.ammo/config.getAmmo() : 90*ammoDrawing;
			gc.setFill(Color.web("#F4762B"));
			gc.fillRect(230, 20+(90-ammoHeight), 30, ammoHeight);
			gc.fillRect(230, 115, 30*config.ammoAmount/config.getStartAmmoAmount(), 15);
			gc.setStroke(Color.web("#FF2800"));
			gc.setLineWidth(4);
			gc.strokeRect(230, 20, 30, 90);
			gc.strokeRect(230, 116, 30, 14);
			
			// Draw timer
			userGamedata.put("gameTime", (double)(now-gameStart));
			gc.setFont(Font.loadFont(MAIN_FONT, 30));
			gc.setFill(Color.WHITE);
			gc.fillText(String.format("%s:%s", (now-gameStart)/60000, (now-gameStart)/1000%60), 30, SCREEN_HEIGHT-30);

			if (player.hp <= 40 && !hpCheck){
				hpCheck = true;
				notification.setText("HP is low!");
				notification.mustShow = true;
				playSound(NOTIFICATION_SOUND, false, 1.0, false);
				rechargeHP();
			}
			if (player.hp >= 70){
				hpCheck = false;
			}

			// Show notification if notification.mustShow = true
			notification.show();
			
			// Auto recharge ammo
			if (reloading == null && player.ammo == 0){
				reloadAmmo();
			}
			
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
			if (volume == null){
				ac.setVolume(0.8);
			}
		} else {
			ac.setVolume(0.9);
		}
		if (volume != null){
			ac.setVolume(volume);
		}
		ac.play();
		audioAllowed = false;
		MainApplication.schedule(() -> audioAllowed = true, 150);
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
	private static String getHPColor(int hp, int max){
		double value = (double)hp/max*100;
		if (value >= 0 && value < 10){
			return "#F52F20";
		} else if (value >= 10 && value < 30){
			return "#E48B40";
		} else if (value >= 30 && value < 50){
			return "#EDDD34";
		} else if (value >= 50 && value < 70){
			return "#569B21";
		} else {
			return "#65E85C";
		}
	}
}
