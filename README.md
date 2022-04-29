# Projectile by OrangoMango
![projectile_logo](https://user-images.githubusercontent.com/61402409/165325541-f4266a2a-9e57-4a32-95db-1ef07dd61d29.png)
# How to run
* Download the repository using this command:
```bash
git clone https://github.com/OrangoMango/Projectile
cd Projectile
```
* Download JavaFX SDK for your OS [here](https://gluonhq.com/products/javafx/) (v17+). Be sure to give as argument the `lib` folder
## Using Java Runtime Environment
Exceute the `.jar` file with this command:
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -jar game.jar
```
## Compiling the code
Execute the `run.sh` file:
```bash
./run.sh /path/to/javafx/lib
```
## Using an executable
### Windows
### Mac
### Linux
# How to play
## What to do
* There are 4 difficulties: *easy*, *medium*, *hard*, *extreme*.
* You get score according to these criteria:
  | What to do        | Score amount
  --------------------|-------------
  | Kill enemy        | +10 Score
  | Get yellow points | +50 score
  | Kill the boss     | +100 Score
  | Miss a bonus point| -150 Score
* Use grenades when there are many enemies near each others
* You can kill enemies once the spawned completely and you can go outside the screen to get back from the other side.
* There are 3 types of enemies: Enemy1 (at 0 score), Enemy2 (at 500 score with 10% chance), Enemy3 (at 1000 score with 7% chance)
* **Survive as much time as possible and earn score**
## Controls
* <Kbd>W</Kbd> <Kbd>A</Kbd> <Kbd>S</Kbd> <Kbd>D</Kbd> to move
  * **Short press mode**: Press once a key and you will travel in that direction until you press another key
  * **Long press mode**: Normal mode. Long press to move
* <Kbd>P</Kbd> to pause/resume
* <Kbd>Q</Kbd> to recharge your hp
* <Kbd>Left-Click</Kbd> to shoot
* <Kbd>Right-Click</Kbd> to shoot grenades
# Screenshots
<img src="https://user-images.githubusercontent.com/61402409/165313479-8a8abd87-d238-4c5d-abb8-566ada7a956e.png" width=400 height=300 /> <img src="https://user-images.githubusercontent.com/61402409/165313485-aeeb826e-2fea-47b5-9100-2d114becad42.png" width=400 height=300 /> <img src="https://user-images.githubusercontent.com/61402409/165313488-8814a380-84d9-4140-8237-7b52e4a12104.png" width=400 height=300 />
<img src="https://user-images.githubusercontent.com/61402409/165313490-8fb93d6d-5782-4f43-b315-2cfe0de09a71.png" width=400 height=300 />
# Wiki
## General
* First boss: 1700 score, then every 1500
* Player can recover hp by killing Enemy2 or Enemy3,(15 + enemy damage)% chance
 
| Key | Value |
--- | ---
| Player hp | 100 |
| Bullet damage | 10 |
| Explosion damage | 20 |
| Player speed (input mode: 1) | 4 |
| Player speed (input mode: 2) | 6 |
| Player speed when shooting | 1/2 |
| Bullet speed | 10 |
## Easy
| Key | Value |
--- | ---
| Enemy damage | 10 |
| Enemy2 damage | 15 |
| Enemy3 damage | 20 |
| Enemy hp | 10 |
| Enemy hp (score > 1500) | 20 |
| Enemy2 hp | 40 |
| Enemy2 hp (score > 1000) | 50 |
| Enemy2 hp (score > 1500) | 60 |
| Enemy3 hp | 60 |
| Enemy3 hp (score > 1500)| 70 |
| Spawn cooldown | 1-2s |
| Boss damage to player cooldown | 0.5s |
| Enemy damage to player cooldown | 0.5s |
| Bonus point timer | 40s |
| Recharge hp cooldown | 25s |
| Boss hp | 450 |
| Boss damage | 15 |
| Max enemies in field | 8 |
| Enemy speed | 3 |
## Medium
| Key | Value |
--- | ---
| Enemy damage | 15
| Enemy2 damage | 20
| Enemy3 damage | 25
| Enemy hp | 10
| Enemy hp (score > 1500) | 20
| Enemy2 hp | 50
| Enemy2 hp (score > 1000) | 60
| Enemy2 hp (score > 1500) | 70
| Enemy3 hp | 70
| Enemy3 hp (score > 1500)| 80
| Spawn cooldown | 0.9-1.9s
| Boss damage to player cooldown | 0.4s
| Enemy damage to player cooldown | 0.4s
| Bonus point timer | 36s
| Recharge hp cooldown | 30s
| Boss hp | 550
| Boss damage | 22
| Max enemies in field | 7
| Enemy speed | 3.1
## Hard
| Key | Value |
--- | ---
| Enemy damage | 20
| Enemy2 damage | 25
| Enemy3 damage | 30
| Enemy hp | 20
| Enemy hp (score > 1500) | 30
| Enemy2 hp | 60
| Enemy2 hp (score > 1000) | 70
| Enemy2 hp (score > 1500) | 80
| Enemy3 hp | 80
| Enemy3 hp (score > 1500)| 90
| Spawn cooldown | 0.85-1.85s
| Boss damage to player cooldown | 0.3s
| Enemy damage to player cooldown | 0.3s
| Bonus point timer | 34s
| Recharge hp cooldown | 35s
| Boss hp | 700
| Boss damage | 30
| Max enemies in field | 6
| Enemy speed | 3.2
## Extreme
| Key | Value |
--- | ---
| Enemy damage | 30
| Enemy2 damage | 35
| Enemy3 damage | 40
| Enemy hp | 30
| Enemy hp (score > 1500) | 40
| Enemy2 hp | 70
| Enemy2 hp (score > 1000) | 80
| Enemy2 hp (score > 1500) | 90
| Enemy3 hp | 90
| Enemy3 hp (score > 1500)| 100
| Spawn cooldown | 0.75-1.75s
| Boss damage to player cooldown | 0.3s
| Enemy damage to player cooldown | 0.25s
| Bonus point timer | 30s
| Recharge hp cooldown | 40s
| Boss hp | 850
| Boss damage | 45
| Max enemies in field | 5
| Enemy speed | 3.3
