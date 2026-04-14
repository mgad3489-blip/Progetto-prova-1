package it.unibo.towerdefense.model;

import java.util.List;
import java.util.ArrayList;

public class Wave {
    private final int totalWaves = 5;
    
    public Wave() {}
    public int getTotalWaves() { return totalWaves; }
    
    public List<Enemy> generateWave(int waveNumber) {
        List<Enemy> enemies = new ArrayList<>();
        int numEnemies = 4 + waveNumber * 3;
        for (int i = 0; i < numEnemies; i++) {
            EnemyType type = EnemyType.BASIC;
            if (waveNumber >= 2 && i % 3 == 0) type = EnemyType.FAST;
            if (waveNumber >= 3 && i % 4 == 0) type = EnemyType.TANK;
            enemies.add(new Enemy(type, waveNumber));
        }
        return enemies;
    }
}