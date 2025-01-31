package Entities;
import java.awt.*;
import java.util.*;

import GameState.GameState;
import Utilities.SoundPlayer;

public class Ship extends Entity {
    public ArrayList<Image> imageArray;
    public int velocityX;
    public int lives;
    public int shipSpriteState;
    public int shipSpriteTimer;
    public boolean shipHit;
    public boolean movingLeft;
    public boolean movingRight;
    public int reloadTime;

    public Ship(GameState gs, ArrayList<Image> imageArray) {
        super(
            (gs.tileSize*gs.cols)/2 - gs.tileSize, 
            gs.boardHeight - gs.tileSize*2, 
            gs.tileSize*2, 
            gs.tileSize
        );
        this.imageArray = imageArray;
        this.velocityX = 8;
        this.lives = 3;
        this.shipSpriteState = 0;
        this.shipSpriteTimer = 0;
        this.shipHit = false;
        this.reloadTime = 60;
    }

    public void allowShipControl(int boardWidth) {
        if (this.movingLeft && this.x > this.velocityX && !this.shipHit) {
            this.x -= this.velocityX;
        } else if (this.movingRight && this.x < boardWidth - this.width - this.velocityX && !this.shipHit) {
            this.x += this.velocityX;
        }
    }

    public void hit() {
        this.lives -= 1;
        this.shipHit = true;
    }

    public void handleShipHit(GameState gs, AlienArray alienArray) {
        if (this.shipHit) {
            SoundPlayer.playSound("../assets/sounds/shipExplosion.wav");
            
            this.shipSpriteTimer++;
            
            if (this.shipSpriteTimer % 5 == 0) {
                this.shipSpriteState = (this.shipSpriteState == 1) ? 2 : 1;
            }

            if (this.shipSpriteTimer == 50) {
                if (this.lives == 0) {
                    gs.gameOver = true;
                    this.shipHit = false;
                } else {
                    this.shipHit = false;
                    this.shipSpriteState = 0;
                }
                this.shipSpriteTimer = 0;
            }
        }
    }

    public void reload() {
        if (this.reloadTime < 60) {
            this.reloadTime++;
        }
    }
}