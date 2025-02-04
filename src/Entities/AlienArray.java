package Entities;

import java.awt.*;
import java.util.*;

import Controllers.UFOController;
import GameState.GameState;
import Utilities.SoundPlayer;

public class AlienArray {
    public ArrayList<Alien> alienArray;
    public int alienRows;
    public int alienCols;

    public int alienVelocityX;

    public int alienCount;
    public int aliveSquidCount;

    public int alienStepTimer;
    public int alienStepRate;
    public int note;

    public int alienSpriteTimer = 0;
    public int alienSpriteState = 0;

    public boolean wallCollison;
    public boolean allowMove;

    public int explodedAlienIndex;
    public int explosionTimer;

    public int rechargingAlien;

    public AlienArray() {
        this.alienArray = new ArrayList<Alien>();
        this.alienRows = 5; 
        this.alienCols = 11;

        this.alienVelocityX = 8;

        this.alienStepRate = 24;
        this.alienStepTimer = 0;
        this.note = 4;

        this.alienSpriteTimer = 0;
        this.alienSpriteState = 0;

        this.alienCount = 0;
        this.aliveSquidCount = 0;

        this.wallCollison = false;
        this.allowMove = true;

        this.explodedAlienIndex = -1;
        this.explosionTimer = 0;
    }

    public void createAliens(GameState gs, ArrayList<Image> squidImgs, ArrayList<Image> crabImgs,
            ArrayList<Image> octopusImgs) {
        for (int row = 0; row < this.alienRows; row++) {
            for (int col = 0; col < this.alienCols; col++) {
                ArrayList<Image> imgSet;
                double paddingRatio;
                int points;

                int yLevelOffset = gs.level;
                if (gs.level > 5) {
                    yLevelOffset = 5;
                }

                if (row < 1) {
                    imgSet = squidImgs;
                    paddingRatio = 0.5;
                    points = 30;
                } else if (row < 3) {
                    imgSet = crabImgs;
                    paddingRatio = 0.3125;
                    points = 20;
                } else {
                    imgSet = octopusImgs;
                    paddingRatio = 0.25;
                    points = 10;
                }

                Alien alien = new Alien(gs, col, row + yLevelOffset, imgSet, paddingRatio, points);

                this.alienArray.add(alien);
            }
        }
        this.alienCount = alienArray.size();
    }

    public void alienMovement(GameState gs, Ship ship, UFOController ufoController) {
        this.alienStepTimer++;
        if (alienStepTimer >= alienStepRate && !ship.shipHit) {

            
            SoundPlayer.playSound("../assets/sounds/note" + Integer.toString(this.note) + ".wav");
            if (this.note == 4) {
                this.note = 1;
            } else {
                this.note += 1;
            }
            
            if (!wallCollison) {
                checkWallCollision(gs.tileSize * 2, gs.boardWidth);
            }

            if (!allowMove) {
                moveVertically(gs, ship, ufoController);
                allowMove = true;
            } else {
                moveHorizontally();
                wallCollison = false;
            }
            this.alienStepTimer = 0;

            updateAlienSprite();
        }
    }

    public void checkWallCollision(int alienWidth, int boardWidth) {
        for (int i = 0; i < alienArray.size(); i++) {
            Alien alien = alienArray.get(i);
            if (alien.alive) {
                // check if aliens are touching walls
                if (alien.x + alienWidth == boardWidth || alien.x == 0) {
                    wallCollison = true;
                    allowMove = false;
                    alienStepRate -= 2;
                    break;
                }
            }
        }
    }

    public void moveHorizontally() {
        for (int i = 0; i < alienArray.size(); i++) {
            Alien alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += this.alienVelocityX;
            }
        }
    }

    public void moveVertically(GameState gs, Ship ship, UFOController ufoController) {
        this.alienVelocityX *= -1;
        for (int i = 0; i < alienArray.size(); i++) {
            Alien alien = alienArray.get(i);
            if (alien.alive) {
                if (alien.y + gs.tileSize >= ship.y) {
                    aliensWin(gs, ship);
                } else {
                    alien.y += gs.tileSize;
                    if (alien.y >= gs.tileSize * 6 && !ufoController.allowSpawn) {
                        ufoController.allowSpawn = true;
                    }
                }
            }
        }
    }

    public void updateAlienSprite() {
        this.alienSpriteState = (this.alienSpriteState == 0) ? 1 : 0;
    }

    public void aliensWin(GameState gs, Ship ship) {
        gs.gameOver = true;
        alienArray.clear();
        ship.shipHit = true;
        ship.shipSpriteState = 1;
        ship.lives = 0;
    }

    public void countAliveSquids() {
        this.aliveSquidCount = 0;
        if (!this.alienArray.isEmpty()) {
            for (int i = 0; i < this.alienCols; i++) {
                Alien alien = alienArray.get(i);
                if (alien.alive) {
                    this.aliveSquidCount++;
                }
            }
        }
    }

    public void alienExplosionTimer() {
        if (this.explodedAlienIndex >= 0) {
            this.explosionTimer++;
            if (this.explosionTimer == 2) {
                this.explodedAlienIndex = -1;
                this.explosionTimer = 0;
            }
        }
    }
}
