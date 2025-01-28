import java.awt.*;
import java.util.*;

public class Ship extends Entity {
    int velocityX;
    int lives;
    int shipSpriteState;
    int shipSpriteTimer;
    boolean shipHit;
    boolean movingLeft;
    boolean movingRight;
    int reloadTime;

    Ship(int tileSize, int cols, int boardHeight, ArrayList<Image> imgArray, double paddingRatio, int points) {
        super((tileSize*cols)/2 - tileSize, boardHeight - tileSize*2, tileSize*2, tileSize, imgArray, paddingRatio, points);
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

    public void handleShipHit(GameState gameState, AlienArray alienArray) {
        if (this.shipHit) {
            SoundPlayer.playSound("sounds/shipExplosion.wav");
            
            this.shipSpriteTimer++;
            
            if (this.shipSpriteTimer % 5 == 0) {
                this.shipSpriteState = (this.shipSpriteState == 1) ? 2 : 1;
            }

            if (this.shipSpriteTimer == 50) {
                if (this.lives == 0) {
                    gameState.gameOver = true;
                    this.shipHit = false;
                } else {
                    this.shipHit = false;
                    this.shipSpriteState = 0;
                }
                this.shipSpriteTimer = 0;
            }
        } else {
            alienArray.updateAlienSprite();
        }
    }
}