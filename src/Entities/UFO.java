package Entities;

import java.awt.*;
import java.util.*;

public class UFO extends Entity {
    public ArrayList<Image> imageArray;
    public int velocityX;
    public boolean hit;
    public int points;
    public boolean playUFOHitSFX;

    public UFO(int x, int velocityX, int tileSize, ArrayList<Image> imageArray) {
        super(x, tileSize * 4 - 16, tileSize * 2, tileSize);
        this.velocityX = velocityX;
        this.imageArray = imageArray;
        this.points = points();
        this.hit = false;
        this.playUFOHitSFX = true;
    }

    public static int points() {
        Random r = new Random();
        int randomInteger = r.nextInt(450) + 1;
        int points = 50 + (randomInteger / 50) * 50;
        return points;
    }
}
