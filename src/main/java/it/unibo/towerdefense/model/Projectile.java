package it.unibo.towerdefense.model;

/**
 * A projectile moving smoothly towards its target.
 */
public class Projectile {
    private final Tower source;
    private final Enemy target;
    private double x;
    private double y;
    private final double speed = 15.0; // Px per tick
    private boolean alive = true;

    public Projectile(Tower source, Enemy target) {
        this.source = source;
        this.target = target;
        // Start from center of tower (assuming 40x40 sprites, center is roughly +20 if pos is top-left,
        // but now our spots are centers. So just use pixel coordinates directly).
        this.x = source.getPixelX();
        this.y = source.getPixelY();
    }

    public void update() {
        if (!target.isAlive()) {
            this.alive = false;
            return;
        }

        // Aim at target center
        double tx = target.getPixelX() + 20; 
        double ty = target.getPixelY() + 20;
        
        double dx = tx - x;
        double dy = ty - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed) {
            // Hit!
            target.takeDamage(source.getDamage());
            if (source.getType() == TowerType.ICE) {
                target.applySlow(0.5, 20);
            }
            this.alive = false;
        } else {
            // Move
            this.x += (dx / dist) * speed;
            this.y += (dy / dist) * speed;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public TowerType getSourceTowerType() { return source.getType(); }
    public boolean isAlive() { return alive; }
}
