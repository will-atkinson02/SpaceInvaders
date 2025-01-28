import java.awt.*;
import java.util.*;

public class AlienArray {
    ArrayList<Entity> alienArray;
    int alienRows;
    int alienCols;

    int alienVelocityX;

    int alienCount;
    int aliveSquidCount;

    int alienStepTimer;
    int alienStepRate;

    int alienSpriteTimer = 0;
    int alienSpriteState = 0;

    boolean wallCollison;
    boolean allowMove;

    AlienArray() {
        this.alienArray = new ArrayList<Entity>();
        this.alienRows = 5;
        this.alienCols = 11;

        this.alienVelocityX = 8;

        this.alienStepRate = 24;
        this.alienStepTimer = 0;

        this.alienSpriteTimer = 0;
        this.alienSpriteState = 0;

        this.alienCount = 0;
        this.aliveSquidCount = 0;

        this.wallCollison = false;
        this.allowMove = true;
    }

    public void createAliens(
        int alienX,
        int alienY,
        int alienWidth,
        int alienHeight,
        ArrayList<Image> squidImgs,
        ArrayList<Image> crabImgs,
        ArrayList<Image> octopusImgs
    ) {
        for (int row = 0; row < alienRows; row++) {
            for (int col = 0; col < alienCols; col++) {
                ArrayList<Image> imgSet;
                double paddingRatio;
                int points; 

                if (row < 1) {
                    imgSet = squidImgs;
                    paddingRatio = 0.5;
                    points = 30;
                }
                else if (row < 3) {
                    imgSet = crabImgs;
                    paddingRatio = 0.3125;
                    points = 20;
                } else {
                    imgSet = octopusImgs;
                    paddingRatio = 0.25;
                    points = 10;
                }

                Entity alien = new Entity(
                        alienX + col*alienWidth,
                        alienY + row*alienHeight*3/2,
                        alienWidth,
                        alienHeight,
                        imgSet,
                        paddingRatio,
                        points
                    );

                this.alienArray.add(alien);
            }
        }
        this.alienCount = alienArray.size();
    }

    public void alienMovement(int alienWidth, int boardWidth, int tileSize, Ship ship, boolean allowUFOs) {
        this.alienStepTimer++;
        if (alienStepTimer >= alienStepRate && !ship.shipHit) {
            if (!wallCollison) {
                checkWallCollision(alienWidth, boardWidth);
            } 

            if (!allowMove) {
                moveVertically(tileSize, ship, allowUFOs);
                allowMove = true;
            } else {
                moveHorizontally();
                wallCollison = false;
            }
            this.alienStepTimer = 0;
        }
    }

    public void checkWallCollision(int alienWidth, int boardWidth) {
        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (alien.alive) {
                //check if aliens are touching walls
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
            Entity alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += this.alienVelocityX;  
            }
        }
    }

    public void moveVertically(int tileSize, Ship ship, boolean allowUFOs) {
        this.alienVelocityX *= -1;

        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (alien.alive) {
                if (alien.y + tileSize == ship.y) {
                    aliensWin(ship.shipHit, ship.shipSpriteState, ship.lives);
                } else {
                    alien.y += tileSize;
                    if (alien.y >= tileSize*6 && !allowUFOs) {
                        allowUFOs = true;
                    }
                }
            }
        }
    }

    public void updateAlienSprite() {
        this.alienSpriteTimer++;
        if (this.alienSpriteTimer == this.alienStepRate) {
            this.alienSpriteState = (this.alienSpriteState == 0) ? 1 : 0;
            this.alienSpriteTimer = 0;
        }
    }
    
    public void aliensWin(boolean shipHit, int shipSpriteState, int lives) {
        alienArray.clear();
        shipHit = true;
        shipSpriteState = 1;
        lives = 0;
    }

    public int countAliveSquids() {
        int aliveSquids = 0;
        if (!alienArray.isEmpty()) {
            for (int i = 0; i < 11; i++) {
                Entity alien = alienArray.get(i);
                if (alien.alive) {
                    aliveSquids++;
                }
            }
            return aliveSquids;
        }
        return aliveSquids;
    }
}
