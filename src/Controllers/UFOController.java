package Controllers;

import java.awt.Image;
import java.util.ArrayList;

import Entities.UFO;
import GameState.GameState;
import Utilities.SoundPlayer;

public class UFOController {
    public UFO ufo;

    public int spawnTimer;

    public int explosionTimer;
    public boolean allowSpawn;

    public UFOController() {
        this.ufo = null;
        this.explosionTimer = 0;
        this.allowSpawn = false;
    }

    public UFO spawnUFO(GameState gs, ArrayList<Image> ufoImages, double randomNumber) {
        int x;
        int velocityX;
        if (randomNumber > 0.85) {
            x = gs.boardWidth;
            velocityX = -4;
        } else if (randomNumber < 0.15) {
            x = -gs.tileSize * 2;
            velocityX = 4;
        } else {
            return null;
        }

        return new UFO(x, velocityX, gs.tileSize, ufoImages);
    }

    public void moveHorizontally() {
        this.ufo.x += this.ufo.velocityX;
    }

    public boolean outOfBounds(GameState gs) {
        if (this.ufo.velocityX > 0 && this.ufo.x >= gs.boardWidth) {
            return true;
        } else if (this.ufo.velocityX < 0 && this.ufo.x <= -gs.tileSize * 2) {
            return true;
        }
        return false;
    }

    public void checkHit() {
        if (this.ufo.hit) {
            this.ufo.velocityX = 0;
            this.explosionTimer++;
            if (this.explosionTimer >= 24) {
                this.ufo = null;
                this.spawnTimer = 0;
                this.explosionTimer = 0;
            }
        }
    }

    public void handle(GameState gs, ArrayList<Image> ufoImages) {
        if (ufo == null) {
            this.spawnTimer++;
            if (this.spawnTimer == 160) {
                double randomNumber = Math.random();
                if ((randomNumber > 0.85 || randomNumber < 0.15) && this.allowSpawn) {
                    this.ufo = spawnUFO(gs, ufoImages, randomNumber);
                    SoundPlayer.playLoop("../assets/sounds/ufo-moving.wav");
                }
                this.spawnTimer = 0;
            }
        } else {
            moveHorizontally();
            if (outOfBounds(gs)) {
                SoundPlayer.stopSound("../assets/sounds/ufo-moving.wav");
                ufo = null;
            } else {
                checkHit();
            }
        }
    }
}