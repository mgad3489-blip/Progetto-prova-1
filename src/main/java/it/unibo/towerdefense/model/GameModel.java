package it.unibo.towerdefense.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game state. Spawn delay set for 60fps.
 */
public class GameModel {

    private GameState state;
    private Map map;
    private final Player player;
    private final Wave wave;

    private final List<Enemy> activeEnemies;
    private final List<Enemy> spawnQueue;
    private final List<Projectile> projectiles;

    private int spawnCooldownTicks = 0;
    private int currentWaveIndex = 0;
    private boolean waveInProgress = false;

    private static final int BASE_DAMAGE_PER_ENEMY = 10;
    private static final int SPAWN_DELAY_TICKS = 60; // ~1 second at 60fps
    
    private int fireCooldownTicks = 0;
    private int freezeCooldownTicks = 0;
    private int fireAnimTicks = 0;
    private int freezeAnimTicks = 0;
    private int victoryDelayTicks = -1;
    private static final int FIRE_COOLDOWN = 900;  // 15 seconds
    private static final int FREEZE_COOLDOWN = 480; // 8 seconds
    
    // Level system
    private int currentLevel = 1;
    private int maxUnlockedLevel = 1;

    public GameModel() { this(null); }

    public GameModel(String mapPath) {
        this.player = new Player();
        this.wave = new Wave();
        this.state = GameState.MENU;
        this.activeEnemies = new ArrayList<>();
        this.spawnQueue = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        loadLevel(1);
    }

    public void loadLevel(int levelNum) {
        this.currentLevel = levelNum;
        it.unibo.towerdefense.commons.MapLoader loader = new it.unibo.towerdefense.commons.MapLoader();
        it.unibo.towerdefense.commons.MapData data = loader.loadFromClasspath("maps/level" + levelNum + ".json");
        
        if (data == null) {
            data = loader.loadFromClasspath("maps/map.json");
        }

        if (data != null) {
            this.map = new Map(data.getWidth(), data.getHeight(), data.getBackground(), 
                              data.getWaypoints(), data.getBuildingSpots());
        } else {
            List<double[]> wp = new ArrayList<>();
            wp.add(new double[]{0, 300});
            wp.add(new double[]{800, 300});
            this.map = new Map(800, 600, "", wp, new ArrayList<>());
        }
    }

    public void start() {
        this.state = GameState.PLAYING;
        this.currentWaveIndex = 0;
        this.activeEnemies.clear();
        this.spawnQueue.clear();
        this.projectiles.clear();
        this.waveInProgress = false;
        this.player.reset();
        // Reset all building spots
        for (BuildingSpot spot : map.getBuildingSpots()) {
            spot.setTower(null);
        }
    }

    public void startNextWave() {
        if (!waveInProgress && currentWaveIndex < wave.getTotalWaves()) {
            currentWaveIndex++;
            spawnQueue.addAll(wave.generateWave(currentWaveIndex));
            waveInProgress = true;
            spawnCooldownTicks = 30;
        }
    }

    public void update() {
        // Victory/defeat countdown (must run even when not PLAYING)
        if (victoryDelayTicks > 0) {
            victoryDelayTicks--;
            return;
        }

        if (state != GameState.PLAYING) return;

        if (fireCooldownTicks > 0) fireCooldownTicks--;
        if (freezeCooldownTicks > 0) freezeCooldownTicks--;
        if (fireAnimTicks > 0) fireAnimTicks--;
        if (freezeAnimTicks > 0) freezeAnimTicks--;

        // Spawn - gradual, one enemy per second
        if (spawnCooldownTicks > 0) {
            spawnCooldownTicks--;
        } else if (!spawnQueue.isEmpty()) {
            final Enemy newEnemy = spawnQueue.remove(0);
            if (!map.getWaypoints().isEmpty()) {
                double[] start = map.getWaypoints().get(0);
                newEnemy.setPosition(start[0], start[1]);
            }
            activeEnemies.add(newEnemy);
            spawnCooldownTicks = SPAWN_DELAY_TICKS;
        }

        // Move enemies
        for (final Enemy enemy : activeEnemies) {
            if (!enemy.isAlive()) continue;
            enemy.tickVisuals();
            enemy.updateStatus();

            boolean reachedEnd = enemy.moveAlongPath(map.getWaypoints());
            if (reachedEnd) {
                player.takeBaseDamage(BASE_DAMAGE_PER_ENEMY);
                enemy.setReachedEnd(true);
                enemy.takeDamage(enemy.getHealth() + 999);
            }
        }

        // Projectiles
        projectiles.removeIf(p -> { p.update(); return !p.isAlive(); });

        // Towers attack
        for (final Tower tower : map.getTowers()) {
            tower.tick();
            if (!tower.isAlive()) continue;
            for (final Enemy enemy : activeEnemies) {
                if (enemy.isAlive()) {
                    Projectile p = tower.attack(enemy);
                    if (p != null) {
                        projectiles.add(p);
                        break;
                    }
                }
            }
        }

        // Win/lose
        if (!player.isBaseAlive()) {
            state = GameState.DEFEAT;
        } else if (waveInProgress && spawnQueue.isEmpty() && getAliveEnemyCount() == 0) {
            waveInProgress = false;
            if (currentWaveIndex >= wave.getTotalWaves()) {
                state = GameState.VICTORY;
                victoryDelayTicks = 180; // 3 seconds then redirect
                // Unlock next level
                if (currentLevel < 3 && currentLevel >= maxUnlockedLevel) {
                    maxUnlockedLevel = currentLevel + 1;
                }
            }
        }

        // Collect rewards and remove dead
        activeEnemies.removeIf(e -> {
            if (!e.isAlive()) {
                if (!e.isReachedEnd() && !e.isCoinAwarded()) {
                    player.addCoins(e.getReward());
                    e.setCoinAwarded(true);
                }
                return true;
            }
            return false;
        });
    }
    
    private int getAliveEnemyCount() {
        int count = 0;
        for (Enemy e : activeEnemies) {
            if (e.isAlive()) count++;
        }
        return count;
    }

    public boolean buildTowerOnSpot(Tower tower, BuildingSpot spot) {
        if (player.getCoins() >= tower.getType().getCost() && !spot.isOccupied()) {
            if (map.addTowerToSpot(tower, spot)) {
                player.addCoins(-tower.getType().getCost());
                return true;
            }
        }
        return false;
    }

    public boolean upgradeTower(Tower tower) {
        int cost = tower.getType().getCost() / 2;
        if (player.getCoins() >= cost && tower.getLevel() < 3) {
            player.addCoins(-cost);
            tower.upgrade();
            return true;
        }
        return false;
    }

    public boolean sellTower(BuildingSpot spot) {
        if (spot == null || !spot.isOccupied()) return false;
        Tower tower = spot.getTower();
        int refund = tower.getType().getCost() / 2;
        player.addCoins(refund);
        map.removeTowerFromSpot(spot);
        return true;
    }

    public void castRainOfFire() {
        if (fireCooldownTicks > 0 || state != GameState.PLAYING) return;
        for (Enemy e : activeEnemies) e.takeDamage(50);
        fireCooldownTicks = FIRE_COOLDOWN;
        fireAnimTicks = 60; // 1 second animation
    }

    public void castGlobalFreeze() {
        if (freezeCooldownTicks > 0 || state != GameState.PLAYING) return;
        for (Enemy e : activeEnemies) e.applySlow(0.3, 180);
        freezeCooldownTicks = FREEZE_COOLDOWN;
        freezeAnimTicks = 60; // 1 second animation
    }

    public void pause() { if (state == GameState.PLAYING) state = GameState.PAUSED; }
    public void resume() { if (state == GameState.PAUSED) state = GameState.PLAYING; }

    public GameState getState() { return state; }
    public void setState(GameState s) { this.state = s; }
    public Map getMap() { return map; }
    public Player getPlayer() { return player; }
    public List<Enemy> getActiveEnemies() { return new ArrayList<>(activeEnemies); }
    public List<Projectile> getProjectiles() { return new ArrayList<>(projectiles); }
    public int getCurrentWave() { return currentWaveIndex; }
    public int getTotalWaves() { return wave.getTotalWaves(); }
    public boolean isWaveInProgress() { return waveInProgress; }
    public int getFireCooldown() { return fireCooldownTicks; }
    public int getFreezeCooldown() { return freezeCooldownTicks; }
    public int getFireAnimTicks() { return fireAnimTicks; }
    public int getFreezeAnimTicks() { return freezeAnimTicks; }
    public boolean isVictoryRedirectReady() { return state == GameState.VICTORY && victoryDelayTicks == 0; }
    public int getCurrentLevel() { return currentLevel; }
    public int getMaxUnlockedLevel() { return maxUnlockedLevel; }
}