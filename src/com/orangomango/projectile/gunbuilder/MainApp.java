package com.orangomango.projectile.gunbuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import javafx.scene.media.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import com.orangomango.projectile.*;

public class MainApp extends Application{
    public static final String VERSION = "1.0";
    private BulletConfig config;
    private Player player;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<FloatingText> texts = new ArrayList<>();
    private GraphicsContext gc;
    private boolean shooting = false;
    private boolean modified = false;
    private boolean reloading = false;
    private int currentAmmo, currentAmount;
    private double pseudoDrawing;
    private DamageLine dmgline;
    private long shootStart;

    private void startMainLoop(){
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(1000.0/40), e -> {
            gc.clearRect(0, 0, 300, 450);
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, 300, 450);
            player.draw();
            for (int i = 0; i < bullets.size(); i++){
                Bullet b = bullets.get(i);
                b.travel();
                // b.config is equal to this.config. No difference
                if (((b.getX() <= 0 || b.getX() >= 300 || b.getY() <= 0 || b.getY() >= 450) && !this.config.willBounce()) || b.getSpeed()*b.getFrames() >= this.config.getMaxDistance()){
                    bullets.remove(i--);
                } else if (this.dmgline != null && b.getY() <= this.dmgline.getY() && b.getY() >= this.dmgline.getY()-15){
                    int dmg = b.config.willDoDamageOnDistance() ? b.config.getDamageData()[0]+b.config.getDamageData()[2]*b.getFrames() : b.config.getDamage();
                    if (b.config.willDoDamageOnDistance() && dmg > b.config.getDamageData()[1] && b.config.getDamageData()[1] > b.config.getDamageData()[0]){
                        dmg = b.config.getDamageData()[1];
                    } else if (dmg < 0){
                        dmg = 0;
                    }
                    texts.add(new FloatingText(Integer.toString(dmg), b.getX(), b.getY()));
                    if (!b.config.willGoPast()) bullets.remove(i--);
                }
            }
            for (int i = 0; i < texts.size(); i++){
                FloatingText ft = texts.get(i);
                ft.draw(gc);
                if (ft.getMovements() >= 20){
                    texts.remove(i);
                    MainApplication.playSound(MainApplication.SCORE_SOUND, false, null, true);
                    i--;
                }
            }
            if (this.dmgline != null) this.dmgline.draw();
            gc.setLineWidth(2);
            gc.setFill(Color.web("#FFAA00"));
            gc.fillRect(5, 5, 100*(this.reloading ? this.pseudoDrawing : (double)this.currentAmmo/this.config.getAmmo()), 10);
            gc.setStroke(Color.web("#C28407"));
            gc.strokeRect(5, 5, 100, 10);
            gc.setFill(Color.web("#FFAA00"));
            gc.fillRect(115, 5, 100*this.currentAmount/this.config.getStartAmmoAmount(), 10);
            gc.setStroke(Color.web("#C28407"));
            gc.strokeRect(115, 5, 100, 10);
            gc.setFill(Color.web("#00FFE0"));
            double cld = this.shootStart == 0 ? 1 : (double)(System.currentTimeMillis()-this.shootStart)/this.config.getCooldown();
            gc.fillRect(5, 20, 100*(cld > 1 ? 1.0 : cld), 10);
            gc.setStroke(Color.web("#0766C2"));
            gc.strokeRect(5, 20, 100, 10);
            if (this.modified){
                gc.save();
                gc.setGlobalAlpha(0.7);
                gc.setFill(Color.GRAY);
                gc.fillRect(0, 0, 300, 450);
                gc.restore();
            }
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
    }
    
    public VBox getLayout(Stage stage){
        VBox layout = new VBox();
        
        SplitPane pane = new SplitPane();
        Canvas canvas = new Canvas(300, 450);
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(key -> {
            switch(key.getCode()){
                case R:
                    if (this.currentAmount == 0 || this.reloading || this.currentAmmo == this.config.getAmmo()) return;
                    this.reloading = true;
                    MainApplication.playSound(MainApplication.AMMO_RELOAD_SOUND, false, null, false);
                    this.currentAmount--;
                    Timeline rc = new Timeline(new KeyFrame(Duration.millis(this.config.getRechargeFrames()[0]), ev -> {
                        this.pseudoDrawing += 1.0/this.config.getRechargeFrames()[1];
                    }));
                    rc.setOnFinished(ev -> {
                        this.currentAmmo = this.config.getAmmo();
                        this.reloading = false;
                        this.pseudoDrawing = 0;
                    });
                    rc.setCycleCount(this.config.getRechargeFrames()[1]);
                    rc.play();
                    break;
            }
        });
        gc = canvas.getGraphicsContext2D();
        
        this.config = new BulletConfig(null, null, null, null, false, null, null, null, false, null, null, null, BulletConfig.Rarity.COMMON);
        this.currentAmmo = this.config.getAmmo();
        this.currentAmount = this.config.ammoAmount;
        
        EventHandler<MouseEvent> event = ev -> {
            if (ev.getButton() == MouseButton.MIDDLE){
                if (this.dmgline != null){
                    this.dmgline = null;
                } else {
                    this.dmgline = new DamageLine(gc, ev.getY());
                }
                return;
            }
            if (this.shooting || (this.currentAmmo == 0 && ev.getButton() != MouseButton.PRIMARY) || this.reloading) return;
            if (ev.getButton() == MouseButton.PRIMARY) this.shootStart = System.currentTimeMillis();
            Timeline shot = new Timeline(new KeyFrame(Duration.millis(this.config.getTiming()[1]), e -> {
                for (int i = 0; i < this.config.getCount(); i++){
                    if (this.currentAmmo == 0){
                        continue;
                    }
                    Bullet bullet = new Bullet(gc, player.getX(), player.getY(), Math.atan2(ev.getY()-player.getY(), ev.getX()-player.getX())+Math.toRadians(this.config.getAngles()[i]), this.config);
                    bullet.WIDTH = 300;
                    bullet.HEIGHT = 450;
                    bullet.doExplosion = ev.getButton() == MouseButton.SECONDARY;
                    bullets.add(bullet);
                    if (ev.getButton() == MouseButton.PRIMARY){
                        MainApplication.playSound(this.config.getShootSound(), false, null, true);
                        this.currentAmmo--;
                    } else {
                        MainApplication.playSound(MainApplication.EXPLOSION_SOUND, false, null, true);
                    }
                }
            }));
            shot.setCycleCount(ev.getButton() == MouseButton.SECONDARY && !this.config.allowMultipleExplosions ? 1 : this.config.getTiming()[0]);
            shot.play();
            this.shooting = true;
            MainApplication.schedule(() -> this.shooting = false, ev.getButton() == MouseButton.SECONDARY && !this.config.allowMultipleExplosions ? this.config.getCooldown() : this.config.getCooldown()+this.config.getTiming()[0]*this.config.getTiming()[1]);
        };
        
        canvas.setOnMouseDragged(event);
        canvas.setOnMousePressed(event);
        
        player = new Player(gc);
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.setVgap(5);
        grid.setHgap(3);
        String[] labels = new String[]{"Speed: ", "Damage: ", "Sound: ", "Ammo: ", "Cooldown: ", "Max distance: ", "Ammo amount: ", "RechargeFrames (a;b): ", "Timing (a;b): ", "Angles (.;.): ", "Distance damage (a;b;c): ", "Gun name: "};
        String[] tooltips = new String[]{"Bullet speed (int)", "Damage (int)", "Audio variable name", "Bullets per recharge (int)", "Cooldown (ms)", "Max distance (double)", "Ammo amount (int)", "Recharge frames (ms/frame, frames number)", "Timing (bullet, ms)", "Angles (a1, a2 ..) double", "Distance damage (min, max, step)", "You gun's name"};
        String[] defaults = new String[]{"10", "10", "SHOOT_SOUND", "10", "230", "450.0", "5", "300;10", "1;1", "0", "null", "Untitled gun"};
        TextField[] fields = new TextField[labels.length];
        int cont = 0;
        for (String text : labels){
            Label label = new Label(text);
            TextField field = new TextField();
            field.setPromptText(defaults[cont]);
            field.setOnKeyPressed(k -> {
                canvas.setDisable(true);
                this.modified = true;
            });
            fields[cont] = field;
            label.setTooltip(new Tooltip(tooltips[cont]));
            grid.add(label, 0, cont);
            grid.add(field, 1, cont++);
        }
        Button browse = new Button("Browse..");
        browse.setOnAction(ev -> {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")+File.separator+".projectile"+File.separator+"assets"+File.separator+"audio"));
            File foundFile = fc.showOpenDialog(stage);
            if (foundFile != null){
                boolean add = foundFile.getAbsolutePath().startsWith("/");
                String path = "file://"+(add ? "" : "/")+foundFile.getAbsolutePath().replace("\\", "/");
                fields[2].setText(path);
                MainApplication.playSound(new Media(path), false, null, false);
            }
            canvas.setDisable(true);
            this.modified = true;
        });
        grid.add(browse, 2, 2);
        ComboBox<String> rarity = new ComboBox<>();
        rarity.setTooltip(new Tooltip("Gun rarity"));
        rarity.getItems().addAll("common", "rare", "epic", "mythic", "leggendary");
        rarity.getSelectionModel().select(0);
        Label rar = new Label("Rarity: ");
        grid.add(rar, 0, cont);
        grid.add(rarity, 1, cont++);
        CheckBox bounce = new CheckBox("Bounce");
        CheckBox goPast = new CheckBox("Go past");
        CheckBox multi = new CheckBox("Multiple explosions");
        grid.add(bounce, 0, cont++);
        grid.add(goPast, 0, cont++);
        grid.add(multi, 0, cont++);
        
        Button test = new Button("Test");
        test.setOnAction(ev -> {
            try {
                Integer speed = fields[0].getText().equals("") ? null : Integer.parseInt(fields[0].getText());
                Integer damage = fields[1].getText().equals("") ? null : Integer.parseInt(fields[1].getText());
                Media media;
                if (fields[2].getText().startsWith("file://") || fields[2].getText().equals("")){
                    media = fields[2].getText().equals("") ? null : new Media(fields[2].getText());
                } else {
                    Class ma = Class.forName("com.orangomango.projectile.MainApplication");
                    Field field = ma.getField(fields[2].getText());
                    media = (Media)field.get(null);
                }
                Integer ammo = fields[3].getText().equals("") ? null : Integer.parseInt(fields[3].getText());
                Integer cooldown = fields[4].getText().equals("") ? null : Integer.parseInt(fields[4].getText());
                Double maxDistance = fields[5].getText().equals("") ? null : Double.parseDouble(fields[5].getText());
                Integer ammoAmount = fields[6].getText().equals("") ? null : Integer.parseInt(fields[6].getText());
                int[] rechargeFrames = fields[7].getText().equals("") ? null : getIntArray(fields[7].getText());
                if (rechargeFrames != null && rechargeFrames.length != 2){
                    throw new IllegalArgumentException();
                }
                int[] timing = fields[8].getText().equals("") ? null : getIntArray(fields[8].getText());
                if (timing != null && timing.length != 2){
                    throw new IllegalArgumentException();
                }
                int[] distanceDmg = fields[10].getText().equals("") ? null : getIntArray(fields[10].getText());
                if (distanceDmg != null && distanceDmg.length != 3){
                    throw new IllegalArgumentException();
                }
                String[] data = fields[9].getText().equals("") ? null : fields[9].getText().split(";");
                double[] angl = data == null ? null : new double[data.length];
                if (angl != null){
                    int count = 0;
                    for (String p : data){
                        angl[count++] = Double.parseDouble(p);
                    }
                }
                this.config = new BulletConfig(speed, cooldown, damage, angl, bounce.isSelected(), ammo, timing, rechargeFrames, goPast.isSelected(), maxDistance, ammoAmount, media, BulletConfig.Rarity.valueOf(rarity.getSelectionModel().getSelectedItem().toUpperCase()));
                this.currentAmmo = this.config.getAmmo();
                this.currentAmount = this.config.ammoAmount;
                this.config.allowMultipleExplosions = multi.isSelected();
                this.pseudoDrawing = 0;
                if (distanceDmg != null){
                    this.config.setDamageOnDistance(distanceDmg[0], distanceDmg[1], distanceDmg[2]);
                }
                this.modified = false;
                canvas.setDisable(false);
                canvas.requestFocus();
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Arguments error");
                alert.setHeaderText("Incorrect data given");
                alert.setContentText("Please check your inputs");
                alert.showAndWait();
            }
        });
        Button print = new Button("Print");
        print.setOnAction(ev -> {
            System.out.println(this.config);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Current created gun");
            alert.setHeaderText("Your gun");
            alert.setContentText(this.config.toString());
            alert.showAndWait();
        });
        Button saveBtn = new Button("Save gun");
        HBox hbox = new HBox(3, test, print, saveBtn);
        grid.add(hbox, 1, cont++);
        
        pane.getItems().addAll(canvas, grid);
        
        startMainLoop();
        
        MenuBar bar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newFile.setOnAction(ev -> {
            for (TextField tf : fields){
                tf.setText("");
            }
            bounce.setSelected(false);
            goPast.setSelected(false);
            multi.setSelected(false);
            rarity.getSelectionModel().select(0);
            canvas.setDisable(false);
            this.modified = false;
        });
        MenuItem saveFile = new MenuItem("Save");
        MenuItem openFile = new MenuItem("Open");
        MenuItem resetGuns = new MenuItem("Reset guns");
        resetGuns.setOnAction(ev -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Gun reset");
            alert.setHeaderText("Reset all saved guns?");
            alert.setContentText("Are you sure that you want to delete all guns saved in this session?");
            alert.showAndWait();
        });
        file.getItems().addAll(newFile, saveFile, openFile, new SeparatorMenuItem(), resetGuns);
        Menu help = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        helpItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        help.getItems().add(helpItem);
        bar.getMenus().addAll(file, help);
        
        layout.getChildren().addAll(bar, pane, new Label("  Projectile GunBuilder by OrangoMango v"+VERSION));
        return layout;
    }
    
    public void start(Stage stage){
        stage.setTitle("GunBuilder v"+VERSION);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(getLayout(stage), 850, 540));
        stage.show();
    }
    
    private static int[] getIntArray(String data){
        String[] pieces = data.split(";");
        int[] out = new int[pieces.length];
        int count = 0;
        for (String p : pieces){
            out[count++] = Integer.parseInt(p);
        }
        return out;
    }

    public static void main(String[] args){
        launch(args);
    }
}
