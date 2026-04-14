package it.unibo.towerdefense.model;

import it.unibo.towerdefense.commons.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the grid-based map.
 */
public class Map {

    private final int width;
    private final int height;
    private final String backgroundPath;
    
    // Pixel-center waypoints for enemy smooth movement
    private final List<double[]> pixelWaypoints;
    private final List<BuildingSpot> buildingSpots;
    
    // Grid: 0 = Grass, 1 = Path, 2 = Buildable Spot
    private final int[][] grid;

    public Map(int width, int height, String backgroundPath, List<double[]> pathGridCoords, List<double[]> spotGridCoords) {
        this.width = width;
        this.height = height;
        this.backgroundPath = backgroundPath != null ? backgroundPath : "";
        
        this.grid = new int[GameConstants.ROWS][GameConstants.COLS];
        this.pixelWaypoints = new ArrayList<>();
        this.buildingSpots = new ArrayList<>();
        
        if (pathGridCoords != null) {
            for (double[] coord : pathGridCoords) {
                int col = (int) coord[0];
                int row = (int) coord[1];
                if (col >= 0 && col < GameConstants.COLS && row >= 0 && row < GameConstants.ROWS) {
                    grid[row][col] = 1; // Mark as path
                    pixelWaypoints.add(new double[]{
                        (col * GameConstants.TILE_SIZE) + (GameConstants.TILE_SIZE / 2.0),
                        (row * GameConstants.TILE_SIZE) + (GameConstants.TILE_SIZE / 2.0)
                    });
                }
            }
        }
        
        if (spotGridCoords != null) {
            for (double[] coord : spotGridCoords) {
                int col = (int) coord[0];
                int row = (int) coord[1];
                if (col >= 0 && col < GameConstants.COLS && row >= 0 && row < GameConstants.ROWS) {
                    grid[row][col] = 2; // Mark as buildable
                    buildingSpots.add(new BuildingSpot(col, row));
                }
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getBackgroundPath() { return backgroundPath; }
    
    public List<double[]> getWaypoints() { return pixelWaypoints; }
    public List<BuildingSpot> getBuildingSpots() { return buildingSpots; }
    public int[][] getGrid() { return grid; }

    public List<Tower> getTowers() {
        return buildingSpots.stream()
            .filter(BuildingSpot::isOccupied)
            .map(BuildingSpot::getTower)
            .collect(Collectors.toList());
    }

    /** Returns the exact building spot at the requested grid coordinates, or null. */
    public BuildingSpot getSpotAt(int col, int row) {
        if (col < 0 || col >= GameConstants.COLS || row < 0 || row >= GameConstants.ROWS) return null;
        for (BuildingSpot spot : buildingSpots) {
            if (spot.getCol() == col && spot.getRow() == row) {
                return spot;
            }
        }
        return null;
    }

    public boolean addTowerToSpot(Tower tower, BuildingSpot spot) {
        if (!spot.isOccupied()) {
            spot.setTower(tower);
            return true;
        }
        return false;
    }

    public boolean removeTower(Tower tower) {
        for (BuildingSpot spot : buildingSpots) {
            if (spot.getTower() == tower) {
                spot.setTower(null);
                return true;
            }
        }
        return false;
    }

    public void removeTowerFromSpot(BuildingSpot spot) {
        if (spot != null) spot.setTower(null);
    }
}