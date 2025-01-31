package Entities;
import java.awt.*;
import java.util.*;

import GameState.GameState;

public class UFO extends Entity {
    public ArrayList<Image> imageArray;
    public int velocityX;
    public boolean hit;
    public boolean isActive;
    public int spawnTimer;
    public boolean allowSpawn;
    public int explosionTimer;
    public int points;

    public UFO(int tileSize, ArrayList<Image> imageArray) {
        super(-tileSize*2, tileSize*4 - 16, tileSize*2, tileSize);
        this.imageArray = imageArray;
        this.points = 0;
        this.velocityX = 0;
        this.explosionTimer = 0;
        this.hit = false;
        this.isActive = false;
        this.allowSpawn = false;
    }

    public void spawnUFO(GameState gs, ArrayList<Image> ufoImages, double randomNumber) {
        if (randomNumber > 0.75) {
            this.x = gs.boardWidth;
            this.velocityX = -4;
            this.points = points();
            this.isActive = true;
        } else if (randomNumber < 0.25) {
            this.x = -gs.tileSize * 2;
            this.velocityX = 4;
            this.points = points();
            this.isActive = true;
        } else {
            this.isActive = false;
        }
    }

    public void handle(GameState gs, ArrayList<Image> ufoImages) {
        if (!this.isActive) {
            this.spawnTimer++;
            if (this.spawnTimer == 160) {
                double randomNumber = Math.random();
                if (randomNumber > 0.75 || randomNumber < 0.25 && this.allowSpawn) {
                    spawnUFO(gs, ufoImages, randomNumber);
                }
                this.spawnTimer = 0;
            }
        } else {
            checkHit();
            if (outOfBounds(gs)) {
                this.velocityX = 0;
                this.isActive = false;
            }
            moveHorizontally();
        }
    }

    public void moveHorizontally() {
        this.x += this.velocityX;
    }

    public boolean outOfBounds(GameState gs) {
        if (this.velocityX > 0 && this.x >= gs.boardWidth) {
            return true;
        } else if (this.velocityX < 0 && this.x <= -gs.tileSize*2) {
            return true;
        }
        return false;
    }

    public void checkHit() {
        if (this.hit) {
            this.explosionTimer++;
            if (this.explosionTimer >= 24) {
                this.velocityX = 0;
                this.hit = false;
                this.isActive = false;
                this.spawnTimer = 0;
                this.explosionTimer = 0;
            }
        }
    }

    public static int points() {
        Random r = new Random();
        int randomInteger = r.nextInt(450) + 1;
        int points = 50 + (randomInteger/50)*50;
        return points;
    }
}
