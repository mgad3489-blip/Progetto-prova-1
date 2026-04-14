package it.unibo.towerdefense.model;

public class Player {
    private int coins;
    private int baseHealth;

    public Player() {
        reset();
    }
    
    public void reset() {
        this.coins = it.unibo.towerdefense.commons.GameConstants.STARTING_COINS;
        this.baseHealth = it.unibo.towerdefense.commons.GameConstants.BASE_HEALTH;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public boolean spendCoins(int amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }

    public void takeBaseDamage(int damage) {
        this.baseHealth -= damage;
    }

    public int getCoins() { return coins; }
    public int getBaseHealth() { return baseHealth; }
    public boolean isBaseAlive() { return baseHealth > 0; }
}