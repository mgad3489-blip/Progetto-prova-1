package it.unibo.towerdefense.commons;

import java.util.List;
import java.util.ArrayList;

/**
 * POJO for custom JSON map data.
 */
public class MapData {

    private int width;
    private int height;
    private String background;
    private List<double[]> waypoints = new ArrayList<>();
    private List<double[]> buildingSpots = new ArrayList<>();

    public MapData() {}

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getBackground() { return background; }
    public void setBackground(String background) { this.background = background; }

    public List<double[]> getWaypoints() { return waypoints; }
    public void setWaypoints(List<double[]> waypoints) { this.waypoints = waypoints; }

    public List<double[]> getBuildingSpots() { return buildingSpots; }
    public void setBuildingSpots(List<double[]> buildingSpots) { this.buildingSpots = buildingSpots; }
}