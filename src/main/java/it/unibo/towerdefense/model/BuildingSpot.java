package it.unibo.towerdefense.model;

import it.unibo.towerdefense.commons.GameConstants;

/**
 * Represents a fixed grid cell spot where a tower can be built.
 */
public class BuildingSpot {
    private final int col;
    private final int row;
    private Tower tower;

    public BuildingSpot(int col, int row) {
        this.col = col;
        this.row = row;
        this.tower = null;
    }

    public int getCol() { return col; }
    public int getRow() { return row; }

    /** Returns the center pixel X coordinate of this grid cell for rendering/range checks. */
    public double getPixelCenterX() {
        return (col * GameConstants.TILE_SIZE) + (GameConstants.TILE_SIZE / 2.0);
    }

    /** Returns the center pixel Y coordinate of this grid cell for rendering/range checks. */
    public double getPixelCenterY() {
        return (row * GameConstants.TILE_SIZE) + (GameConstants.TILE_SIZE / 2.0);
    }

    public boolean isOccupied() { return tower != null; }
    public Tower getTower() { return tower; }
    
    public void setTower(Tower tower) {
        this.tower = tower;
        if (tower != null) {
            tower.setPosition(getPixelCenterX(), getPixelCenterY());
        }
    }
}
