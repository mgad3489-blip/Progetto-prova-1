package it.unibo.towerdefense.model;

/**
 * Each tower type. Damage is per-hit, cooldown is in ticks (at 60fps).
 */
public enum TowerType {
    BASIC(50, 5, 3, 60, "Torre Cristallo"),
    SNIPER(120, 15, 5, 120, "Cavaliere"),
    RAPID(80, 2, 3, 20, "Mago"),
    ICE(90, 3, 3, 60, "Torre Ghiaccio");

    private final int cost;
    private final int damage;
    private final int range;
    private final int cooldown;
    private final String description;

    TowerType(int cost, int damage, int range, int cooldown, String description) {
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.cooldown = cooldown;
        this.description = description;
    }

    public int getCost() { return cost; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public int getCooldown() { return cooldown; }
    public String getDescription() { return description; }
}
