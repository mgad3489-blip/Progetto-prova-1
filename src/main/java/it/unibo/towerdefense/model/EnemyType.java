package it.unibo.towerdefense.model;

/**
 * Enum representing the different types of enemies.
 * HP scaled for 60fps tick rate - enemies need to survive longer.
 */
public enum EnemyType {
    BASIC(80, 1, 10, "Orco Raider"),
    FAST(45, 2, 15, "Goblin Veloce"),
    TANK(200, 1, 25, "Orco Bruto");

    private final int health;
    private final int speed;
    private final int reward;
    private final String description;

    EnemyType(int health, int speed, int reward, String description) {
        this.health = health;
        this.speed = speed;
        this.reward = reward;
        this.description = description;
    }

    public int getHealth() { return health; }
    public int getSpeed() { return speed; }
    public int getReward() { return reward; }
    public String getDescription() { return description; }
}
