package it.unibo.towerdefense.commons;

public final class GameConstants {
    private GameConstants() {}

    public static final int MAP_WIDTH = 800;
    public static final int MAP_HEIGHT = 600;
    
    // Grid definitions
    public static final int TILE_SIZE = 50;
    public static final int COLS = MAP_WIDTH / TILE_SIZE; // 16
    public static final int ROWS = MAP_HEIGHT / TILE_SIZE; // 12

    public static final int BASE_DAMAGE_PER_ENEMY = 10;
    public static final int ENEMY_REWARD = 10;

    public static final int STARTING_COINS = 250;
    public static final int BASE_HEALTH = 100;
}