package Entities;

public class Entity {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean alive = true;
    public boolean used = false;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getPaddingRatio() {
        return 0;
    }
}