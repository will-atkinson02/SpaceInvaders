package Entities;
import java.awt.*;
import java.util.ArrayList;

public class Entity {
    public int x;
    public int y;
    public int width;
    public int height;
    public ArrayList<Image> imgArray;
    public boolean alive = true;
    public boolean used = false;

    public Entity(int x, int y, int width, int height, ArrayList<Image> imgArray) {
        this.x = x;
        this.y = y; 
        this.width = width;
        this.height = height;
        this.imgArray = imgArray;
    }

    public double getPaddingRatio() {
        return 0;
    }
}